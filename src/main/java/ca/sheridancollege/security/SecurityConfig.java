package ca.sheridancollege.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private LoginAccessDeniedHandler accessdeniedhandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.csrf().disable();//remove in deployment
		//http.headers().frameOptions().disable();//remove in deployment
		http.authorizeRequests()
		.antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST, "/register").permitAll()
		.antMatchers("/",
					"/js/**",
					"/css/**",
					"/img/**",
					"/**").permitAll()
		.antMatchers("/h2-console/**").permitAll()
		.antMatchers("/h2-console/login.do?jsessionid=b2306491fbbca7fb12fbd8ac3e59c7e2").permitAll()
		.anyRequest().authenticated()
		.and().formLogin().loginPage("/login").permitAll()
		.and().logout().invalidateHttpSession(true).clearAuthentication(true)
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout").permitAll()
		.and().exceptionHandling().accessDeniedHandler(accessdeniedhandler);
}
    /**protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();//remove in deployment
		http.headers().frameOptions().disable();//remove in deployment
		http.authorizeRequests()
			//Define URL's and who has access
			.antMatchers("/user/**").hasRole("USER")
			.antMatchers(HttpMethod.POST, "/register").permitAll()
			.antMatchers("/",
						"/images/**",
						"/css/**",
						"/js/**",
						"/**").permitAll()
			.antMatchers("/h2-console/**").permitAll()//remove in deployment
			.anyRequest().authenticated();
	}*/
	
	@Autowired
	UserDetailedServiceImple userDetailsService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
}