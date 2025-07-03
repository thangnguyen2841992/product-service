package com.order.product.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final String JWT_SECRET = "839874983AVCfsds7274892378947238947238avcvsdsdw9472398723984723987239";

    // Lấy serect key
    private byte[] getSigneKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes).getEncoded();
    }

    // Trích xuất thông tin
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigneKey()).parseClaimsJws(token).getBody();
    }

    // Trích xuất thông tin cho 1 claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Kiểm tra tời gian hết hạn từ JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Kiểm tra tời gian hết hạn từ JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Kiểm tra cái JWT đã hết hạn
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Kiểm tra tính hợp lệ
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        System.out.println(username);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
