package ru.clevertec.bank.product.domain.dto.credit;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCreditDTO {

    private long currentDebt;
    private LocalDate repaymentDate;
    private double rate;
    private boolean possibleRepayment;
    private boolean isClosed;

    public void setIsClosed(boolean closed) {
        isClosed = closed;
    }

    public boolean isIsClosed() {
        return isClosed;
    }
}
