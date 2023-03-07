package com.SecurityService.Security.service;

import com.SecurityService.Security.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetailService implements UserDetailsService {


    @Autowired
    RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDTO user = restTemplate.getForObject("http://localhost:8001/user/listName/"+username, UserDTO.class);
        if(user == null) {

            throw new UsernameNotFoundException("No existe el usuario");
        }
        List<GrantedAuthority> authorities = user.getRoleList()
                .stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getRole()))
                .collect(Collectors.toList());
        System.out.println(user);
        System.out.println(authorities);
        return new User(user.getName(), user.getPass(), user.getActive(), Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, authorities);
    }
}
