package ru.clevertec.bank.currency.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "rates")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "rates_list_id")
    @JsonIgnore
    private Rates ratesListId;
    @Column(name = "buy_rate")
    private double buyRate; //": 3.33,
    @Column(name = "sell_rate")
    private double sellRate; //": 3.43,
    @Column(name = "src_curr")
    private String srcCurr; //":"EUR",
    @Column(name = "req_cur")
    private String reqCurr; //":"BYN"
}
