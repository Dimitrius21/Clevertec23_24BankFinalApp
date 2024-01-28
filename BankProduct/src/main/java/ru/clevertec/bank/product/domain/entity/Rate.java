package ru.clevertec.bank.product.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rate {

    private long id;
    private double buyRate;
    private double sellRate;
    private String srcCurr;
    private String reqCurr;

}
