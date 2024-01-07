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
import ru.clevertec.bank.product.domain.dto.deposit.DepInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.DepInfoResponse;
import ru.clevertec.bank.product.domain.dto.deposit.DepInfoUpdateRequest;
import ru.clevertec.bank.product.domain.dto.deposit.DepositInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.DepositInfoResponse;
import ru.clevertec.bank.product.service.DepositService;
import ru.clevertec.bank.product.validation.ValidPageable;

@Validated
@RestController
@RequestMapping("/deposits")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @GetMapping("/{id}")
    public ResponseEntity<DepositInfoResponse> findWithAccountById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(depositService.findWithAccountById(id));
    }

    @GetMapping
    public ResponseEntity<Page<DepositInfoResponse>> findAllWithAccounts(@ValidPageable Pageable pageable) {
        return ResponseEntity.ok(depositService.findAllWithAccounts(pageable));
    }

    @PostMapping
    public ResponseEntity<DepositInfoResponse> saveWithAccount(@RequestBody @Valid DepositInfoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(depositService.saveWithAccount(request));
    }

    @PostMapping("/{accId}")
    public ResponseEntity<DepInfoResponse> saveByAccountId(@PathVariable @Positive Long accId,
                                                           @RequestBody @Valid DepInfoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(depositService.saveByAccountId(accId, request));
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
