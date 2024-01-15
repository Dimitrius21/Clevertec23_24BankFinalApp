package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_number")
    private String cardNumber; //": "5200000000001096",

    @Column(name = "iban")
    private String iban; //": "AABBCCCDDDDEEEEEEEEEEEEEEEE",

    @Column(name = "cardholder")
    private String cardholder; //": "CARDHOLDER NAME",

}
