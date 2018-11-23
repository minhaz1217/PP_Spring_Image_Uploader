package io.github.minhaz1217.PP_SPRING_IMAGE_UPLOAD;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService{

    public final UserRepository userRepository;


    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),
                Stream.of(user.getRoles())
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList()));

    }

}




