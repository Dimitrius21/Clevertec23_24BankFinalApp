package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.repository.DepositRepository;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepo;
}
