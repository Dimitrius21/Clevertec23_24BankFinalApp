package ru.clevertec.bank.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.bank.product.domain.dto.deposit.DepInfoDto;
import ru.clevertec.bank.product.domain.dto.deposit.DepositInfoDto;
import ru.clevertec.bank.product.service.DepositService;

@RestController
@RequestMapping("/deposits")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @GetMapping("/{id}")
    public ResponseEntity<DepositInfoDto> findWithAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(depositService.findWithAccountById(id));
    }

    @GetMapping
    public ResponseEntity<Page<DepositInfoDto>> findAllWithAccounts(Pageable pageable) {
        return ResponseEntity.ok(depositService.findAllWithAccounts(pageable));
    }

    @PostMapping("accounts/{accId}") // TODO add customer id
    public ResponseEntity<DepositInfoDto> saveByAccountId(@PathVariable Long accId, @RequestBody DepInfoDto depInfoDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(depositService.saveByAccountId(accId, depInfoDto));
    }

}
