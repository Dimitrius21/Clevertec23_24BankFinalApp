package ru.clevertec.bank.product.testutil.deposit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.product.domain.dto.deposit.response.AccInfoResponse;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepInfoResponse;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepositInfoResponse;
import ru.clevertec.bank.product.testutil.TestBuilder;
import ru.clevertec.bank.product.util.CustomerType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepositInfoResponse")
@With
public class DepositInfoResponseTestBuilder implements TestBuilder<DepositInfoResponse> {

    private UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
    private CustomerType customerType = CustomerType.LEGAL;
    private AccInfoResponse accInfo = AccInfoResponseTestBuilder.aAccInfoResponse().build();
    private DepInfoResponse depInfo = DepInfoResponseTestBuilder.aDepInfoResponse().build();

    @Override
    public DepositInfoResponse build() {
        return new DepositInfoResponse(customerId, customerType, accInfo, depInfo);
    }

}
