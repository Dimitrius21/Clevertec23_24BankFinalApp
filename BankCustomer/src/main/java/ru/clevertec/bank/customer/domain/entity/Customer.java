package ru.clevertec.bank.customer.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.clevertec.bank.customer.domain.entity.listener.CustomerListener;
import ru.clevertec.bank.customer.util.CustomerType;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(CustomerListener.class)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    private String unp;
    private LocalDate registerDate;
    private String email;
    private String phoneCode;
    private String phoneNumber;
    private String customerFullName;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Customer customer = (Customer) object;
        return Objects.equals(customerId, customer.customerId) && Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, email);
    }

}
