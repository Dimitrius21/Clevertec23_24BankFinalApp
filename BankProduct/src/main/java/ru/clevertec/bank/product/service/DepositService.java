package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.deposit.DepInfoUpdateRequest;
import ru.clevertec.bank.product.domain.dto.deposit.DepositInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.DepositInfoResponse;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.mapper.DepositMapper;
import ru.clevertec.bank.product.repository.AccountRepository;
import ru.clevertec.bank.product.repository.DepositRepository;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;
    private final AccountRepository accountRepository;
    private final DepositMapper depositMapper;

    public DepositInfoResponse findWithAccountById(Long id) {
        return depositRepository.findWithAccountById(id)
                .map(depositMapper::toDepositInfoResponse)
                .orElseThrow(throwResourceNotFoundException(id));
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
                .orElseThrow(() -> new ResourceNotFountException("Cant save deposit")); // TODO add better exception for this message later
    }

    public DepositInfoResponse updateById(Long id, DepInfoUpdateRequest request) {
        return depositRepository.findWithAccountById(id)
                .map(deposit -> {
                    depositMapper.updateDeposit(request, deposit);
                    depositRepository.save(deposit);
                    return deposit;
                })
                .map(depositMapper::toDepositInfoResponse)
                .orElseThrow(throwResourceNotFoundException(id));
    }

    public DeleteResponse deleteById(Long id) {
        return depositRepository.findWithAccountById(id)
                .map(deposit -> {
                    depositRepository.deleteById(deposit.getId());
                    return deposit;
                })
                .map(deposit -> new DeleteResponse("Deposit with id %s was successfully deleted".formatted(deposit.getId())))
                .orElseThrow(throwResourceNotFoundException(id));
    }

    private Supplier<ResourceNotFountException> throwResourceNotFoundException(Long id) {
        return () -> new ResourceNotFountException("Deposit with id %s is not found".formatted(id));
    }

}
