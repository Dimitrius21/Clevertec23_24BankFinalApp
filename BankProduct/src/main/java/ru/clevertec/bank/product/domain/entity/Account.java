package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.bank.product.util.CustomerType;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name; //": "Название счёта",
    @Column(name = "iban")
    private String iban; //: "AABBCCCDDDDEEEEEEEEEEEEEEEE",
    @Column(name = "iban_readable")
    private String ibanReadable; // ": "AABB CCC DDDD EEEE EEEE EEEE EEEE",
    @Column(name = "amount")
    private long amount; //": 210000, в копейках
    @Column(name = "currency_code")
    private int currencyCode; //": "933",
    @Column(name = "open_date")
    private LocalDate openDate; //": "dd.MM.yyyy",
    @Column(name = "main_acc")
    private boolean mainAcc; // ": true,
    @Column(name = "customer_id")
    private UUID customerId; //": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
    @Column(name = "customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType customerType; //" : "LEGAL",
    @Column(name = "rate")
    private double rate; //": 0.01
}
