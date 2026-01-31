package org.example.securityservice.config;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/incidents/sensor-events").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/ws-incidents/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable(); // For H2 console
    }
}