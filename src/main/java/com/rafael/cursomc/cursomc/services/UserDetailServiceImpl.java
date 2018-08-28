package com.rafael.cursomc.cursomc.services;

import com.rafael.cursomc.cursomc.domain.Cliente;
import com.rafael.cursomc.cursomc.repositories.ClienteRepository;
import com.rafael.cursomc.cursomc.security.UserSpringSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(email);

        if (cliente == null) throw new UsernameNotFoundException(email + " não existe");

        return new UserSpringSecurity(cliente.getId(),cliente.getEmail(),cliente.getSenha(), cliente.getPerfis());
    }
}
