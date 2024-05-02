package java.com.mycarlong.mycarlongback.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @GetMapping("/{provider}")
    public String handleLogin(@PathVariable String provider, @RequestParam String code) {
        // provider와 code를 이용한 로직 처리
        return "success";
    }
}