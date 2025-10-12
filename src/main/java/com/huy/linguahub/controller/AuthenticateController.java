package com.huy.linguahub.controller;

import com.huy.linguahub.service.dto.request.ReqLoginDTO;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticateController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthenticateController(AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/auth/login")
    public String Login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                reqLoginDTO.getUsername(),
                reqLoginDTO.getPassword()
        );
        //authenticate => implement UserDetailsService and override loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Set authentication to SecurityContext (used later in controllers, services,..)
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return "Authenticate";
    }
}
