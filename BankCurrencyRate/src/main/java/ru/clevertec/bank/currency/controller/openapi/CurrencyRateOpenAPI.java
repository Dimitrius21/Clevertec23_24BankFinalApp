package ru.clevertec.bank.currency.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.bank.currency.domain.dto.RatesInDto;
import ru.clevertec.bank.currency.domain.dto.RatesOutDto;

import java.time.ZonedDateTime;

public interface CurrencyRateOpenAPI {
    @Operation(summary = "Get current rates",
            description = "Get the exchange rates for the present time.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = RatesOutDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Inner server error",
                    content = {@Content(schema = @Schema())})})
    public ResponseEntity<RatesOutDto> getCurrent();

    @Operation(summary = "Get current rates for time",
            description = "Get the exchange rates at the given moment of time")
    @Parameter(in = ParameterIn.PATH, name = "time", description = "the given moment of time")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = RatesOutDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Incorrect input data",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Inner server error",
                    content = {@Content(schema = @Schema())})})
    public ResponseEntity<RatesOutDto> getLastForTime(@PathVariable ZonedDateTime time);


    @Operation(summary = "To create(save) rates",
            description = "To create(save) list of the exchange rate at the indicated moment of time.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "the exchange rates",
            content = {@Content(schema = @Schema(implementation = RatesInDto.class), mediaType = "application/json")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = RatesOutDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Incorrect input data",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", description = "Inner server error",
                    content = {@Content(schema = @Schema())})})
    public ResponseEntity<RatesOutDto> create(@RequestBody RatesInDto inDto);
}

