package com.mycarlong.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface JsoupService {
	//List<String> newDocParsing(String url, String queryType, String query) throws IOException;

	List<String> elementSelector(String searchingTarget, String queryType, String query) throws IOException;

	Map<String,String> photoGet(int year, String model) throws IOException;

	String specInfoGet(int year, String model) throws IOException;
	//private String urlParser();
}
