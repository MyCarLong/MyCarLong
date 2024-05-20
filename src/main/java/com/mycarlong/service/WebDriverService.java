package com.mycarlong.service;

import com.mycarlong.config.CustomException;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

/**
 * 작성자 : 김근휘 <br>
 * 작성일시 : 2024 - 04 -30 <br>
 * WebDriver의 LifeCycle 관리를 위한 클래스입니다.
 */

@Service
@Tag(name = "WebDriver Creation Service", description = "If Not Initiated Any WebDriver Object Create New WebDriver ," +
		" Manage Lifecycle")
public class WebDriverService extends CustomException {
	private WebDriver driver;

	/**
	 * WebDriver가 없다면 새로운 크롬드라이버를 생성합니다. 설정은 내부 메소드의 설정을 따릅니다. <br>( 백그라운드에서 실행 등등 사전설정이 되어있음.)
	 */
	@Operation(summary = "Get WebDriver instance", description = "Get the WebDriver instance. If it's null, initialize a new one.")
	public WebDriver getDriver() {
		if (driver == null) {
			try {
				driver = new ChromeDriver(setting());
			} catch (Exception e) {
				throw new WebDriverInitializationException("Failed to initialize WebDriver", e);
			}
		}
		return driver;
	}

	/**
	 * 열려있는 모든 driver를 닫습니다. driver를 모두 사용한 후에 반드시 필요한 작업입니다.
	 */
	@Operation(summary = "Quit WebDriver instance", description = "Quit the WebDriver instance.")
	public void quitDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}


	@Operation(summary = "Set WebDriver options", description = "Set the options for WebDriver.")
	private ChromeOptions setting() {
		// 웹 드라이버 옵션 설정
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--disable-dev-shm-usage");

		// User-Agent 설정 추가
		options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537");
		// 웹 드라이버 초기화
		WebDriverManager.chromedriver().setup();
		return options;
	}
}
