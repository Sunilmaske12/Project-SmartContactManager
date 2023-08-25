package SCM.SmartContactManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import SCM.SmartContactManager.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private AuthenticationSuccessHandler successHandler;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() { 
		
		return new   BCryptPasswordEncoder(); 
	}
	  @Bean
	  public UserDetailsService getUserDetailService() { 
		   return new UserDetailServiceImpl();
		  }
	 

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().authorizeHttpRequests().
		requestMatchers("/Normal/**").hasRole("USER").
		requestMatchers("/**").permitAll().
		anyRequest().authenticated().and().formLogin().loginPage("/signIn").
		successHandler(successHandler);
		httpSecurity.authenticationProvider(authenticationProvider());
		return httpSecurity.build();

	}

	
	  @Bean 
	  public DaoAuthenticationProvider authenticationProvider() {
	  DaoAuthenticationProvider dap=new DaoAuthenticationProvider();
	  dap.setUserDetailsService(this.getUserDetailService());
	  dap.setPasswordEncoder(passwordEncoder());
	  
	  return dap;
	  }
	 

	  
	
	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
