package ru.clevertec.bank.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.bank.product.service.CreditService;

import java.util.UUID;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable long id){
        return null;
    }

    @GetMapping("client/{id}")
    public ResponseEntity getByClientId(@PathVariable UUID id){
        String uuid = id.toString();
        return new ResponseEntity<>(uuid, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody long dto){
        return null;
    }

    @PutMapping
    public ResponseEntity update(@RequestBody long dto){
        return null;
    }

    @DeleteMapping
    public ResponseEntity delete(@PathVariable long id){
        return null;
    }
}
