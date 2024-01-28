package ru.clevertec.bank.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepInfoUpdateRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositFilterRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositRabbitPayloadRequest;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepositInfoResponse;
import ru.clevertec.bank.product.mapper.DepositMapper;
import ru.clevertec.bank.product.repository.DepositRepository;
import ru.clevertec.bank.product.repository.specification.DepositSpecification;
import ru.clevertec.exceptionhandler.exception.InternalServerErrorException;
import ru.clevertec.exceptionhandler.exception.RequestBodyIncorrectException;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepositService {

    private final DepositRepository depositRepository;
    private final DepositMapper depositMapper;

    public DepositInfoResponse findByIban(String iban) {
        return depositRepository.findById(iban)
                .map(depositMapper::toDepositInfoResponse)
                .orElseThrow(throwResourceNotFoundException(iban));
    }

    public Page<DepositInfoResponse> findAll(Pageable pageable) {
        return depositRepository.findAll(pageable)
                .map(depositMapper::toDepositInfoResponse);
    }

    public Page<DepositInfoResponse> findAllByFilter(DepositFilterRequest request, Pageable pageable) {
        return depositRepository.findAll(new DepositSpecification(request), pageable)
                .map(depositMapper::toDepositInfoResponse);
    }

    @Transactional
    public DepositInfoResponse save(DepositInfoRequest request) {
        depositRepository.findById(request.accInfo().accIban())
                .ifPresent(deposit -> {
                    throw new RequestBodyIncorrectException("Deposit with such acc_iban is already exist");
                });
        return Optional.of(request)
                .map(depositMapper::toDeposit)
                .map(depositRepository::save)
                .map(depositMapper::toDepositInfoResponse)
                .orElseThrow(() -> new InternalServerErrorException("Cant save customer"));
    }

    @Transactional
    public void save(DepositRabbitPayloadRequest request) {
        Optional.of(request)
                .map(depositMapper::toDeposit)
                .ifPresent(depositRepository::save);
    }

    @Transactional
    public DepositInfoResponse updateByIban(String iban, DepInfoUpdateRequest request) {
        return depositRepository.findById(iban)
                .map(deposit -> depositMapper.updateDeposit(request, deposit))
                .map(depositRepository::save)
                .map(depositMapper::toDepositInfoResponse)
                .orElseThrow(throwResourceNotFoundException(iban));
    }

    @Transactional
    public DeleteResponse deleteByIban(String iban) {
        return depositRepository.findById(iban)
                .map(deposit -> {
                    depositRepository.delete(deposit);
                    return deposit;
                })
                .map(deposit -> new DeleteResponse("Deposit with iban %s was successfully deleted".formatted(deposit.getAccIban())))
                .orElseThrow(throwResourceNotFoundException(iban));
    }

    private Supplier<ResourceNotFountException> throwResourceNotFoundException(String iban) {
        return () -> new ResourceNotFountException("Deposit with iban %s is not found".formatted(iban));
    }

}
