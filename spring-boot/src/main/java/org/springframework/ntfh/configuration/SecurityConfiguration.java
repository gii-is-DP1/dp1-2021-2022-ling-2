package org.springframework.ntfh.configuration;

import java.security.SecureRandom;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */

/**
 * @author japarejo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    String adminString = "admin";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors() // enable CORS requests
                .and() // enable CSRF
                .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests() // antMatchers:
                .antMatchers("/resources/**", "/webjars/**", "/h2-console/**").permitAll() // static resources

                // USER ENDPOINTS
                // Allow to register
                .antMatchers(HttpMethod.POST, "/users/register").permitAll()
                // Allow to login
                .antMatchers(HttpMethod.POST, "/users/login").permitAll()
                // Allow admins to list all the users
                .antMatchers(HttpMethod.GET, "/users").hasAuthority(adminString)
                // Allow everyone to get user number
                .antMatchers(HttpMethod.GET, "/users/count").permitAll()
                // Update user's profile
                .antMatchers(HttpMethod.PUT, "/users").hasAnyAuthority("user", adminString)
                // Update user's current character
                .antMatchers(HttpMethod.PUT, "/users/{userId}/character/{characterId}").hasAuthority("user")
                // Everyone can see a user's profile
                .antMatchers(HttpMethod.GET, "/users/{userId}").permitAll()
                // An admin can delete a user
                .antMatchers(HttpMethod.DELETE, "/users/{userId}").hasAuthority(adminString)
                // An admin can ban a user
                .antMatchers(HttpMethod.PUT, "/users/{userId}/ban").hasAuthority(adminString)
                // Everyone can see a user's match history
                .antMatchers(HttpMethod.GET, "/users/{userId}/history").permitAll()
                // Set character
                .antMatchers(HttpMethod.PUT, "/users/{userId}/character").hasAuthority("user")
                // See earned achievements
                .antMatchers(HttpMethod.GET, "/users/{userId}/achievements").permitAll()
                .antMatchers(HttpMethod.GET, "/users/{userId}/achievements/count").permitAll()
                // UNREGISTERED USER ENDPOINTS
                // Allow to request unregistered user credentials
                .antMatchers(HttpMethod.POST, "/unregistered-users").permitAll()
                // PLAYER ENDPOINTS
                .antMatchers(HttpMethod.PUT, "/players/{playerId}/character/{characterId}").hasAuthority("user")
                // GAME ENDPOINTS
                .antMatchers(HttpMethod.GET, "/games").hasAuthority("admin") // Allow admins to see all games
                .antMatchers(HttpMethod.POST, "/games").hasAuthority("user") // Allow users to create new games
                .antMatchers(HttpMethod.GET, "/games/count").permitAll() // Allow everyone to see how many games are
                .antMatchers(HttpMethod.GET, "/games/lobby").permitAll() // Allow everyone to see lobbies
                .antMatchers(HttpMethod.GET, "/games/ongoing").permitAll() // Allow everyone to see ongoing games
                .antMatchers(HttpMethod.GET, "/games/finished").permitAll()// Allow everyone to see finished games
                .antMatchers(HttpMethod.GET, "/games/ongoing/count").permitAll() // Allow everyone to see past games
                .antMatchers(HttpMethod.GET, "/games/finished/count").permitAll() // Allow everyone to see past games
                .antMatchers(HttpMethod.GET, "/games/{gameId}").permitAll() // Allow everyone to see a game
                .antMatchers(HttpMethod.PUT, "/games/{gameId}").hasAuthority("user") // Allow users to update a game
                .antMatchers(HttpMethod.GET, "/games/{gameId}/turn").permitAll() // Allow everyone to get a game's turn
                // Allow users to go to the next turn stage
                .antMatchers(HttpMethod.POST, "/games/new").hasAuthority("user") // Allow users to create games
                .antMatchers(HttpMethod.POST, "/games/{gameId}/add/{username}").hasAuthority("user")
                .antMatchers(HttpMethod.POST, "/games/{gameId}/remove/{username}").hasAuthority("user")
                .antMatchers(HttpMethod.DELETE, "/games/{gameId}").hasAnyAuthority("admin", "user")
                .antMatchers(HttpMethod.POST, "/games/{gameId}/start").hasAuthority("user")
                .antMatchers(HttpMethod.POST, "/games/{gameId}/turn/next").hasAuthority("user")
                .antMatchers(HttpMethod.GET, "/games/ongoing/count").hasAuthority("user")
                .antMatchers(HttpMethod.GET, "/games/finished/count").hasAuthority("user")
                // ACHIEVEMENT ENDPOINTS
                .antMatchers(HttpMethod.GET, "/achievements").permitAll() // Allow everyone to list all achievements
                .antMatchers(HttpMethod.GET, "/achievements/count").permitAll()
                .antMatchers(HttpMethod.GET, "/achievements/types").hasAuthority("admin")
                .antMatchers(HttpMethod.POST, "/achievements/new").hasAuthority("admin")
                .antMatchers(HttpMethod.PUT, "/achievements").hasAuthority("admin") // Update achievement
                .antMatchers(HttpMethod.GET, "/achievements/{achievementId}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/achievements/{achievementId}").hasAuthority("admin")
                // SCENE ENDPOINTS
                .antMatchers(HttpMethod.GET, "/scenes/count").permitAll() // Allow everyone to get the number of scenes
                // ABILITY CARD ENDPOINTS
                .antMatchers(HttpMethod.POST, "/ability-cards/{abilityCardIngameId}").hasAuthority("user")
                // MARKET CARD ENDPOINTS
                .antMatchers(HttpMethod.GET, "/market-cards/{gameId}").permitAll() // Allow everyone to list a game's
                                                                                   // market cards
                .antMatchers(HttpMethod.POST, "/market-cards/buy/{marketCardIngameId}").hasAuthority("user")

                .antMatchers("/admin/**").hasAuthority(adminString) // access to admin info
                // RANKING ENDPOINTS
                .antMatchers(HttpMethod.GET, "/statistics/users/{userId}/games/count").permitAll()
                .antMatchers(HttpMethod.GET, "/statistics/users/{userId}/games/statistics").permitAll()
                .antMatchers(HttpMethod.GET, "/statistics/games/count").permitAll()
                .antMatchers(HttpMethod.GET, "/statistics/ranking/wins").permitAll()
                .antMatchers(HttpMethod.GET, "/statistics/ranking/wins").permitAll()
                .antMatchers(HttpMethod.GET, "/statistics/ranking/glory").permitAll()
                .antMatchers(HttpMethod.GET, "/statistics/ranking/kills").permitAll()
                // OTHER ENDPOINTS
                .anyRequest().denyAll() // else, deny
                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        // Configuración para que funcione la consola de administración
        // de la BD H2 (deshabilitar las cabeceras de protección contra
        // ataques de tipo csrf y habilitar los framesets si su contenido
        // se sirve desde esta misma página.

        http.headers().frameOptions().sameOrigin();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled " + "from users " + "where username = ?")
                .authoritiesByUsernameQuery("select username, authority " + "from authorities " + "where username = ?")
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom("NoTiMeFoRhErOeS$sEeD".getBytes()));
    }
}
