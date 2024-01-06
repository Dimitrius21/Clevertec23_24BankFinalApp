package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.domain.dto.deposit.DepInfoDto;
import ru.clevertec.bank.product.domain.dto.deposit.DepositInfoDto;
import ru.clevertec.bank.product.mapper.DepositMapper;
import ru.clevertec.bank.product.repository.AccountRepository;
import ru.clevertec.bank.product.repository.DepositRepository;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;
    private final AccountRepository accountRepository;
    private final DepositMapper depositMapper;

    public DepositInfoDto findWithAccountById(Long id) {
        return depositRepository.findWithAccountById(id)
                .map(depositMapper::toDepositInfoDto)
                .orElseThrow(() -> new ResourceNotFountException("Deposit with id %s is not found".formatted(id)));
    }

    public Page<DepositInfoDto> findAllWithAccounts(Pageable pageable) {
        return depositRepository.findAllWithAccounts(pageable)
                .map(depositMapper::toDepositInfoDto);
    }

    public DepositInfoDto saveByAccountId(Long accId, DepInfoDto depInfoDto) {
        return accountRepository.findById(accId)
                .map(account -> depositMapper.toDeposit(depInfoDto, account))
                .map(depositRepository::save)
                .map(depositMapper::toDepositInfoDto)
                .orElseThrow(() -> new ResourceNotFountException("Account with id %s is not found".formatted(accId)));
    }

}
