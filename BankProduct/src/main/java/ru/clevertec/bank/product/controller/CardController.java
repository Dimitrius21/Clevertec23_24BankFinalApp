package ru.clevertec.bank.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.bank.product.domain.dto.card.request.CardRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardUpdateRequest;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponse;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponseWithAmount;
import ru.clevertec.bank.product.service.CardService;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponse> save(@Valid @RequestBody CardRequest request) {
        return new ResponseEntity<>(cardService.save(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseWithAmount> getById(@PathVariable String id) {
        return new ResponseEntity<>(cardService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<CardResponse>> getAll(Pageable pageable) {
        return new ResponseEntity<>(cardService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<CardResponse> getByClientId(@PathVariable UUID id) {
        return new ResponseEntity<>(cardService.findByCustomerId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> update(@PathVariable String id, @RequestBody @Valid CardUpdateRequest request) {
        return new ResponseEntity<>(cardService.update(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        return new ResponseEntity<>(cardService.remove(id), HttpStatus.OK);
    }
}
