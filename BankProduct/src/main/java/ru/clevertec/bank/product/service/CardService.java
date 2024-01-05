package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.repository.CardRepository;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepo;

}
