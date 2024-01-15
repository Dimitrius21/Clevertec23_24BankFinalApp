package ru.clevertec.bank.product.testutil.deposit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.product.domain.dto.deposit.request.AccInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositInfoRequest;
import ru.clevertec.bank.product.testutil.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepositInfoRequest")
@With
public class DepositInfoRequestTestBuilder implements TestBuilder<DepositInfoRequest> {

    private String customerId = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc721";
    private String customerType = "LEGAL";
    private AccInfoRequest accInfo = AccInfoRequestTestBuilder.aAccInfoRequest().build();
    private DepInfoRequest depInfo = DepInfoRequestTestBuilder.aDepositInfoRequest().build();

    @Override
    public DepositInfoRequest build() {
        return new DepositInfoRequest(customerId, customerType, accInfo, depInfo);
    }

}
