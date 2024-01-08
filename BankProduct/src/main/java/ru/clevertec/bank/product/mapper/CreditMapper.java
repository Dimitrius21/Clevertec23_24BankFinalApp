package ru.clevertec.bank.product.mapper;


import org.mapstruct.*;
import ru.clevertec.bank.product.domain.dto.credit.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.CreditResponseDTO;
import ru.clevertec.bank.product.domain.dto.credit.UpdateCreditDTO;
import ru.clevertec.bank.product.domain.entity.Credit;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    CreditResponseDTO toCreditResponseDTO(Credit credit);

    Credit toCredit(CreateCreditDTO createCreditDTO);

    void toCredit(UpdateCreditDTO request, @MappingTarget Credit credit);
}
