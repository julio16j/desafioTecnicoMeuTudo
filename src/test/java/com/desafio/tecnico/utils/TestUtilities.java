package com.desafio.tecnico.utils;

import java.nio.charset.Charset;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class TestUtilities {
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	public static MvcResult performGetRequest(MockMvc mockMvc, String url) throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(url))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andReturn();
		return result;
	}
	
	public static MvcResult performDeleteRequest(MockMvc mockMvc, String url) throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(url)
				.contentType(TestUtilities.APPLICATION_JSON_UTF8))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andReturn();
		return result;
		
	}

	public static MvcResult performUpdateRequest(MockMvc mockMvc, String url, String requestJson) throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(url)
				.contentType(TestUtilities.APPLICATION_JSON_UTF8)
				.content(requestJson))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andReturn();
		return result;
		
	}

	public static MvcResult performPostRequest(MockMvc mockMvc, String url, String requestJson) throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url)
				.contentType(TestUtilities.APPLICATION_JSON_UTF8)
				.content(requestJson))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andReturn();
		return result;
	}
	
	private TestUtilities () {
		
	}
	
}
