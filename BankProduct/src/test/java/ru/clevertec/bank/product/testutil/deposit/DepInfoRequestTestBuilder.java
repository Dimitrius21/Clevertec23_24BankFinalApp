package ru.clevertec.bank.product.testutil.deposit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepInfoRequest;
import ru.clevertec.bank.product.testutil.TestBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepositInfoRequest")
@With
public class DepInfoRequestTestBuilder implements TestBuilder<DepInfoRequest> {

    private BigDecimal rate = BigDecimal.valueOf(0.04);
    private Integer termVal = 1;
    private String termScale = "D";
    private String depType = "IRREVOCABLE";
    private Boolean autoRenew = false;

    @Override
    public DepInfoRequest build() {
        return new DepInfoRequest(rate, termVal, termScale, depType, autoRenew);
    }

}
