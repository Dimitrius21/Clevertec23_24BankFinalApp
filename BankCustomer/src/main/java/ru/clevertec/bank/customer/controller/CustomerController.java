package ru.clevertec.bank.customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.bank.customer.controller.openapi.CustomerControllerOpenApi;
import ru.clevertec.bank.customer.domain.dto.CustomerRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerResponse;
import ru.clevertec.bank.customer.domain.dto.CustomerUpdateRequest;
import ru.clevertec.bank.customer.domain.dto.DeleteResponse;
import ru.clevertec.bank.customer.service.CustomerService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController implements CustomerControllerOpenApi {

    private final CustomerService customerService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(customerService.findById(id, authentication));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(customerService.findAll(pageable));
    }

    @Override
    @PostMapping
    public ResponseEntity<CustomerResponse> save(@RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.save(request));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateById(@PathVariable UUID id,
                                                       @RequestBody CustomerUpdateRequest request,
                                                       Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.updateById(id, request, authentication));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.deleteById(id));
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponse> restoreById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.restoreById(id));
    }

}
