package com.supervielle.desafio_tecnico.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

//Uso un id para cada relacion entre ids
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name="relaciones")
public class Relaciones {
  
	@Id
	private Long id;
	
	private Long id_1;
	
	private Long id_2;
	
	private String relacion; //TIO - HERMANO - PRIMO 
}
