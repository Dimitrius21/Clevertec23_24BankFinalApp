package ru.clevertec.bank.customer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.bank.customer.domain.dto.JwtRequest;
import ru.clevertec.bank.customer.domain.dto.JwtResponse;
import ru.clevertec.bank.customer.testutil.JwtRequestTestBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;
    private static final String SECRET_KEY = "73357638792F423F4528482B4D6251655468576D5A7133743677397A24432646";
    private static final Long EXPIRATION = 86400000L;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET_KEY, EXPIRATION);
    }

    @Test
    @DisplayName("test generateJwt should return expected response")
    void testGenerateJwtShouldReturnExpectedResponse() {
        JwtRequest request = JwtRequestTestBuilder.aJwtRequest().build();

        JwtResponse response = jwtService.generateJwt(request);

        assertThat(response.jwt()).isNotBlank();
    }

    @Test
    @DisplayName("test extractId should return expected id")
    void testExtractIdShouldReturnExpectedId() {
        JwtRequest request = JwtRequestTestBuilder.aJwtRequest().build();

        JwtResponse response = jwtService.generateJwt(request);
        UUID id = jwtService.extractId(response.jwt());

        assertThat(id).hasToString(request.id());
    }

    @Test
    @DisplayName("test extractRole should return expected role")
    void testExtractRoleShouldReturnExpectedRole() {
        JwtRequest request = JwtRequestTestBuilder.aJwtRequest().build();

        JwtResponse response = jwtService.generateJwt(request);
        String role = jwtService.extractRole(response.jwt());

        assertThat(role).isEqualTo(request.role());
    }

    @Test
    @DisplayName("test isTokenValid should return true")
    void testIsTokenValidShouldReturnTrue() {
        JwtRequest request = JwtRequestTestBuilder.aJwtRequest().build();

        JwtResponse response = jwtService.generateJwt(request);
        Boolean isTokenValid = jwtService.isTokenValid(response.jwt(), UUID.fromString(request.id()));

        assertThat(isTokenValid).isTrue();
    }

    @Test
    @DisplayName("test isTokenValid should return false")
    void testIsTokenValidShouldReturnFalse() {
        JwtRequest request = JwtRequestTestBuilder.aJwtRequest().build();

        JwtResponse response = jwtService.generateJwt(request);
        Boolean isTokenValid = jwtService.isTokenValid(response.jwt(), UUID.randomUUID());

        assertThat(isTokenValid).isFalse();
    }

    @Test
    @DisplayName("test isTokenExpired should return false")
    void testIsTokenExpiredShouldReturnFalse() {
        JwtRequest request = JwtRequestTestBuilder.aJwtRequest().build();

        JwtResponse response = jwtService.generateJwt(request);
        Boolean isTokenExpired = jwtService.isTokenExpired(response.jwt());

        assertThat(isTokenExpired).isFalse();
    }

    @Test
    @DisplayName("test extractExpiration should return expected LocalDateTime")
    void testExtractExpirationShouldReturnExpectedLocalDateTime() {
        JwtRequest request = JwtRequestTestBuilder.aJwtRequest().build();

        JwtResponse response = jwtService.generateJwt(request);
        LocalDateTime expirationTime = jwtService.extractExpiration(response.jwt());

        LocalDateTime now = LocalDateTime.now();
        assertAll(
                () -> assertThat(expirationTime.getYear()).isEqualTo(now.getYear()),
                () -> assertThat(expirationTime.getMonthValue()).isEqualTo(now.getMonthValue()),
                () -> assertThat(expirationTime.getDayOfMonth()).isEqualTo(now.plusDays(1).getDayOfMonth()),
                () -> assertThat(expirationTime.getHour()).isEqualTo(now.getHour())
        );
    }

}
