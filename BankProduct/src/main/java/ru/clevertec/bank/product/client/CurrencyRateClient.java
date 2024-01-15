package ru.clevertec.bank.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.clevertec.bank.product.domain.entity.RateFeign;

@FeignClient(name = "${client.name}", url = "${client.url.rate}")
public interface CurrencyRateClient {

    @GetMapping
    RateFeign getCurrent();

}
