package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ru.clevertec.bank.product.domain.entity.util.DepositIdentifierGenerator;
import ru.clevertec.bank.product.domain.entity.util.DepositListener;
import ru.clevertec.bank.product.util.CustomerType;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "deposits")
@EntityListeners(DepositListener.class)
public class Deposit implements Serializable {

    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = DepositIdentifierGenerator.class)
    private String accIban;

    private UUID customerId;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    @Embedded
    private AccInfo accInfo;

    @Embedded
    private DepInfo depInfo;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Deposit deposit = (Deposit) object;
        return Objects.equals(accIban, deposit.accIban) && Objects.equals(customerId, deposit.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accIban, customerId);
    }

}
