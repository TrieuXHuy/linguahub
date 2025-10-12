package com.huy.linguahub.controller;

import com.huy.linguahub.domain.User;
import com.huy.linguahub.service.AuthenticateService;
import com.huy.linguahub.service.UserService;
import com.huy.linguahub.service.dto.request.ReqLoginDTO;
import com.huy.linguahub.service.dto.response.auth.ResLoginDTO;
import com.huy.linguahub.service.dto.response.auth.ResUserLoginDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    @Value("${security.authentication.jwt.token-validity-in-seconds}")
    private long accessTokenExpiration;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthenticateService authenticateService;
    private final UserService userService;

    public AuthenticateController(AuthenticationManagerBuilder authenticationManagerBuilder, AuthenticateService authenticateService, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.authenticateService = authenticateService;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> Login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                reqLoginDTO.getUsername(),
                reqLoginDTO.getPassword()
        );
        //authenticate => implement UserDetailsService and override loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Set authentication to SecurityContext (used later in controllers, services,..)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = authenticateService.createToken(authentication);

        ResLoginDTO loginDTO = new ResLoginDTO();
        loginDTO.setAccessToken(accessToken);
        loginDTO.setTokenType("Bearer");
        loginDTO.setExpiresIn(accessTokenExpiration);

        User userDB = this.userService.getUserByUsername(authentication.getName());

        if(userDB != null) {
            ResUserLoginDTO userLoginDTO = new ResUserLoginDTO();
            userLoginDTO.setId(userDB.getId());
            userLoginDTO.setEmail(userDB.getEmail());
            userLoginDTO.setFullName(userDB.getFirstName() + " " + userDB.getLastName());
            loginDTO.setUser(userLoginDTO);
        }
        return ResponseEntity.ok(loginDTO);
    }
}
