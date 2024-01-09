package ru.clevertec.bank.product.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepInfoUpdateRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositFilterRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepositInfoResponse;
import ru.clevertec.bank.product.service.DepositService;

@Validated
@RestController
@RequestMapping("/deposits")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @GetMapping("/{id}")
    public ResponseEntity<DepositInfoResponse> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(depositService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<DepositInfoResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(depositService.findAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<DepositInfoResponse>> findAllByFilter(DepositFilterRequest request, Pageable pageable) {
        return ResponseEntity.ok(depositService.findAllByFilter(request, pageable));
    }

    @PostMapping
    public ResponseEntity<DepositInfoResponse> save(@RequestBody @Valid DepositInfoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(depositService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepositInfoResponse> updateById(@PathVariable @Positive Long id,
                                                          @RequestBody @Valid DepInfoUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(depositService.updateById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(depositService.deleteById(id));
    }

}
