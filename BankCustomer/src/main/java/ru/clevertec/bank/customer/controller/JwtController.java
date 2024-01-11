package ru.clevertec.bank.customer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.bank.customer.domain.dto.JwtRequest;
import ru.clevertec.bank.customer.domain.dto.JwtResponse;
import ru.clevertec.bank.customer.service.JwtService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt")
public class JwtController {

    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<JwtResponse> generateJwt(@RequestBody @Valid JwtRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jwtService.generateJwt(request));
    }

}
