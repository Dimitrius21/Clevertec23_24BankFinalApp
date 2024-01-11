package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import ru.clevertec.bank.product.util.CustomerType;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "credits")
public class Credit implements IdentifierGenerator {

    @Column(name = "customer_id")
    private UUID customerId;

    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = Credit.class)
    @Column(name = "contract_number")
    private String contractNumber;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Column(name = "total_debt")
    private Long totalDebt;

    @Column(name = "current_debt")
    private Long currentDebt;

    @Column(name = "currency")
    private String currency;

    @Column(name = "repayment_date")
    private LocalDate repaymentDate;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "iban")
    private String iban;
    @Column(name = "possible_repayment")
    private Boolean possibleRepayment;
    @Column(name = "is_closed")
    private Boolean isClosed;

    @Column(name = "customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        Credit credit = (Credit) object;
        return credit.getContractNumber();
    }
}
