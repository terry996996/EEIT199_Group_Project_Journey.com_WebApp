package journey.config;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * API 速率限制配置
 * 
 * 功能說明：
 * - 防止 API 濫用和惡意攻擊
 * - 基於 IP 地址的速率限制
 * - 支援不同端點的不同限制規則
 * - 使用記憶體快取進行計數
 * 
 * 限制規則：
 * - 評論創建：每分鐘最多 10 次
 * - 按讚操作：每分鐘最多 20 次
 * - 評論查詢：每分鐘最多 100 次
 * - 檢舉提交：每分鐘最多 5 次
 * 
 * @author Journey Team
 * @version 1.0
 * @since 2025-07-15
 */
@Configuration
public class RateLimitConfig implements WebMvcConfigurer {

    private final ConcurrentHashMap<String, RateLimitInfo> rateLimitCache = new ConcurrentHashMap<>();
    
    // 速率限制規則
    private static final int COMMENT_CREATE_LIMIT = 10; // 每分鐘最多 10 次
    private static final int LIKE_OPERATION_LIMIT = 20; // 每分鐘最多 20 次
    private static final int COMMENT_QUERY_LIMIT = 100; // 每分鐘最多 100 次
    private static final int REPORT_SUBMIT_LIMIT = 5;   // 每分鐘最多 5 次
    
    private static final Duration WINDOW_DURATION = Duration.ofMinutes(1);

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor())
                .addPathPatterns("/api/comments/**", "/api/comment-reports/**");
    }

    /**
     * 速率限制攔截器
     */
    private class RateLimitInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String clientIp = getClientIpAddress(request);
            String requestPath = request.getRequestURI();
            String httpMethod = request.getMethod();
            
            // 組合鍵：IP + 路徑 + 方法
            String key = clientIp + ":" + requestPath + ":" + httpMethod;
            
            // 獲取限制規則
            int limit = getRateLimit(requestPath, httpMethod);
            
            // 檢查速率限制
            if (!isAllowed(key, limit)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"速率限制已達上限，請稍後再試\",\"retryAfter\":60}");
                return false;
            }
            
            return true;
        }

        /**
         * 獲取客戶端真實 IP 地址
         */
        private String getClientIpAddress(HttpServletRequest request) {
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
                return xForwardedFor.split(",")[0].trim();
            }
            
            String xRealIp = request.getHeader("X-Real-IP");
            if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
                return xRealIp;
            }
            
            return request.getRemoteAddr();
        }

        /**
         * 根據路徑和方法獲取速率限制規則
         */
        private int getRateLimit(String path, String method) {
            // 評論創建
            if (path.startsWith("/api/comments") && "POST".equals(method)) {
                return COMMENT_CREATE_LIMIT;
            }
            
            // 按讚操作
            if (path.matches("/api/comments/\\d+/like") && "POST".equals(method)) {
                return LIKE_OPERATION_LIMIT;
            }
            
            // 檢舉提交
            if (path.equals("/api/comment-reports") && "POST".equals(method)) {
                return REPORT_SUBMIT_LIMIT;
            }
            
            // 評論查詢（預設）
            return COMMENT_QUERY_LIMIT;
        }

        /**
         * 檢查是否允許請求
         */
        private boolean isAllowed(String key, int limit) {
            long now = System.currentTimeMillis();
            RateLimitInfo info = rateLimitCache.compute(key, (k, v) -> {
                if (v == null || now - v.getStartTime() > WINDOW_DURATION.toMillis()) {
                    return new RateLimitInfo(now);
                }
                return v;
            });
            
            return info.incrementAndGet() <= limit;
        }
    }

    /**
     * 速率限制資訊類別
     */
    private static class RateLimitInfo {
        private final long startTime;
        private final AtomicInteger count;

        public RateLimitInfo(long startTime) {
            this.startTime = startTime;
            this.count = new AtomicInteger(0);
        }

        public long getStartTime() {
            return startTime;
        }

        public int incrementAndGet() {
            return count.incrementAndGet();
        }
    }
} 