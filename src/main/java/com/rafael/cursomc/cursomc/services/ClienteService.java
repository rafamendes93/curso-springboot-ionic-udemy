package com.rafael.cursomc.cursomc.services;

import com.rafael.cursomc.cursomc.domain.Cidade;
import com.rafael.cursomc.cursomc.domain.Cliente;
import com.rafael.cursomc.cursomc.domain.Endereco;
import com.rafael.cursomc.cursomc.domain.enums.Perfil;
import com.rafael.cursomc.cursomc.domain.enums.TipoCliente;
import com.rafael.cursomc.cursomc.dto.ClienteDTO;
import com.rafael.cursomc.cursomc.dto.ClienteNewDTO;
import com.rafael.cursomc.cursomc.repositories.ClienteRepository;
import com.rafael.cursomc.cursomc.repositories.EnderecoRepository;
import com.rafael.cursomc.cursomc.security.UserSS;
import com.rafael.cursomc.cursomc.services.exception.AuthorizationException;
import com.rafael.cursomc.cursomc.services.exception.DataIntegrityException;
import com.rafael.cursomc.cursomc.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.net.URI;
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

	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;

	@Value("${img.prefix.cliente.profile}")
	private String prefix;

	@Value("${img.profile.size}")
	private Integer size;

	
	public Cliente find(Integer id) {

		UserSS user = UserService.authenticated();

		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())){
			throw new AuthorizationException("Acesso negado");
		}
		
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

	/**
	 * Primeiramente esse método pega o usuário autenticado no momento.
	 * Após a busca ele verifica se o Cliente logado é nulo (ou seja, não tem ninguém logado)
	 * ou o perfil é ADMIN e o e-mail buscado é o mesmo e-mail procurado.
	 * Então caso o IF seja true ele instância um novo Cliente buscando no DB pelo e-mail.
	 * Caso essa busca retorne false, é lançado uma ObjectNotFoundException, caso ele passe  por todas as verificações
	 * então é retornado o objeto cliente.
	 * @param email e-mail do cliente a ser buscado
	 * @return retorna o cliente buscado após passar pelas duas verificações
	 */
	public Cliente findByEmail(String email){
		UserSS user = UserService.authenticated();

		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())){
			throw new AuthorizationException("Acesso negado");
		}
		Cliente obj = repo.findByEmail(email);

		if (obj == null){
			throw new ObjectNotFoundException("Objeto não encontrado! ID: " + user.getId());
		}

		return obj;
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

	/**
	 * Faz o upload da imagem para o profile do cliente
	 * @param multipartFile arquivo do tipo MultiPartFile enviado pelo usuário e convertido pelo Spring
	 * @return Retorna o URL da imagem no S3 da Amazon
	 */
	public URI uploadProfilePicture(MultipartFile multipartFile){

		UserSS user = UserService.authenticated();
		if (user == null){
			throw new AuthorizationException("Acesso negado");
		}

		BufferedImage jpgImg = imageService.getJpgImageFromFile(multipartFile);
		jpgImg = imageService.cropSquare(jpgImg);
		jpgImg = imageService.resize(jpgImg,size);

		String fileName = prefix + user.getId() + ".jpg";

		return s3Service.uploadFiles(imageService.getInputStream(jpgImg,"jpg"),fileName, "image");
	}






}
