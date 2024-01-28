package ru.clevertec.bank.product.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositFilterRequest;
import ru.clevertec.bank.product.domain.entity.Deposit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class DepositSpecification implements Specification<Deposit> {

    private transient DepositFilterRequest request;

    @Override
    public Predicate toPredicate(@NonNull Root<Deposit> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(request.accIban())) {
            predicates.add(criteriaBuilder.equal(root.get("accInfo").get("accIban"), request.accIban()));
        }
        if (Objects.nonNull(request.amount())) {
            if (request.greaterThan()) {
                predicates.add(criteriaBuilder.greaterThan(root.get("accInfo").get("currAmount"), request.amount()));
            } else {
                predicates.add(criteriaBuilder.lessThan(root.get("accInfo").get("currAmount"), request.amount()));
            }
        }
        if (Objects.nonNull(request.currency())) {
            predicates.add(criteriaBuilder.equal(root.get("accInfo").get("currAmountCurrency"), request.currency()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}
