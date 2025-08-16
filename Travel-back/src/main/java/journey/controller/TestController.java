package journey.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
// @CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" },
// allowCredentials = "true")
public class TestController {

    @GetMapping("/cors")
    public Map<String, Object> testCors() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "CORS is working!");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", "success");
        return response;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Server is running");
        return response;
    }

    @GetMapping("/security")
    public Map<String, Object> testSecurity() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Security is configured correctly!");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", "success");
        response.put("apiAccess", "enabled");
        return response;
    }

    @GetMapping("/comments/test")
    public Map<String, Object> testCommentsEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Comments API endpoint is accessible!");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", "success");
        response.put("endpoint", "/api/comments/**");
        return response;
    }
}