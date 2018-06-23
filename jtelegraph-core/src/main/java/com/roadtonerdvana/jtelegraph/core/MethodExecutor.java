package com.roadtonerdvana.jtelegraph.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
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
    private String downloadUrl;
    private String downloadDirectory;
    private String downloadUrlWithToken;

    public <T, U> T executeMethod(Method method, U request, Class<T> responseClass) {
        var url = getMethodUrl(method);
        try {
            switch (method) {
                case GET_UPDATES:
                case SEND_MESSAGE:
                case SEND_PHOTO_WITH_ID:
                case GET_FILE:
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
    
    public String downloadFile(com.roadtonerdvana.jtelegraph.telegrambotapi.types.File file) {
        FileOutputStream fos = null;
        try {
            var filePath = file.getFilePath();
            var website = new URL(getDownloadUrlWithToken() + filePath);
            var rbc = Channels.newChannel(website.openStream());
            var downloadFilePath = getDownloadPath(filePath);
            fos = new FileOutputStream(downloadFilePath);
            fos.getChannel().transferFrom(rbc, 0, file.getFileSize());
            return downloadFilePath;
        } catch(Exception e) {
            logger.error("Error downloading file",e);
            return null;
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                logger.error("Failed closing fileoutputstream",e);
            }
        }
    }
    
    private String getDownloadPath(String filePath) {
        return downloadDirectory + System.currentTimeMillis() + filePath.substring(filePath.lastIndexOf('/') + 1 );
    }

    private <T, U> T executeMethod(String url, String fileKey, U request, Class<T> responseClass) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        Map<String, String> simpleMap = objectMapper.convertValue(request, new TypeReference<Map<String, String>>() {
        });
        var map = new LinkedMultiValueMap<String, Object>();
        map.add(fileKey, new FileSystemResource(new File((String) simpleMap.get(fileKey))));
  
        for (Entry<String, String> entry : simpleMap.entrySet()) {
            if (!map.containsKey(entry.getKey())) {
                map.add(entry.getKey(), entry.getValue());
            }
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map,
                headers);

        var result = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseClass);
        if (result.getStatusCode() == HttpStatus.OK) {
            return result.getBody();
        }

        return null;
    }

    private <T, U> T executeMethod(String url, U request, Class<T> responseClass)
            throws JsonParseException, JsonMappingException, IOException {

        var jsonNode = restTemplate.postForObject(url, request, JsonNode.class);
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
    
    private String getDownloadUrlWithToken() {
        if(downloadUrlWithToken == null) {
            downloadUrlWithToken = downloadUrl + token+ '/';
        }
        return downloadUrlWithToken;
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

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public MethodExecutor setDownloadUrl(String downloadUrl) {
        if(this.downloadUrl == null) {
            this.downloadUrl = downloadUrl;
        }
        return this;
    }

    public String getDownloadDirectory() {
        return downloadDirectory;
    }

    public MethodExecutor setDownloadDirectory(String downloadDirectory) {
        if(this.downloadDirectory == null) {
            this.downloadDirectory = downloadDirectory;
        }
        return this;
    }

}
