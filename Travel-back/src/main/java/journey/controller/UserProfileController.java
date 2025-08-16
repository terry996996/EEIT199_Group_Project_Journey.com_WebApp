/**
 * User Profile API
 * @author Max
 * @since 2025-07-10
 */
package journey.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.dto.user.JwtDTO;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtDTO jwt)) {
            return ResponseEntity.status(401).body("未登入");
        }
        return ResponseEntity.ok(Map.of(
            "username", jwt.getUsername(),
            "userType", jwt.getUserType(),
            "email", jwt.getEmail()
        ));
    }
} 