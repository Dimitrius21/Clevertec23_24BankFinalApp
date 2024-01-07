package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.domain.dto.deposit.DepositInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.DepositInfoResponse;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.mapper.DepositMapper;
import ru.clevertec.bank.product.repository.AccountRepository;
import ru.clevertec.bank.product.repository.DepositRepository;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;
    private final AccountRepository accountRepository;
    private final DepositMapper depositMapper;

    public DepositInfoResponse findWithAccountById(Long id) {
        return depositRepository.findWithAccountById(id)
                .map(depositMapper::toDepositInfoResponse)
                .orElseThrow(() -> new ResourceNotFountException("Deposit with id %s is not found".formatted(id)));
    }

    public Page<DepositInfoResponse> findAllWithAccounts(Pageable pageable) {
        return depositRepository.findAllWithAccounts(pageable)
                .map(depositMapper::toDepositInfoResponse);
    }

    public DepositInfoResponse saveWithAccount(DepositInfoRequest request) {
        return Optional.of(request)
                .map(depositMapper::toDeposit)
                .map(deposit -> {
                    Account saved = accountRepository.save(deposit.getAccount());
                    deposit.setAccount(saved);
                    return deposit;
                })
                .map(depositRepository::save)
                .map(depositMapper::toDepositInfoResponse)
                .orElseThrow();
    }

}
