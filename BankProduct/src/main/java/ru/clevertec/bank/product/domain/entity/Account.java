package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import ru.clevertec.bank.product.util.CustomerType;
import ru.clevertec.bank.product.util.StringIdEmptyGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account implements IdentifierGenerator {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = Account.class)
    @Column(name = "iban")
    private String iban;
    @Column(name = "name")
    private String name;
    @Column(name = "amount")
    private long amount;
    @Column(name = "currency_code")
    private String currencyCode;
    @Column(name = "open_date")
    private LocalDate openDate;
    @Column(name = "main_acc")
    private boolean mainAcc;
    @Column(name = "customer_id")
    private UUID customerId;
    @Column(name = "customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
    @Column(name = "rate")
    private double rate;

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        Account acc = (Account) object;
        return acc.getIban();
    }
}
