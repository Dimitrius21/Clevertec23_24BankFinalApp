package ru.clevertec.bank.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.bank.product.domain.dto.DepositResponse;
import ru.clevertec.bank.product.service.DepositService;

@RestController
@RequestMapping("/deposits")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @GetMapping("/{id}")
    public ResponseEntity<DepositResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(depositService.findById(id));
    }

}
