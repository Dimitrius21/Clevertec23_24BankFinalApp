package ru.clevertec.bank.product.mapper;

import org.mapstruct.*;
import ru.clevertec.bank.product.domain.dto.card.request.CardRabbitPayloadRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardUpdateRequest;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponse;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponseWithAmount;
import ru.clevertec.bank.product.domain.entity.Card;

@Mapper
public interface CardMapper {

    Card toCard(CardRabbitPayloadRequest request);

    Card toCard(CardRequest request);

    @Mapping(source = "account.iban", target = "iban")
    CardResponse toCardResponse(Card card);

    @Mapping(source = "account.iban", target = "iban")
    CardResponseWithAmount toCardResponseWithAmounts(Card card);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CardUpdateRequest request, @MappingTarget Card card);

}
