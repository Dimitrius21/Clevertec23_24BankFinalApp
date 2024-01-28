package ru.clevertec.bank.product.testutil.deposit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepInfoResponse;
import ru.clevertec.bank.product.testutil.TestBuilder;
import ru.clevertec.bank.product.util.DepositType;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepInfoResponse")
@With
public class DepInfoResponseTestBuilder implements TestBuilder<DepInfoResponse> {

    private BigDecimal rate = BigDecimal.valueOf(0.04);
    private Integer termVal = 1;
    private Character termScale = 'D';
    private LocalDate expDate = LocalDate.of(2024, 1, 2);
    private DepositType depType = DepositType.IRREVOCABLE;
    private Boolean autoRenew = false;

    @Override
    public DepInfoResponse build() {
        return new DepInfoResponse(rate, termVal, termScale, expDate, depType, autoRenew);
    }

}
