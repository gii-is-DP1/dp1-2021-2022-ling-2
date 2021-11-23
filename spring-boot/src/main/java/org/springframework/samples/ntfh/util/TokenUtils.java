package org.springframework.samples.ntfh.util;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.samples.ntfh.user.User;
import org.springframework.samples.ntfh.user.authorities.Authorities;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Contains methods useful for validating tokens and authentication.
 * 
 * @author andrsdt
 */
public class TokenUtils {

    private final static String SECRET = "VGhpcyBpcyBhIHNlY3JldCBrZXkgZm9yIG91ciBObyBUaW1lIGZvciBIZXJvZXMgZ2FtZS4gWW91IGtub3csIHRoZSBwb2ludCBpcyB0aGF0IHdlIGNhbiB1c2UgdGhpcyB0byB2YWxpZGF0ZSBpZiBhIHRva2VuIGhhcyBiZWVuIGlzc3VlZCBieSB1cyBvciBub3Q=";
    private final static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    /**
     * @author andrsdt
     * @param user that we want to generate the token for
     * @return String token containing user's information
     * @author andrsdt
     * @see http://javadox.com/io.jsonwebtoken/jjwt/0.4/io/jsonwebtoken/Jwts.html
     */
    public static String generateJWTToken(User userParam) {
        User user = new User();
        user.setUsername(userParam.getUsername());
        user.setEmail(userParam.getEmail());

        Set<Authorities> authorities = userParam.getAuthorities();
        user.setAuthorities(userParam.getAuthorities());

        // TODO untested
        String authoritiesString = authorities.stream().map(Authorities::getAuthority).collect(Collectors.joining(","));
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(authoritiesString);

        // WARNING should user be a JSON? does it work if not?
        return Jwts.builder().setSubject(user.getUsername()).claim("data", user)
                .claim("authorities",
                        grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis())).signWith(SIGNATURE_ALGORITHM, SECRET.getBytes())
                .compact();
    }

    /**
     * 
     * @author andrsdt
     * @param token                     that we want to check authorities for
     * @param commaSeparatedAuthorities example: "admin,user"
     * @return true if the token has any at least one of the authorities in the
     *         param
     */
    public static Boolean tokenHasAnyAuthorities(String token, String commaSeparatedAuthorities) {
        String tokenWithoutBearer = token.replace("Bearer ", "").trim();
        List<String> authorities = Stream.of(commaSeparatedAuthorities.split(",")).map(String::trim)
                .collect(Collectors.toList());
        JwtParser jwtParser = Jwts.parser().setSigningKey(SECRET.getBytes());
        // We don´t have to assure that the token was issued with our SECRET KEY because
        // we have a filter in the API that does it before us on every incoming request
        Claims claims = (Claims) jwtParser.parse(tokenWithoutBearer).getBody();

        @SuppressWarnings("unchecked")
        List<String> tokenAuthorities = (List<String>) claims.get("authorities");
        return tokenAuthorities.stream().anyMatch(authorities::contains); // At least contains 1 of them
    }

    /**
     * 
     * @author andrsdt
     * @param token that we want to check authorities for
     * @return username of the user in the token,
     */
    public static String usernameFromToken(String token) {
        String tokenWithoutBearer = token.replace("Bearer ", "").trim();
        JwtParser jwtParser = Jwts.parser().setSigningKey(SECRET.getBytes());
        Claims claims = jwtParser.parseClaimsJws(tokenWithoutBearer).getBody();

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) claims.get("data");
        return (String) data.get("username");
    }

    public static void main(String[] args) {
        final String TEST_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbmRyZXMiLCJkYXRhIjp7InVzZXJuYW1lIjoiYW5kcmVzIiwicGFzc3dvcmQiOm51bGwsImVtYWlsIjoiYW5kcmVzQG1haWwuY29tIiwiZW5hYmxlZCI6ZmFsc2V9LCJhdXRob3JpdGllcyI6WyJ1c2VyIl0sImlhdCI6MTYzNzYxMjc2NX0.jMJyx_yGsvOS6OD6q2cKhnhkalrryeBiApkCb1fpjIGUUxGbzrNy5I_S-Mcuu3iTne0W3FAvioDAIfT9jB3gVA";
        User testUser = new User();
        testUser.setUsername("andres");
        testUser.setEmail("andres@test.com");
        testUser.setPassword("andres");
        testUser.setEnabled(true);
        testUser.setAuthorities(Set.of());
        String generatedToken = generateJWTToken(testUser);
        System.out.println(generatedToken);
        tokenHasAnyAuthorities(TEST_TOKEN, "user");

        String username = usernameFromToken(generatedToken);
        System.out.println(username);
    }

}
