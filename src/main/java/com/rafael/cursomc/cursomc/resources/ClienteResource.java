package com.rafael.cursomc.cursomc.resources;

import com.rafael.cursomc.cursomc.domain.Cliente;
import com.rafael.cursomc.cursomc.dto.ClienteDTO;
import com.rafael.cursomc.cursomc.dto.ClienteNewDTO;
import com.rafael.cursomc.cursomc.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		
		Cliente obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	}

    /**
     * Esse método é executado quando o endpoint /clientes/id, então é buscado o ID do cliente
     * mas apenas o cliente pode buscar pelo próprio id ou um cliente com role de ADMIN pode fazer isso.
     * @param objDTO objeto do ClienteDTO que será convertido em Cliente no método
     * @param id id do cliente a ser buscado
     * @return Retorna uma resposta HTTP 204 No content com os dados do objeto Cliente
     */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDTO, @PathVariable Integer id){
		Cliente obj = service.fromDTO(objDTO);
		obj.setId(id);
		service.update(obj);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<Cliente> delete(@PathVariable Integer id) {

		service.delete(id);

		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll() {

		List<Cliente> list = service.findAll();

		List<ClienteDTO> listaDTO =  list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());

		return ResponseEntity.ok().body(listaDTO);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/page",method=RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
													   @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
													   @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
													   @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		Page<Cliente> list = service.findPage(page, linesPerPage, orderBy, direction);

		Page<ClienteDTO> listaDTO = list.map(obj -> new ClienteDTO(obj));

		return ResponseEntity.ok().body(listaDTO);
	}

	/**
	 * Esse método é executado atráves do endpoint /clientes/email?value=EMAIL_A_SER_BUSCADO
	 * Esse método busca todos os dados do cliente através do e-mail, o método findByEmail
	 * permite apenas que o próprio usuário olhe suas informações ou um usuário com a role ADMIN faça isso.
	 * @param email e-mail do usuário a ser buscado
	 * @return retorna uma resposta HTTP 200 OK com os dados do objeto Cliente
	 */
	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@RequestParam(value = "value") String email){
		Cliente obj = service.findByEmail(email);
		return ResponseEntity.ok().body(obj);
	}



	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDTO){
		Cliente obj = service.fromDTO(objDTO);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();

		return ResponseEntity.created(uri).build();

	}

	@RequestMapping(value = "/picture", method = RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file")  MultipartFile file){

		URI uri = service.uploadProfilePicture(file);

		return ResponseEntity.created(uri).build();

	}




	
}
