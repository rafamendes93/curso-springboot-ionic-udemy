package com.rafael.cursomc.cursomc.repositories;


import com.rafael.cursomc.cursomc.domain.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

    //@Transactional(readOnly = true)
    //@Query("SELECT obj FROM Cidade obj WHERE obj.estado.id = :estado_id ORDER BY obj.nome")
   //List<Cidade> findCidades(@Param("estado_id") Integer estado_id);

    List<Cidade> findCidadesByEstado_Id(Integer estado_id);
}
