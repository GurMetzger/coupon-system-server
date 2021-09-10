package app.core.utils;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import app.core.exceptions.TokenValidationException;
import app.core.login.ClientType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtUtil {

	// Token Algorithm
	private String signatureAlgorithm = SignatureAlgorithm.HS256.getJcaName();
	// Secret Key
	private String encodedSecretKey = "HJHJf67n6TYU67BV4C465476698799IykJgU876MHGJHGJFTTUrLgN";

	private Key decodedSecretKey = new SecretKeySpec(Base64.getDecoder().decode(encodedSecretKey), this.signatureAlgorithm);

	/** For creating new Tokens after a successful login */
	public String generateToken(int id, String name, String email, ClientType clientType) {

		Map<String, Object> claims = new HashMap<>();
		claims.put("id", id);
		claims.put("name", name);
		claims.put("clientType", clientType);

		return createToken(claims, email);
	}

	private String createToken(Map<String, Object> claims, String subject) {
		Instant now = Instant.now();

		return Jwts.builder().setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plus(10, ChronoUnit.HOURS)))
				.signWith(this.decodedSecretKey)
				.compact();
	}

	// Extracts all the claims in a body of a token
	private Claims extractAllClaims(String token) {
		JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(this.decodedSecretKey).build();
		return jwtParser.parseClaimsJws(token).getBody();
	}

	public Date extractExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	/**
	 * Will throw an Exception if:
	 * 
	 * 1. Token's signature is incorrect
	 * 2. Token has expired
	 * 3. Any other reason
	 */
	public void validateToken(String token) throws TokenValidationException {
		try {
			extractAllClaims(token);
		} catch (SignatureException e) {
			throw new TokenValidationException("Token's Signature does not match!", e);
		} catch (ExpiredJwtException e) {
			throw new TokenValidationException("Your Token has expired! Please log in again", e);
		} catch (Exception e) {
			throw new TokenValidationException(e.getMessage(), e);
		}
	}

}
