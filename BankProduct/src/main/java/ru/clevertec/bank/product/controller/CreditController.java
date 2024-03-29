package ru.clevertec.bank.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.bank.product.controller.openapi.CreditOpenApi;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.credit.request.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.domain.dto.credit.request.UpdateCreditDTO;
import ru.clevertec.bank.product.service.CreditService;
import ru.clevertec.loggingstarter.annotation.Loggable;

import java.util.List;
import java.util.UUID;

@Loggable
@RestController
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditController implements CreditOpenApi {

    private final CreditService creditService;

    @Override
    @GetMapping("/{contractNumber}")
    public ResponseEntity<CreditResponseDTO> findByContractNumber(@PathVariable String contractNumber) {
        return ResponseEntity.ok(creditService.findByContractNumber(contractNumber));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<CreditResponseDTO>> findALl(Pageable pageable) {
        return ResponseEntity.ok(creditService.findAll(pageable));
    }

    @Override
    @GetMapping("/customers/{id}")
    public ResponseEntity<List<CreditResponseDTO>> findAllByCustomerId(@PathVariable UUID id) {
        return ResponseEntity.ok(creditService.findAllByClientId(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<CreditResponseDTO> save(@RequestBody CreateCreditDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(creditService.save(dto));
    }

    @Override
    @PutMapping("/{contractNumber}")
    public ResponseEntity<CreditResponseDTO> updateByContractNumber(@PathVariable String contractNumber, @RequestBody UpdateCreditDTO dto) {
        return ResponseEntity.ok(creditService.updateByContractNumber(contractNumber, dto));
    }

    @Override
    @DeleteMapping("/{contractNumber}")
    public ResponseEntity<DeleteResponse> deleteByContractNumber(@PathVariable String contractNumber) {
        return ResponseEntity.ok(creditService.deleteByContractNumber(contractNumber));
    }
}


