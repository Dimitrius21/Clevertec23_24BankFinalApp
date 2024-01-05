package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "deposits")
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "customer_id")
    private UUID customer_id; //": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
    @Column(name = "customer_type")
    private String customer_type; //" : "LEGAL/PHYSIC",

    @Column(name = "acc_iban")
    private String accIban; //": "AABBCCCDDDDEEEEEEEEEEEEEEEE",
    @Column(name = "acc_open_date")
    private LocalDate accOpenDate; //": "dd.MM.yyyy",
    @Column(name = "curr_amount")
    private long currAmount; //": 3000.00,
    @Column(name = "curr_amount_currency")
    private String currAmountCurrency; //": "BYN"

    @Column(name = "rate")
    private double rate; //: 14.50,
    @Column(name = "term_val")
    private int termVal; //": 24,
    @Column(name = "term_scale")
    private char term_scale; //": "M/D",
    @Column(name = "exp_date")
    private LocalDate expDate; //": "dd.MM.yyyy",
    @Column(name = "dep_type")
    private String depType; //": "REVOCABLE/IRREVOCABLE",
    @Column(name = "auto_renew")
    private boolean autoRenew; //": true
}

