package ru.clevertec.bank.customer.domain.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LegalCustomerResponse.class, name = "LEGAL"),
        @JsonSubTypes.Type(value = PhysicCustomerResponse.class, name = "PHYSIC")
})
public class PhysicCustomerResponse {

    private UUID id;
    private String type;
    private LocalDate registerDate;
    private String email;
    private String phoneCode;
    private String phoneNumber;
    private String fullName;

}
