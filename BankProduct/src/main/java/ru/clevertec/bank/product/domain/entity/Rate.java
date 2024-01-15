package ru.clevertec.bank.product.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Rate {

    private long id;
    private double buyRate;
    private double sellRate;
    private String srcCurr;
    private String reqCurr;

}
