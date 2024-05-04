package com.mycarlong.dto;

import jakarta.validation.constraints.NotBlank;

public class simpleCarInfoDto {

	private Long carId;

	@NotBlank(message = "Do Not Blank carModelName")
	private String carModelName;

	@NotBlank(message = "Do Not Blank make")
	private String make;
	@NotBlank(message = "Do Not Blank year")
	private String year;
	@NotBlank(message = "Do Not Blank fuelType")
	private String fuelType;
	@NotBlank(message = "Do Not Blank mileage;")
    private String mileage;
	@NotBlank(message = "Do Not Blank color")
    private String color;
	@NotBlank(message = "Do Not Blank price")
	private String price;
}
