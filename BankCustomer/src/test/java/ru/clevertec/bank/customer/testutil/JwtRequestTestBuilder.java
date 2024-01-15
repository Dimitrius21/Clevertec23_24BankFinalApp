package ru.clevertec.bank.customer.testutil;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.bank.customer.domain.dto.JwtRequest;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aJwtRequest")
@With
public class JwtRequestTestBuilder implements TestBuilder<JwtRequest> {

    private String id = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc929";
    private String role = "USER";

    @Override
    public JwtRequest build() {
        return new JwtRequest(id, role);
    }

}
