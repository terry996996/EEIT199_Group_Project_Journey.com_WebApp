package journey.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// 為了處理cors的問題以及spring security的潛在權限約束問題，定義此類別來設定可允許的路由
// 測試的時候permit權限先開最大，之後可以再調
// 為了處理cors的問題以及spring security的潛在權限約束問題，定義此類別來設定可允許的路由
// 測試的時候permit權限先開最大，之後可以再調

// 這個類別是為了啟用spring security寫的

/**
 * Security 設定
 *
 * @author Max
 * @since 2025-07-09
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 啟用 CORS 配置。Spring Security 會使用下面定義的 corsConfigurationSource() Bean。
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 禁用 CSRF 防護 (通常用於前後端分離的 RESTful API)
                .csrf(csrf -> csrf.disable())
                // 配置授權規則
                .authorizeHttpRequests(authorize -> authorize
                        // =========================================================================
                        // 關鍵修改點 1：允許所有 OPTIONS 請求通過，這是 CORS 預檢請求的關鍵
                        // 必須在任何需要 permitAll() 的路徑之前定義
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // =========================================================================
                        // =========================================================================
                        // .requestMatchers("/api/options/**").permitAll()
                        .requestMatchers("/api/login/**").permitAll()
                        .requestMatchers("/api/registrate/**").permitAll()
                        .requestMatchers("/api/locations/**").permitAll()
                        .requestMatchers("/api/profile/**").permitAll()
                        .requestMatchers("/api/partners/**").permitAll()
                        .requestMatchers("/api/plans/**").permitAll()

                        .requestMatchers("/flights/**").permitAll()
                        .requestMatchers("/traffictickets/**").permitAll()
                        .requestMatchers("/api/options/**").permitAll()
                        .requestMatchers("/api/attractions/**").permitAll()
                        .requestMatchers("/api/ticket-types/**").permitAll()
                        .requestMatchers("/api/tickets/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/api/lodgings/**").permitAll()
                        .requestMatchers("/api/vendor/lodgings/**").permitAll()
                        .requestMatchers("/api/comments/**").permitAll()
                        .requestMatchers("/api/points/**").permitAll()
                        .requestMatchers("/api/comment-reports/**").permitAll()
                        .requestMatchers("/api/images/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll() // 允許訪問上傳的檔案
                        .requestMatchers("/coupons/**").permitAll()
                        // 允許特定 POST /api/consults (如果 /api/consult/** 不夠精確)
                        .requestMatchers(HttpMethod.POST, "/api/consults").permitAll()
                        // =========================================================================
                        // =========================================================================

                        // 允許所有 /api/customer-service/ 下的 API 都無需認證
                        .requestMatchers("/api/customer-service/**").permitAll()
                        .requestMatchers("/api/feedback/**").permitAll()
                        .requestMatchers("/api/feedback/chatroom/**").permitAll()

                        // =========================================================================
                        // 允許 /api/consult/** 路徑下的所有請求無需認證
                        .requestMatchers("/api/consult/**").permitAll()
                        .requestMatchers("/api/user/info").permitAll()
                        // =========================================================================

                        // =========================================================================
                        // 支付相關的 API 端點和回調路徑無需認證

                        // 綠界支付發起端點
                        .requestMatchers("/api/ecpay/create-payment").permitAll()
                        // LINE Pay 支付發起端點
                        .requestMatchers("/api/linepay/create-payment").permitAll()

                        // **關鍵修正點：將綠界回調路徑精確匹配為 /ecpay/** ** (移除了 /api/)**
                        // 這會包含 /ecpay/notify, /ecpay/result, /ecpay/cancel
                        .requestMatchers("/ecpay/**").permitAll()

                        // LINE Pay 回調及相關頁面（例如 /api/linepay/confirm）
                        .requestMatchers("/api/linepay/**").permitAll()
                        // =========================================================================

                        // **非常重要：允許訪問 /error 路徑**
                        // 當應用程式內部發生例外時，Spring Boot 會轉發到 /error。
                        // 這行必須在 anyRequest().authenticated() 之前！
                        .requestMatchers("/error").permitAll()

                        // **非常重要：允許訪問 /api/auth/** 路徑**
                        .requestMatchers("/api/auth/**").permitAll()

                        // 允許 admin API 路徑
                        .requestMatchers("/api/admin/**").permitAll()

                        // 其他所有未明確指定的路徑都需要認證
                        // 這行應該是 authorizeHttpRequests 鏈中的最後一個匹配器
                        .anyRequest().authenticated())
                // 禁用 Spring Security 默認的表單登入機制
                .formLogin(form -> form.disable())
                // 禁用 HTTP Basic 認證 (如果不需要)
                .httpBasic(httpBasic -> httpBasic.disable())
                // 設定 Session 管理策略為無狀態 (對 RESTful API 很重要)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // =========================================================================
    // 關鍵修改點：定義 CORS 配置的 Bean
    // 這個 Bean 會被 securityFilterChain 中的 .cors() 方法使用
    // =========================================================================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 設置允許的前端來源，**同時加入綠界網域和 'null'**
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173", // 您的前端應用程式的地址和端口
                "https://payment-stage.ecpay.com.tw", // 綠界測試環境的支付頁面網域
                "https://payment.ecpay.com.tw", // 綠界正式環境的支付頁面網域
                "null" // **極度關鍵：當瀏覽器重定向或某些表單提交時，Origin 可能為 "null"**
        ));

        // 設置允許的 HTTP 方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 設置允許的請求頭
        configuration.setAllowedHeaders(List.of("*")); // 允許所有請求頭

        // 允許發送憑證 (Cookies, Authorization headers)
        // 這對您前端使用 axios.defaults.withCredentials = true; 非常重要
        configuration.setAllowCredentials(true);

        // 預檢請求的緩存時間 (秒)，可以減少 OPTIONS 請求的次數
        configuration.setMaxAge(3600L); // 設置為 1 小時

        // 允許發送 Response Header Authorization
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 將此 CORS 配置應用於所有路徑 (/**)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}