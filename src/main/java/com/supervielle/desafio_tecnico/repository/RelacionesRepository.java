package com.supervielle.desafio_tecnico.repository;









import org.springframework.data.jpa.repository.JpaRepository;

import com.supervielle.desafio_tecnico.model.Relaciones;


public interface RelacionesRepository extends JpaRepository<Relaciones,Long>{
     Relaciones findById(long id);
    
}
