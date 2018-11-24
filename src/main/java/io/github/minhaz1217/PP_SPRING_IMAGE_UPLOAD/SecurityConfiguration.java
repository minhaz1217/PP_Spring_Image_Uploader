package io.github.minhaz1217.PP_SPRING_IMAGE_UPLOAD;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/images/**", "/main.css","/webjars/**").permitAll()
                .antMatchers(HttpMethod.POST, "/images").hasRole("USER")
                .antMatchers("/imageMessage/**").permitAll()
            .anyRequest().fullyAuthenticated()
            .and()
                .formLogin()
                .permitAll()
        .and()
        .logout().logoutSuccessUrl("/");
    }
    @Autowired
    public void configureJPABasedUsers(AuthenticationManagerBuilder auth,
                                       UserDetailsService  userDetailsService) throws Exception {


        auth.userDetailsService(userDetailsService);

        /*
        User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build();


        /*

        auth
                .inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("user").password("password").roles("USER");
*/

/*
        User.withUsername("greg").password("temp").roles("ADMIN", "USER");
        User.withUsername("myuser").password("myuser").roles("USER");
        User.withUsername("123").password("123").roles("USER").disabled(true);
        User.withUsername("asd").password("asd").roles("USER").accountLocked(true);
        */
        /*
        auth.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance()).withUser("temp").password("temp").roles("ADMIN", "USER")
        .and()
                .passwordEncoder(NoOpPasswordEncoder.getInstance()).withUser("myuser").password("myuser").roles("USER")
        .and()
                .passwordEncoder(NoOpPasswordEncoder.getInstance()).withUser("123").password("123").roles("USER").disabled(true)
        .and()
                .passwordEncoder(NoOpPasswordEncoder.getInstance()).withUser("asd").password("asd").roles("USER").accountLocked(true);
                */

    }
}
