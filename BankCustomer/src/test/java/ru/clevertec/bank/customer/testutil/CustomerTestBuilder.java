package ru.clevertec.bank.customer.testutil;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.customer.domain.entity.Customer;
import ru.clevertec.bank.customer.util.CustomerType;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCustomer")
@With
public class CustomerTestBuilder implements TestBuilder<Customer> {

    private UUID customerId = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
    private CustomerType customerType = CustomerType.LEGAL;
    private String unp = "123AB6789";
    private LocalDate registerDate = LocalDate.of(2024, 1, 1);
    private String email = "HornsAndHooves@example.com";
    private String phoneCode = "37529";
    private String phoneNumber = "1111111";
    private String customerFullName = "ООО Рога и копыта";
    private boolean deleted = false;

    @Override
    public Customer build() {
        return new Customer(customerId, customerType, unp, registerDate,
                email, phoneCode, phoneNumber, customerFullName, deleted);
    }

}
