package com.hourfun.cashexchange.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import com.hourfun.cashexchange.filter.CustomRememberMeFilter;
import com.hourfun.cashexchange.handler.CustomLogoutSuccessHandler;
import com.hourfun.cashexchange.service.CustomSecurityRememberMeService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService customSecurityService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.addFilterAt(customRememberMeFilter(), RememberMeAuthenticationFilter.class).cors().and().csrf().disable() // csrf
				.httpBasic().and().rememberMe()
//				.alwaysRemember(true)
//				.key("qwer")
//				.rememberMeCookieName("qwer")
				.rememberMeServices(customSecurityRememberMeService()).and().logout().logoutUrl("/logout")
				.logoutSuccessHandler(logoutSuccessHandler()).and().authorizeRequests().antMatchers("/board/**")
				.hasAnyRole("USER", "ADMIN", "MANAGER").antMatchers("/users/**").hasAnyRole("USER")
				.antMatchers("/admin/**").hasAnyRole("ADMIN").antMatchers("/manager/**").hasAnyRole("MANAGER")
				.antMatchers("/**").permitAll();

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring().antMatchers("/users/login/**").antMatchers("/users/findId/**")
				.antMatchers("/users/findPassword/**").antMatchers("/users/signin/").antMatchers("/admin/login/**")
				.antMatchers("/admin/findId/**").antMatchers("/admin/findPassword/**").antMatchers("/admin/signin/")
				.antMatchers("/manager/login/**").antMatchers("/manager/findId/**")
				.antMatchers("/manager/findPassword/**").antMatchers("/manager/signin/");

	}

	@Bean
	public PasswordEncoder customPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("qwer").password("{noop}qwer").roles("USER");

		auth.userDetailsService(customSecurityService).passwordEncoder(customPasswordEncoder());
	}

	@Bean
	public RememberMeServices customSecurityRememberMeService() {

		return new CustomSecurityRememberMeService("qwer", customSecurityService);
	}

	@Bean
	public RememberMeAuthenticationFilter customRememberMeFilter() throws Exception {
		return new CustomRememberMeFilter(authenticationManagerBean(), customSecurityRememberMeService());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}

}
