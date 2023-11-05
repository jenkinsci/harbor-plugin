package io.jenkins.plugins.harbor.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public final class JsonParser {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setDateFormat(new StdDateFormat())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static <T> T toJava(String data, Class<T> type) throws JsonProcessingException {
        return objectMapper.readValue(data, type);
    }
}
