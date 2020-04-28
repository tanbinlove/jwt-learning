package com.tanbin.jwt.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken生成的工具类
 */
@Component
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    // 用户信息是用map形式存放的
    //用户名的键
    private static final String CLAIM_KEY_USERNAME = "sub";
    //过期时间的键
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("$(jwt.expiration)")
    private Long expiration;

    /**
     * 根据用户信息生成mao
     */
    public String generateToken(Map<String, Object> claims) {
        //设置用户信息
        //设置过期时间
        //设置加密算法，提供密匙
        //生成
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取payload（包含用户信息）
     * @param token
     * @return
     */
    private Claims getClaimFromToken(String token){
        Claims claims = null;
        try{
           claims = Jwts.parser()
                   .setSigningKey(secret)
                   .parseClaimsJws(token)
                   .getBody();
        }catch(Exception e){
            LOGGER.info("JWT解析失败:{}", token);
        }
        return claims;
    }

    /**
     * 生成过期时间
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取用户名
     */
    public String getUsernameFormToken(String token){
        String username = null;
        try{
            Claims claims = getClaimFromToken(token);
            username = claims.getSubject();
        }catch (Exception e){
            LOGGER.info("获取用户名失败：{}", token);
        }
        return username;
    }

    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUsernameFormToken(token);
        return username.equals(userDetails.getUsername()) && isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 提取UserDetails中的用户名，在加上当前时间，生成token
     *
     */
    public String generateToken(UserDetails userDetails ){
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 判断token是否可以被刷新
     *只要没过期就不能刷新
     */
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     */
    public String refresh(String token){
        Claims claims = getClaimFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}
