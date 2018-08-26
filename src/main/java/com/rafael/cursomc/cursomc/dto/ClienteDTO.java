package com.rafael.cursomc.cursomc.dto;

import com.rafael.cursomc.cursomc.domain.Cliente;
import com.rafael.cursomc.cursomc.services.validation.ClienteUpdate;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@ClienteUpdate
public class ClienteDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    @NotEmpty(message = "O nome não pode estar vazio")
    @Length(min = 5,max = 120,message = "O nome deve ter entre 5 e 120 caracteres")
    private String nome;


    @NotEmpty(message = "O email não pode estar vazio")
    @Email(message = "Email inválido")
    private String email;

    public ClienteDTO() {
    }

    public ClienteDTO(Cliente obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.email = obj.getEmail();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
