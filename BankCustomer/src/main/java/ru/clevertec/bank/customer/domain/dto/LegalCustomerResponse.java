package ru.clevertec.bank.customer.domain.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class LegalCustomerResponse extends PhysicCustomerResponse {

    private String unp;

}
