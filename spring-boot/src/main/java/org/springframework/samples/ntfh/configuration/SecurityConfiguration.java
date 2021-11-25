package org.springframework.samples.ntfh.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
				.antMatchers(HttpMethod.POST, "/users/register").permitAll() // Allow to register
				.antMatchers(HttpMethod.POST, "/users/login").permitAll() // Allow to login
				.antMatchers(HttpMethod.GET, "/users").hasAnyAuthority("admin") // Allow admins to list all the users
				.antMatchers(HttpMethod.PUT, "/users").hasAnyAuthority("user", "admin") // Update user's profile
				.antMatchers(HttpMethod.PUT, "/users/character").hasAnyAuthority("user") // Update user's current
																							// character
				.antMatchers(HttpMethod.GET, "/users/{userId}").permitAll() // Everyone can see a user's profile
				.antMatchers(HttpMethod.GET, "/users/{userId}/history").permitAll() // Everyone can see a user's match history
				.antMatchers(HttpMethod.GET, "/lobbies").permitAll() // Allow everyone to list all games
				.antMatchers(HttpMethod.POST, "/lobbies").hasAnyAuthority("user") // Allow users to create new lobbies
				.antMatchers(HttpMethod.PUT, "/lobbies/{lobbyId}").hasAnyAuthority("user") // Update lobby
				.antMatchers(HttpMethod.DELETE, "/lobbies/{lobbyId}/remove/{username}").hasAnyAuthority("user")
				.antMatchers(HttpMethod.POST, "/lobbies/{lobbyId}/join").hasAnyAuthority("user") // Join a lobby
				.antMatchers(HttpMethod.GET, "/gameHistory").permitAll() // Allow admins to list all the old games
				.antMatchers(HttpMethod.GET, "/achievements").permitAll() // Allow everyone to list all achievements
				.antMatchers(HttpMethod.GET, "/games/count").permitAll() // Allow everyone to see how many games are ongoing
				.antMatchers(HttpMethod.GET, "/lobbies/count").permitAll() // Enables to show how many of the elements in the game browser are open lobbies
				.antMatchers(HttpMethod.GET, "/gameHistory/count").permitAll() // Allow everyone to see how many games have been played
				
				.antMatchers(HttpMethod.PUT, "/achievements").hasAnyAuthority("admin") // Update achievement
				.antMatchers(HttpMethod.GET, "/achievements/{achievementId}").permitAll() // Everyone can see an achievement
				.antMatchers(HttpMethod.GET, "/achievements/{achievementId}").permitAll() // Everyone can see an achievement
				.antMatchers(HttpMethod.GET, "/lobbies/{lobbyId}").permitAll() // Allow everyone to see a lobby status
				.antMatchers(HttpMethod.GET, "/games").permitAll() // Allow everyone to list all games in the app
				.antMatchers(HttpMethod.POST, "/games").hasAnyAuthority("user") // Allow users to create new games
				.antMatchers(HttpMethod.GET, "/unregistered-users").permitAll() // Allow to request unregistered user
																				// credentials
				.antMatchers("/admin/**").hasAnyAuthority("admin") // access to admin info
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
		PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
		// store passwords in plain text
		return encoder;
	}

}
