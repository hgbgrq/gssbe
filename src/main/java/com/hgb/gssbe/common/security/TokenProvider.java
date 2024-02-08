package com.hgb.gssbe.common.security;

import com.hgb.gssbe.login.model.UserGrantedAuthority;
import com.hgb.gssbe.login.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    public static final String AUTHORITIES_KEY = "auth";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String USER = "userId";
    private final long tokenValidityInMilliseconds;
    private final String secret;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.secret = secret;
    }

    private static Claims extractClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public String createToken(UserInfo userInfo) {

        String authorities = userInfo.getGrantedAuthorityList().stream().map(UserGrantedAuthority::getAuthId).collect(Collectors.joining(","));
        log.info("권한:" + authorities);

        Claims claims = Jwts.claims();
        claims.put(USER, userInfo.getUserId());
        claims.put(AUTHORITIES_KEY, authorities);

        // 토큰의 expire 시간을 설정
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(validity)
                .compact();
    }

    // 토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 authentication 객체를 리턴
    public Authentication getAuthentication(String token) {
        String userId = getUserId(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(extractClaims(token, secret).get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(userId, token, authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public String getUserId(String token) {
        return extractClaims(token, secret).get("userId").toString();

    }

    public boolean isExpired(String token) {
        Date expiredDate = extractClaims(token, secret).getExpiration();
        return expiredDate.before(new Date());
    }

    public boolean validateToken(String token) {
        if (isExpired(token)) {
            return false;
        }
        return true;
    }

}
