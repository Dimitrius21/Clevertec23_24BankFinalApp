package ru.clevertec.bank.product.testutil.deposit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.product.domain.dto.deposit.request.AccInfoRequest;
import ru.clevertec.bank.product.testutil.TestBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aAccInfoRequest")
@With
public class AccInfoRequestTestBuilder implements TestBuilder<AccInfoRequest> {

    private String accIban = "SA0380000000608010167519";
    private BigDecimal currAmount = BigDecimal.valueOf(100000.44);
    private String currAmountCurrency = "SAR";

    @Override
    public AccInfoRequest build() {
        return new AccInfoRequest(accIban, currAmount, currAmountCurrency);
    }

}
