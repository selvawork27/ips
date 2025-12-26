package devenv.ips.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys; // Important import
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import devenv.ips.entity.user.User;

@Component
public class JwtConfig {
    
    private final String SECRET = "your-very-long-secret-key-with-at-least-64-characters-for-hs512-security";
    
    // Convert the string to a Key object
    private final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_TIME = 86400000; 

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder() // Use parserBuilder() for modern JJWT
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}