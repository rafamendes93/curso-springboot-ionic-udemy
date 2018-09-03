package com.rafael.cursomc.cursomc.services;

import com.rafael.cursomc.cursomc.domain.Estado;
import com.rafael.cursomc.cursomc.repositories.EstadoRepository;
import com.rafael.cursomc.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoService {

    @Autowired
    EstadoRepository estadoRepository;

    public Estado find(Integer id){
        Optional<Estado> opt = estadoRepository.findById(id);
        return opt.orElseThrow(()
                -> new ObjectNotFoundException("Objeto não encontrado, Estado ID: " + id));
    }

    public List<Estado> findAll(){
        return estadoRepository.findAllByOrderByNome();
    }
}
