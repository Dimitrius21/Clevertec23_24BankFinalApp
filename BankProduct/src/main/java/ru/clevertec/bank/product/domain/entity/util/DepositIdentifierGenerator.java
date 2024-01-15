package ru.clevertec.bank.product.domain.entity.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import ru.clevertec.bank.product.domain.entity.Deposit;

public class DepositIdentifierGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        Deposit deposit = (Deposit) o;
        return deposit.getAccIban();
    }

}
