package com.example.he_thong_thong_minh.Config;//package com.example.system__cccd.Config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
//
//        http = http.authorizeRequests()
//                .antMatchers("/**").permitAll()
//                .antMatchers("/admin").authenticated().and();
//        http.httpBasic().disable();
//        return http.build();
//    }
//
//}
