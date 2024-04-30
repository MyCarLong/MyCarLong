package com.mycarlong.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class JsoupServiceImpl implements JsoupService{
	private Logger logger = LoggerFactory.getLogger(JsoupServiceImpl.class);
	private static final String defaultSearchURL = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=";
//	@Override
//	public List<String> elementSelector(String searchingTarget, String queryType, String query) throws IOException {
//		Document naverSearch =  Jsoup.connect(url).get();
//		String findItem;
//		switch(queryType) {
//			case "css" :
//				findItem = docu.select(query).text();
//			case "xPath" :
//				findItem = docu.selectXpath(query).text();
//		}
//
//		return
//	}

	@Override
	public List<String> elementSelector(String searchingTarget, String queryType, String query) throws IOException {
		return List.of();
	}

	@Override
	public Map<String,String> photoGet(int year, String model) throws IOException {
		String regex = "^[a-zA-Z0-9가-힣]*$";
		String years = String.valueOf(year);
		String targetURL = null;
		logger.info("input year {} , input model {}", year, model);
		if(years.matches(regex) && model.matches(regex)) {
			targetURL = defaultSearchURL
					+year +"%20" + "%" +
					Arrays.toString(model.getBytes(StandardCharsets.US_ASCII)) + "%20" +
					Arrays.toString("포토".getBytes(StandardCharsets.US_ASCII));
			String encodedModel = URLEncoder.encode(model, StandardCharsets.UTF_8);
			String encodedKeyword = URLEncoder.encode("포토", StandardCharsets.UTF_8);

			targetURL = defaultSearchURL + year + "%20" + encodedModel + "%20" + encodedKeyword;
			logger.info("target URL {}", targetURL);
		}

		Document photoDoc = Jsoup.connect(targetURL).get();
//		String imgTagCsstemp = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > div.cm_content_wrap._content > div > div > div > div:nth-child(3) > div.movie_photo_list > ul > li:nth-child(1) > a > img";
//		String imgTagCssExperior = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > div.cm_content_wrap._content > div > div > div > div:nth-child(1) > div.movie_photo_list.type_absolute > ul > li:nth-child(1) > a > img";
//		String imgTagCssbefor2 = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > div.cm_content_wrap._content > div > div > div > div:nth-child(1) > div.movie_photo_list.type_absolute > ul > li:nth-child(1) > a::before";
		String imgTagCssbefor = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > div.cm_content_wrap._content > div > div > div > div:nth-child(1) > div.movie_photo_list.type_absolute > ul > li:nth-child(1)";
		String pathTest = "#main_pack > div.sc_new.cs_common_module.case_normal.color_5._cs_car_single > div.cm_content_wrap._content > div > div > div > div:nth-child(1) > div.movie_photo_list.type_absolute > ul > li:nth-child(1) > a > img";
		logger.info("pathTest {}", pathTest);
//		Elements listItems = photoDoc.select(imgTagCssbefor);
//		for (Element element : listItems) {
//			String dataImgUrl= element.attr("data-img-url");
//			logger.info(" dataImgUrl {}" ,dataImgUrl);
//		}
		String ImgUrl = photoDoc.select(pathTest).attr("src");
//		String dataImgUrl= listItem.attr("data-img-url");

		logger.info(" ImgUrl {}", ImgUrl);



		//img src URL 추출
//		int start = imgTag.indexOf("\"") + 1; // 첫 번째 따옴표 다음 위치를 시작점으로 설정
//		int end = imgTag.indexOf("\"", start); // 시작점 이후의 첫 번째 따옴표 위치를 끝점으로 설정
//		String imgUrl = imgTag.substring(start, end); // 시작점과 끝점 사이의 문자열을 추출

		Map<String, String> ResponseInfo = new HashMap<String, String>();
//		ResponseInfo.put("imgUrl", imgUrl);
		ResponseInfo.put("imgTag", ImgUrl);

		return ResponseInfo;
	}

	@Override
	public String specInfoGet(int year, String model) throws IOException {
		String regex = "^[a-zA-Z0-9가-힣]*$";
		String years = String.valueOf(year);
		if(years.matches(regex) && model.matches(regex)) {
			String targetURL = defaultSearchURL
					+years +"%20" + "%" +
					Arrays.toString(model.getBytes(StandardCharsets.US_ASCII)) +
					Arrays.toString("정보".getBytes(StandardCharsets.US_ASCII));

		}
		return "";
	}

	private String urlParser(String sourceImgTag) {
		//img src URL 추출
		int start = sourceImgTag.indexOf("\"") + 1; // 첫 번째 따옴표 다음 위치를 시작점으로 설정
		int end = sourceImgTag.indexOf("\"", start); // 시작점 이후의 첫 번째 따옴표 위치를 끝점으로 설정
		return sourceImgTag.substring(start, end);
	}
}
