package ru.clevertec.bank.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.credit.request.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.request.CreditRabbitRequestDTO;
import ru.clevertec.bank.product.domain.dto.credit.request.UpdateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.domain.entity.Credit;
import ru.clevertec.bank.product.mapper.CreditMapper;
import ru.clevertec.bank.product.repository.CreditRepository;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;
    private final ObjectMapper jacksonMapper;

    @Transactional(readOnly = true)
    public CreditResponseDTO findByContractNumber(String contractNumber) {
        Credit credit = creditRepository.findById(contractNumber).orElseThrow(() -> new ResourceNotFountException(
                String.format("Credit with contractNumber = %s not found", contractNumber)));
        return creditMapper.toCreditResponseDTO(credit);
    }

    @Transactional
    public CreditResponseDTO save(CreateCreditDTO dto) {
        Credit credit = creditMapper.toCredit(dto);
        Optional<Credit> creditInDb = creditRepository.findById(credit.getContractNumber());
        if (creditInDb.isPresent()) {
            throw new RequestBodyIncorrectException("Credit with such contractNumber already exist");
        }
        credit = creditRepository.save(credit);
        return creditMapper.toCreditResponseDTO(credit);
    }

    @Transactional
    public CreditResponseDTO saveCreditFromRabbit(String message) {
        try {
            CreditRabbitRequestDTO rabbitDto = jacksonMapper.readValue(message, CreditRabbitRequestDTO.class);
            Credit credit = creditMapper.toCredit(rabbitDto.getPayload());
            return creditMapper.toCreditResponseDTO(creditRepository.save(credit));
        } catch (JsonProcessingException e) {
            throw new RequestBodyIncorrectException("Data in the request body isn't correct: " + message);
        }
    }

    @Transactional
    public CreditResponseDTO updateByContractNumber(String contractNumber, UpdateCreditDTO request) {
        return creditRepository.findById(contractNumber).map(credit -> creditMapper.toCredit(request, credit))
                .map(creditRepository::save)
                .map(creditMapper::toCreditResponseDTO)
                .orElseThrow(() -> new ResourceNotFountException(
                        String.format("Credit with contractNumber = %s not found", contractNumber)));
    }

    @Transactional(readOnly = true)
    public List<CreditResponseDTO> findAllByClientId(UUID customerId) {
        return creditRepository.findAllByCustomerId(customerId).stream().map(creditMapper::toCreditResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public Page<CreditResponseDTO> findAll(Pageable pageable) {
        return creditRepository.findAll(pageable).map(creditMapper::toCreditResponseDTO);
    }

    @Transactional
    public DeleteResponse deleteByContractNumber(String contractNumber) {
        Credit credit = creditRepository.findById(contractNumber).orElseThrow(() -> new ResourceNotFountException(
                String.format("Credit with contractNumber = %s not found", contractNumber)));
        creditRepository.delete(credit);
        return new DeleteResponse("Credit with contract number %s was successfully deleted"
                .formatted(credit.getContractNumber()));
    }
}
