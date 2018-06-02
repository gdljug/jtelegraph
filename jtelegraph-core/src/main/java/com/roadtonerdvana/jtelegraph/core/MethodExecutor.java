package com.roadtonerdvana.jtelegraph.core;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MethodExecutor {

    private RestTemplate restTemplate;
    private ExecutorService executorService;
    private ObjectMapper objectMapper;
    private Map<Method,String> methods;
    private String token;
    private String url;
    
    public <T, U> T executeMethod(Method method, U request, Class<T> responseClass) {
        try {
            JsonNode jsonNode = restTemplate.postForObject(getMethodUrl(method), request, JsonNode.class);
            if (jsonNode.get("ok").asBoolean()) {
                return objectMapper.readValue(jsonNode.get("result").toString(), responseClass);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;    
    }
    
    public <T, U>  void executeMethodAsync(Method method, U request, Class<T> responseClass) {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                T response = executeMethod(method,request,responseClass);
                System.out.println(response);
            }
            
        });
    }
    
    private String getMethodUrl(Method method) {
        if(!methods.containsKey(method)) {
            methods.put(method, url + token + method.getMethodName());
        } 
        return methods.get(method);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<Method, String> getMethods() {
        return methods;
    }

    public void setMethods(Map<Method, String> methods) {
        this.methods = methods;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
    
    
    
}
