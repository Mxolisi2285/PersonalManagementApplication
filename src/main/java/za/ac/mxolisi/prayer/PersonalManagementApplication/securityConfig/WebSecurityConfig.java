package za.ac.mxolisi.prayer.PersonalManagementApplication.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    // ---------------- PASSWORD ENCODER ----------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ---------------- SECURITY FILTER CHAIN ----------------
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ---------------- CSRF ----------------
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // ignore H2 console if needed
                )

                // ---------------- AUTHORIZE REQUESTS ----------------
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                            "/login",
                            "/signup",
                            "/forgotPassword",
                            "/resetPassword",
                            "/css/**",
                            "/js/**",
                            "/oauth2/**" // allow Google OAuth2
                    ).permitAll();
                    auth.anyRequest().authenticated();
                })

                // ---------------- FORM LOGIN ----------------
                .formLogin(form -> {
                    form.loginPage("/login");
                    form.defaultSuccessUrl("/home", true);
                    form.permitAll();
                })

                // ---------------- GOOGLE OAUTH2 LOGIN ----------------
                .oauth2Login(oauth2 -> {
                    oauth2.loginPage("/login"); // reuse same login page
                    oauth2.defaultSuccessUrl("/home", true);
                    // optionally add customOAuth2UserService if storing Google users
                })

                // ---------------- LOGOUT ----------------
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login?logout=true");
                    logout.invalidateHttpSession(true);
                    logout.deleteCookies("JSESSIONID");
                    logout.permitAll();
                })

                // ---------------- SESSION MANAGEMENT ----------------
                .sessionManagement(session ->
                        session.maximumSessions(1)
                                .maxSessionsPreventsLogin(false)
                );

        return http.build();
    }
}
