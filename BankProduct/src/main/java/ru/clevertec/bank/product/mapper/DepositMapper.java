package ru.clevertec.bank.product.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.bank.product.domain.dto.DepositResponse;
import ru.clevertec.bank.product.domain.entity.Deposit;

@Mapper
public interface DepositMapper {

    DepositResponse toResponse(Deposit deposit);

}
