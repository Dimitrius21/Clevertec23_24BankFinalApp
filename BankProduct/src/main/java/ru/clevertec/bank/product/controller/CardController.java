package ru.clevertec.bank.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.bank.product.controller.openapi.CardOpenApi;
import ru.clevertec.bank.product.domain.dto.card.request.CardRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardUpdateRequest;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponse;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponseWithAmount;
import ru.clevertec.bank.product.service.CardService;
import ru.clevertec.loggingstarter.annotation.Loggable;

import java.util.UUID;

@Loggable
@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController implements CardOpenApi {

    private final CardService cardService;

    @Override
    @PostMapping
    public ResponseEntity<CardResponse> save(@RequestBody CardRequest request) {
        return new ResponseEntity<>(cardService.save(request), HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CardResponseWithAmount> getById(@PathVariable String id) {
        return new ResponseEntity<>(cardService.findById(id), HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<CardResponse>> getAll(@RequestParam int page,
                                                     @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(cardService.findAll(pageable), HttpStatus.OK);
    }

    @Override
    @GetMapping("/client/{id}")
    public ResponseEntity<CardResponse> getByClientId(@PathVariable UUID id) {
        return new ResponseEntity<>(cardService.findByCustomerId(id), HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> update(@PathVariable String id, @RequestBody CardUpdateRequest request) {
        return new ResponseEntity<>(cardService.update(id, request), HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        return new ResponseEntity<>(cardService.remove(id), HttpStatus.OK);
    }

}
