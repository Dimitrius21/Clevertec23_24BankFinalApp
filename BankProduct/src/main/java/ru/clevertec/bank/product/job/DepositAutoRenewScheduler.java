package ru.clevertec.bank.product.job;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.clevertec.bank.product.service.DepositService;

@Component
@RequiredArgsConstructor
public class DepositAutoRenewScheduler {

    private final DepositService depositService;

    @Scheduled(cron = "${scheduler.autoRenew.cron}")
    public void autoRenew() {
        depositService.updateExpDate();
    }

}
