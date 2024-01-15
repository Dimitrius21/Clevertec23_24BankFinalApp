package ru.clevertec.bank.product.testutil.deposit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.product.domain.entity.AccInfo;
import ru.clevertec.bank.product.testutil.TestBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aAccInfo")
@With
public class AccInfoTestBuilder implements TestBuilder<AccInfo> {

    private String accIban = "SA0380000000608010167519";
    private LocalDate accOpenDate = LocalDate.of(2024, 1, 4);
    private BigDecimal currAmount = BigDecimal.valueOf(100000.44);
    private String currAmountCurrency = "SAR";

    @Override
    public AccInfo build() {
        AccInfo accInfo = new AccInfo();
        accInfo.setAccIban(accIban);
        accInfo.setAccOpenDate(accOpenDate);
        accInfo.setCurrAmount(currAmount);
        accInfo.setCurrAmountCurrency(currAmountCurrency);
        return accInfo;
    }

}
