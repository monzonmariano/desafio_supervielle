package com.supervielle.desafio_tecnico.model;





import javax.persistence.Entity;

import javax.persistence.Id;

import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name="persona")
public class Persona {
	@Id
	private long id;
    
  
	private long dni;
	
  
	private String pais;
	
    
	private String sexo;
	
    @NonNull
    private String dato_contacto;
    
    
    private String tipo_dni;
    
    private int edad;


	
	
   
}
