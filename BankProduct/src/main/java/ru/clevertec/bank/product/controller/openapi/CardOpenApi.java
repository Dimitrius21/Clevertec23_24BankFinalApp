package ru.clevertec.bank.product.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.bank.product.domain.dto.card.request.CardRequest;
import ru.clevertec.bank.product.domain.dto.card.request.CardUpdateRequest;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponse;
import ru.clevertec.bank.product.domain.dto.card.response.CardResponseWithAmount;
import ru.clevertec.exceptionhandler.domain.ErrorInfo;
import ru.clevertec.exceptionhandler.domain.ValidationExceptionResponse;

import java.util.UUID;

@Validated
@Tag(name = "Card", description = "The Card Api")
public interface CardOpenApi {

    @Operation(summary = "Save new Card.", tags = "Card",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            requestBody = @RequestBody(description = "RequestBody for CardRequest",
                    content = @Content(schema = @Schema(implementation = CardRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "card_number": "5645256445895632",
                                      "card_number_readable": "5645 2564 4589 5632",
                                      "iban": "AZDEFGBNMKIOLPHNADTYHNBDFGH",
                                      "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                      "customer_type": "LEGAL",
                                      "cardholder": "Сергей Сергеев Сергеевич",
                                      "card_status": "ACTIVE"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card saved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardResponse.class), examples = @ExampleObject("""
                            {
                              "card_number": "5645256445895632",
                              "iban": "AZDEFGBNMKIOLPHNADTYHNBDFGH",
                              "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                              "customer_type": "LEGAL",
                              "cardholder": "Сергей Сергеев Сергеевич",
                              "cardStatus": "ACTIVE"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Card with iban is not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class),
                            examples = @ExampleObject("""
                                    {
                                      "errorCode": 404,
                                      "errorMessage": "Account with iban=AZDEFGBNMKIOLPHNADTYHNBDFGH not found"
                                    }
                                    """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class),
                            examples = @ExampleObject("""
                                    {
                                      "violations": [
                                        {
                                          "field_name": "cardStatus",
                                          "message": "must match \\"ACTIVE|INACTIVE|BLOCKED|NEW\\""
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<CardResponse> save(@Valid CardRequest request);

    @Operation(summary = "Get Card by card number.", tags = "Card",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter card number here", example = "5645256445895632"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardResponseWithAmount.class), examples = @ExampleObject("""
                            {
                              "cardNumber": "5645256445895632",
                              "iban": "AZDEFGBNMKIOLPHNADTYHNBDFGH",
                              "customerId": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                              "customerType": "LEGAL",
                              "cardholder": "Сергей Сергеев Сергеевич",
                              "cardStatus": "ACTIVE",
                              "amounts": [
                              {
                                "amount": "2.7",
                                "currency": "EUR"
                              },
                              {
                                "amount": "2.3",
                                "currency": "USD"
                              }
                              ]
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Card with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class), examples = @ExampleObject("""
                            {
                              "errorCode": 404,
                              "errorMessage": "Card with id=5645256445895631 not found"
                            }
                            """)))
    })
    ResponseEntity<CardResponseWithAmount> getById(String id);

    @Operation(summary = "Find all Cards with pagination.", tags = "Card",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = {
                    @Parameter(name = "page", description = "Enter your page number here", example = "0"),
                    @Parameter(name = "size", description = "Enter your page size here", example = "1")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of Cards retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardResponse.class), examples = @ExampleObject("""
                            {
                              "content": [
                                {
                                  "cardNumber": "5645256445895632",
                                  "iban": "AZDEFGBNMKIOLPHNADTYHNBDFGH",
                                  "customerId": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                  "customerType": "LEGAL",
                                  "cardholder": "Сергей Сергеев Сергеевич",
                                  "cardStatus": "ACTIVE"
                                }
                              ],
                              "pageable": {
                                "pageNumber": 0,
                                "pageSize": 1,
                                "sort": [],
                                "offset": 0,
                                "paged": true,
                                "unpaged": false
                              },
                              "last": true,
                              "totalElements": 1,
                              "totalPages": 1,
                              "size": 1,
                              "number": 0,
                              "sort": [],
                              "first": true,
                              "numberOfElements": 1,
                              "empty": false
                            }
                            """)))
    })
    ResponseEntity<Page<CardResponse>> getAll(int page, int size);

    @Operation(summary = "Get Card by customer id.", tags = "Card",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter customer id here", example = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardResponseWithAmount.class), examples = @ExampleObject("""
                            {
                              "cardNumber": "5645256445895632",
                              "iban": "AZDEFGBNMKIOLPHNADTYHNBDFGH",
                              "customerId": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                              "customerType": "LEGAL",
                              "cardholder": "Сергей Сергеев Сергеевич",
                              "cardStatus": "ACTIVE"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Card with this customer id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class), examples = @ExampleObject("""
                            {
                              "errorCode": 404,
                              "errorMessage": "Card with customer uuid=1a72a05f-4b8f-43c5-a889-1ebc6d9dc729 not found"
                            }
                            """)))
    })
    ResponseEntity<CardResponse> getByClientId(UUID id);

    @Operation(summary = "Update Card by card number.", tags = "Card",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter card number here", example = "5645256445895632"),
            requestBody = @RequestBody(description = "RequestBody for CardUpdateRequest",
                    content = @Content(schema = @Schema(implementation = CardUpdateRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "iban": "AZDEFGBNMKIOLPHNADTYHNBDFGH",
                                      "customer_type": "LEGAL",
                                      "card_status": "INACTIVE"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardResponse.class), examples = @ExampleObject("""
                            {
                              "cardNumber": "5645256445895632",
                              "iban": "AZDEFGBNMKIOLPHNADTYHNBDFGH",
                              "customerId": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                              "customerType": "LEGAL",
                              "cardholder": "Сергей Сергеев Сергеевич",
                              "cardStatus": "INACTIVE"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Card with this card number in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class), examples = @ExampleObject("""
                            {
                              "errorCode": 404,
                              "errorMessage": "Card with id=5645256445895631 not found"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationExceptionResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "violations": [
                                        {
                                          "field_name": "cardStatus",
                                          "message": "must match \\"ACTIVE|INACTIVE|BLOCKED|NEW\\""
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<CardResponse> update(String id, @Valid CardUpdateRequest request);

    @Operation(summary = "Delete Card by card number.", tags = "Card",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter card number here", example = "5645256445895632"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class), examples = @ExampleObject("5645256445895632"))),
            @ApiResponse(responseCode = "404", description = "No Card with this card number in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class), examples = @ExampleObject("""
                            {
                              "errorCode": 404,
                              "errorMessage": "Card with id=5645256445895631 not found"
                            }
                            """)))
    })
    ResponseEntity<String> deleteById(String id);

}
