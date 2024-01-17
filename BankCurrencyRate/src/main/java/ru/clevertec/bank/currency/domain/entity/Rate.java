package ru.clevertec.bank.currency.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "rates")
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "buy_rate")
    private double buyRate;
    @Column(name = "sell_rate")
    private double sellRate;
    @Column(name = "src_curr")
    private String srcCurr;
    @Column(name = "req_cur")
    private String reqCurr;
}
