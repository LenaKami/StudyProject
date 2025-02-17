//package com.example.Player;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.io.Encoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@Service
//public class JwtService {
//
//    static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generuje klucz odpowiedniej długości
//    private static final String SECRET_KEY = Encoders.BASE64.encode(key.getEncoded());
//
//    public boolean isTokenValid(String token, UserDetails userDetails){
//        final String userName = extractUsername(token);
//        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
//    }
//
//    public boolean isTokenExpired(String token){
//        return extractExpiration(token).before(new Date());
//    }
//
//    public Date extractExpiration(String token){
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public String generateToken(UserDetails userDetails){
//        return  generateToken(new HashMap<>(), userDetails);
//    }
//
//    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
//        String roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        extraClaims.put("roles", roles);
//
//        return Jwts
//                .builder()
//                .addClaims(extraClaims)
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 24))
//                .signWith(getSignInKey())
//                .compact();
//    }
//
//    public String extractUsername(String token){
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token){
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private Key getSignInKey(){
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//}
