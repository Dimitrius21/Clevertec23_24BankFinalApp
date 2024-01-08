package ru.clevertec.bank.product.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "deposits")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "term_val")
    private Integer termVal;

    @Column(name = "term_scale")
    private Character termScale;

    @Column(name = "exp_date")
    private LocalDate expDate;

    @Column(name = "dep_type")
    private String depType;

    @Column(name = "auto_renew")
    private Boolean autoRenew;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Deposit deposit = (Deposit) object;
        return Objects.equals(id, deposit.id) && Objects.equals(termVal, deposit.termVal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, termVal);
    }

}
