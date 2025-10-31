package com.myrecovery.fit.service;

import com.myrecovery.fit.model.UserInfo;
import com.myrecovery.fit.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    // Inject PasswordEncoder. Since PasswordEncoder is a @Bean in SecurityConfig,
    // and SecurityConfig does *not* directly depend on UserInfoService, this is fine.
    private PasswordEncoder encoder;

    // Method to load user details by username (email)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the database by email (username)
        Optional<UserInfo> userInfo = repository.findByEmail(username);

        if(userInfo.isEmpty()){
            throw new UsernameNotFoundException("User non found with email: "+username);
        }

        // Convert Userinfo to UserDetails (UserInfoDetails)
        UserInfo user = userInfo.get();

        return new User(user.getEmail(), user.getPassword(), convertRolesStringToGrantedAuthorities(user.getRoles()));
    }

    // metodo per convertire i ruoli dalla stringa con i valori "ROLE_USER,ROLE_ADMIN" al set di tipo GrantedAuthority
    public static Set<GrantedAuthority> convertRolesStringToGrantedAuthorities(String rolesString) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (rolesString != null && !rolesString.trim().isEmpty()) {
            // 1. Splittare la stringa dei ruoli
            String[] roles = rolesString.split(",");

            // 2. Iterare sui ruoli e creare SimpleGrantedAuthority
            for (String role : roles) {
                String trimmedRole = role.trim(); // Rimuovi spazi bianchi
                if (!trimmedRole.isEmpty()) {
                    // È buona pratica assicurarsi che i ruoli inizino con "ROLE_"
                    // se si segue la convenzione di Spring Security
                    if (!trimmedRole.startsWith("ROLE_")) {
                        trimmedRole = "ROLE_" + trimmedRole;
                    }
                    authorities.add(new SimpleGrantedAuthority(trimmedRole));
                }
            }
        }
        return authorities;
    }

    // Add any additional methods for registering or managing users
    public String addUser(UserInfo userInfo) {
        // Encrypt password before saving
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User added successfully!";
    }
}
