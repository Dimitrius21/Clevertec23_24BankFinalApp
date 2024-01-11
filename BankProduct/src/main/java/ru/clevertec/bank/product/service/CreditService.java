package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.bank.product.domain.dto.credit.request.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.domain.dto.credit.request.UpdateCreditDTO;
import ru.clevertec.bank.product.domain.entity.Credit;
import ru.clevertec.bank.product.mapper.CreditMapper;
import ru.clevertec.bank.product.repository.CreditRepository;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;

    @Transactional
    public CreditResponseDTO findById(String contractNumber) {
        Credit credit = creditRepository.findById(contractNumber)
                .orElseThrow(() -> new ResourceNotFountException(
                        String.format("Credit with contractNumber = %s not found", contractNumber)));
        return creditMapper.toCreditResponseDTO(credit);
    }

    @Transactional
    public void deleteById(String contractNumber) {
        creditRepository.deleteById(contractNumber);
    }

    @Transactional
    public CreditResponseDTO save(CreateCreditDTO dto) {
        Credit credit = creditRepository.save(creditMapper.toCredit(dto));
        return creditMapper.toCreditResponseDTO(credit);
    }

    @Transactional
    public CreditResponseDTO updateById(String contractNumber, UpdateCreditDTO request) {
        return creditRepository.findById(contractNumber)
                .map(credit -> creditMapper.toCredit(request, credit))
                .map(creditRepository::save)
                .map(creditMapper::toCreditResponseDTO)
                .orElseThrow(() -> new ResourceNotFountException(
                        String.format("Credit with contractNumber = %s not found", contractNumber)));
    }

    @Transactional
    public List<CreditResponseDTO> findAllByClientId(UUID customerId) {
        return creditRepository.findAllByCustomerId(customerId)
                .stream().map(creditMapper::toCreditResponseDTO).toList();
    }

    @Transactional
    public List<CreditResponseDTO> findAll() {
        return creditRepository.findAll()
                .stream().map(creditMapper::toCreditResponseDTO).toList();
    }
}
