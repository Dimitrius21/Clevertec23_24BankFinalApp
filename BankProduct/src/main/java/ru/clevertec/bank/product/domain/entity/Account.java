package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import ru.clevertec.bank.product.util.CustomerType;
import ru.clevertec.bank.product.util.StringIdEmptyGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
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

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY )
    @BatchSize(size = 50)
    private List<Card> cards;

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        Account acc = (Account) object;
        return acc.getIban();
    }
}
