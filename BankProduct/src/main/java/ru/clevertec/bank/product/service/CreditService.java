package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.domain.dto.credit.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.CreditResponseDTO;
import ru.clevertec.bank.product.domain.dto.credit.UpdateCreditDTO;
import ru.clevertec.bank.product.mapper.CreditMapper;
import ru.clevertec.bank.product.repository.CreditRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;

    //TODO: add exceptions
    public CreditResponseDTO findById(Long id) {
        return creditMapper.toCreditResponseDTO(creditRepository.findById(id).get());
    }

    public void deleteById(Long id) {
        creditRepository.deleteById(id);
    }

    public CreditResponseDTO save(CreateCreditDTO dto) {
        return creditMapper.toCreditResponseDTO(creditRepository.save(creditMapper.toCredit(dto)));
    }

    public CreditResponseDTO updateById(Long id, UpdateCreditDTO dto) {
        return creditRepository.findById(id)
                .map(credit -> {
                    creditMapper.toCredit(dto, credit);
                    creditRepository.save(credit);
                    return credit;
                })
                .map(creditMapper::toCreditResponseDTO).get();
    }

    public List<CreditResponseDTO> findAllByClientId(UUID customerId) {

        return creditRepository.findAllByCustomerId(customerId)
                .stream().map(creditMapper::toCreditResponseDTO).toList();
    }

    public List<CreditResponseDTO> findAll() {

        return creditRepository.findAll()
                .stream().map(creditMapper::toCreditResponseDTO).toList();
    }
}
