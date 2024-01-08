package ru.clevertec.bank.product.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.bank.product.domain.dto.credit.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.CreditResponseDTO;
import ru.clevertec.bank.product.domain.dto.credit.UpdateCreditDTO;
import ru.clevertec.bank.product.service.CreditService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @GetMapping("/{id}")
    public ResponseEntity<CreditResponseDTO> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(creditService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CreditResponseDTO>> findALl() {
        return ResponseEntity.ok(creditService.findAll());
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<List<CreditResponseDTO>> findAllByClientId(@PathVariable UUID id) {
        return ResponseEntity.ok(creditService.findAllByClientId(id));
    }

    @PostMapping
    public CreditResponseDTO create(@RequestBody CreateCreditDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(creditService.save(dto)).getBody();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditResponseDTO> updateById(@PathVariable @Positive Long id, @RequestBody UpdateCreditDTO dto) {
        return ResponseEntity.ok(creditService.updateById(id, dto));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Positive Long id) {
        creditService.deleteById(id);
    }
}
