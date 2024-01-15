package ru.clevertec.bank.product.testutil.deposit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.product.domain.entity.DepInfo;
import ru.clevertec.bank.product.testutil.TestBuilder;
import ru.clevertec.bank.product.util.DepositType;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepInfo")
@With
public class DepInfoTestBuilder implements TestBuilder<DepInfo> {

    private BigDecimal rate = BigDecimal.valueOf(0.04);
    private Integer termVal = 1;
    private Character termScale = 'D';
    private LocalDate expDate = LocalDate.of(2024, 1, 2);
    private DepositType depType = DepositType.IRREVOCABLE;
    private Boolean autoRenew = false;

    @Override
    public DepInfo build() {
        DepInfo depInfo = new DepInfo();
        depInfo.setRate(rate);
        depInfo.setTermVal(termVal);
        depInfo.setTermScale(termScale);
        depInfo.setExpDate(expDate);
        depInfo.setDepType(depType);
        depInfo.setAutoRenew(autoRenew);
        return depInfo;
    }

}
