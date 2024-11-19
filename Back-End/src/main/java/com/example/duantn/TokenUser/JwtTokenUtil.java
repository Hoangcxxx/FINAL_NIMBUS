package com.example.duantn.TokenUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenUtil {

    // Giả sử bạn có một danh sách đen (blacklist) để lưu trữ token đã vô hiệu hóa
    private static final Set<String> invalidTokens = new HashSet<>();

    // Phương thức vô hiệu hóa token
    public void invalidateToken(String token) {
        invalidTokens.add(token);  // Lưu token vào danh sách đen
    }

    // Phương thức kiểm tra token có hợp lệ hay không (có trong blacklist không)
    public boolean isTokenValid(String token) {
        return !invalidTokens.contains(token);
    }
    private final SecretKey secretKeyForAccessToken ;
    private final SecretKey secretKeyForRefreshToken = Keys.secretKeyFor(SignatureAlgorithm.HS256);

//    private  static  final long EXPIRATION_TIME_FOR_TOKEN = 604_800_000; //1 Day
//    private  static  final long EXPIRATION_TIME_FOR_REFRSH_TOKEN = 604_800_000; //1 Day

    private static final long EXPIRATION_TIME_FOR_TOKEN = 2_592_000_000L; // 1 Month (30 Days)
    private static final long EXPIRATION_TIME_FOR_REFRSH_TOKEN = 2_592_000_000L; // 1 Month (30 Days)


    public JwtTokenUtil(){
        //Khởi tạo Secret key
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.secretKeyForAccessToken = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    //Tạo Token
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        // Extract roles and add them as claims
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Extract the authority name
                .collect(Collectors.toList());
        claims.put("roles", roles);
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_FOR_TOKEN))
                .signWith(secretKeyForAccessToken)
                .compact();
    }

    // Tạo refresh Token
    public String generateRefreshToken( UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        // Extract roles and add them as claims
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Extract the authority name
                .collect(Collectors.toList());
        claims.put("roles", roles);
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_FOR_REFRSH_TOKEN))
                .signWith(secretKeyForRefreshToken)
                .compact();
    }


    // TODO: Các phương thức có sẵn

    //Tách email ra từ JWT Token
    public String extractUsernameToken(String token) {
        try {
            return extractClaims(token, Claims::getSubject);
        } catch (Exception e) {
            log.error("Error extracting username from token", e);
            return null; // Hoặc xử lý theo cách khác
        }
    }

    //Tách email ra từ JWT Token


    private <T> T extractClaims( String token, Function<Claims, T> claimsTFunction){


        return claimsTFunction.apply(
                Jwts.parser().verifyWith(secretKeyForAccessToken).build().parseSignedClaims(token).getPayload()
        );

    }
    public List<String> extractClaimsRoleToken(String token) {
        Claims claims;
        try {
            // Parse the token and get the claims
            claims = Jwts.parser().verifyWith(secretKeyForAccessToken).build().parseSignedClaims(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e); // Handle the error appropriately
        }

        // Extract roles from the claims
        return claims.get("roles", List.class); // Return the roles as a List<String>
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsernameToken(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isRefreshValid(String token, UserDetails userDetails) {
        final String username = extractUsernameToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired( String token) {
        return extractClaims( token, Claims::getExpiration).before(new Date());
    }



}