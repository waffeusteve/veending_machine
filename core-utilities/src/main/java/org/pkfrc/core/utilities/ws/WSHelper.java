package org.pkfrc.core.utilities.ws;

import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WSHelper {
	private static RestTemplate TEMPLATE = new RestTemplate();
	public static String DATE_FORMAT = "ddMMyyyy";

	private static Logger LOGGER = LoggerFactory.getLogger(WSHelper.class);

	public static <E> E get(WSQuery<E> query) {
		String url = putQueryParams(query);
		LOGGER.info("Query One via URL :" + url + "  and Params " + query.getPathParams());
		return TEMPLATE.getForObject(url, query.getClazz(), query.getPathParams());
	}

	public static <E> E post(WSQuery<E> query) throws JsonProcessingException {
		String body = new ObjectMapper().writeValueAsString(query.getBody());
//		RestTemplate rt = new RestTemplate();
//		rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//		rt.getMessageConverters().add(new StringHttpMessageConverter());

		String url = putQueryParams(query);
		LOGGER.info("Query One via URL :" + url + "  and Params " + query.getPathParams());
		return TEMPLATE.postForObject(url, body, query.getClazz());
	}

	public static <E> List<E> getList(WSQuery<E> query) throws Exception {
		String url = putQueryParams(query);
		LOGGER.info("Query List via URL :" + url + "  and Params " + query.getPathParams());
		List<E> response = TEMPLATE.exchange(url, query.getMethod(), null, query.getType(), query.getPathParams())
				.getBody();

		return response;
	}

	private static <E> String putQueryParams(WSQuery<E> query) {
		if (query.getUrlParams() == null) {
			return query.getUrl();
		}
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(query.getUrl());
		for (String key : query.getUrlParams().keySet()) {
			builder.queryParam(key, query.getUrlParams().get(key));
		}
		return builder.toUriString();
	}
}
