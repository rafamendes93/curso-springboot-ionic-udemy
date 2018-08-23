package com.rafael.cursomc.cursomc.services;

import com.rafael.cursomc.cursomc.domain.Pedido;
import com.rafael.cursomc.cursomc.repositories.PedidoRepository;
import com.rafael.cursomc.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	public Pedido find(Integer id) {
		
		//Implementação do JAVA 8, objeto OPTIONAL que encapsula uma pedido
		//Optional pois ele é opcional (objeto ou nulo) para evitar nullpointerException
		Optional<Pedido> cat = repo.findById(id);
		
		//Retona a pedido encontrada no ID ou caso não ache, retorna nulo
		return cat.orElseThrow(()
				-> new ObjectNotFoundException("Objeto não encontrado na base de dados:" + id
						+ " Tipo: " + Pedido.class.getName()));
		
	}
}
