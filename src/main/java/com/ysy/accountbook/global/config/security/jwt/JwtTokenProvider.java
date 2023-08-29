package com.ysy.accountbook.global.config.security.jwt;

import com.ysy.accountbook.domain.user.entity.Password;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import com.ysy.accountbook.global.config.security.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * • JWT 토큰을 생성하는 클래스<br><br>
 * • Access Token : LocalStorage / Refresh Token : Cookie(http only secure)에 저장한다<br><br>
 * • Refresh Token은 DB에 저장하여 갱신시 일치여부 판단을 하는데 사용한다<br><br>
 */
@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@Component
public class JwtTokenProvider {
    private final String SECRET_KEY;
    //private final String COOKIE_REFRESH_TOKEN_KEY;
    private final Long ACCESS_TOKEN_EXPIRE_LENGTH;
    private final Long REFRESH_TOKEN_EXPIRE_LENGTH;
    private final String AUTHORITIES_KEY = "role";
    private final String ISSUER;
    private final Key key;
    private final PasswordEncoder passwordEncoder;
    //private final UserRepository userRepository;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey,
                            @Value("${jwt.cookie-key}") String cookieKey,
                            @Value("${jwt.access-token-validity-in-second}") long accessTokenExpireLength,
                            @Value("${jwt.refresh-token-validity-in-second}") long refreshTokenExpireLength,
                            @Value("${jwt.issuer}") String issuer,
                            UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {

        this.SECRET_KEY = Base64.getEncoder()
                                .encodeToString(secretKey.getBytes());
        //this.COOKIE_REFRESH_TOKEN_KEY = cookieKey;
        this.ACCESS_TOKEN_EXPIRE_LENGTH = accessTokenExpireLength;
        this.REFRESH_TOKEN_EXPIRE_LENGTH = refreshTokenExpireLength;
        this.ISSUER = issuer;

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.passwordEncoder = passwordEncoder;
        //this.userRepository = userRepository;
    }

    /**
     * 엑세스 토큰 생성
     *
     * @param authentication
     * @return
     */
    public String createAccessToken(Authentication authentication) {
        log.debug("excute createAccessToken()");

        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String email = userDetails.getUsername();
        String role = authentication.getAuthorities()
                                    .stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.joining(","));

        return Jwts.builder()
                   .signWith(key,
                             SignatureAlgorithm.HS256)
                   .setSubject(email)
                   .claim(AUTHORITIES_KEY,
                          role)
                   .setIssuer(ISSUER)
                   .setIssuedAt(now)
                   .setExpiration(validity)
                   .compact();
    }

    /**
     * 리프레쉬 토큰 생성
     *
     * @param authentication
     */
    public String createRefreshToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_LENGTH);

        String refreshToken = Jwts.builder()
                                  .signWith(key,
                                            SignatureAlgorithm.HS256)
                                  .setIssuer(ISSUER)
                                  .setIssuedAt(now)
                                  .setExpiration(validity)
                                  .compact();

        saveRefreshToken(authentication,
                         refreshToken);
        return refreshToken;
    }

    private void saveRefreshToken(Authentication authentication,
                                  String refreshToken) {
        CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();
        //userRepository.updateRefreshToken(id, refreshToken);
    }

    // Access Token을 검사하고 얻은 정보로 Authentication 객체 생성
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY)
                                                                                 .toString()
                                                                                 .split(","))
                                                                   .map(SimpleGrantedAuthority::new)
                                                                   .collect(Collectors.toList());

        CustomUserDetails principal = new CustomUserDetails(claims.getSubject(),
                                                            passwordEncoder.encode(Password.PASSWORD),
                                                            authorities);

        return new UsernamePasswordAuthenticationToken(principal,
                                                       passwordEncoder.encode(Password.PASSWORD),
                                                       authorities);
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalStateException e) {
            log.info("JWT 토큰이 잘못되었습니다");
        }
        return false;
    }

    // Access Token 만료시 갱신때 사용할 정보를 얻기 위해 Claim 리턴
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                       .setSigningKey(key)
                       .build()
                       .parseClaimsJws(accessToken)
                       .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
