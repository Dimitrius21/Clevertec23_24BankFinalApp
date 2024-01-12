package ru.clevertec.bank.product.testutil.deposit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositFilterRequest;
import ru.clevertec.bank.product.testutil.TestBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepositFilterRequest")
@With
public class DepositFilterRequestTestBuilder implements TestBuilder<DepositFilterRequest> {

    private String accIban = "SA0380000000608010167519";
    private boolean greaterThan = true;
    private BigDecimal amount = BigDecimal.valueOf(1000);
    private String currency = "SAR";

    @Override
    public DepositFilterRequest build() {
        return new DepositFilterRequest(accIban, greaterThan, amount, currency);
    }

}
