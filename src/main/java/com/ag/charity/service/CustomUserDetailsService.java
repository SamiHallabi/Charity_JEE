package com.ag.charity.service;

import com.ag.charity.entities.jpa.Organization;
import com.ag.charity.entities.jpa.User;
import com.ag.charity.repositories.jpa.OrganizationRepository;
import com.ag.charity.repositories.jpa.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public CustomUserDetailsService(UserRepository userRepository,
                                    OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );
        }

        Organization org = organizationRepository.findByOrgEmail(email);
        if (org != null) {
            return new org.springframework.security.core.userdetails.User(
                    org.getOrgEmail(),
                    org.getOrgPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN_ORG"))
            );
        }

        throw new UsernameNotFoundException("Utilisateur non trouvé : " + email);
    }
}
