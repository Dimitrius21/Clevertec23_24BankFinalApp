package ru.clevertec.bank.product.controller.openapi;


public interface AccountOpenAPI {
/*    @Operation(summary = "Find News by ID",
            description = "Get a News object by specifying its id (without Comments).")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "News Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = NewsShortDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Content hasn't found for submitted ID",  content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<NewsShortDto> getNewsById(@PathVariable long id);


    @Operation(summary = "Find News by ID",
            description = "Get a News object by specifying its id with Comments to it.")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "News Id")
    @Parameter(name = "pageable", hidden = true)
    @PageableAsQueryParam
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = NewsFullDto.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Content hasn't found for submitted ID",  content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<NewsFullDto> getNewsWithCommentsById(@PathVariable long id, Pageable pageable);


    @Operation(summary = "Get all News",
            description = "Get all News (without Comments)")
    @Parameter(name = "pageable", hidden = true)
    @PageableAsQueryParam
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = NewsShortDto.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<List<NewsShortDto>> getAllNews(Pageable pageable);


    @Operation(summary = "Find News by value of its fields",
            description = "Find News by value of required its fields ")
    @Parameter(in = ParameterIn.QUERY, name = "field", required = true, description = "Name of News's field that take part in search (except ID)")
    @Parameter(in = ParameterIn.QUERY, name = "value", required = true, description = "Value of above field that searching")
    @Parameter(name = "pageable", hidden = true)
    @PageableAsQueryParam
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = NewsShortDto.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<List<NewsShortDto>> getNewsBySearch(@RequestParam String[] field,
                                                              @RequestParam String[] value,
                                                              Pageable pageable);

    @Operation(summary = "Delete News by ID",
            description = "Delete a note about a News by specifying ID. ")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Comment Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(schema = @Schema(implementation = Long.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity deleteNews(@PathVariable long id);

    @Operation(summary = "Create(save) News",
            description = "Create(save) submitted News.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "News for save",
            content = {@Content(schema = @Schema(implementation = News.class), mediaType = "application/json")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = NewsShortDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<NewsShortDto> createNews(@RequestBody News news);


    @Operation(summary = "Update News",
            description = "Update News to submitted one.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Comment for update",
            content = {@Content(schema = @Schema(implementation = NewsShortDto.class), mediaType = "application/json")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(schema = @Schema(implementation = NewsShortDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "There's no News with submitted ID for update",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<NewsShortDto> updateNews(@RequestBody News news);*/
}