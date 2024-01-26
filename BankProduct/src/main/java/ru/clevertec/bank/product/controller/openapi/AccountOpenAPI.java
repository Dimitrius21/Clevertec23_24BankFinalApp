package ru.clevertec.bank.product.controller.openapi;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.bank.product.domain.dto.account.request.AccountInDto;
import ru.clevertec.bank.product.domain.dto.account.response.AccountFullOutDto;
import ru.clevertec.bank.product.domain.dto.account.response.AccountOutDto;
import ru.clevertec.exceptionhandler.domain.ErrorInfo;

import java.util.List;
import java.util.UUID;

public interface AccountOpenAPI {
    @Operation(summary = "Find Account by Iban",
            description = "Get the date about Account by specifying Iban number.",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @Parameter(in = ParameterIn.PATH, name = "iban", description = "Iban number")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = AccountOutDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Content hasn't found for submitted ID",
                    content = {@Content(schema = @Schema(implementation = ErrorInfo.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "No authentication, no Jwt-token has not been submitted or incorrect"),
            @ApiResponse(responseCode = "403", description = "There isn't rights for this operation for submitted role"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(schema = @Schema())})})
    public ResponseEntity<AccountOutDto> getById(@PathVariable String iban);

    @Operation(summary = "Find Accounts of customer",
            description = "Get a list of accounts for customer with specifying UUID.")
    @Parameter(in = ParameterIn.PATH, name = "uuid", description = "uuid of customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = AccountOutDto.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "No authentication, no Jwt-token has not been submitted or incorrect"),
            @ApiResponse(responseCode = "403", description = "There isn't rights for this operation for submitted role"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(schema = @Schema())})})
    public ResponseEntity<List<AccountOutDto>> getByCustomerId(@PathVariable UUID id);


    @Operation(summary = "Get all account",
            description = "Get the list of all accounts with linked cards)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @Parameter(name = "pageable", hidden = true)
    @PageableAsQueryParam
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = AccountFullOutDto.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "No authentication, no Jwt-token has not been submitted or incorrect"),
            @ApiResponse(responseCode = "403", description = "There isn't rights for this operation for submitted role"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(schema = @Schema())})})
    public ResponseEntity<List<AccountFullOutDto>> getAll(Pageable pageable);


    @Operation(summary = "Delete Account by Iban",
            description = "Delete from DB the account with a specifying Iban number.",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @Parameter(in = ParameterIn.PATH, name = "iban", description = "Iban number")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "401", description = "No authentication, no Jwt-token has not been submitted or incorrect"),
            @ApiResponse(responseCode = "403", description = "There isn't rights for this operation for submitted role"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(schema = @Schema())})})
    public void delete(@PathVariable String iban);

    @Operation(summary = "Create(save) Account",
            description = "Create(save) submitted Account.",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Account for save",
            content = {@Content(schema = @Schema(implementation = AccountInDto.class), mediaType = "application/json")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = AccountOutDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Data in request body is not correct",
                    content = {@Content(schema = @Schema(implementation = ErrorInfo.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "No authentication, no Jwt-token has not been submitted or incorrect"),
            @ApiResponse(responseCode = "403", description = "There isn't rights for this operation for submitted role"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(schema = @Schema())})})
    public ResponseEntity<AccountOutDto> create(@RequestBody @Valid AccountInDto dtoIn);


    @Operation(summary = "Update Account",
            description = "Update Account to submitted one (only possible fields).",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Comment for update",
            content = {@Content(schema = @Schema(implementation = AccountInDto.class), mediaType = "application/json")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = AccountOutDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Data in request body is not correct",
                    content = {@Content(schema = @Schema(implementation = ErrorInfo.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "No authentication, no Jwt-token has not been submitted or incorrect"),
            @ApiResponse(responseCode = "403", description = "There isn't rights for this operation for submitted role"),
            @ApiResponse(responseCode = "404", description = "There's no Account with submitted Iban for update",
                    content = {@Content(schema = @Schema(implementation = ErrorInfo.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(schema = @Schema())})})
    public ResponseEntity<AccountOutDto> update(@RequestBody AccountInDto inDto);
}