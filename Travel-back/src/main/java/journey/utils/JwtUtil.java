package journey.utils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import journey.domain.users.AllUserBean;
import journey.dto.user.JwtDTO;
import journey.enums.UserTypeEnum;

/**
 * create and verify JWT
 * 
 * @author 乃文
 * @version 1.0
 * @since 07/03/2025
 */
public class JwtUtil {

    private static final String STORE_NAME = "jwtkeystore.jks";
    private static final String STORE_PASS = "P@ssw0rd";
    private static final String ALIAS = "EEIT199Travel_JWT";

    /**
     * how long does the JWT expire, default = 7 days
     */
    private static final long EXPIRE_DAYS = 7;

    private static final RSAPrivateKey PRIVATE_KEY = (RSAPrivateKey) LoadKey.getPrivateKey(STORE_NAME, STORE_PASS,
            ALIAS);
    private static final RSAPublicKey PUBLIC_KEY = (RSAPublicKey) LoadKey.getPublicKey(STORE_NAME, STORE_PASS,
            ALIAS);

    /**
     * create JWT
     * 
     * @return base64Url of the token
     */
    public static String createJWT(AllUserBean user) {
        if (user != null) {
            Algorithm algorithm = Algorithm.RSA256(null, PRIVATE_KEY);
            LocalDateTime now = LocalDateTime.now().plusDays(EXPIRE_DAYS);
            Date expire = Date.from(now.atZone(ZoneId.of("Asia/Taipei")).toInstant());

            return JWT.create()
                    .withIssuer("Journey.com")
                    .withAudience("Journey.com")
                    .withExpiresAt(expire)
                    .withClaim("id", user.getId())
                    .withClaim("user", user.getUsername())
                    .withClaim("email", user.getEmail())
                    .withClaim("userType", user.getUserType().getType().name()) // USER, ADMIN, or VENDOR
                    .withClaim("icon", user.getIcon())
                    .sign(algorithm);
        }
        return null;
    }

    /**
     * verfiy the JWT token
     * 
     * @return a dto contains user id, name, type @see JwtDTO
     */
    public static JwtDTO verfiy(String token) {
        if (token != null && token.length() != 0) {
            Algorithm algorithm = Algorithm.RSA256(PUBLIC_KEY, null);
            try {
                DecodedJWT jwt = JWT
                        .require(algorithm).withIssuer("Journey.com")
                        .build()
                        .verify(token);
                Map<String, Claim> claims = jwt.getClaims();
                return new JwtDTO(
                        claims.get("id").asInt(),
                        claims.get("user").asString(),
                        claims.get("email").asString(),
                        UserTypeEnum.valueOf(claims.get("userType").asString()));

            } catch (TokenExpiredException e) {
                System.out.println("Token Has Expired!");
                return null;
            } catch (AlgorithmMismatchException e) {
                System.out.println("Algorithm mismatched!");
                return null;
            } catch (SignatureVerificationException e) {
                System.out.println("Signature Invalid!");
                return null;
            } catch (MissingClaimException e) {
                System.out.println("Missing Necessary Claim!");
                return null;
            } catch (JWTVerificationException e) {
                System.out.println("Error When Verifying JWT");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * extract the token from the authorization header
     * 
     * @author Fan
     * @version 1.0
     * @since 07/11/2025
     * @param authHeader the authorization header
     * @return the token
     */
    public static String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
