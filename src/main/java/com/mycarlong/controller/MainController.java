package com.mycarlong.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags="메인 컨트롤러")
@RestController("/")
public class MainController {
    @ApiOperation(value = "메인으로 이동")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리턴 성공",
            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @GetMapping("/")
    public String main(){
        return "main";
    }
}