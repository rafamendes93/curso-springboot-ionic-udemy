package com.rafael.cursomc.cursomc.dto;

import com.rafael.cursomc.cursomc.domain.Estado;

import java.io.Serializable;

public class EstadoDTO implements Serializable {

    private Integer id;

    private String nome;

    public EstadoDTO() {
    }

    public EstadoDTO(Estado estado){
        this.id = estado.getId();
        this.nome = estado.getNome();
    }

    public EstadoDTO(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
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
}
