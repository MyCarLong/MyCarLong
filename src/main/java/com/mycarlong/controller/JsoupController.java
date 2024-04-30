package com.mycarlong.controller;


import com.mycarlong.service.JsoupServiceImpl;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Tag(name = "Main", description = "Main 관련 API 입니다.")
@Controller
@Slf4j
@RequiredArgsConstructor
public class JsoupController {


	private Logger logger = LoggerFactory.getLogger(JsoupController.class);
	private JsoupServiceImpl jsoupService;

	@Operation(
			summary = "jsoup()",
			description = "크롤링을 실행."
	)
	@ApiResponse(
			responseCode = "200",
			description = "메인페이지로 이동이 성공했습니다."
	)
	@GetMapping("/jsoup/doc")
	public String jsouptest(Model model) throws Exception {
		try {

			String pathTest = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > div.cm_content_wrap._content > div > div > div > div:nth-child(1) > div.movie_photo_list.type_absolute > ul > li:nth-child(1)";
			// 웹 페이지에 연결합니다.
			Document doc = Jsoup.connect(
					"https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=2023%20%EC%95%84%EB%B0%98%EB%96%BC%20%ED%8F%AC%ED%86%A0").get();

			Elements maybeImg = doc.select(pathTest);
			logger.info("doc {}" , doc);
//			logger.info("maybeImg {}" , maybeImg);
			model.addAttribute("maybeImg", doc);
//			model.addAttribute("imgView", url);



		} catch (Exception e) {
			e.printStackTrace();
		}


		return "main";
	}

	@GetMapping("/jsoup/serviceTest")
	public String serviceTest(@RequestParam("year") String year, @RequestParam("model") String model, Model modelView) throws Exception {

		int years = Integer.parseInt(year);
		Map<String, String> result = jsoupService.photoGet(years, model);

		String imgTag = result.get("imgTag");

		logger.info("controller imgTag {}", imgTag);

		//		modelView.addAttribute("imgUrl",imgUrl);
		modelView.addAttribute("imgTag", imgTag);

		return "main";
	}
//
//

//
	@GetMapping("/selenium/test")
	public String seleniumTest(@RequestParam("year") String year, @RequestParam("model") String model,
	                           Model modelView) throws Exception {
//		String regex = "^[a-zA-Z0-9가-힣]*$";
//		String years = String.valueOf(year);
//		String targetURL = null;
//		logger.info("input year {} , input model {}", year, model);
//		if (years.matches(regex) && model.matches(regex)) {
//			String encodedModel = URLEncoder.encode(model, StandardCharsets.UTF_8);
//			String encodedKeyword = URLEncoder.encode("포토", StandardCharsets.UTF_8);
//
//			targetURL = defaultSearchURL + year + "%20" + encodedModel + "%20" + encodedKeyword;
//			logger.info("target URL {}", targetURL);
//		}
//

//		// 웹 드라이버 옵션 설정
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--remote-allow-origins=*");
//		options.addArguments("headless");
//		options.addArguments("disable-gpu");
//		options.addArguments("--no-sandbox");
//		options.addArguments("--window-size=1920,1080");
//		options.addArguments("--disable-dev-shm-usage");
//
//		// 웹 드라이버 초기화
//		WebDriverManager.chromedriver().setup();
//		WebDriver driver = null;
//		try {
//			driver = new ChromeDriver(options);
//			// 웹 페이지에 연결
//			driver.get(targetURL);
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(200));
//			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
//			logger.info("elementm {}", element);
//
//			String imgTag = element.getAttribute("data-img-url");
//			logger.info("imgTag {} " , imgTag);
//			modelView.addAttribute("imgTag", imgTag);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (driver != null) {
//				// 모든 창 및 탭을 닫습니다.
//				for (String handle : driver.getWindowHandles()) {
//					driver.switchTo().window(handle);
//					driver.close();
//				}
//				driver.quit(); // 웹 드라이버를 종료합니다.
//			}
//		}
//		return "main";
	return "main";
	}

	@GetMapping("/selenium/init")
	public String testSelenium() {
		// WebDriverManager를 사용하여 ChromeDriver를 설정합니다.
		WebDriverManager.chromedriver().setup();

		// ChromeDriver 인스턴스를 생성합니다.
		WebDriver driver = new ChromeDriver();

		// 웹 페이지에 접속합니다.
		driver.get("https://www.google.com");

		// 웹 페이지의 제목을 출력합니다.
		System.out.println("Page title is: " + driver.getTitle());
		logger.info("title {}" , driver.getTitle());
		// 웹 드라이버를 종료합니다.
		driver.quit();
		return "main";
	}

//	@GetMapping("/sel")
//	public String testSelenium2() {
//
//	}
}
