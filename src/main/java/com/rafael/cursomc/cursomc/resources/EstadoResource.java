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

/**
 * Essa classe tem como objetivo solicitar os dados ao Service de Estado e entregar os dados solicitados
 * ao endpoint.
 * É nessa classe que será encontrados os métodos que respondem ao endpoint específico
 * atrávés da annotattion RequestMapping
 */
@RestController
@RequestMapping(value="/estados")
public class EstadoResource {
	
	@Autowired
	private EstadoService service;

	@Autowired
	private CidadeService cidadeService;

	/**
	 * É o endpoint padrão "/estados" no método HTTP GET
	 * que retorna uma lista simples de todos os estados,
	 * para isso solicita essa lista ao Service de Estado através do método "findAll"
	 * e retorna um objeto DTO customizado com os dados de Estado.
	 * @return retorna ao endpoint a lista de EstadosDTO
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> findAll(){
		List<Estado> list = service.findAll();
		List<EstadoDTO> listDto = list.stream().map(EstadoDTO::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	/**
	 * Esse método responde ao endpoint "/estados/{id}" através de um método
	 * HTTP GET e utiliza um Service de Estado através do método "find(id)" para entregar ao endpoint
	 * o objeto de Estado especificado na URL.
	 * @param id id do estado solicitado
	 * @return retorna ao endpoint o Estado especificado na URL
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Estado> find(@PathVariable Integer id) {
		
		Estado obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	}

	/**
	 * Esse método responde ao endpoint "/estados/{estado id}/cidades" através de um metodo HTTP GET
	 * e retorna ao endpoint uma lista customizada de Cidades relacionadas ao ID do estado passado
	 * através da URL.
	 * @param id ID do estado a ser buscado as Cidades relacionadas
	 * @return retorna ao endpoint uma lista de cidades relacionados ao ID do estado passado no parâmetro
	 */
	@RequestMapping(value = "/{id}/cidades", method = RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> findAllbyEstado(@PathVariable Integer id){

		List<Cidade> list = cidadeService.findCidadesByEstado(id);

		List<CidadeDTO> listDto = list.stream().map(CidadeDTO::new).collect(Collectors.toList());

		return ResponseEntity.ok().body(listDto);
	}

}
