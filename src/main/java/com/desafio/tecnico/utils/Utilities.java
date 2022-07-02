package com.desafio.tecnico.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public final class Utilities {
	
	public static final ObjectMapper OBJECT_MAPPER = objectMapperConfigured();
	public static final ObjectWriter OBJECT_WRITTER = OBJECT_MAPPER.writer().withDefaultPrettyPrinter();

	
	private Utilities () {
		
	}
	
	private static ObjectMapper objectMapperConfigured () {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		return mapper;
	}
}
