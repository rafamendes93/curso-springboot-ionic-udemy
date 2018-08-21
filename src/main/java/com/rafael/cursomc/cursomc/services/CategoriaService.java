package com.rafael.cursomc.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rafael.cursomc.cursomc.domain.Categoria;
import com.rafael.cursomc.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		
		//Implementação do JAVA 8, objeto OPTIONAL que encapsula uma categoria
		//Optional pois ele é opcional (objeto ou nulo) para evitar nullpointerException
		Optional<Categoria> cat = repo.findById(id);
		
		//Retona a categoria encontrada no ID ou caso não ache, retorna nulo
		return cat.orElse(null);
		
	}
}
