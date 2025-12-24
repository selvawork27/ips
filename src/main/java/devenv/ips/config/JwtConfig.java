package devenv.ips.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys; // Important import
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtConfig {
    
    private final String SECRET = "your-very-long-secret-key-with-at-least-64-characters-for-hs512-security";
    
    // Convert the string to a Key object
    private final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_TIME = 86400000; 

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS512) // Use Key object
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