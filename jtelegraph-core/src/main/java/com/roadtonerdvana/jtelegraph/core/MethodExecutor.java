package com.roadtonerdvana.jtelegraph.core;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MethodExecutor {
    
    private static Logger logger = LogManager.getLogger();

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
        } catch (Exception e) {;
            logger.error(e);
        }
        return null;    
    }
    
    public <T, U>  void executeMethodAsync(Method method, U request, Class<T> responseClass) {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                T response = executeMethod(method,request,responseClass);
                logger.info(response);
            }
            
        });
    }
    
    private String getMethodUrl(Method method) {
        if(!methods.containsKey(method)) {
            methods.put(method, url + token + method.getMethodName());
        } 
        return methods.get(method);
    }


    public void setRestTemplate(RestTemplate restTemplate) {
        if (this.restTemplate == null) {
            this.restTemplate = restTemplate;            
        }
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        if (this.objectMapper == null) {
            this.objectMapper = objectMapper;            
        }
    }


    public void setMethods(Map<Method, String> methods) {
        if (this.methods == null) {
            this.methods = methods;
        }
    }

    public void setToken(String token) {
        if (this.token == null) {
            this.token = token;
        }
    }

    public void setUrl(String url) {
        if (this.url == null) {
            this.url = url;            
        }
    }

    public void setExecutorService(ExecutorService executorService) {
        if (this.executorService == null) {
            this.executorService = executorService;
        }
    }
    
    
    
}
