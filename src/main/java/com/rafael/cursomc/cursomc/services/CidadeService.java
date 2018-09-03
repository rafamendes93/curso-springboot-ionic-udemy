package com.rafael.cursomc.cursomc.services;

import com.rafael.cursomc.cursomc.domain.Cidade;
import com.rafael.cursomc.cursomc.repositories.CidadeRepository;
import com.rafael.cursomc.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Essa Classe Service de Cidade, esse classe tem como objetivo prover os dados necessários
 * para o Resource de Cidade.
 * É nessa classe que será encontrados métodos para buscar uma Cidade especifica ou uma lista de Cidades
 * por determinado critério.
 * Essa classe se comunica com o Repository de Cidade para que consiga os dados propostos.
 */
@Service
public class CidadeService {

    @Autowired
    CidadeRepository cidadeRepository;

    /**
     * Método para buscar uma cidade pelo seu ID, caso contrário retorna um exceção ObjectNotFoundException
     * @param id id da cidade a ser buscada
     * @return retorna o objeto da Cidade buscada
     */
    public Cidade find(Integer id){
        Optional<Cidade> opt = cidadeRepository.findById(id);
        return opt.orElseThrow(()
                -> new ObjectNotFoundException("Objeto não encontrado, Estado ID: " + id));
    }

    /**
     * Método para buscar todas as cidades cadastradas do DB
     * @return retorna uma List de Cidades cadastradas no DB
     */
    public List<Cidade> findAll(){
        return cidadeRepository.findAll();
    }

    /**
     * Métodos para buscar Cidades cadastradas do DB pelo ID do estado associado.
     * @param id id do Estado que deseja retornar todas as Cidades associadas.
     * @return retorna uma Lista de Cidades associadas ao ID do Estado
     */
    public List<Cidade> findCidadesByEstado(Integer id) {
        //return cidadeRepository.findCidades(id);
        return cidadeRepository.findCidadesByEstado_Id(id);
    }
}
