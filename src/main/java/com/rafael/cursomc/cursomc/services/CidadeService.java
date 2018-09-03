package com.rafael.cursomc.cursomc.services;

import com.rafael.cursomc.cursomc.domain.Cidade;
import com.rafael.cursomc.cursomc.repositories.CidadeRepository;
import com.rafael.cursomc.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadeService {

    @Autowired
    CidadeRepository cidadeRepository;

    public Cidade find(Integer id){
        Optional<Cidade> opt = cidadeRepository.findById(id);
        return opt.orElseThrow(()
                -> new ObjectNotFoundException("Objeto não encontrado, Estado ID: " + id));
    }

    public List<Cidade> findAll(){
        return cidadeRepository.findAll();
    }

    public List<Cidade> findCidadesByEstado(Integer id) {
        //return cidadeRepository.findCidades(id);
        return cidadeRepository.findCidadesByEstado_Id(id);
    }
}
