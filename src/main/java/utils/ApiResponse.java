package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApiResponse {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static void success(HttpServletResponse response, int statusCode, Object data) throws IOException {

        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        result.put("success", true);
        result.put("data", data);

        objectMapper.writeValue(response.getWriter(), result);
    }

    public static void error(HttpServletResponse response, int statusCode, String message) throws IOException {

        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        result.put("success", false);
        result.put("error", message);

        objectMapper.writeValue(response.getWriter(), result);
    }
}