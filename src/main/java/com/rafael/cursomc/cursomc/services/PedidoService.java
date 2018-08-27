package com.rafael.cursomc.cursomc.services;

import com.rafael.cursomc.cursomc.domain.ItemPedido;
import com.rafael.cursomc.cursomc.domain.PagamentoComBoleto;
import com.rafael.cursomc.cursomc.domain.Pedido;
import com.rafael.cursomc.cursomc.domain.enums.EstadoPagamento;
import com.rafael.cursomc.cursomc.repositories.ItemPedidoRepository;
import com.rafael.cursomc.cursomc.repositories.PagamentoRepository;
import com.rafael.cursomc.cursomc.repositories.PedidoRepository;
import com.rafael.cursomc.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ClienteService clienteService;
	
	public Pedido find(Integer id) {
		
		//Implementação do JAVA 8, objeto OPTIONAL que encapsula uma pedido
		//Optional pois ele é opcional (objeto ou nulo) para evitar nullpointerException
		Optional<Pedido> cat = repo.findById(id);
		
		//Retona a pedido encontrada no ID ou caso não ache, retorna nulo
		return cat.orElseThrow(()
				-> new ObjectNotFoundException("Objeto não encontrado na base de dados:" + id
						+ " Tipo: " + Pedido.class.getName()));
		
	}

	@Transactional
	public Pedido insert(Pedido obj){
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);

		if (obj.getPagamento() instanceof PagamentoComBoleto){
			PagamentoComBoleto pgto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pgto,obj.getInstante());
		}

		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());

		for (ItemPedido ip: obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreço());
			ip.setPedido(obj);
		}

		itemPedidoRepository.saveAll(obj.getItens());
		System.out.println(obj);
		return obj;
	}
}
