package com.sathvik.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
//The parent class is so that this filter is only used once per request.
public class JwtAuthFilter extends OncePerRequestFilter {

    private UserAuthProvider userAuthProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //Request refers to the incoming http request, and the getHeader method fetches the value of
        //the authorization header from the request.
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null) {
            //The header is split into an array of elements, separated by spaces.
            String[] elements = header.split(" ");

            //Condition checks if the length of the elements array is 2, and if the
            //first element is equal to the string "Bearer".
            if (elements.length == 2 && elements[0].equals("Bearer")) {
                try {
                    //If the credentials are valid, the authentication bean is added into the
                    //security context.
                    SecurityContextHolder.getContext().setAuthentication(
                            userAuthProvider.validateToken(elements[1])
                    );
                }
                catch (RuntimeException e) {
                    //If something goes wrong, error message is thrown and security context is cleared.
                    SecurityContextHolder.clearContext();
                    throw e;
                }
            }
        }

        //idk the point of this figure out later. just something necessary
        filterChain.doFilter(request, response);
    }
}
