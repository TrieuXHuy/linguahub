package com.huy.linguahub.service;

import com.huy.linguahub.domain.User;
import com.huy.linguahub.secutiry.SecurityUtils;
import com.huy.linguahub.service.dto.response.auth.ResUserTokenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthenticateService {

    @Value("${security.authentication.jwt.token-validity-in-seconds}")
    private long accessTokenExpiration;

    private final JwtEncoder jwtEncoder;

    public AuthenticateService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createToken(String email, User userDB) {
        ResUserTokenDTO userTokenDTO = new ResUserTokenDTO();

        userTokenDTO.setId(userDB.getId());
        userTokenDTO.setEmail(userDB.getEmail());
        userTokenDTO.setFullName(userDB.getFirstName() + " " + userDB.getLastName());

        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userTokenDTO)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(SecurityUtils.JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
