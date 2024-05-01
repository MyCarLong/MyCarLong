package com.mycarlong.service;


import com.mycarlong.config.CustomException;
import com.mycarlong.dto.CarInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SeleniumServiceImpl extends CustomException implements SeleniumService {
	private Logger logger = LoggerFactory.getLogger(SeleniumServiceImpl.class);
	private static final String DEFAULT_SEARCH_URL = "https://search.naver.com/search" +
			".naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=";  // 네이버 검색 기본 URL을 잡는다.
	private static final Duration WAIT_DURATION = Duration.ofMillis(300);
	private WebDriverService webDriverService;
	private CustomException customException;

	@Override
	public Map<String, String> photoGet(String year, String model, WebDriver driver) {
		Map<String, String> photoGets = null;
		Map<String, String> generated = this.sourceGenerator(year, model, false);
		try {
//			WebDriver driver = null;

			String imgURL = null;
			// 웹 페이지에 연결
			driver.get(generated.get("targetURL"));
			WebDriverWait wait = new WebDriverWait(driver,WAIT_DURATION);
			WebElement element = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector(generated.get("selector"))));
			logger.info("element {}", element);
			imgURL = element.getAttribute("data-img-url");
			if (imgURL == null) {
				imgURL = "Info: Img URL not found";
			}
			logger.info("imgTag {} ", imgURL);

			photoGets = new HashMap<>();
			photoGets.put("imgURL", imgURL);
			photoGets.put("model", model);
			photoGets.put("year", year);


		} catch (Exception e) {
			e.printStackTrace();
		}
		return photoGets;
	}

	@Override
	public Map<String, String> infomationGet(String year, String model, WebDriver driver) {

		Map<String, String> infoSource = this.sourceGenerator(year, model, true);
		Map<String, String> infoMap = new HashMap<>();
		try {
			driver.get(infoSource.get("targetURL"));
			WebDriverWait wait = new WebDriverWait(driver, WAIT_DURATION);
			List<WebElement> dlElements = wait.until(
					ExpectedConditions.visibilityOfAllElementsLocatedBy(
							By.cssSelector(infoSource.get("selector"))));
			String boxCss = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > div:nth-child(3) > div > div.title_area.type_keep._title_area > h2 > span > strong";
			WebElement boxCheck = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector(boxCss)));
			logger.info("boxCheck {} :" ,boxCheck.getText());
			if (boxCheck.getText()==null) {
				throw new TargetNotFoundException("Targeted Information Box Not Found");
			}

			infoMap.put("boxCheck", boxCheck.getText());
//			infoMap.put("imgURL", imgURL);
			for (WebElement dl : dlElements) {
				// dt 요소의 목록을 가져옵니다.
				List<WebElement> dtElements = dl.findElements(By.tagName("dt"));
				// dd 요소의 목록을 가져옵니다.
				List<WebElement> ddElements = dl.findElements(By.tagName("dd"));

				for (int i = 0; i < dtElements.size(); i++) {
					String dtText = dtElements.get(i).getText();// dt 요소의 innerText를 가져옵니다. ( 라벨 )
					String ddText = ddElements.get(i).getText();// dd 요소의 innerText를 가져옵니다. ( 값 )
					infoMap.put(dtText, ddText);
					logger.info("dt: {}, dd: {}", dtText, ddText);
				}
			}
			return infoMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoMap;
	}



		@Override
		public CarInfoDto  mappingToJson(String year, String model) {
			WebDriver driver = webDriverService.getDriver();
			Map<String , Object> beforeConv = new HashMap<>();
			Map<String , String> parametersInfo = new HashMap<>();
			parametersInfo.put("year", year);
			parametersInfo.put("model", model);
			CarInfoDto carInfoDto = new CarInfoDto();

			if (!regularExp(year, model)) {
				carInfoDto.setInformation(Map.of("ErrorInfomation", "Please Input Correct Parameters . . "));
				carInfoDto.setParameters(parametersInfo);
				carInfoDto.setStatus(400);

			}
			try {
				Map<String, String> getPhoto = photoGet(year, model , driver);
				Map<String , String> getInfo = infomationGet(year , model ,driver);


				if(getPhoto != null) {
//					beforeConv.put("status", 200);
//					beforeConv.put("Information", getInfo);
					carInfoDto.setStatus(200);
					carInfoDto.setInformation(getInfo);
				}
				else if (!getInfo.get("boxCheck").contains(model)) {  //box check 결과에 parameter인 model이 없다면
//					beforeConv.put("status", 406);
//					beforeConv.put("Information", "Box Miss Matching to Parameter Model...");
					carInfoDto.setStatus(406);
					carInfoDto.setInformation(Map.of("Information", "Box Miss Matching to Parameter Model..."));
				}else if (getPhoto.get("imgURL").equals("Info: Img URL not found")) {
//					beforeConv.put("status", 404);
//					beforeConv.put("Information", "Cannot Found ImageURL");
					carInfoDto.setStatus(404);
					carInfoDto.setInformation(Map.of("Information", "Cannot Found ImageURL"));
				}
//				beforeConv.put("parameters", parametersInfo);
//				beforeConv.put("imgURL", getPhoto.get("imgURL"));
//				carInfoDto.setParameters(parametersInfo);
				carInfoDto.setImgURL(getPhoto.get("imgURL"));

//				return beforeConv;
				return carInfoDto;
			} catch (Exception e) {
				throw new MergingDataException(e.getMessage());
			}finally {
				if (driver != null) {
					// 모든 창 및 탭을 닫습니다.
					webDriverService.quitDriver();
				}
			}
		}



	private WebElement waitForElement(WebDriver driver, String targetURL, String cssSelector) {
		driver.get(targetURL);
		WebDriverWait wait = new WebDriverWait(driver,WAIT_DURATION);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
	}



	private boolean regularExp(String year, String model) {
		String regex = "^[a-zA-Z0-9가-힣]*$";  // 정규식을 세우고
		logger.info("input: year {} , input model {}", year, model);
		return year.matches(regex) && model.matches(regex); //정규식에 포함되지 않은 특수문자등이 입력된 경우 false , 이외 올바른 입력은 true;
	}

	private Map<String, String> sourceGenerator(String year, String model, boolean wannaInfo) {
		String targetURL = null;
		String cssSelector = null;
		String encodedKeyword = null;
		String encodedModel = URLEncoder.encode(model, StandardCharsets.UTF_8);
		if (!wannaInfo) {
			encodedKeyword = URLEncoder.encode("포토", StandardCharsets.UTF_8);
			cssSelector = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > div.cm_content_wrap._content > div > div > div > div:nth-child(1) > div.movie_photo_list.type_absolute > ul > li:nth-child(1)";
		} else {
			encodedKeyword = URLEncoder.encode("정보", StandardCharsets.UTF_8);
			cssSelector = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > " +
					"div.cm_content_wrap._content > div:nth-child(1) > div > div > dl";
		}

		targetURL = DEFAULT_SEARCH_URL + year + "%20" + encodedModel + "%20" + encodedKeyword;
		logger.info("target URL {}", targetURL);
		Map<String, String> generatedSource = new HashMap<>();
		generatedSource.put("targetURL", targetURL);
		generatedSource.put("selectorType", "css");
		;
		generatedSource.put("selector", cssSelector);

		return generatedSource;
	}

}
//TODO DTO mapping needs
/*
*example)
* public class PhotoGetDTO {
    private String imgURL;
    private String model;
    private String year;

    // getters and setters
}

public class InformationGetDTO {
    private String boxCheck;
    private Map<String, String> infoMap;

    // getters and setters
}

public class MappingToJsonDTO {
    private int status;
    private InformationGetDTO information;
    private Map<String, String> parameters;
    private String imgURL;

    // getters and setters
}
*/

/*
* need to move Origin project  - > SeleniumServiceImpl , service interface , controller , maybe DTOs...
* */