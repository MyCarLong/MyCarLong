package com.mycarlong.service;

import org.openqa.selenium.WebDriver;

import java.util.Map;

public interface SeleniumService {

	Map<String, String> photoGet(String year, String model, WebDriver driver);

	Map<String, String> infomationGet(String year, String model, WebDriver driver);

//	Map<String, Object>  mappingToJson(String year, String model); // return JSON object for REST API calls
	CarInfoDto mappingToJson(String year, String model); // return JSON object for REST API calls
}
