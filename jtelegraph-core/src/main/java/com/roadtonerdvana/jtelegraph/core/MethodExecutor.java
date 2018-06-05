package com.roadtonerdvana.jtelegraph.core;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MethodExecutor {

    private static Logger logger = LogManager.getLogger();

    private RestTemplate restTemplate;
    private ExecutorService executorService;
    private ObjectMapper objectMapper;
    private Map<Method, String> methods;
    private String token;
    private String url;

    public <T, U> T executeMethod(Method method, U request, Class<T> responseClass) {
        String url = getMethodUrl(method);
        try {
            switch (method) {
                case GET_UPDATES:
                case SEND_MESSAGE:
                case SEND_PHOTO_WITH_ID:
                    return executeMethod(url, request, responseClass);
                case SEND_PHOTO_WITH_FILE:
                    return executeMethod(url, method.getFileKey(), request, responseClass);
                default:
                    logger.error("Method: " + method + " not implemented");
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public <T, U> void executeMethodAsync(Method method, U request, Class<T> responseClass) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                T response = executeMethod(method, request, responseClass);
                logger.info(response);
            }
        });
    }

    private <T, U> T executeMethod(String url, String fileKey, U request, Class<T> responseClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        Map<String, String> simpleMap = objectMapper.convertValue(request, new TypeReference<Map<String, String>>() {
        });
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add(fileKey, new FileSystemResource(new File((String) simpleMap.get(fileKey))));
  
        for (Entry<String, String> entry : simpleMap.entrySet()) {
            if (!map.containsKey(entry.getKey())) {
                map.add(entry.getKey(), entry.getValue());
            }
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map,
                headers);

        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseClass);
        if (result.getStatusCode() == HttpStatus.OK) {
            return result.getBody();
        }

        return null;
    }

    private <T, U> T executeMethod(String url, U request, Class<T> responseClass)
            throws JsonParseException, JsonMappingException, IOException {

        JsonNode jsonNode = restTemplate.postForObject(url, request, JsonNode.class);
        if (jsonNode.get("ok").asBoolean()) {
            return objectMapper.readValue(jsonNode.get("result").toString(), responseClass);
        }

        return null;
    }

    private String getMethodUrl(Method method) {
        if (!methods.containsKey(method)) {
            methods.put(method, url + token + method.getMethodName());
        }
        return methods.get(method);
    }

    public MethodExecutor setRestTemplate(RestTemplate restTemplate) {
        if (this.restTemplate == null) {
            this.restTemplate = restTemplate;
        }        
        return this;
    }

    public MethodExecutor setObjectMapper(ObjectMapper objectMapper) {
        if (this.objectMapper == null) {
            this.objectMapper = objectMapper;
        }
        return this;
    }

    public MethodExecutor setMethods(Map<Method, String> methods) {
        if (this.methods == null) {
            this.methods = methods;
        }
        return this;
    }

    public MethodExecutor setToken(String token) {
        if (this.token == null) {
            this.token = token;
        }
        return this;
    }

    public MethodExecutor setUrl(String url) {
        if (this.url == null) {
            this.url = url;
        }
        return this;
    }

    public MethodExecutor setExecutorService(ExecutorService executorService) {
        if (this.executorService == null) {
            this.executorService = executorService;
        }
        return this;
    }

}
