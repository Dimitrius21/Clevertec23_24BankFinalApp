package ru.clevertec.bank.product.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateFeign {

    private long id;
    private LocalDateTime start;
    private List<Rate> exchangeRates;

}
