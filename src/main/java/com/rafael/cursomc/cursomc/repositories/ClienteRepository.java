package com.rafael.cursomc.cursomc.repositories;

import com.rafael.cursomc.cursomc.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Cliente findByEmail(String email);

}
