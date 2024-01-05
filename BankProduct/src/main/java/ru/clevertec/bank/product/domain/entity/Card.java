package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "card_number")
    private String cardNumber; //": "5200000000001096",
    //??
    @Column(name = "card_number_readable")
    private String cardNumberReadable; //": "5200 0000 0000 1096",
    @Column(name = "iban")
    private String iban; //": "AABBCCCDDDDEEEEEEEEEEEEEEEE",
    @Column(name = "customerId")
    private UUID customer_id; //": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
    @Column(name = "customer_type")
    private String customerType; //" : "LEGAL/PHYSIC",
    @Column(name = "cardholder")
    private String cardholder; //": "CARDHOLDER NAME",
    @Column(name = "card_status")
    private String cardStatus; //": "ACTIVE/INACTIVE/BLOCKED/NEW"

}
