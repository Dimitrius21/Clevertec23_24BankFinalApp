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
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepositService {

    private final DepositRepository depositRepository;
    private final DepositMapper depositMapper;

    public DepositInfoResponse findById(Long id) {
        return depositRepository.findById(id)
                .map(depositMapper::toDepositInfoResponse)
                .orElseThrow(throwResourceNotFoundException(id));
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
        return Optional.of(request)
                .map(depositMapper::toDeposit)
                .map(depositRepository::save)
                .map(depositMapper::toDepositInfoResponse)
                .orElseThrow(() -> new ResourceNotFountException("Cant save deposit")); // TODO add better exception for this message later
    }

    @Transactional
    public void save(DepositRabbitPayloadRequest request) {
        Optional.ofNullable(request)
                .map(depositMapper::toDeposit)
                .map(depositRepository::save)
                .orElseThrow(() -> new ResourceNotFountException("Cant save deposit")); // TODO add better exception for this message later
    }

    @Transactional
    public DepositInfoResponse updateById(Long id, DepInfoUpdateRequest request) {
        return depositRepository.findById(id)
                .map(deposit -> depositMapper.updateDeposit(request, deposit))
                .map(depositRepository::save)
                .map(depositMapper::toDepositInfoResponse)
                .orElseThrow(throwResourceNotFoundException(id));
    }

    @Transactional
    public DeleteResponse deleteById(Long id) {
        return depositRepository.findById(id)
                .map(deposit -> {
                    depositRepository.delete(deposit);
                    return deposit;
                })
                .map(deposit -> new DeleteResponse("Deposit with id %s was successfully deleted".formatted(deposit.getId())))
                .orElseThrow(throwResourceNotFoundException(id));
    }

    private Supplier<ResourceNotFountException> throwResourceNotFoundException(Long id) {
        return () -> new ResourceNotFountException("Deposit with id %s is not found".formatted(id));
    }

}
