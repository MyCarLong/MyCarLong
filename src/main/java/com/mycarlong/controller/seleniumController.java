package com.mycarlong.controller;


import com.mycarlong.service.SeleniumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
public class seleniumController {

	private final SeleniumService seleniumService;
	@GetMapping("/car/info")
	public Map<String,Object> getInfo(@RequestParam("year") String year , @RequestParam("model") String model) {
		return seleniumService.mappingToJson(year , model);
	}
}
