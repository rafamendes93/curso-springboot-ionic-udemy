package com.rafael.cursomc.cursomc.services;

import com.rafael.cursomc.cursomc.domain.Cidade;
import com.rafael.cursomc.cursomc.domain.Cliente;
import com.rafael.cursomc.cursomc.domain.Endereco;
import com.rafael.cursomc.cursomc.domain.enums.TipoCliente;
import com.rafael.cursomc.cursomc.dto.ClienteDTO;
import com.rafael.cursomc.cursomc.dto.ClienteNewDTO;
import com.rafael.cursomc.cursomc.repositories.ClienteRepository;
import com.rafael.cursomc.cursomc.repositories.EnderecoRepository;
import com.rafael.cursomc.cursomc.services.exception.DataIntegrityException;
import com.rafael.cursomc.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	
	public Cliente find(Integer id) {
		
		//Implementação do JAVA 8, objeto OPTIONAL que encapsula uma categoria
		//Optional pois ele é opcional (objeto ou nulo) para evitar nullpointerException
		Optional<Cliente> cat = repo.findById(id);
		
		//Retona a categoria encontrada no ID ou caso não ache, retorna nulo
		return cat.orElseThrow(()
				-> new ObjectNotFoundException("Objeto não encontrado na base de dados:" + id
						+ " Tipo: " + Cliente.class.getName()));
		
	}

	@Transactional
	public Cliente insert (Cliente obj){
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return repo.save(obj);
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
			throw new DataIntegrityException("Não é possivel excluir clientes porque há pedidos relacionados");
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
		return new Cliente(objDto.getId(),objDto.getNome(),objDto.getEmail(),null,null,null);
	}

	public Cliente fromDTO(ClienteNewDTO objDto){
		Cliente cli = new Cliente(null,objDto.getNome(),objDto.getEmail(),objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()),passwordEncoder.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(),null,null);
		Endereco end = new Endereco(null,objDto.getLogradouro(), objDto.getNumero(),objDto.getComplemento(),objDto.getBairro(), objDto.getCep(),cli,cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());

		if(objDto.getTelefone2() != null) cli.getTelefones().add(objDto.getTelefone2());

		if(objDto.getTelefone3() != null) cli.getTelefones().add(objDto.getTelefone3());

		return cli;
	}

	private void updateData(Cliente newObj, Cliente obj){
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

}
