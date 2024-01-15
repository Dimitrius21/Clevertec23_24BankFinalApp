package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.domain.dto.credit.request.CreateCreditDTO;
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

    public CreditResponseDTO findByContractNumber(String contractNumber) {
        Credit credit = creditRepository.findById(contractNumber)
                .orElseThrow(() -> new ResourceNotFountException(
                        String.format("Credit with contractNumber = %s not found", contractNumber)));
        return creditMapper.toCreditResponseDTO(credit);
    }

    public void deleteByContractNumber(String contractNumber) {
        creditRepository.deleteById(contractNumber);
    }

    public CreditResponseDTO save(CreateCreditDTO dto) {
        Credit credit = creditMapper.toCredit(dto);
        Optional<Credit> creditInDb = creditRepository.findById(credit.getContractNumber());
        if (creditInDb.isPresent()) {
            throw new RequestBodyIncorrectException("Credit with such contractNumber already exist");
        }
        credit = creditRepository.save(credit);
        return creditMapper.toCreditResponseDTO(credit);
    }

    public CreditResponseDTO updateByContractNumber(String contractNumber, UpdateCreditDTO request) {
        return creditRepository.findById(contractNumber)
                .map(credit -> creditMapper.toCredit(request, credit))
                .map(creditRepository::save)
                .map(creditMapper::toCreditResponseDTO)
                .orElseThrow(() -> new ResourceNotFountException(
                        String.format("Credit with contractNumber = %s not found", contractNumber)));
    }

    public List<CreditResponseDTO> findAllByClientId(UUID customerId) {
        return creditRepository.findAllByCustomerId(customerId)
                .stream().map(creditMapper::toCreditResponseDTO).toList();
    }

    public Page<CreditResponseDTO> findAll(Pageable pageable) {
        return creditRepository.findAll(pageable)
                .map(creditMapper::toCreditResponseDTO);
    }

}
