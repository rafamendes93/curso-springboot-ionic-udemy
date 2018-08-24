package com.rafael.cursomc.cursomc.services;

import com.rafael.cursomc.cursomc.domain.Cliente;
import com.rafael.cursomc.cursomc.dto.ClienteDTO;
import com.rafael.cursomc.cursomc.repositories.ClienteRepository;
import com.rafael.cursomc.cursomc.services.exception.DataIntegrityException;
import com.rafael.cursomc.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		
		//Implementação do JAVA 8, objeto OPTIONAL que encapsula uma categoria
		//Optional pois ele é opcional (objeto ou nulo) para evitar nullpointerException
		Optional<Cliente> cat = repo.findById(id);
		
		//Retona a categoria encontrada no ID ou caso não ache, retorna nulo
		return cat.orElseThrow(()
				-> new ObjectNotFoundException("Objeto não encontrado na base de dados:" + id
						+ " Tipo: " + Cliente.class.getName()));
		
	}

	public Cliente update(Cliente obj){
		Cliente newObj = find(obj.getId());
		updateData(newObj,obj);
		return repo.save(newObj);
	}

	public void delete(Integer id){
		find(id);

		try {
			repo.deleteById(id);
		}catch (DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possivel excluir clientes porque há entidades relacionadas");
		}
	}

	public List<Cliente> findAll(){
		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objDto){
		return new Cliente(objDto.getId(),objDto.getNome(),objDto.getEmail(),null,null);
	}

	private void updateData(Cliente newObj, Cliente obj){
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

}
