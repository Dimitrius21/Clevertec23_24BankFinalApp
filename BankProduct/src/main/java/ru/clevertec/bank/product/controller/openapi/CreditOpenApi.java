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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.bank.product.domain.dto.DeleteResponse;
import ru.clevertec.bank.product.domain.dto.credit.request.CreateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.request.UpdateCreditDTO;
import ru.clevertec.bank.product.domain.dto.credit.response.CreditResponseDTO;
import ru.clevertec.bank.product.domain.dto.deposit.response.DepositInfoResponse;
import ru.clevertec.exceptionhandler.domain.ErrorInfo;

import java.util.List;
import java.util.UUID;

@Validated
@Tag(name = "Credit", description = "The Credit Api")
public interface CreditOpenApi {

    @Operation(summary = "Find Credit by creditNumber.", tags = "Credit",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "contractNumber", description = "Enter contractNumber here", example = "11-0216444-2-0"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreditResponseDTO.class), examples = @ExampleObject("""
                            {
                              "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                              "contractNumber": "11-0216444-2-0",
                              "contractStartDate": "30.03.2022",
                              "totalDebt": 8113.99,
                              "currentDebt": 361.99,
                              "currency": "BYN",
                              "repaymentDate": "16.01.2023",
                              "rate": 22.8,
                              "iban": "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                              "possibleRepayment": true,
                              "isClosed": false,
                              "customer_type" : "LEGAL"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Credit with this contractNumber in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class), examples = @ExampleObject("""
                            {
                              "errorCode": 404,
                              "errorMessage": "Credit with contractNumber = 11-0216444-2-0 not found"
                            }
                            """)))
    })
    ResponseEntity<CreditResponseDTO> findByContractNumber(String contractNumber);

    @Operation(summary = "Find all Credits with pagination.", tags = "Credit",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = {
                    @Parameter(name = "page", description = "Enter your page number here", example = "0"),
                    @Parameter(name = "size", description = "Enter your page size here", example = "2")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of Credits retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreditResponseDTO.class), examples = @ExampleObject("""
                            {
                              "content": [
                                {
                                  "contractNumber": "11-0216444-2-0",
                                  "contractStartDate": "01.01.2023",
                                  "totalDebt": 8113.99,
                                  "currentDebt": 361.99,
                                  "currency": "BYN",
                                  "repaymentDate": "16.08.2023",
                                  "rate": 22.6,
                                  "iban": "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                                  "possibleRepayment": true,
                                  "isClosed": true,
                                  "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                  "customer_type": "LEGAL"
                                },
                                {
                                  "contractNumber": "11-0216444-2-3",
                                  "contractStartDate": "01.01.2023",
                                  "totalDebt": 8113.99,
                                  "currentDebt": 361.99,
                                  "currency": "BYN",
                                  "repaymentDate": "01.01.2024",
                                  "rate": 22.8,
                                  "iban": "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                                  "possibleRepayment": true,
                                  "isClosed": false,
                                  "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                  "customer_type": "LEGAL"
                                }
                              ],
                              "pageable": {
                                "pageNumber": 0,
                                "pageSize": 2,
                                "sort": [],
                                "offset": 0,
                                "paged": true,
                                "unpaged": false
                              },
                              "last": true,
                              "totalPages": 1,
                              "totalElements": 2,
                              "size": 2,
                              "number": 0,
                              "sort": [],
                              "first": true,
                              "numberOfElements": 2,
                              "empty": false
                            }
                            """)))
    })
    ResponseEntity<Page<CreditResponseDTO>> findALl(@ParameterObject Pageable pageable);

    @Operation(summary = "Get Credits by customer id.", tags = "Credit",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter customer id here", example = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc730"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credits retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreditResponseDTO.class), examples = @ExampleObject("""
                            [
                              {
                                "contractNumber": "11-0216444-2-5",
                                "contractStartDate": "01.01.2023",
                                "totalDebt": 8113.99,
                                "currentDebt": 361.99,
                                "currency": "BYN",
                                "repaymentDate": "01.01.2024",
                                "rate": 22.8,
                                "iban": "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                                "possibleRepayment": true,
                                "isClosed": false,
                                "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc730",
                                "customer_type": "LEGAL"
                              }
                            ]
                            """)))
    })
    ResponseEntity<List<CreditResponseDTO>> findAllByCustomerId(UUID id);


    @Operation(summary = "Save new Credit.", tags = "Credit",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            requestBody = @RequestBody(description = "RequestBody for CreateCreditDTO",
                    content = @Content(schema = @Schema(implementation = CreateCreditDTO.class),
                            examples = @ExampleObject("""
                                    {
                                      "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                      "contractNumber": "11-0216444-2-0",
                                      "contractStartDate": "30.03.2022",
                                      "totalDebt": 8113.99,
                                      "currentDebt": 361.99,
                                      "currency": "BYN",
                                      "repaymentDate": "16.01.2023",
                                      "rate": 22.8,
                                      "iban": "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                                      "possibleRepayment": true,
                                      "isClosed": false,
                                      "customer_type" : "LEGAL"
                                    }
                                     """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Credit saved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepositInfoResponse.class), examples = @ExampleObject("""
                               {
                                 "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                 "contractNumber": "11-0216444-2-0",
                                 "contractStartDate": "30.03.2022",
                                 "totalDebt": 8113.99,
                                 "currentDebt": 361.99,
                                 "currency": "BYN",
                                 "repaymentDate": "16.01.2023",
                                 "rate": 22.8,
                                 "iban": "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                                 "possibleRepayment": true,
                                 "isClosed": false,
                                 "customer_type" : "LEGAL"
                               }
                            """))),
            @ApiResponse(responseCode = "400", description = "Credit with iban already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class),
                            examples = @ExampleObject("""
                                    {
                                      "errorCode": 400,
                                      "errorMessage": "Credit with such contractNumber already exist"
                                    }
                                    """)))
    })
    ResponseEntity<CreditResponseDTO> save(CreateCreditDTO dto);

    @Operation(summary = "Update Credit by creditNumber.", tags = "Credit",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "creditNumber", description = "Enter creditNumber here", example = "11-0216444-2-0"),
            requestBody = @RequestBody(description = "RequestBody for UpdateCreditDTO",
                    content = @Content(schema = @Schema(implementation = UpdateCreditDTO.class),
                            examples = @ExampleObject("""
                                    {
                                      "repaymentDate": "16.08.2023",
                                      "rate": 22.6,
                                      "possibleRepayment": true,
                                      "isClosed": true
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepositInfoResponse.class), examples = @ExampleObject("""
                               {
                                 "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                 "contractNumber": "11-0216444-2-0",
                                 "contractStartDate": "30.03.2022",
                                 "totalDebt": 8113.99,
                                 "currentDebt": 361.99,
                                 "currency": "BYN",
                                 "repaymentDate": "16.08.2023",
                                 "rate": 22.6,
                                 "iban": "AABBCCCDDDDEEEEEEEEEEEEEEEE",
                                 "possibleRepayment": true,
                                 "isClosed": true,
                                 "customer_type" : "LEGAL"
                               }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Credit with this contractNumber in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class), examples = @ExampleObject("""
                            {
                              "errorCode": 404,
                              "errorMessage": "Credit with contractNumber = 11-0216444-2-0 not found"
                            }
                            """)))
    })
    ResponseEntity<CreditResponseDTO> updateByContractNumber(String contractNumber, UpdateCreditDTO dto);

    @Operation(summary = "Delete Credit by creditNumber.", tags = "Credit",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "creditNumber", description = "Enter creditNumber here", example = "11-0216444-2-0"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeleteResponse.class), examples = @ExampleObject("""
                            {
                              "message": "Credit with contractNumber 11-0216444-2-0 was successfully deleted"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Credit with this iban in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorInfo.class), examples = @ExampleObject("""
                            {
                              "errorCode": 404,
                              "errorMessage": "Credit with contractNumber = 11-0216444-2-0 not found"
                            }
                            """)))
    })
    ResponseEntity<DeleteResponse> deleteByContractNumber(String contractNumber);

}

