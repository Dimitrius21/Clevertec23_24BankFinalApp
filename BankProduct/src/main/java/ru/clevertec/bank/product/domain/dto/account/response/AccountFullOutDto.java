package ru.clevertec.bank.product.domain.dto.account.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.bank.product.domain.entity.Card;
import ru.clevertec.bank.product.util.CustomerType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AccountFullOutDto {
        private String name;
        private String iban;
        private long amount;
        private String currencyCode;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        private LocalDate openDate;
        private boolean mainAcc;
        private UUID customerId;
        private CustomerType customerType;
        private double rate;
        private List<Card> cards;
}
