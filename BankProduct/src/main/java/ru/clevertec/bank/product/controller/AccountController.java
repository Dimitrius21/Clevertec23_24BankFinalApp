package ru.clevertec.bank.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.bank.product.domain.dto.AccountChangeSumDto;
import ru.clevertec.bank.product.domain.dto.AccountInDto;
import ru.clevertec.bank.product.domain.dto.AccountOutDto;
import ru.clevertec.bank.product.domain.entity.Account;
import ru.clevertec.bank.product.service.AccountService;
import ru.clevertec.bank.product.util.AccountMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController  {

    private final AccountService accService;
    private final AccountMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable long id){
        Account account = accService.getAccountById(id);
        AccountOutDto dto = mapper.toAccountOutDto(account);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("client/{id}")
    public ResponseEntity getByCustomerId(@PathVariable UUID id){
        List<Account> accounts = accService.getAccountsByCustomerId(id);
        List<AccountOutDto> dtoList = accounts.stream()
                .map(mapper::toAccountOutDto)
                .toList();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody AccountInDto dtoIn){
        Account account = mapper.toAccount(dtoIn);
        account = accService.saveAccount(account);
        AccountOutDto dtoOut = mapper.toAccountOutDto(account);
        return new ResponseEntity<>(dtoOut, HttpStatus.CREATED);
    }

    @PutMapping("/rename")
    public ResponseEntity updateName(@RequestBody AccountInDto dtoIn){
        Account account = mapper.toAccount(dtoIn);
        account = accService.updateAccountName(account);
        AccountOutDto dtoOut = mapper.toAccountOutDto(account);
        return new ResponseEntity<>(dtoOut, HttpStatus.OK);
    }

    @PutMapping("/amount")
    public ResponseEntity updateSum(@RequestBody AccountChangeSumDto dto){
        Account account = accService.updateAccountSum(dto);
        AccountOutDto dtoOut = mapper.toAccountOutDto(account);
        return new ResponseEntity<>(dtoOut, HttpStatus.OK);
    }

    @DeleteMapping
    public void delete(@PathVariable long id){
        accService.deleteAccountById(id);
    }


}
