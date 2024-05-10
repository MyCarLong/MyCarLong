//package com.mycarlong.service;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mycarlong.MyCarLongApplication;
//import com.mycarlong.dto.ArticleImageDto;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = MyCarLongApplication.class)
//@TestPropertySource(properties = {"spring.profiles.active=unittest"})
//@DirtiesContext
//public class FileTests {
//
//	@Autowired
//	private WebApplicationContext context;
//
//	private MockMvc mockMvc;
//
//	@Before
//	public void prepare() throws Exception {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
//				.addFilters(new CharacterEncodingFilter("UTF-8", true)).build();
//	}
//
//	@After
//	public void tearDown() throws Exception {
//
//	}
//
//	@Test
//	public void fileSaveTest() throws Exception {
//		ClassPathResource classPathResource = new ClassPathResource("file:///C:\\Users\\kghui\\Pictures\\sample\\test.jpg");
//
//		MvcResult result = mockMvc.perform(multipart("/board/imgUpload")
//				                                   .file(new MockMultipartFile("file", "test.jpg", "image/jpeg", classPathResource.getInputStream()))
//				                                   .param("imageOriginName", "imageOriginName")
//				                                   .contentType("multipart/form-data"))
//				.andExpect(status().isOk())
//				.andReturn();
//
//		ArticleImageDto response = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
//		                                                        new TypeReference<ArticleImageDto>() {
//		                                                          });
//
//		assertThat(response.getImageOriginName()).isEqualTo("imageOriginName");
//	}
//}