package ru.clevertec.bank.customer.testutil;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.customer.domain.dto.CustomerUpdateRequest;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCustomerUpdateRequest")
@With
public class CustomerUpdateRequestTestBuilder implements TestBuilder<CustomerUpdateRequest> {

    private String email = "HornsAndHooves@example.com";
    private String phoneCode = "37529";
    private String phoneNumber = "1111111";

    @Override
    public CustomerUpdateRequest build() {
        return new CustomerUpdateRequest(email, phoneCode, phoneNumber);
    }

}
