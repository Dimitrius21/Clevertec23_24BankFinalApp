package ru.clevertec.bank.product.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.bank.product.util.CardStatus;
import ru.clevertec.bank.product.util.CustomerType;

import java.util.List;

@Entity
@Data
@Table(name = "cards")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    @Id
    @Column(name = "card_number")
    private String cardNumber;

//    @Column(name = "iban")
//    private String iban;

    @Column(name = "customer_id")
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type")
    private CustomerType customerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_status")
    private CardStatus cardStatus;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "iban")
    private List<Account> accounts;
}
