package com.rafael.cursomc.cursomc.services;

import com.rafael.cursomc.cursomc.domain.Categoria;
import com.rafael.cursomc.cursomc.domain.Pedido;
import com.rafael.cursomc.cursomc.domain.Produto;
import com.rafael.cursomc.cursomc.repositories.CategoriaRepository;
import com.rafael.cursomc.cursomc.repositories.ProdutoRepository;
import com.rafael.cursomc.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Produto find(Integer id) {
		
		//Implementação do JAVA 8, objeto OPTIONAL que encapsula uma pedido
		//Optional pois ele é opcional (objeto ou nulo) para evitar nullpointerException
		Optional<Produto> cat = repo.findById(id);
		
		//Retona a pedido encontrada no ID ou caso não ache, retorna nulo
		return cat.orElseThrow(()
				-> new ObjectNotFoundException("Objeto não encontrado na base de dados:" + id
						+ " Tipo: " + Pedido.class.getName()));
		
	}

	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome,categorias,pageRequest);
	}
}
