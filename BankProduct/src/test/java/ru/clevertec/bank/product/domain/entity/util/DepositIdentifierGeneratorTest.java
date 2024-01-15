package ru.clevertec.bank.product.domain.entity.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.bank.product.domain.entity.Deposit;
import ru.clevertec.bank.product.testutil.deposit.DepositTestBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DepositIdentifierGeneratorTest {

    @Test
    @DisplayName("test should return expected iban")
    void testShouldReturnExpectedIban() {
        DepositIdentifierGenerator depositIdentifierGenerator = new DepositIdentifierGenerator();
        Deposit deposit = DepositTestBuilder.aDeposit().build();

        Object actual = depositIdentifierGenerator.generate(null, deposit);

        assertThat(actual).hasToString(deposit.getAccIban());
    }

}
