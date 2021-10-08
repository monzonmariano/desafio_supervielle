package com.supervielle.desafio_tecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supervielle.desafio_tecnico.model.Persona;

public interface PersonaRepository extends JpaRepository<Persona,Long>{
 Persona findById(long id);
 
}
