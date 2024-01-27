package ru.clevertec.bank.currency.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "rates_list")
@NoArgsConstructor
@AllArgsConstructor
public class Rates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "start_time")
    private ZonedDateTime start;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "rates_list_id", nullable=false)
    private List<Rate> exchangeRates = new ArrayList<>();
}
