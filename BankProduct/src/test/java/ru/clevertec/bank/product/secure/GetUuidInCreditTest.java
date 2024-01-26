package ru.clevertec.bank.product.secure;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.domain.entity.Credit;
import ru.clevertec.bank.product.mapper.CreditMapper;
import ru.clevertec.bank.product.service.CreditService;
import ru.clevertec.bank.product.util.CustomerType;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUuidInCreditTest {

    private CreditMapper mapper = Mappers.getMapper(CreditMapper.class);

    @Mock
    private CreditService creditService;

    @InjectMocks
    private GetUuidInCredit getUuidInCredit;


    @Test
    void getTest() {
        UUID uuid = UUID.fromString("1a72a05f-4b8f-43c5-a889-1ebc6d9dc729");
        String contractNumber = "1/2024";
        Credit credit = getCredit(contractNumber, uuid);
        CreditResponseDTO outDto = mapper.toCreditResponseDTO(credit);
        when(creditService.findByContractNumber(contractNumber)).thenReturn(outDto);

        UUID res = getUuidInCredit.get(contractNumber);

        Assertions.assertThat(res).isEqualTo(uuid);
    }

    private Credit getCredit(String number, UUID uuid) {
        return new Credit(uuid, number, LocalDate.now(), 100L, 100L, "BYN",
                LocalDate.of(2024, 10, 10), 10.0, "", true, false,
                CustomerType.LEGAL);
    }
}