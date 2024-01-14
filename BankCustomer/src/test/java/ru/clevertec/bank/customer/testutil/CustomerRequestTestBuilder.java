package ru.clevertec.bank.customer.testutil;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.customer.domain.dto.CustomerRequest;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCustomerRequest")
@With
public class CustomerRequestTestBuilder implements TestBuilder<CustomerRequest> {

    private String customerType = "LEGAL";
    private String unp = "123AB6789";
    private String email = "HornsAndHooves@example.com";
    private String phoneCode = "37529";
    private String phoneNumber = "1111111";
    private String customerFullName = "ООО Рога и копыта";

    @Override
    public CustomerRequest build() {
        return new CustomerRequest(customerType, unp, email, phoneCode, phoneNumber, customerFullName);
    }

}
