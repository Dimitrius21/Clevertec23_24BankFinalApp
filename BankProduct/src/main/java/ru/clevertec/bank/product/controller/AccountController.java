package ru.clevertec.bank.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.bank.product.domain.dto.account.response.AccountFullOutDto;
import ru.clevertec.bank.product.domain.dto.account.request.AccountInDto;
import ru.clevertec.bank.product.domain.dto.account.response.AccountOutDto;
import ru.clevertec.bank.product.service.AccountService;
import ru.clevertec.loggingstarter.annotation.Loggable;

import java.util.List;
import java.util.UUID;

@Loggable
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accService;

    @GetMapping("/{iban}")
    public ResponseEntity<AccountOutDto> getById(@PathVariable String iban) {
        AccountOutDto outDto = accService.getAccountByIban(iban);
        return new ResponseEntity<>(outDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AccountFullOutDto>> getAll(Pageable pageable) {
        List<AccountFullOutDto> dto = accService.getAllAccounts(pageable);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("customer/{id}")
    public ResponseEntity<List<AccountOutDto>> getByCustomerId(@PathVariable UUID id) {
        List<AccountOutDto> dtoList = accService.getAccountsByCustomerId(id);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountOutDto> create(@RequestBody @Validated AccountInDto dtoIn) {
        AccountOutDto dtoOut = accService.createAccount(dtoIn);
        return new ResponseEntity<>(dtoOut, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<AccountOutDto> update(@RequestBody AccountInDto inDto) {
        AccountOutDto outDto = accService.updateAccount(inDto);
        return new ResponseEntity<>(outDto, HttpStatus.OK);
    }

    @DeleteMapping("/{iban}")
    public void delete(@PathVariable String iban) {
        accService.deleteAccountByIban(iban);
    }
}
