package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "credits")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "customer_id")
    private UUID customerId; //": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
    @Column(name = "contract_number")
    private String contractNumber; //": "11-0216444-2-0",
    @Column(name = "contract_start_date")
    private LocalDate contractStartDate; //": "30.03.2022",
    @Column(name = "total_debt")
    private long totalDebt; //": 8113.99,
    @Column(name = "current_debt")
    private long currentDebt; //": 361.99,
    @Column(name = "currency")
    private String currency; //": "BYN",
    @Column(name = "repayment_date")
    private LocalDate repaymentDate; //": "16.01.2023",
    @Column(name = "rate")
    private double rate; //": 22.8,
    @Column(name = "iban")
    private String iban; //": "AABBCCCDDDDEEEEEEEEEEEEEEEE",
    @Column(name = "possible_repayment")
    private boolean possibleRepayment; //": true,
    @Column(name = "is_closed")
    private boolean isClosed; //": false,
    @Column(name = "customer_type")
    private String customer_type; //" : "LEGAL/PHYSIC"

}
