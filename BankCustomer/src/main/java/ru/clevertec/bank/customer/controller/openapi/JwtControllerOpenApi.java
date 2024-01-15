package ru.clevertec.bank.customer.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.bank.customer.domain.dto.JwtRequest;
import ru.clevertec.bank.customer.domain.dto.JwtResponse;
import ru.clevertec.exceptionhandler.domain.ValidationExceptionResponse;

@Validated
@Tag(name = "JWT", description = "The JWT Api")
public interface JwtControllerOpenApi {

    @Operation(summary = "Generate new JWT by customer_id and role", tags = "JWT",
            requestBody = @RequestBody(description = "RequestBody for JwtRequest",
                    content = @Content(schema = @Schema(implementation = JwtRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "id" : "1a72a05f-4b8f-43c5-a889-1ebc6d9dc729",
                                      "role": "ADMINISTRATOR"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "JWT generated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class), examples = @ExampleObject("""
                            {
                              "jwt": "your pretty JWT"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationExceptionResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "violations": [
                                        {
                                          "field_name": "role",
                                          "message": "Acceptable roles are only: USER, ADMINISTRATOR or SUPER_USER"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<JwtResponse> generateJwt(@Valid JwtRequest request);

}
