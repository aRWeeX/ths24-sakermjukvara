package com.example.ths_java_spring_boot_project.service;

import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import com.example.ths_java_spring_boot_project.security.CustomUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Fetch user from database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new CustomUserDetails(
                user.getId(),               // ID from entity
                user.getEmail(),            // Use email as username
                user.getHashedPassword(),   // Encrypted password from DB
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())),  // Spring Security requires "ROLE_"
                                                                                     // prefix
                true,                       // accountNonExpired (placeholder)
                true,                       // accountNonLocked (placeholder)
                true,                       // credentialsNonExpired (placeholder)
                user.isEnabled()            // enabled, no inversion needed
        );

//        // Convert to Spring Security's UserDetails
//        return org.springframework.security.core.userdetails.User.builder()
//                .username(user.getEmail())              // Use email as username
//                .password(user.getHashedPassword())     // Encrypted password from DB
//                .authorities("ROLE_" + user.getRole())  // Spring Security requires "ROLE_" prefix
//                .disabled(!user.isEnabled())            // Invert enabled for disabled
//                .build();
    }
}
