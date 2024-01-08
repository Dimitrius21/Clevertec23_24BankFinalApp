package ru.clevertec.bank.currency.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "rates_list")
@NoArgsConstructor
public class Rates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "start_time")
    private LocalDateTime start;
    @OneToMany(mappedBy = "ratesListId") //, cascade = CascadeType.PERSIST)
    private List<Rate> exchangeRates;
}
