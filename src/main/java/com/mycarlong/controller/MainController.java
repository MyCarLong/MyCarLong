package com.mycarlong.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Main", description = "Main 관련 API 입니다.")
@RestController("/")
public class MainController {

    @Operation(
            summary = "Main()",
            description = "메인페이지로 이동합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "메인페이지로 이동이 성공했습니다."
    )
    @GetMapping("/")
    public String main(){
        return "main";
    }
}