package com.rafael.cursomc.cursomc.resources;

import com.rafael.cursomc.cursomc.domain.Produto;
import com.rafael.cursomc.cursomc.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService service;



	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id) {
		
		Produto obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	}

	
}
