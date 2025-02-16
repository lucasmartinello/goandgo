package org.goandgo.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.goandgo.usuario.Usuario;
import java.security.Key;
import java.util.Date;

public class JWTController {
    public static final String X_IDENTIFICADOR = "GoAndGo1234567890";
    private static final String SECRET_KEY_BASE64 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY_BASE64));

    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getId().toString()) // ID do usuário
                .setIssuedAt(new Date()) // Data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Expira em 1 hora
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // Assinar com a chave secreta
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().after(new Date()); // Verifica se ainda é válido
        } catch (Exception e) {
            return false; // Token inválido ou expirado
        }
    }
}