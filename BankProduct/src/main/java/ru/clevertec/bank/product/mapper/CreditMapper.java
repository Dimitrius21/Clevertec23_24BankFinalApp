package ru.clevertec.bank.product.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.bank.product.domain.dto.credit.request.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.request.UpdateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.domain.entity.Credit;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CreditMapper {

    CreditResponseDTO toCreditResponseDTO(Credit credit);

    @Mapping(target = "totalDebt", expression = "java(createCreditDTO.getTotalDebt().multiply(java.math.BigDecimal.valueOf(100L)).longValueExact())")
    @Mapping(target = "currentDebt", expression = "java(createCreditDTO.getCurrentDebt().multiply(java.math.BigDecimal.valueOf(100L)).longValueExact())")
    Credit toCredit(CreateCreditDTO createCreditDTO);

    Credit toCredit(UpdateCreditDTO request, @MappingTarget Credit credit);
}
