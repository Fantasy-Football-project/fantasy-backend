package com.sathvik.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sathvik.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {
    //In order to generate and read the JWT, a secret key is necessary.
    @Value("${security.jwt.token.secret-key:secret-value}")
    private String secretKey;

    private final UserService userService;

    //This annotation is for a method that needs to be executed after dependency
    //injection by Spring.
    @PostConstruct
    public void init() {
        //The purpose of this line is to avoid having the secret key in plaintext in the JVM,
        //and rather have it encoded.
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //The purpose of this method is to create the JWT token. It is set to expire after an hour.
    public String createToken(String login) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3_600_000);

        return JWT.create()
                .withIssuer(login)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC256(secretKey));
    }

    //The purpose of this method is to validate the token.
    public UsernamePasswordAuthenticationToken validateToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();

        //To verify the JWT, we first need to decode the token.
        DecodedJWT jwt = verifier.verify(token);

        UserDto user = userService.findByLogin(decoded.getIssuer());

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        //We are also checking to see if the user already exists in the database.
    }

}
