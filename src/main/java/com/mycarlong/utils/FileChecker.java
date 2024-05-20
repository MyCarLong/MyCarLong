package com.mycarlong.utils;


import com.mycarlong.constant.AllowedFileExtension;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;


//@PropertySource("classpath:BoardSetting.yml") //application.yml에 import 하는식으로 attached
/**
 * 파일 확장자 검사를 수행하는 서비스 클래스입니다. <br>
 * 작성자: KGH <br>
 * 작성일시: 2024-04-25 <br>
 * @author KGH
 * @since v0.1
 */
@Slf4j
@Service

public class FileChecker {
	private final Tika tika;

//	@Value(value = "${allowedFileExtensions}") // 허용되는 확장자 목록 : (List<String>)
//	private List<String> allowedFileExtensions;


	public FileChecker() {
        this.tika = new Tika();
    }
	/**
	 * 파일의 확장자를 분석하여 반환합니다.
	 *
	 * @param fileData 파일 데이터 : (byte[])
	 * @return 파일 확장자  : (String)
	 */
	@Operation(summary = "Analyze file extension")
	private String analyzeFileExtension(byte[] fileData) {
		return tika.detect(fileData);
	}

	/**
	 * 업로드된 파일의 확장자가 허용되는 확장자 목록에 포함되는지 확인합니다.
	 *
	 * @param fileData 파일 데이터 : (byte[])
	 * @return 허용 여부 (true: 허용, false: 비허용)
	 */
	@Operation(summary = "Check if the file extension is allowed")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "No problem"),
			@ApiResponse(responseCode = "500", description = "Check fileData")
	})
	public boolean isAllowedExtension(byte[] fileData) {

		try {
			String detectedExtension = this.analyzeFileExtension(fileData);
			for (AllowedFileExtension extension : AllowedFileExtension.values()) {
				if (extension.getExtension().equals(detectedExtension)) {
					return false;
				}
			}
			log.error("주어진 허용 확장자 목록에서 찾을 수 없음");
			return true;
			//allowedFileExtensions.contains(detectedExtension);
		} catch (Exception e) {
			log.error("file 확장자 detect 실패");
			return true;
		}
	}
}
