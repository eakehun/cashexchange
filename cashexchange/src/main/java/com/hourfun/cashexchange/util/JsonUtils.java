package com.hourfun.cashexchange.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Map;

public final class JsonUtils {
    @SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    /**
     * json값이 json 타입이 맞는지 확인.
     */
    public static boolean maybeJson(String json) {
        return json != null && !"null".equals(json)
                && ((json.startsWith("[") && json.endsWith("]"))
                || (json.startsWith("{") && json.endsWith("}")));
    }

    /**
     * json 값이 Json타입이면서 값이 비어있지 않은지 확인.
     *
     * @param json
     * @return
     */
    public static boolean maybeJsonAndNotEmpty(String json) {
        return maybeJson(json) && !"[]".equals(json) && !"{}".equals(json);
    }

    public static String toJson(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String jsonStr, Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonStr, cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toJsonNodeFromJson(JsonNode jsonNode, Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(jsonNode, cls);
    }

    public static <T> T fromJson(String jsonStr, TypeReference<T> typeReference) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonStr, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode fromJson(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("rawtypes")
	public static <T> T mapToClass(Map map, Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(map, cls);
    }

    @SuppressWarnings("rawtypes")
	public static <T extends Collection> T fromJson(String jsonStr, CollectionType collectionType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonStr, collectionType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toPrettyJson(String json) {
        Object jsonObject = JsonUtils.fromJson(json, Object.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String inputStreamToString(InputStream input) throws IOException {
        BufferedReader bR = new BufferedReader(new InputStreamReader(input));
        String line = "";
        StringBuilder responseStrBuilder = new StringBuilder();
        while ((line = bR.readLine()) != null) {
            responseStrBuilder.append(line);
        }
        return responseStrBuilder.toString();
    }


}


