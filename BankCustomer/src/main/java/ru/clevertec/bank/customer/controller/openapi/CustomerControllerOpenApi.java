package ru.clevertec.bank.customer.controller.openapi;

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
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.bank.customer.domain.dto.CustomerRequest;
import ru.clevertec.bank.customer.domain.dto.CustomerResponse;
import ru.clevertec.bank.customer.domain.dto.CustomerUpdateRequest;
import ru.clevertec.bank.customer.domain.dto.DeleteResponse;
import ru.clevertec.bank.customer.validation.ValidLegalUnp;
import ru.clevertec.exceptionhandler.domain.ErrorIfo;
import ru.clevertec.exceptionhandler.domain.ValidationExceptionResponse;

import java.util.UUID;

@Validated
@Tag(name = "Customer", description = "The Customer Api")
public interface CustomerControllerOpenApi {

    @Operation(summary = "Find Customer by id.", tags = "Customer",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter id here", example = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc724"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class), examples = @ExampleObject("""
                            {
                              "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc724",
                              "customer_type": "LEGAL",
                              "unp": "3GH789R56",
                              "register_date": "2024-01-07",
                              "email": "Matrix@example.com",
                              "phoneCode": "37533",
                              "phoneNumber": "7777777",
                              "customer_fullname": "ООО Матрица"
                            }
                            """))),
            @ApiResponse(responseCode = "401", description = "Not Authenticated User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class), examples = @ExampleObject("""
                            {
                              "time": "2024-01-14 16:04:39",
                              "errorCode": 401,
                              "errorMessage": "Full authentication is required to access this resource"
                            }
                            """))),
            @ApiResponse(responseCode = "403", description = "Access denied for User with this role",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class), examples = @ExampleObject("""
                            {
                              "time": "2024-01-14 16:06:44",
                              "errorCode": 403,
                              "errorMessage": "With a ROLE_USER, you can only view/update your customer"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Customer with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class), examples = @ExampleObject("""
                            {
                              "time": "2024-01-14 15:49:49",
                              "errorCode": 404,
                              "errorMessage": "Customer with id 1a72a05f-4b8f-43c5-a889-1ebc6d9dc111 is not found"
                            }
                            """)))
    })
    ResponseEntity<CustomerResponse> findById(UUID id, Authentication authentication);

    @Operation(summary = "Find all Customers with pagination.", tags = "Customer",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = {
                    @Parameter(name = "page", description = "Enter your page number here", example = "0"),
                    @Parameter(name = "size", description = "Enter your page size here", example = "2"),
                    @Parameter(name = "sort", description = "Enter your sort by here",
                            schema = @Schema(type = "array", example = "[\"customerId\"]"))
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of Customers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class), examples = @ExampleObject("""
                            {
                               "content": [
                                 {
                                   "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                   "customer_type": "LEGAL",
                                   "unp": "123AB6789",
                                   "register_date": "2024-01-01",
                                   "email": "HornsAndHooves@example.com",
                                   "phoneCode": "37529",
                                   "phoneNumber": "1111111",
                                   "customer_fullname": "ООО Рога и копыта"
                                 },
                                 {
                                   "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc929",
                                   "customer_type": "LEGAL",
                                   "unp": "98S65D321",
                                   "register_date": "2024-01-02",
                                   "email": "JollyRoger@example.com",
                                   "phoneCode": "37533",
                                   "phoneNumber": "2222222",
                                   "customer_fullname": "ООО Веселый роджер"
                                 }
                               ],
                               "pageable": {
                                 "pageNumber": 0,
                                 "pageSize": 2,
                                 "sort": {
                                   "empty": true,
                                   "sorted": false,
                                   "unsorted": true
                                 },
                                 "offset": 0,
                                 "paged": true,
                                 "unpaged": false
                               },
                               "last": false,
                               "totalPages": 6,
                               "totalElements": 12,
                               "size": 2,
                               "number": 0,
                               "sort": {
                                 "empty": true,
                                 "sorted": false,
                                 "unsorted": true
                               },
                               "first": true,
                               "numberOfElements": 2,
                               "empty": false
                            }
                            """))),
            @ApiResponse(responseCode = "401", description = "Not Authenticated User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class), examples = @ExampleObject("""
                            {
                              "time": "2024-01-14 16:04:39",
                              "errorCode": 401,
                              "errorMessage": "Full authentication is required to access this resource"
                            }
                            """)))
    })
    ResponseEntity<Page<CustomerResponse>> findAll(@ParameterObject Pageable pageable);

    @Operation(summary = "Save new Customer.", tags = "Customer",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            requestBody = @RequestBody(description = "RequestBody for CustomerRequest",
                    content = @Content(schema = @Schema(implementation = CustomerRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "customer_type": "LEGAL",
                                      "unp": "ABCDEFGHI",
                                      "email": "example@email.com",
                                      "phoneCode": "37529",
                                      "phoneNumber": "1112233",
                                      "customer_fullname": "Иванов Иван Иванович"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer saved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class), examples = @ExampleObject("""
                            {
                              "customer_id": "0e8ac0f4-0f0b-4008-af9a-6da7a95b13e0",
                              "customer_type": "LEGAL",
                              "unp": "ABCDEFGHI",
                              "register_date": "2024-01-14",
                              "email": "example@email.com",
                              "phoneCode": "37529",
                              "phoneNumber": "1112233",
                              "customer_fullname": "Иванов Иван Иванович"
                            }
                            """))),
            @ApiResponse(responseCode = "401", description = "Not Authenticated User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class), examples = @ExampleObject("""
                            {
                              "time": "2024-01-14 16:04:39",
                              "errorCode": 401,
                              "errorMessage": "Full authentication is required to access this resource"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class),
                            examples = @ExampleObject("""
                                    {
                                      "violations": [
                                        {
                                          "field_name": "customerType",
                                          "message": "Acceptable customerTypes are only: LEGAL or PHYSIC"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<CustomerResponse> save(@Valid @ValidLegalUnp CustomerRequest request);

    @Operation(summary = "Update Customer by id.", tags = "Customer",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter id here", example = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc726"),
            requestBody = @RequestBody(description = "RequestBody for CustomerUpdateRequest",
                    content = @Content(schema = @Schema(implementation = CustomerUpdateRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "email": "singleton@email.com",
                                      "phoneCode": "37533",
                                      "phoneNumber": "2223344"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class), examples = @ExampleObject("""
                            {
                              "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc726",
                              "customer_type": "PHYSIC",
                              "register_date": "2024-01-09",
                              "email": "singleton@email.com",
                              "phoneCode": "37533",
                              "phoneNumber": "2223344",
                              "customer_fullname": "Стариков Сергей Васильевич"
                            }
                            """))),
            @ApiResponse(responseCode = "401", description = "Not Authenticated User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class), examples = @ExampleObject("""
                            {
                              "time": "2024-01-14 16:04:39",
                              "errorCode": 401,
                              "errorMessage": "Full authentication is required to access this resource"
                            }
                            """))),
            @ApiResponse(responseCode = "403", description = "Access denied for User with this role",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class), examples = @ExampleObject("""
                            {
                              "time": "2024-01-14 16:06:44",
                              "errorCode": 403,
                              "errorMessage": "With a ROLE_USER, you can only view/update your customer"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Customer with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class), examples = @ExampleObject("""
                            {
                              "time": "2024-01-14 15:49:49",
                              "errorCode": 404,
                              "errorMessage": "Customer with id 1a72a05f-4b8f-43c5-a889-1ebc6d9dc111 is not found"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationExceptionResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "violations": [
                                        {
                                          "field_name": "email",
                                          "message": "must be a well-formed email address"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<CustomerResponse> updateById(UUID id, @Valid CustomerUpdateRequest request, Authentication authentication);

    @Operation(summary = "Delete Customer by id.", tags = "Customer",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter id here", example = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc726"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeleteResponse.class), examples = @ExampleObject("""
                            {
                              "message": "Customer with id 1a72a05f-4b8f-43c5-a889-1ebc6d9dc724 was successfully deleted"
                            }
                            """))),
            @ApiResponse(responseCode = "401", description = "Not Authenticated User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class), examples = @ExampleObject("""
                            {
                              "time": "2024-01-14 16:04:39",
                              "errorCode": 401,
                              "errorMessage": "Full authentication is required to access this resource"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Customer with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class), examples = @ExampleObject("""
                            {
                              "time": "2024-01-14 15:49:49",
                              "errorCode": 404,
                              "errorMessage": "Customer with id 1a72a05f-4b8f-43c5-a889-1ebc6d9dc111 is not found"
                            }
                            """)))
    })
    ResponseEntity<DeleteResponse> deleteById(UUID id);

    @Operation(summary = "Restore Customer by id.", tags = "Customer",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter id here", example = "1a72a05f-4b8f-43c5-a889-1ebc6d9dc726"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer restored successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class), examples = @ExampleObject("""
                            {
                              "customer_id": "1a72a05f-4b8f-43c5-a889-1ebc6d9dc724",
                              "customer_type": "LEGAL",
                              "unp": "3GH789R56",
                              "register_date": "2024-01-07",
                              "email": "Matrix@example.com",
                              "phoneCode": "37533",
                              "phoneNumber": "7777777",
                              "customer_fullname": "ООО Матрица"
                            }
                            """))),
            @ApiResponse(responseCode = "401", description = "Not Authenticated User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class), examples = @ExampleObject("""
                            {
                              "time": "2024-01-14 16:04:39",
                              "errorCode": 401,
                              "errorMessage": "Full authentication is required to access this resource"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Customer with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorIfo.class), examples = @ExampleObject("""
                            {
                              "time": "2024-01-14 15:49:49",
                              "errorCode": 404,
                              "errorMessage": "Customer with id 1a72a05f-4b8f-43c5-a889-1ebc6d9dc111 is not found"
                            }
                            """)))
    })
    ResponseEntity<CustomerResponse> restoreById(UUID id);

}
