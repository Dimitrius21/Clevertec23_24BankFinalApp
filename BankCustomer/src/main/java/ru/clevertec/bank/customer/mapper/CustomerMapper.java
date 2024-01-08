package ru.clevertec.bank.customer.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.bank.customer.domain.dto.PhysicCustomerResponse;
import ru.clevertec.bank.customer.domain.dto.LegalCustomerResponse;
import ru.clevertec.bank.customer.domain.entity.Customer;

@Mapper
public interface CustomerMapper {

    PhysicCustomerResponse toPhysicResponse(Customer customer);

    LegalCustomerResponse toLegalResponse(Customer customer);

    default PhysicCustomerResponse toResponse(Customer customer) {
        return switch (customer.getType()) {
            case PHYSIC -> toPhysicResponse(customer);
            case LEGAL -> toLegalResponse(customer);
        };
    }

}
