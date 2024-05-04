package com.mycarlong.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Map;

/**
 *
 *  작성자 : KGH <br>
 *  작성일시 : 2024 - 04 - 30 <br>
 *  최종 수정일시 : 2024 - 05 -01 <br>
 * CarInfoDto는 응답에 대한 Car Info를 담는 Data Transfer Object로 자동차의 정보를 포함하고있습니다.
 */
@Getter @Setter
@ToString
@RequiredArgsConstructor
public class CarInfoDto {
	/**
	 * 자동차 정보의 상태 코드. 이 값은 비어 있을 수 없습니다.
	 */
	@NotBlank(message = "Status code could not be blank")
	private int status;

	/**
	 * 자동차 정보를 담고 있는 맵. 키는 정보의 이름이고, 값은 해당 정보입니다.
	 */
	private Map<String, String> information;
	/**
	 * 자동차 정보의 매개변수를 담고 있는 맵. 이 값은 비어 있을 수 없습니다. <br>
	 * 요청받은 자동차 정보 매개변수 ( 연식 year 와 모델명 model ) 을 담고 있습니다.
	 */
	@NotBlank(message = "parameters could not be blank")
	private Map<String, String> parameters;
	/**
	 * 자동차 이미지의 URL.
	 */
	private String imgURL;
}
