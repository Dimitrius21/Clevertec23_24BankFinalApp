package ru.clevertec.bank.product.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.bank.product.domain.dto.card.request.CardRabbitPayloadRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardUpdateRequest;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponse;
import ru.clevertec.bank.product.domain.entity.Card;

@Mapper
public interface CardMapper {

    Card toCard(CardRabbitPayloadRequest request);

    Card toCard(CardRequest request);

    CardResponse toCardResponse(Card card);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CardUpdateRequest request, @MappingTarget Card card);

}
