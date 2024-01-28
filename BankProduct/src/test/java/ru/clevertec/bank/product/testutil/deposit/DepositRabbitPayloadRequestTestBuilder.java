package ru.clevertec.bank.product.testutil.deposit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositRabbitPayloadRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.RabbitAccInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.RabbitDepInfoRequest;
import ru.clevertec.bank.product.testutil.TestBuilder;
import ru.clevertec.bank.product.util.CustomerType;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepositRabbitPayloadRequest")
@With
public class DepositRabbitPayloadRequestTestBuilder implements TestBuilder<DepositRabbitPayloadRequest> {

    private String customerId = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc721";
    private CustomerType customerType = CustomerType.LEGAL;
    private RabbitAccInfoRequest accInfo = RabbitAccInfoRequestTestBuilder.aRabbitAccInfoRequest().build();
    private RabbitDepInfoRequest depInfo = RabbitDepInfoRequestTestBuilder.aRabbitDepInfoRequest().build();

    @Override
    public DepositRabbitPayloadRequest build() {
        return new DepositRabbitPayloadRequest(customerId, customerType, accInfo, depInfo);
    }

}
