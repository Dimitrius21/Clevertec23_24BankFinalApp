package ru.clevertec.bank.product.domain.dto.account.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.bank.product.util.CustomerType;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AccountOutDto {
        private String name;
        private String iban;
        private String amount;
        private String currencyCode;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        private LocalDate openDate;
        private boolean mainAcc;
        private UUID customerId;
        private CustomerType customerType;
        private double rate;
}
