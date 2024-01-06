package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.domain.dto.DepositResponse;
import ru.clevertec.bank.product.mapper.DepositMapper;
import ru.clevertec.bank.product.repository.DepositRepository;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;
    private final DepositMapper depositMapper;

    public DepositResponse findById(Long id) {
        return depositRepository.findById(id)
                .map(depositMapper::toResponse)
                .orElseThrow();
    }

}
