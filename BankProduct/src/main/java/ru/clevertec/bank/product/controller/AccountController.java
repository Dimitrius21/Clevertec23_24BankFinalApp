package ru.clevertec.bank.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.bank.product.domain.dto.AccountInDto;
import ru.clevertec.bank.product.domain.dto.AccountOutDto;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.service.AccountService;
import ru.clevertec.bank.product.util.AccountMapper;

import java.util.UUID;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accService;
    private final AccountMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable long id){
        Account account = accService.getAccountById(id);
        AccountOutDto dto = mapper.toAccountOutDto(account);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("client/{id}")
    public ResponseEntity getByClientId(@PathVariable UUID id){
        Account account = accService.getAccountByClientId(id);
        AccountOutDto dto = mapper.toAccountOutDto(account);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody AccountInDto dtoIn){
        Account account = mapper.toAccount(dtoIn);
        account = accService.saveAccount(account);
        AccountOutDto dtoOut = mapper.toAccountOutDto(account);
        return new ResponseEntity<>(dtoOut, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody long dto){
        return null;
    }

    @DeleteMapping
    public void delete(@PathVariable long id){
        accService.deleteAccountById(id);
    }


}
