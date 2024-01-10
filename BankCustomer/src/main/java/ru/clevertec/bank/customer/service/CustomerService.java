package ru.clevertec.bank.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.bank.customer.domain.dto.CustomerRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerResponse;
import ru.clevertec.bank.customer.domain.dto.CustomerUpdateRequest;
import ru.clevertec.bank.customer.domain.dto.DeleteResponse;
import ru.clevertec.bank.customer.mapper.CustomerMapper;
import ru.clevertec.bank.customer.repository.CustomerRepository;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerResponse findById(UUID id) {
        return customerRepository.findByCustomerIdAndDeletedFalse(id)
                .map(customerMapper::toResponse)
                .orElseThrow(throwResourceNotFoundException(id));
    }

    public Page<CustomerResponse> findAll(Pageable pageable) {
        return customerRepository.findAllByDeletedFalse(pageable)
                .map(customerMapper::toResponse);
    }

    @Transactional
    public CustomerResponse save(CustomerRequest request) {
        return Optional.of(request)
                .map(customerMapper::toCustomer)
                .map(customerRepository::save)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFountException("Cant save customer")); //TODO add better exception for this message
    }

    //TODO unique exception handler for unique field
    @Transactional
    public CustomerResponse updateById(UUID id, CustomerUpdateRequest request) {
        return customerRepository.findByCustomerIdAndDeletedFalse(id)
                .map(customer -> customerMapper.updateCustomer(request, customer))
                .map(customerRepository::save)
                .map(customerMapper::toResponse)
                .orElseThrow(throwResourceNotFoundException(id));
    }

    @Transactional
    public DeleteResponse deleteById(UUID id) {
        return customerRepository.findByCustomerIdAndDeletedFalse(id)
                .map(customer -> {
                    customer.setDeleted(true);
                    return customerRepository.save(customer);
                })
                .map(customer -> new DeleteResponse("Customer with id %s was successfully deleted"
                        .formatted(customer.getCustomerId())))
                .orElseThrow(throwResourceNotFoundException(id));
    }

    @Transactional
    public CustomerResponse restoreById(UUID id) {
        return customerRepository.findByCustomerIdAndDeletedTrue(id)
                .map(customer -> {
                    customer.setDeleted(false);
                    return customerRepository.save(customer);
                })
                .map(customerMapper::toResponse)
                .orElseThrow(throwResourceNotFoundException(id));
    }

    private Supplier<ResourceNotFountException> throwResourceNotFoundException(UUID id) {
        return () -> new ResourceNotFountException("Customer with id %s is not found".formatted(id));
    }

}
