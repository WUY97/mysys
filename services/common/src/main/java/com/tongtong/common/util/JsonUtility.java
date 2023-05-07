package com.tongtong.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtility {

    public static final Logger logger = LoggerFactory.getLogger(JsonUtility.class);

    public static <T> String stringify(T object) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Unable to parse object into json : " + object.toString());
            logger.error(e.getStackTrace().toString());
        }
        return jsonStr;
    }

    public static <T> T parse(String jsonStr, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        T object = null;
        try {
            object = mapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            logger.error("Unable to parse json string into object : " + jsonStr);
            logger.error(e.getStackTrace().toString());
        }
        return object;
    }
}
