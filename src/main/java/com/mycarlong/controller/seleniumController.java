package com.mycarlong.controller;


import com.mycarlong.dto.CarInfoDto;
import com.mycarlong.service.SeleniumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
*  작성자 : KGH <br>
*  작성일시 : 2024 - 04 - 30 <br>
*  최종 수정일시 : 2024 - 05 -01 <br>
* SeleniumController는 SeleniumService를 사용하여 특정 자동차 모델의 정보를 가져오는 API를 제공합니다.
*/
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "CrawlerService for Simple Car Info & Photo" , description = "crawling photo & information using selenium library")
public class seleniumController {

	private final SeleniumService seleniumService;

	/**
	 * 특정 자동차 모델의 정보를 가져옵니다.
	 *
	 * @param year 자동차 모델의 연도
	 * @param model 자동차 모델의 이름
	 * @return CarInfoDto 자동차 정보
	 * @see CarInfoDto
	 */
	@Operation(summary = "Get car information", description = "Get the information of a specific car model.")
	@ApiResponses(value = {
   	  @ApiResponse(responseCode = "200", description = "Successfully retrieved data"),
    	  @ApiResponse(responseCode = "404", description = "Parameters (Model / Year) not found", response = CustomException.ParameterNotFoundException.class),
    	  @ApiResponse(responseCode = "500", description = "WebDriver initialization failed", response = CustomException.WebDriverInitializationException.class),
   	  @ApiResponse(responseCode = "404", description = "Element not found", response = CustomException.ElementNotFoundException.class),
    	  @ApiResponse(responseCode = "408", description = "Page load timeout", response = CustomException.PageLoadTimeoutException.class),
    	  @ApiResponse(responseCode = "404", description = "Target not found", response = CustomException.TargetNotFoundException.class),
    	  @ApiResponse(responseCode = "500", description = "Error during Merging DATA", response = CustomException.MergingDataException.class),
    	  @ApiResponse(responseCode = "500", description = "Error during Merging DATA", response = CustomException.InformationNotFoundException.class)
	})
	@GetMapping("/car/info")
	public CarInfoDto getInfo(@RequestParam("year")  @Parameter(description = "The year of the car model.") String year,
	                          @RequestParam("model")  @Parameter(description = "The name of the car model.") String model) {
		return seleniumService.mappingToJson(year , model);
	}
}
