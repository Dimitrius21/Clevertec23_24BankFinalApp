package ru.clevertec.bank.customer.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.bank.customer.domain.dto.CustomerRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerResponse;
import ru.clevertec.bank.customer.domain.entity.Customer;

@Mapper
public interface CustomerMapper {

    CustomerResponse toResponse(Customer customer);

    Customer toCustomer(CustomerRequest request);

}
