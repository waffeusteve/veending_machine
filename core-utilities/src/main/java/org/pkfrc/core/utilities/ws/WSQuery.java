package org.pkfrc.core.utilities.ws;

import java.util.HashMap;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

public class WSQuery<E> {

	private HttpMethod method;
	private Class<E> clazz;
	private String url;
	private HashMap<String, Object> pathParams = new HashMap<>(0);
	private HashMap<String, Object> urlParams;
	private Object body;
	private ParameterizedTypeReference<List<E>> type;

	public WSQuery(Class<E> clazz, String url, HttpMethod method) {
		super();
		this.clazz = clazz;
		this.method = method;
		this.url = url;
		urlParams = new HashMap<>(0);
	}

	public WSQuery(Class<E> clazz, String url, HttpMethod method, HashMap<String, Object> urlParams) {
		this(clazz, url, method);
		this.urlParams = urlParams;
	}

	public Class<E> getClazz() {
		return clazz;
	}

	public void setClazz(Class<E> clazz) {
		this.clazz = clazz;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HashMap<String, Object> getPathParams() {
		return pathParams;
	}

	public void setPathParams(HashMap<String, Object> pathParams) {
		this.pathParams = pathParams;
	}

	public HashMap<String, Object> getUrlParams() {
		return urlParams;
	}

	public void setUrlParams(HashMap<String, Object> urlParams) {
		this.urlParams = urlParams;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public ParameterizedTypeReference<List<E>> getType() {
		return type;
	}

	public void setType(ParameterizedTypeReference<List<E>> type) {
		this.type = type;
	}

}
