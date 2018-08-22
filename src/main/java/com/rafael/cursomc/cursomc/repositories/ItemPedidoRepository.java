package com.rafael.cursomc.cursomc.repositories;

import com.rafael.cursomc.cursomc.domain.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
	
	
}
