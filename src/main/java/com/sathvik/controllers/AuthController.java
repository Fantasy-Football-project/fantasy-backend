package com.sathvik.controllers;

import com.sathvik.config.UserAuthProvider;
import com.sathvik.dto.CredentialsDto;
import com.sathvik.dto.SignUpDto;
import com.sathvik.dto.UserDto;
import com.sathvik.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Base64;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto user = userService.login(credentialsDto);

        //Once logged in, a fresh JWT is made.
        user.setToken(userAuthProvider.createToken(user.getLogin()));

        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto userDto) {
        UserDto user = userService.register(userDto);

        //Once registered, a fresh JWT is made.
        user.setToken(userAuthProvider.createToken(user.getLogin()));

        //When creating an entity (in this case a user), it is best practice to return
        //a 201 HTTP code, with the URL where we can find the new entity.
        return ResponseEntity.created(URI.create("/users/" + user.getId()))
                .body(user);
    }

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        try {
            byte[] decodedToken = Base64.getDecoder().decode(token);
            String decodedTokenString = new String(decodedToken);
            UsernamePasswordAuthenticationToken auth = userAuthProvider.validateToken(decodedTokenString);
            //UsernamePasswordAuthenticationToken auth = userAuthProvider.validateToken(token);
            return ResponseEntity.ok(auth != null ? "Token is valid" : "Token is invalid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed: " + e.getMessage());
        }
    }
}
