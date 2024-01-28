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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.bank.product.domain.dto.account.request.AccountInDto;
import ru.clevertec.bank.product.domain.dto.account.response.AccountFullOutDto;
import ru.clevertec.bank.product.domain.dto.account.response.AccountOutDto;
import ru.clevertec.exceptionhandler.domain.ErrorInfo;

import java.util.List;
import java.util.UUID;

@Validated
@Tag(name = "Account", description = "The Account Api")
public interface AccountOpenAPI {
    @Operation(summary = "Find Account by Iban", tags = "Account",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            description = "Get the date about Account by specifying Iban number.")
    @Parameter(in = ParameterIn.PATH, name = "iban", description = "Iban number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = AccountOutDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Content hasn't found for submitted ID",
                    content = {@Content(schema = @Schema(implementation = ErrorInfo.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "No authentication, no Jwt-token has not been submitted or incorrect"),
            @ApiResponse(responseCode = "403", description = "There isn't rights for this operation for submitted role"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(schema = @Schema())})})
    ResponseEntity<AccountOutDto> getById(String iban);

    @Operation(summary = "Find Accounts of customer", tags = "Account",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            description = "Get a list of accounts for customer with specifying UUID.")
    @Parameter(in = ParameterIn.PATH, name = "uuid", description = "uuid of customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = AccountOutDto.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "No authentication, no Jwt-token has not been submitted or incorrect"),
            @ApiResponse(responseCode = "403", description = "There isn't rights for this operation for submitted role"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(schema = @Schema())})})
    ResponseEntity<List<AccountOutDto>> getByCustomerId(UUID id);


    @Operation(summary = "Get all account", tags = "Account",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            description = "Get the list of all accounts with linked cards)")
    @Parameter(name = "pageable", hidden = true)
    @PageableAsQueryParam
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = AccountFullOutDto.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "No authentication, no Jwt-token has not been submitted or incorrect"),
            @ApiResponse(responseCode = "403", description = "There isn't rights for this operation for submitted role"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(schema = @Schema())})})
    ResponseEntity<List<AccountFullOutDto>> getAll(Pageable pageable);


    @Operation(summary = "Delete Account by Iban", tags = "Account",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            description = "Delete from DB the account with a specifying Iban number.")
    @Parameter(in = ParameterIn.PATH, name = "iban", description = "Iban number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "401", description = "No authentication, no Jwt-token has not been submitted or incorrect"),
            @ApiResponse(responseCode = "403", description = "There isn't rights for this operation for submitted role"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(schema = @Schema())})})
    void delete(String iban);

    @Operation(summary = "Create(save) Account", tags = "Account",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            description = "Create(save) submitted Account.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Account for save",
            content = {@Content(schema = @Schema(implementation = AccountInDto.class), mediaType = "application/json")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = AccountOutDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Data in request body is not correct",
                    content = {@Content(schema = @Schema(implementation = ErrorInfo.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "No authentication, no Jwt-token has not been submitted or incorrect"),
            @ApiResponse(responseCode = "403", description = "There isn't rights for this operation for submitted role"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(schema = @Schema())})})
    ResponseEntity<AccountOutDto> create(@Valid AccountInDto dtoIn);


    @Operation(summary = "Update Account", tags = "Account",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            description = "Update Account to submitted one (only possible fields).")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Comment for update",
            content = {@Content(schema = @Schema(implementation = AccountInDto.class), mediaType = "application/json")})
    @ApiResponses(value = {
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
    ResponseEntity<AccountOutDto> update(AccountInDto inDto);

}