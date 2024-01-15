package ru.clevertec.bank.product.testutil.deposit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepInfoUpdateRequest;
import ru.clevertec.bank.product.testutil.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepInfoUpdateRequest")
@With
public class DepInfoUpdateRequestTestBuilder implements TestBuilder<DepInfoUpdateRequest> {

    private String depType = "IRREVOCABLE";
    private Boolean autoRenew = false;

    @Override
    public DepInfoUpdateRequest build() {
        return new DepInfoUpdateRequest(depType, autoRenew);
    }

}
