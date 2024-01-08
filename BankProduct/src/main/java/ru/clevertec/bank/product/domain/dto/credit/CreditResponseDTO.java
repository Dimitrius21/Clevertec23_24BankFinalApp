package ru.clevertec.bank.product.domain.dto.credit;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreditResponseDTO {

    private long id;
    private UUID customerId;
    private String contractNumber;
    private LocalDate contractStartDate;
    private long totalDebt;
    private long currentDebt;
    private String currency;
    private LocalDate repaymentDate;
    private double rate;
    private String iban;
    private boolean possibleRepayment;
    private boolean isClosed; 
    private String customerType; 

    public void setIsClosed(boolean closed) {
        isClosed = closed;
    }

    public boolean isIsClosed() {
        return isClosed;
    }
}
