package ru.clevertec.bank.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.bank.product.controller.openapi.DepositOpenApi;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepInfoUpdateRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositFilterRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepositInfoResponse;
import ru.clevertec.bank.product.service.DepositService;
import ru.clevertec.loggingstarter.annotation.Loggable;

@Loggable
@RestController
@RequestMapping("/deposits")
@RequiredArgsConstructor
public class DepositController implements DepositOpenApi {

    private final DepositService depositService;

    @Override
    @GetMapping("/{iban}")
    public ResponseEntity<DepositInfoResponse> findByIban(@PathVariable String iban) {
        return ResponseEntity.ok(depositService.findByIban(iban));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<DepositInfoResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(depositService.findAll(pageable));
    }

    @Override
    @GetMapping("/filter")
    public ResponseEntity<Page<DepositInfoResponse>> findAllByFilter(DepositFilterRequest request, Pageable pageable) {
        return ResponseEntity.ok(depositService.findAllByFilter(request, pageable));
    }

    @Override
    @PostMapping
    public ResponseEntity<DepositInfoResponse> save(@RequestBody DepositInfoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(depositService.save(request));
    }

    @Override
    @PutMapping("/{iban}")
    public ResponseEntity<DepositInfoResponse> updateByIban(@PathVariable String iban,
                                                            @RequestBody DepInfoUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(depositService.updateByIban(iban, request));
    }

    @Override
    @DeleteMapping("/{iban}")
    public ResponseEntity<DeleteResponse> deleteByIban(@PathVariable String iban) {
        return ResponseEntity.ok(depositService.deleteByIban(iban));
    }

}
