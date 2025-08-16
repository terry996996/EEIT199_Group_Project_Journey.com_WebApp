package journey.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * custom configurations
 * 
 * @author 乃文
 * @version 1.0
 * @since 07/02/2025
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Value("${file.upload-dir}")
        private String uploadDir;

        /**
         * maps frontend request path to the actual path it the project directory,
         * each path should be registrated respectively in THIS method!
         * 
         * usage: registry.addResourceHandler(H).addResourceLocations(R)
         * 
         * H: frontend request path, ex: localhost:8080/myPath => H = myPath
         * R: the actual path of the resources in the project directory
         */
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {

                // for user avatars path
                registry.addResourceHandler("/uploads/avatars/**")
                                .addResourceLocations("file:" + uploadDir + "avatars/");

                // for lodging images path
                registry.addResourceHandler("/uploads/lodgings/**")
                                .addResourceLocations("file:" + uploadDir + "lodgings/");

                // for room type images path
                registry.addResourceHandler("/uploads/room_types/**")
                                .addResourceLocations("file:" + uploadDir + "room_types/");

                // for comment images path
                registry.addResourceHandler("/uploads/comments/**")
                                .addResourceLocations("file:" + uploadDir + "comments/");

        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允許所有路徑
                                .allowedOrigins(
                                                "http://localhost:5173", // 前端
                                                "https://payment-stage.ecpay.com.tw", // 綠界測試環境支付頁面
                                                "https://payment.ecpay.com.tw", // 綠界正式環境支付頁面
                                                "null")

                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                                .allowedHeaders("*")
                                .exposedHeaders("*")
                                .allowCredentials(true)
                                .maxAge(3600);
        }

}
