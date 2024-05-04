package com.mycarlong.service;


import com.mycarlong.config.CustomException;
import com.mycarlong.dto.CarInfoDto;
import lombok.RequiredArgsConstructor;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * 작성자 : KGH <br>
 * 작성일시 : 2024 - 04 - 28
 * 최종 수정일시 : 2024 - 05 -03
 *
 * Selenium을 이용한 웹 크롤링 서비스 클래스입니다. 자동차 정보를 가져오는 메서드들을 포함하고 있습니다. */

@Slf4j
@Service
@RequiredArgsConstructor
public class SeleniumServiceImpl extends CustomException implements SeleniumService {
	private Logger logger = LoggerFactory.getLogger(SeleniumServiceImpl.class);
	private static final String DEFAULT_SEARCH_URL = "https://search.naver.com/search" +
			".naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=";  // 네이버 검색 기본 URL을 잡는다.
	private static final Duration WAIT_DURATION = Duration.ofMillis(5000);

	private final WebDriverService webDriverService;

	/**
	 * 웹 페이지에서 자동차의 사진을 가져옵니다.
	 */
	@Override
	public Map<String, String> photoGet(String year, String model, WebDriver driver) {
		Map<String, String> photoGets = null;
		Map<String, String> generated = this.sourceGenerator(year, model, false);
		try {
			String imgURL = null;
			String errMsg = null;
			// 웹 페이지에 연결
			driver.get(generated.get("targetURL"));
			WebDriverWait wait = new WebDriverWait(driver,WAIT_DURATION);
			// CSS 선택자를 사용하여 요소 대기
			WebElement element = wait.until(
					ExpectedConditions.visibilityOfElementLocated(
							By.cssSelector("li[data-img-url][data-id='1']")));
			if (element == null) {
				throw new PageLoadTimeoutException("Photo Box Element Not Found");
			}
			// 요소에서 data-img-url 속성 값 가져오기
			String imgURLt = element.getAttribute("data-img-url");
			logger.info("imgTag {} ", imgURLt);

			photoGets = new LinkedHashMap<>();
			photoGets.put("imgURL", imgURLt);
			photoGets.put("model", model);
			photoGets.put("year", year);
			photoGets.put("errMsg", errMsg);
			/** 웹 페이지의 URL과 CSS 선택자를 생성합니다. */

		} catch (PageLoadTimeoutException e) {
			photoGets.put("errMsg", e.getMessage());
		}
		return photoGets;
	}


	/**
	 * 웹 페이지에서 자동차의 정보를 가져옵니다.
	 */
	@Override
	public Map<String, String> infomationGet(String year, String model, WebDriver driver) {
		// 웹 페이지의 URL과 CSS 선택자를 생성합니다.
		Map<String, String> infoSource = this.sourceGenerator(year, model, true);
		Map<String, String> infoMap = new LinkedHashMap<>();
		String boxCheckCss = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > div:nth-child(3) > div > div.title_area.type_keep._title_area > h2 > span > strong";
		String yearCheckCss = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > div:nth-child(3) > div > div.title_area.type_keep._title_area > div.sub_title > span:nth-child(3)";

		try {
			// 웹 페이지에 접속합니다.
			driver.get(infoSource.get("targetURL"));
			WebDriverWait wait = new WebDriverWait(driver, WAIT_DURATION);
			// 웹 페이지에서 정보를 포함하고 있는 요소들을 찾습니다.
			List<WebElement> dlElements = wait.until(
					ExpectedConditions.visibilityOfAllElementsLocatedBy(
							By.cssSelector(infoSource.get("infoCssSelector"))));
			// 모델 이름을 확인하는 요소를 찾습니다.
			WebElement boxCheck = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector(boxCheckCss)));
			// 연도를 확인하는 요소를 찾습니다.
			WebElement findYear = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector(yearCheckCss)));
			logger.info("boxCheck {} :" ,boxCheck.getText());
			logger.info("findYearCheck {} :" ,findYear.getText());
			// 모델 이름이 일치하지 않는 경우 예외를 발생시킵니다.
			if(boxCheck.getText() == null || !boxCheck.getText().contains(model)){
				throw new InformationNotFoundException("Model Not Match");
				// 연도가 일치하지 않는 경우 예외를 발생시킵니다.
			} else if(findYear.getText() == null || !findYear.getText().contains(year)){
				throw new InformationNotFoundException("Year Not Match");
			}

			infoMap.put("boxCheck", boxCheck.getText());
			// 각 정보 요소에서 라벨과 값을 추출하여 맵에 저장합니다.
			for (WebElement dl : dlElements) {
				List<WebElement> dtElements = dl.findElements(By.tagName("dt"));
				List<WebElement> ddElements = dl.findElements(By.tagName("dd"));

				for (int i = 0; i < dtElements.size(); i++) {
					String dtText = dtElements.get(i).getText();
					String ddText = ddElements.get(i).getText();
					infoMap.put(dtText, ddText);
					logger.info("dt: {}, dd: {}", dtText, ddText);
				}
			}
			return infoMap;
			// 정보를 찾지 못한 경우 에러 메시지를 맵에 추가합니다.
		} catch (InformationNotFoundException e) {
			infoMap.put("errorMsg", e.getMessage());
			return infoMap;
		}
	}



	/**
	 * 가져온 자동차의 정보를 DTO에 매핑합니다.
	 */
	@Override
		public CarInfoDto  mappingToJson(String year, String model) {
			WebDriver driver = webDriverService.getDriver();
//			Map<String , Object> beforeConv = new HashMap<>();
			Map<String , String> parametersInfo = new LinkedHashMap<>();
			parametersInfo.put("year", year);
			parametersInfo.put("model", model);
			CarInfoDto carInfoDto = new CarInfoDto();

			if (!regularExp(year, model)) {  //사용자 입력이 정규식 필터링에 문제업는 정상입력이 아니라면
				carInfoDto.setInformation(Map.of("ErrorInfomation", "Please Input Correct Parameters . . "));
				carInfoDto.setParameters(parametersInfo);
				carInfoDto.setStatus(400);
			}
			else if (year ==null || model ==null){
				throw new ParameterNotFoundException("Null Parameter Input Error");
			}

			try {
				Map<String, String> getPhoto = photoGet(year, model , driver);
				Map<String , String> getInfo = infomationGet(year , model ,driver);
				if(getPhoto != null) { //정상응답이라 판단
//					beforeConv.put("status", 200);
//					beforeConv.put("Information", getInfo);
					carInfoDto.setStatus(200);
					carInfoDto.setInformation(getInfo);
				}
				else if (!getInfo.get("errorMsg").equals("DetectBoxError")) {  //box check 결과에 parameter인 model이 없다면
//					beforeConv.put("status", 406);
//					beforeConv.put("Information", "Box Miss Matching to Parameter Model...");
					throw new TargetNotFoundException("Box Miss Matching to Parameter Model...");

				}else if (getPhoto.get("imgURL").equals("Info: Img URL not found")) {
//					beforeConv.put("status", 404);
//					beforeConv.put("Information", "Cannot Found ImageURL");
					throw new ElementNotFoundException("Getting Photo URL logic failed");
				}
//				else if () {
//					throw new MergingDataException(e.getMessage());
//				}
//				beforeConv.put("parameters", parametersInfo);
//				beforeConv.put("imgURL", getPhoto.get("imgURL"));
				carInfoDto.setParameters(parametersInfo);
				carInfoDto.setImgURL(getPhoto.get("imgURL"));

//				return beforeConv;
				return carInfoDto;
			} catch (TargetNotFoundException e) {
				carInfoDto.setStatus(406);
				carInfoDto.setInformation(Map.of("Information", "Box Miss Matching to Parameter Model..."));
			} catch (ElementNotFoundException e) {
				carInfoDto.setStatus(404);
				carInfoDto.setInformation(Map.of("Information", "Cannot Found ImageURL"));
			}
			catch (ParameterNotFoundException e){
				carInfoDto.setStatus(404);
				carInfoDto.setInformation(Map.of("Information", "Null Parameter Input Error"));
			}
			catch (Exception e) {
				carInfoDto.setStatus(500);
				carInfoDto.setInformation(Map.of("Information", e.getMessage()));
			}finally {
				if (driver != null) {
					// 모든 창 및 탭을 닫습니다.
					webDriverService.quitDriver();
				}
			}
			return carInfoDto;
	}


	/**
	 * 사용자 입력이 정규식 필터링에 문제없는 정상입력인지 확인합니다.
	 */
	private boolean regularExp(String year, String model) {
		String regex = "^[a-zA-Z0-9가-힣]*$";  // 정규식을 세우고
		logger.info("input: year {} , input model {}", year, model);
		return year.matches(regex) && model.matches(regex); //정규식에 포함되지 않은 특수문자등이 입력된 경우 false , 이외 올바른 입력은 true;
	}

	/**
	 * 웹 페이지의 URL과 CSS 선택자를 생성합니다.
	 */
	private Map<String, String> sourceGenerator(String year, String model, boolean wannaInfo) {
		String targetURL = null;
		String imgCssSelector = null;
		String infoCssSelector = null;
		String encodedKeyword = null;
		String encodedModel = URLEncoder.encode(model, StandardCharsets.UTF_8);
		if (wannaInfo) {
			encodedKeyword = URLEncoder.encode("정보", StandardCharsets.UTF_8);
			infoCssSelector = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > " +
					"div.cm_content_wrap._content > div:nth-child(1) > div > div > dl";
		} else {
			encodedKeyword = URLEncoder.encode("포토", StandardCharsets.UTF_8);
			imgCssSelector = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > div.cm_content_wrap._content > div > div > div > div:nth-child(1) > div.movie_photo_list.type_absolute > ul > li:nth-child(1)";

		}

		targetURL = DEFAULT_SEARCH_URL + year + "%20" + encodedModel + "%20" + encodedKeyword;
		logger.info("target URL {}", targetURL);
		Map<String, String> generatedSource = new LinkedHashMap<>();
		generatedSource.put("targetURL", targetURL);
		generatedSource.put("imgCssSelector", imgCssSelector);
		generatedSource.put("infoCssSelector", infoCssSelector);

		return generatedSource;
	}

}
