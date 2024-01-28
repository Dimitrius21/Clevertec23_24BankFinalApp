package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.bank.product.client.CurrencyRateClient;
import ru.clevertec.bank.product.domain.dto.card.request.CardRabbitPayloadRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardUpdateRequest;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponse;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponseWithAmount;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.domain.entity.Amount;
import ru.clevertec.bank.product.domain.entity.Card;
import ru.clevertec.bank.product.domain.entity.Rate;
import ru.clevertec.bank.product.mapper.CardMapper;
import ru.clevertec.bank.product.repository.AccountRepository;
import ru.clevertec.bank.product.repository.CardRepository;
import ru.clevertec.exceptionhandler.exception.GeneralException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private static final String CURRENCY_NAME_BYN = "BYN";
    public static final int NUMBER_FOR_CONVERT_TO_RUBLE = 100;

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final CurrencyRateClient currencyRateClient;
    private final CardMapper cardMapper;

    @Transactional
    public void saveForRabbit(CardRabbitPayloadRequest request) {
        if (request == null) {
            throw new RequestBodyIncorrectException("Empty request from Rabbit for save card");
        }
        Card card;
        Optional<Card> cardByCardNumber = cardRepository.findById(request.cardNumber());
        if (cardByCardNumber.isEmpty()) {
            card = cardMapper.toCard(request);
        } else {
            card = cardByCardNumber.get();
            cardMapper.updateFromRabbitDto(request, card);
        }
        Account account = accountRepository.findById(request.iban()).orElseThrow(() ->
                new ResourceNotFountException(String.format("Account with iban=%s not found", request.iban())));
        card.setAccount(account);
        cardRepository.save(card);
    }

    @Transactional
    public CardResponse save(CardRequest request) {
        if (request == null) {
            throw new RequestBodyIncorrectException("Empty request for save card");
        }
        checkCardNumber(request.cardNumber());
        Card card = cardMapper.toCard(request);
        Account account = accountRepository.findById(request.iban()).orElseThrow(() ->
                new ResourceNotFountException(String.format("Account with iban=%s not found", request.iban())));
        card.setAccount(account);
        Card cardSaved = cardRepository.save(card);
        return cardMapper.toCardResponse(cardSaved);
    }

    private void checkCardNumber(String cardNumber) {
        if (cardRepository.findById(cardNumber).isPresent()) {
            throw new GeneralException(String.format("Card with number: %s is already exist", cardNumber));
        }
    }


    public CardResponseWithAmount findById(String id) {
        Card card = cardRepository.findWithAccountByCardNumber(id).orElseThrow(() ->
                new ResourceNotFountException(String.format("Card with id=%s not found", id)));
        BigDecimal amount = BigDecimal.valueOf(card.getAccount().getAmount() / NUMBER_FOR_CONVERT_TO_RUBLE).setScale(2);
        List<Rate> exchangeRates = currencyRateClient.getCurrent().getExchangeRates();
        List<Amount> amounts = exchangeRates.stream()
                .filter(r -> CURRENCY_NAME_BYN.equals(r.getReqCurr()))
                .map(r -> new Amount(amount.divide(BigDecimal.valueOf(r.getSellRate()), 2, RoundingMode.HALF_UP), r.getSrcCurr()))
                .collect(Collectors.toList());
        amounts.add(new Amount(amount, CURRENCY_NAME_BYN));
        CardResponseWithAmount cardResponseWithAmounts = cardMapper.toCardResponseWithAmounts(card);
        cardResponseWithAmounts.setAmounts(amounts);
        return cardResponseWithAmounts;
    }

    public Page<CardResponse> findAll(Pageable pageable) {
        return cardRepository.findAll(pageable)
                .map(cardMapper::toCardResponse);
    }

    public CardResponse findByCustomerId(UUID uuid) {
        Card card = cardRepository.findByCustomerId(uuid).orElseThrow(() ->
                new ResourceNotFountException(String.format("Card with customer uuid=%s not found", uuid.toString())));
        return cardMapper.toCardResponse(card);
    }

    @Transactional
    public CardResponse update(String id, CardUpdateRequest request) {
        if (request == null) {
            throw new RequestBodyIncorrectException("Empty request for update card");
        }
        Card card = cardRepository.findById(id).orElseThrow(() ->
                new ResourceNotFountException(String.format("Card with id=%s not found", id)));
        Account account = accountRepository.findById(request.iban()).orElseThrow(() ->
                new ResourceNotFountException(String.format("Account with iban=%s not found", request.iban())));
        cardMapper.updateFromDto(request, card);
        card.setAccount(account);
        Card cardSaved = cardRepository.save(card);
        return cardMapper.toCardResponse(cardSaved);
    }

    public String remove(String id) {
        cardRepository.findById(id).orElseThrow(() ->
                new ResourceNotFountException(String.format("Card with id=%s not found", id)));
        cardRepository.deleteById(id);
        return id;
    }
}
