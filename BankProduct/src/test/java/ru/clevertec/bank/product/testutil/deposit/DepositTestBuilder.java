package ru.clevertec.bank.product.testutil.deposit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.product.domain.entity.AccInfo;
import ru.clevertec.bank.product.domain.entity.DepInfo;
import ru.clevertec.bank.product.domain.entity.Deposit;
import ru.clevertec.bank.product.testutil.TestBuilder;
import ru.clevertec.bank.product.util.CustomerType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDeposit")
@With
public class DepositTestBuilder implements TestBuilder<Deposit> {

    private String accIban = "SA0380000000608010167519";
    private UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc721");
    private CustomerType customerType = CustomerType.LEGAL;
    private AccInfo accInfo = AccInfoTestBuilder.aAccInfo().build();
    private DepInfo depInfo = DepInfoTestBuilder.aDepInfo().build();

    @Override
    public Deposit build() {
        return new Deposit(accIban, customerId, customerType, accInfo, depInfo);
    }

}
