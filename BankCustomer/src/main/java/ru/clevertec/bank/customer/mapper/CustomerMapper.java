package ru.clevertec.bank.customer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.clevertec.bank.customer.domain.dto.CustomerRabbitPayloadRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerResponse;
import ru.clevertec.bank.customer.domain.dto.CustomerUpdateRequest;
import ru.clevertec.bank.customer.domain.entity.Customer;

@Mapper
public interface CustomerMapper {

    CustomerResponse toResponse(Customer customer);

    Customer toCustomer(CustomerRequest request);

    Customer toCustomer(CustomerRabbitPayloadRequest request);

    Customer updateCustomer(CustomerUpdateRequest request, @MappingTarget Customer customer);

}
