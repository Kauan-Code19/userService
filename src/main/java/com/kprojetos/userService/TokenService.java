package com.kprojetos.userService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateUserToken(UserEntity userEntity) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("userService")
                    .withSubject(userEntity.getEmail())
                    .withClaim("id", userEntity.getId())
                    .withClaim("role", userEntity.getProfile().getType())
                    .withExpiresAt(generateTokenExpirationTime())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }


    private Instant generateTokenExpirationTime() {
        return LocalDateTime.now().plusMinutes(25).toInstant(ZoneOffset.of("-03:00"));
    }


    public String getSubject(String token) {
        DecodedJWT jwt = validateToken(token);
        return jwt.getSubject();
    }


    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("userService")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            throw createExpiredTokenException(exception);
        }
    }


    private RuntimeException createExpiredTokenException(
            JWTVerificationException exception
    ) {
        Instant expiracao = extractTokenExpiration(
                exception.getMessage()
        );
        String expiracaoFormatada = formatToBrazilian(expiracao);
        return new RuntimeException(
                "Token Expirado em: "
                        + expiracaoFormatada
        );
    }


    private Instant extractTokenExpiration(String mensagemOriginal) {
        if (mensagemOriginal == null || !mensagemOriginal.
                contains("The Token has expired on")
        ) { return Instant.now(); }

        String dataExpiracaoString = mensagemOriginal
                .replace("The Token has expired on", "")
                .trim()
                .replace(".", "");

        return Instant.parse(dataExpiracaoString);
    }


    private String formatToBrazilian(Instant instant) {
        LocalDateTime dataHora = LocalDateTime.ofInstant(instant, ZoneOffset.of("-03:00"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dataHora.format(formatter);
    }
}
