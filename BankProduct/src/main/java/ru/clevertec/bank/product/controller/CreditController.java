package ru.clevertec.bank.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.bank.product.domain.dto.credit.request.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.domain.dto.credit.request.UpdateCreditDTO;
import ru.clevertec.bank.product.service.CreditService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @GetMapping("/{contractNumber}")
    public ResponseEntity<CreditResponseDTO> findByContractNumber(@PathVariable String contractNumber) {
        return ResponseEntity.ok(creditService.findByContractNumber(contractNumber));
    }
    @GetMapping
    public ResponseEntity<Page<CreditResponseDTO>> findALl(Pageable pageable) {
        return ResponseEntity.ok(creditService.findAll(pageable));
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<List<CreditResponseDTO>> findAllByCustomerId(@PathVariable UUID id) {
        return ResponseEntity.ok(creditService.findAllByClientId(id));
    }

    @PostMapping
    public CreditResponseDTO save(@RequestBody CreateCreditDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(creditService.save(dto)).getBody();
    }

    @PutMapping("/{contractNumber}")
    public ResponseEntity<CreditResponseDTO> updateByContractNumber(@PathVariable String contractNumber, @RequestBody UpdateCreditDTO dto) {
        return ResponseEntity.ok(creditService.updateByContractNumber(contractNumber, dto));
    }

    @DeleteMapping("/{contractNumber}")
    public void deleteByContactNumber(@PathVariable String contractNumber) {
        creditService.deleteByContractNumber(contractNumber);
    }
}


