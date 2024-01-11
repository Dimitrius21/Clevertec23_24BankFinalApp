package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.domain.dto.card.request.CardRabbitPayloadRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardUpdateRequest;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponse;
import ru.clevertec.bank.product.domain.entity.Card;
import ru.clevertec.bank.product.mapper.CardMapper;
import ru.clevertec.bank.product.repository.CardRepository;
import ru.clevertec.exceptionhandler.exception.GeneralException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public void saveForRabbit(CardRabbitPayloadRequest request) {
        if (request == null) {
            throw new RequestBodyIncorrectException("Empty request for save card");
        }
        checkCardNumber(request.cardNumber());
        cardRepository.save(cardMapper.toCard(request));
    }

    public CardResponse save(CardRequest request) {
        if (request == null) {
            throw new RequestBodyIncorrectException("Empty request for save card");
        }
        checkCardNumber(request.cardNumber());
        Card card = cardRepository.save(cardMapper.toCard(request));
        return cardMapper.toCardResponse(card);
    }

    private void checkCardNumber(String cardNumber) {
        if (cardRepository.getCardByCardNumber(cardNumber).isPresent()) {
            throw new GeneralException(String.format("Card with number: %s is already exist", cardNumber));
        }
    }

    public CardResponse findById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() ->
                new ResourceNotFountException(String.format("Card with id=%d not found", id)));
        return cardMapper.toCardResponse(card);
    }

    public Page<CardResponse> findAll(Pageable pageable) {
        return cardRepository.findAll(pageable)
                .map(cardMapper::toCardResponse);
    }

    public CardResponse update(Long id, CardUpdateRequest request) {
        Card card = cardRepository.findById(id).orElseThrow(() ->
                new ResourceNotFountException(String.format("Card with id=%d not found", id)));
        cardMapper.updateFromDto(request, card);
        Card cardSaved = cardRepository.save(card);
        return cardMapper.toCardResponse(cardSaved);
    }

    public Long remove(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() ->
                new ResourceNotFountException(String.format("Card with id=%d not found", id)));
        cardRepository.delete(card);
        return id;
    }
}
