package com.gdczwlkj.common;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Created by franco on 2017/7/14.
 */
public class JwtUtil {

    private final static Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private final static String USER_ID_KEY = "userId";
    private final static String USER_NAME_KEY = "userName";
    private final static String SESSION_ID_KEY = "sessionID";
    private final static String ISSUER = "GDCZKJWL";
    private final static String REFRESH_TOKEN_EXPIRE_DATE_KEY = "refreshTokenExpireDate";
    /**
     * 生成JWT
     * @param securityUser
     * @param secret
     * @param accessTokenExpDate
     * @return
     */
    public static String generateToken(SecurityUser securityUser, String secret,
                                       Date accessTokenExpDate,
                                       Date refreshTokenExpDate) {
        try {
            if (securityUser != null && !StringUtils.isEmpty(securityUser.getUserName())
                && securityUser.getUserId() != null && !StringUtils.isEmpty(secret)) {
                Algorithm algorithm = Algorithm.HMAC256(secret);
                String token = JWT.create()
                    .withClaim(USER_ID_KEY, securityUser.getUserId().toString())
                    .withClaim(USER_NAME_KEY, securityUser.getUserName())
                    .withClaim(SESSION_ID_KEY, securityUser.getSessionId())
                    .withClaim(REFRESH_TOKEN_EXPIRE_DATE_KEY, refreshTokenExpDate.getTime())
                    .withIssuer(ISSUER)
                    .withExpiresAt(accessTokenExpDate)
                    .sign(algorithm);
                logger.debug("SecurityUser生成Token=====" + token);
                return token;
            } else {
                return null;
                //throw new ParameterException("-1","参数错误，用户唯一标识需要userid,username，secret不能为空");
            }
        } catch (UnsupportedEncodingException e) {
           // throw new ParameterException("-1","secret编码错误");
            return null;
        } catch (JWTCreationException exception) {
            //throw new ParameterException("-1","生成token失败");
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
            //throw new ParameterException("-1","生成token失败");
        }
    }

    /**
     * 验证token是否有效
     * @param token
     * @param secret
     */
    public static SecurityUser verifyToken(String token, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            Integer userId = Integer.valueOf(decodedJWT.getClaim(USER_ID_KEY).asString());
            String userName = decodedJWT.getClaim(USER_NAME_KEY).asString();
            Date date = decodedJWT.getClaim(PublicClaims.EXPIRES_AT).asDate();
            String sessionId = decodedJWT.getClaim(SESSION_ID_KEY).asString();
            return new SecurityUser(userId, userName, sessionId, date.getTime());
        } catch (UnsupportedEncodingException e) {
            return null;
            //throw new ParameterException("-1","secret编码错误");
        } catch (JWTVerificationException exception){
            return null;
            //throw new ParameterException("-1","验证token失败，token错误或已经过期");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
            //throw new ParameterException("-1","验证token失败");
        }
    }


    public static void main(String[] args) {
        SecurityUser securityUser = new SecurityUser(10000,"franco", "AAA", System.currentTimeMillis());
        String secret = "FEBCBAAE13751F7F8E44C2F107AFB08D";
        Date accessTokenExpDate = Date.from(LocalDateTime.now().plusMinutes(1)
            .atZone(ZoneId.systemDefault())
            .toInstant());
        Date refreshTokenExpDate = Date.from(LocalDateTime.now().plusMinutes(30)
            .atZone(ZoneId.systemDefault())
            .toInstant());
        String token = generateToken(securityUser, secret, accessTokenExpDate, refreshTokenExpDate);

        verifyToken(token, secret);
    }


}
