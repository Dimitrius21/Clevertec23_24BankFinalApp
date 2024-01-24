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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepInfoUpdateRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositFilterRequest;
import ru.clevertec.bank.product.domain.dto.deposit.request.DepositInfoRequest;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepositInfoResponse;
import ru.clevertec.exceptionhandler.domain.ErrorInfo;
import ru.clevertec.exceptionhandler.domain.ValidationExceptionResponse;

@Validated
@Tag(name = "Deposit", description = "The Deposit Api")
public interface DepositOpenApi {

    @Operation(summary = "Find Deposit by iban.", tags = "Deposit",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "iban", description = "Enter iban here", example = "FR7630001007941234567890185"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepositInfoResponse.class), examples = @ExampleObject("""
                            {
                              "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                              "customer_type": "LEGAL",
                              "acc_info": {
                                "acc_iban": "FR7630001007941234567890185",
                                "acc_open_date": "01.01.2024",
                                "curr_amount": 10000.00,
                                "curr_amount_currency": "EUR"
                              },
                              "dep_info": {
                                "rate": 0.05,
                                "term_val": 12,
                                "term_scale": "D",
                                "exp_date": "13.01.2024",
                                "dep_type": "REVOCABLE",
                                "auto_renew": true
                              }
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Deposit with this iban in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class), examples = @ExampleObject("""
                            {
                              "errorCode": 404,
                              "errorMessage": "Deposit with iban FR763000100794123456789018 is not found"
                            }
                            """)))
    })
    ResponseEntity<DepositInfoResponse> findByIban(String iban);

    @Operation(summary = "Find all Deposits with pagination.", tags = "Deposit",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = {
                    @Parameter(name = "page", description = "Enter your page number here", example = "0"),
                    @Parameter(name = "size", description = "Enter your page size here", example = "2"),
                    @Parameter(name = "sort", description = "Enter your sort by here",
                            schema = @Schema(type = "array", example = "[\"depInfo.expDate\"]"))
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of Customers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepositInfoResponse.class), examples = @ExampleObject("""
                            {
                              "content": [
                                {
                                  "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc721",
                                  "customer_type": "LEGAL",
                                  "acc_info": {
                                    "acc_iban": "SA0380000000608010167519",
                                    "acc_open_date": "04.01.2024",
                                    "curr_amount": 100000.44,
                                    "curr_amount_currency": "SAR"
                                  },
                                  "dep_info": {
                                    "rate": 0.04,
                                    "term_val": 1,
                                    "term_scale": "D",
                                    "exp_date": "02.01.2024",
                                    "dep_type": "IRREVOCABLE",
                                    "auto_renew": false
                                  }
                                },
                                {
                                  "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                  "customer_type": "LEGAL",
                                  "acc_info": {
                                    "acc_iban": "FR7630001007941234567890185",
                                    "acc_open_date": "01.01.2024",
                                    "curr_amount": 10000.00,
                                    "curr_amount_currency": "EUR"
                                  },
                                  "dep_info": {
                                    "rate": 0.05,
                                    "term_val": 12,
                                    "term_scale": "D",
                                    "exp_date": "13.01.2024",
                                    "dep_type": "REVOCABLE",
                                    "auto_renew": true
                                  }
                                }
                              ],
                              "pageable": {
                                "pageNumber": 0,
                                "pageSize": 2,
                                "sort": [
                                  {
                                    "direction": "ASC",
                                    "property": "depInfo.expDate",
                                    "ignoreCase": false,
                                    "nullHandling": "NATIVE",
                                    "ascending": true,
                                    "descending": false
                                  }
                                ],
                                "offset": 0,
                                "unpaged": false,
                                "paged": true
                              },
                              "last": false,
                              "totalPages": 5,
                              "totalElements": 10,
                              "size": 2,
                              "number": 0,
                              "sort": [
                                {
                                  "direction": "ASC",
                                  "property": "depInfo.expDate",
                                  "ignoreCase": false,
                                  "nullHandling": "NATIVE",
                                  "ascending": true,
                                  "descending": false
                                }
                              ],
                              "first": true,
                              "numberOfElements": 2,
                              "empty": false
                            }
                            """))),
            @ApiResponse(responseCode = "406", description = "Wrong pageable sort",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class), examples = @ExampleObject("""
                            {
                              "errorCode": 406,
                              "errorMessage": "No property 'epDate' found for type 'DepInfo'; Did you mean 'expDate'; Traversed path: Deposit.depInfo"
                            }
                            """)))
    })
    ResponseEntity<Page<DepositInfoResponse>> findAll(@ParameterObject Pageable pageable);

    @Operation(summary = "Find all Deposits by filter with pagination.", tags = "Deposit",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = {
                    @Parameter(name = "accIban", description = "Enter your accIban here", example = "FR7630001007941234567890185"),
                    @Parameter(name = "greaterThan", description = "Enter your greaterThan(true or false) here", example = "true"),
                    @Parameter(name = "amount", description = "Enter your amount here", example = "1000"),
                    @Parameter(name = "currency", description = "Enter your currency here", example = "EUR"),
                    @Parameter(name = "page", description = "Enter your page number here", example = "0"),
                    @Parameter(name = "size", description = "Enter your page size here", example = "2"),
                    @Parameter(name = "sort", description = "Enter your sort by here",
                            schema = @Schema(type = "array", example = "[\"depInfo.expDate\"]"))
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of Customers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepositInfoResponse.class), examples = @ExampleObject("""
                            {
                              "content": [
                                {
                                  "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                  "customer_type": "LEGAL",
                                  "acc_info": {
                                    "acc_iban": "FR7630001007941234567890185",
                                    "acc_open_date": "01.01.2024",
                                    "curr_amount": 10000.00,
                                    "curr_amount_currency": "EUR"
                                  },
                                  "dep_info": {
                                    "rate": 0.05,
                                    "term_val": 12,
                                    "term_scale": "D",
                                    "exp_date": "13.01.2024",
                                    "dep_type": "REVOCABLE",
                                    "auto_renew": true
                                  }
                                }
                              ],
                              "pageable": {
                                "pageNumber": 0,
                                "pageSize": 2,
                                "sort": [
                                  {
                                    "direction": "ASC",
                                    "property": "depInfo.expDate",
                                    "ignoreCase": false,
                                    "nullHandling": "NATIVE",
                                    "ascending": true,
                                    "descending": false
                                  }
                                ],
                                "offset": 0,
                                "unpaged": false,
                                "paged": true
                              },
                              "last": true,
                              "totalPages": 1,
                              "totalElements": 1,
                              "size": 2,
                              "number": 0,
                              "sort": [
                                {
                                  "direction": "ASC",
                                  "property": "depInfo.expDate",
                                  "ignoreCase": false,
                                  "nullHandling": "NATIVE",
                                  "ascending": true,
                                  "descending": false
                                }
                              ],
                              "first": true,
                              "numberOfElements": 1,
                              "empty": false
                            }
                            """))),
            @ApiResponse(responseCode = "406", description = "Wrong pageable sort",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class), examples = @ExampleObject("""
                            {
                              "errorCode": 406,
                              "errorMessage": "No property 'epDate' found for type 'DepInfo'; Did you mean 'expDate'; Traversed path: Deposit.depInfo"
                            }
                            """)))
    })
    ResponseEntity<Page<DepositInfoResponse>> findAllByFilter(@ParameterObject DepositFilterRequest request,
                                                              @ParameterObject Pageable pageable);

    @Operation(summary = "Save new Deposit.", tags = "Deposit",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            requestBody = @RequestBody(description = "RequestBody for DepositInfoRequest",
                    content = @Content(schema = @Schema(implementation = DepositInfoRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                      "customer_type": "LEGAL",
                                      "acc_info": {
                                        "acc_iban": "AABBCCCDDDDEEEEEEEEEEEEEEEEE",
                                        "curr_amount": 3000.00,
                                        "curr_amount_currency": "BYN"
                                      },
                                      "dep_info": {
                                        "rate": 14.50,
                                        "term_val": 24,
                                        "term_scale": "M",
                                        "dep_type": "REVOCABLE",
                                        "auto_renew": true
                                      }
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Deposit saved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepositInfoResponse.class), examples = @ExampleObject("""
                            {
                              "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                              "customer_type": "LEGAL",
                              "acc_info": {
                                "acc_iban": "AABBCCCDDDDEEEEEEEEEEEEEEEEE",
                                "acc_open_date": "17.01.2024",
                                "curr_amount": 3000.00,
                                "curr_amount_currency": "BYN"
                              },
                              "dep_info": {
                                "rate": 14.50,
                                "term_val": 24,
                                "term_scale": "M",
                                "exp_date": "17.01.2026",
                                "dep_type": "REVOCABLE",
                                "auto_renew": true
                              }
                            }
                            """))),
            @ApiResponse(responseCode = "400", description = "Deposit with iban already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class),
                            examples = @ExampleObject("""
                                    {
                                      "errorCode": 400,
                                      "errorMessage": "Deposit with such acc_iban is already exist"
                                    }
                                    """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class),
                            examples = @ExampleObject("""
                                    {
                                      "violations": [
                                        {
                                          "field_name": "depInfo.termScale",
                                          "message": "Acceptable termScales are only: D(days) or M(months)"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<DepositInfoResponse> save(@Valid DepositInfoRequest request);

    @Operation(summary = "Update Deposit by iban.", tags = "Deposit",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "iban", description = "Enter iban here", example = "FR7630001007941234567890185"),
            requestBody = @RequestBody(description = "RequestBody for DepInfoUpdateRequest",
                    content = @Content(schema = @Schema(implementation = DepInfoUpdateRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "dep_type": "IRREVOCABLE",
                                      "auto_renew": false
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepositInfoResponse.class), examples = @ExampleObject("""
                            {
                              "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                              "customer_type": "LEGAL",
                              "acc_info": {
                                "acc_iban": "FR7630001007941234567890185",
                                "acc_open_date": "01.01.2024",
                                "curr_amount": 10000.00,
                                "curr_amount_currency": "EUR"
                              },
                              "dep_info": {
                                "rate": 0.05,
                                "term_val": 12,
                                "term_scale": "D",
                                "exp_date": "13.01.2024",
                                "dep_type": "IRREVOCABLE",
                                "auto_renew": false
                              }
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Deposit with this iban in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class), examples = @ExampleObject("""
                            {
                              "errorCode": 404,
                              "errorMessage": "Deposit with iban FR763000100794123456789018 is not found"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationExceptionResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "violations": [
                                        {
                                          "field_name": "depType",
                                          "message": "Acceptable depTypes are only: REVOCABLE or IRREVOCABLE"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<DepositInfoResponse> updateByIban(String iban, @Valid DepInfoUpdateRequest request);

    @Operation(summary = "Delete Deposit by iban.", tags = "Deposit",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "iban", description = "Enter iban here", example = "FR7630001007941234567890185"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeleteResponse.class), examples = @ExampleObject("""
                            {
                              "message": "Deposit with iban FR7630001007941234567890185 was successfully deleted"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Deposit with this iban in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class), examples = @ExampleObject("""
                            {
                              "errorCode": 404,
                              "errorMessage": "Deposit with iban FR7630001007941234567890185 is not found"
                            }
                            """)))
    })
    ResponseEntity<DeleteResponse> deleteByIban(String iban);

}
