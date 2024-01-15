package ru.clevertec.bank.product.domain.entity.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.bank.product.domain.entity.Deposit;
import ru.clevertec.bank.product.testutil.deposit.AccInfoTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepInfoTestBuilder;
import ru.clevertec.bank.product.testutil.deposit.DepositTestBuilder;
import ru.clevertec.exceptionhandler.exception.NotValidRequestParametersException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DepositListenerTest {

    private DepositListener depositListener;

    @BeforeEach
    void setUp() {
        depositListener = new DepositListener();
    }

    @Test
    @DisplayName("test prePersist should insert accOpenDate")
    void testPrePersistShouldInsertAccOpenDate() {
        Deposit deposit = DepositTestBuilder.aDeposit()
                .withAccInfo(AccInfoTestBuilder.aAccInfo()
                        .withAccOpenDate(null)
                        .build())
                .build();

        depositListener.prePersist(deposit);

        assertThat(LocalDate.now()).isEqualTo(deposit.getAccInfo().getAccOpenDate());
    }

    @Test
    @DisplayName("test prePersist should insert expDate by days")
    void testPrePersistShouldInsertExpDateByDays() {
        int termVal = 3;
        Deposit deposit = DepositTestBuilder.aDeposit()
                .withAccInfo(AccInfoTestBuilder.aAccInfo()
                        .withAccOpenDate(null)
                        .build())
                .withDepInfo(DepInfoTestBuilder.aDepInfo()
                        .withTermVal(termVal)
                        .withTermScale('D')
                        .withExpDate(null)
                        .build()).build();
        LocalDate expected = LocalDate.now().plusDays(termVal);

        depositListener.prePersist(deposit);

        assertThat(expected).isEqualTo(deposit.getDepInfo().getExpDate());
    }

    @Test
    @DisplayName("test prePersist should insert expDate by months")
    void testPrePersistShouldInsertExpDateByMonths() {
        int termVal = 3;
        Deposit deposit = DepositTestBuilder.aDeposit()
                .withAccInfo(AccInfoTestBuilder.aAccInfo()
                        .withAccOpenDate(null)
                        .build())
                .withDepInfo(DepInfoTestBuilder.aDepInfo()
                        .withTermVal(termVal)
                        .withTermScale('M')
                        .withExpDate(null)
                        .build()).build();
        LocalDate expected = LocalDate.now().plusMonths(termVal);

        depositListener.prePersist(deposit);

        assertThat(expected).isEqualTo(deposit.getDepInfo().getExpDate());
    }

    @Test
    @DisplayName("test prePersist should throw NotValidRequestParametersException with expected message")
    void testPrePersistShouldThrowNotValidRequestParametersException() {
        Deposit deposit = DepositTestBuilder.aDeposit()
                .withAccInfo(AccInfoTestBuilder.aAccInfo()
                        .withAccOpenDate(null)
                        .build())
                .withDepInfo(DepInfoTestBuilder.aDepInfo()
                        .withTermScale('Z')
                        .build())
                .build();
        String expectedMessage = "Invalid termScale: %s".formatted(deposit.getDepInfo().getTermScale());

        Exception exception = assertThrows(NotValidRequestParametersException.class, () -> depositListener.prePersist(deposit));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}
