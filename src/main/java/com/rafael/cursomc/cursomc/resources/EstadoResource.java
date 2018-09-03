package com.rafael.cursomc.cursomc.resources;

import com.rafael.cursomc.cursomc.domain.Cidade;
import com.rafael.cursomc.cursomc.domain.Estado;
import com.rafael.cursomc.cursomc.dto.CidadeDTO;
import com.rafael.cursomc.cursomc.dto.EstadoDTO;
import com.rafael.cursomc.cursomc.services.CidadeService;
import com.rafael.cursomc.cursomc.services.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/estados")
public class EstadoResource {
	
	@Autowired
	private EstadoService service;

	@Autowired
	private CidadeService cidadeService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> findAll(){
		List<Estado> list = service.findAll();
		List<EstadoDTO> listDto = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Estado> find(@PathVariable Integer id) {
		
		Estado obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	}

	@RequestMapping(value = "/{id}/cidades")
	public ResponseEntity<List<CidadeDTO>> findAllbyEstado(@PathVariable Integer id){

		List<Cidade> list = cidadeService.findCidadesByEstado(id);

		List<CidadeDTO> listDto = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());

		return ResponseEntity.ok().body(listDto);
	}

}
