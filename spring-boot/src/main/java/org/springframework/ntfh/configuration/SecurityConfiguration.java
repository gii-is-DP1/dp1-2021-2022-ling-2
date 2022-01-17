package org.springframework.ntfh.configuration;

import java.security.SecureRandom;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and() // enable CORS requests
				.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests() // antMatchers:
				.antMatchers("/resources/**", "/webjars/**", "/h2-console/**").permitAll() // static resources

				// USER ENDPOINTS
				// Allow to register
				.antMatchers(HttpMethod.POST, "/users/register").permitAll()
				// Allow to login
				.antMatchers(HttpMethod.POST, "/users/login").permitAll()
				// Allow admins to list all the users
				.antMatchers(HttpMethod.GET, "/users").hasAuthority("admin")
				// Allow everyone to get user number
				.antMatchers(HttpMethod.GET, "/users/count").permitAll()
				// Update user's profile
				.antMatchers(HttpMethod.PUT, "/users").hasAnyAuthority("user", "admin")
				// Update user's current character
				.antMatchers(HttpMethod.PUT, "/users/character").hasAuthority("user")
				// Everyone can see a user's profile
				.antMatchers(HttpMethod.GET, "/users/{userId}").permitAll()
				// An admin can delete a user
				.antMatchers(HttpMethod.DELETE, "/users/{userId}").hasAuthority("admin")
				// An admin can ban a user
				.antMatchers(HttpMethod.PUT, "/users/{userId}/ban").hasAuthority("admin")
				// Everyone can see a user's match history
				.antMatchers(HttpMethod.GET, "/users/{userId}/history").permitAll()
				// Set character
				.antMatchers(HttpMethod.PUT, "/users/{userId}/character").hasAuthority("user")
				// UNREGISTERED USER ENDPOINTS
				// Allow to request unregistered user credentials
				.antMatchers(HttpMethod.POST, "/unregistered-users").permitAll()
				// LOBBY ENDPOINTS
				.antMatchers(HttpMethod.PUT, "/lobbies/{lobbyId}").hasAuthority("user") // Update lobby
				.antMatchers(HttpMethod.POST, "/lobbies/{lobbyId}/join").hasAuthority("user") // Join a lobby
				.antMatchers(HttpMethod.DELETE, "/lobbies/{lobbyId}/remove/{username}").hasAuthority("user")
				// GAME ENDPOINTS
				.antMatchers(HttpMethod.GET, "/games").permitAll() // Allow everyone to list all games in the app
				.antMatchers(HttpMethod.POST, "/games").hasAuthority("user") // Allow users to create new games
				.antMatchers(HttpMethod.GET, "/games/count").permitAll() // Allow everyone to see how many games are
				.antMatchers(HttpMethod.GET, "/games/{gameId}").permitAll() // Allow everyone to see a game
				.antMatchers(HttpMethod.PUT, "/games/{gameId}").hasAuthority("user") // Allow users to update a game
				.antMatchers(HttpMethod.GET, "/games/{gameId}/turn").permitAll() // Allow everyone to get a game's turn
				// Allow users to go to the next turn stage
				.antMatchers(HttpMethod.POST, "/games/{gameId}/turn/next").hasAuthority("user")
				.antMatchers(HttpMethod.POST, "/games/{gameId}/ability-cards/{abilityCardIngameId}")
				.hasAuthority("user") // Allow users to play cards
				.antMatchers(HttpMethod.POST, "/games/{gameId}/market-cards/{marketCardIngameId}").hasAuthority("user")
				// Allow users to buy cards in the market
				// ACHIEVEMENT ENDPOINTS
				.antMatchers(HttpMethod.GET, "/achievements").permitAll() // Allow everyone to list all achievements
				.antMatchers(HttpMethod.PUT, "/achievements").hasAuthority("admin") // Update achievement
				.antMatchers(HttpMethod.GET, "/achievements/{achievementId}").permitAll() // Everyone can see an
																							// achievement
				.antMatchers(HttpMethod.GET, "/achievements/{achievementId}").permitAll() // Everyone can see an
																							// achievement
				// SCENE ENDPOINTS
				.antMatchers(HttpMethod.GET, "/scenes/count").permitAll() // Allow everyone to get the number of scenes
				// MARKET CARD ENDPOINTS
				.antMatchers(HttpMethod.GET, "/market-cards/{gameId}").permitAll() // Allow everyone to list a game's
																					// market cards
				// HORDE ENEMIES ENDPOINTS
				.antMatchers(HttpMethod.GET, "/horde-enemies/{gameId}").permitAll() // Allow everyone to list a game's
																					// horde enemies
				// WARLORDS ENDPOINTS
				.antMatchers(HttpMethod.GET, "/warlords/{gameId}").permitAll() // Allow everyone to list a game's
																				// warlord
																				// ADMIN ENDPOINTS
				.antMatchers("/admin/**").hasAuthority("admin") // access to admin info
				// OTHER ENDPOINTS
				.anyRequest().denyAll(); // else, deny

		// Configuración para que funcione la consola de administración
		// de la BD H2 (deshabilitar las cabeceras de protección contra
		// ataques de tipo csrf y habilitar los framesets si su contenido
		// se sirve desde esta misma página.

		http.csrf().disable(); // TODO csrf token in JSON for better security

		http.headers().frameOptions().sameOrigin();
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
		return new BCryptPasswordEncoder(10, new SecureRandom("NTFHseed".getBytes()));
	}

}
