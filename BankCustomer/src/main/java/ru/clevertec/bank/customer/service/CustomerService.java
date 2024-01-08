package ru.clevertec.bank.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.bank.customer.domain.dto.CustomerRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerResponse;
import ru.clevertec.bank.customer.mapper.CustomerMapper;
import ru.clevertec.bank.customer.repository.CustomerRepository;
import ru.clevertec.exceptionhandler.exception.ResourceNotFountException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerResponse findById(UUID id) {
        return customerRepository.findById(id)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFountException("Customer with id %s is not found".formatted(id)));
    }

    public Page<CustomerResponse> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::toResponse);
    }

    public CustomerResponse save(CustomerRequest request) {
        return Optional.of(request)
                .map(customerMapper::toCustomer)
                .map(customerRepository::save)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFountException("Cant save customer")); //TODO add better exception for this message
    }

}
