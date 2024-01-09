package ru.clevertec.bank.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepInfoUpdateRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepositInfoResponse;
import ru.clevertec.bank.product.domain.entity.Deposit;

@Mapper
public interface DepositMapper {

    DepositInfoResponse toDepositInfoResponse(Deposit deposit);

    Deposit toDeposit(DepositInfoRequest request);

    @Mapping(target = "depInfo", source = "request")
    void updateDeposit(DepInfoUpdateRequest request, @MappingTarget Deposit deposit);

}
