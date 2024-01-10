package ru.clevertec.bank.customer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.bank.customer.domain.dto.CustomerRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerResponse;
import ru.clevertec.bank.customer.domain.dto.CustomerUpdateRequest;
import ru.clevertec.bank.customer.domain.dto.DeleteResponse;
import ru.clevertec.bank.customer.service.CustomerService;
import ru.clevertec.bank.customer.validation.ValidLegalUnp;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(customerService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> save(@RequestBody @Valid @ValidLegalUnp CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateById(@PathVariable UUID id, @RequestBody @Valid CustomerUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.updateById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.deleteById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponse> restoreById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.restoreById(id));
    }

}
