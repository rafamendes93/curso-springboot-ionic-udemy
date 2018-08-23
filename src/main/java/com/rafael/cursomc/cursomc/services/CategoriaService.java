package com.rafael.cursomc.cursomc.services;

import com.rafael.cursomc.cursomc.domain.Categoria;
import com.rafael.cursomc.cursomc.repositories.CategoriaRepository;
import com.rafael.cursomc.cursomc.services.exception.DataIntegrityException;
import com.rafael.cursomc.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		
		//Implementação do JAVA 8, objeto OPTIONAL que encapsula uma categoria
		//Optional pois ele é opcional (objeto ou nulo) para evitar nullpointerException
		Optional<Categoria> cat = repo.findById(id);
		
		//Retona a categoria encontrada no ID ou caso não ache, retorna nulo
		return cat.orElseThrow(()
				-> new ObjectNotFoundException("Objeto não encontrado na base de dados:" + id
						+ " Tipo: " + Categoria.class.getName()));
		
	}

	public Categoria insert (Categoria obj){
		obj.setId(null);
		return repo.save(obj);
	}

	public Categoria update(Categoria obj){
		find(obj.getId());
		return repo.save(obj);
	}

	public void delete(Integer id){
		find(id);

		try {
			repo.deleteById(id);
		}catch (DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possui produtos");
		}
	}
}
