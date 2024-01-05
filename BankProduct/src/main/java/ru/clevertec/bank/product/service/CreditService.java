package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.repository.CreditRepository;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepo;

}
