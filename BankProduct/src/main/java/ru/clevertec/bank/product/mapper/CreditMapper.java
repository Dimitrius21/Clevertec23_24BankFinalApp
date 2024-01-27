package ru.clevertec.bank.product.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.clevertec.bank.product.domain.dto.credit.request.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.request.UpdateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.domain.entity.Credit;

@Mapper
public interface CreditMapper {

    @Mapping(target = "totalDebt", expression = "java(java.math.BigDecimal.valueOf(credit.getTotalDebt()).divide(java.math.BigDecimal.valueOf(100L)))")
    @Mapping(target = "currentDebt", expression = "java(java.math.BigDecimal.valueOf(credit.getCurrentDebt()).divide(java.math.BigDecimal.valueOf(100L)))")
    CreditResponseDTO toCreditResponseDTO(Credit credit);

    @Mapping(target = "totalDebt", expression = "java(createCreditDTO.getTotalDebt().multiply(java.math.BigDecimal.valueOf(100L)).longValueExact())")
    @Mapping(target = "currentDebt", expression = "java(createCreditDTO.getCurrentDebt().multiply(java.math.BigDecimal.valueOf(100L)).longValueExact())")
    Credit toCredit(CreateCreditDTO createCreditDTO);

    Credit toCredit(UpdateCreditDTO request, @MappingTarget Credit credit);
}
