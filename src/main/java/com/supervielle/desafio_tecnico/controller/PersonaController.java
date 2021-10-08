package com.supervielle.desafio_tecnico.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.supervielle.desafio_tecnico.model.Persona;
import com.supervielle.desafio_tecnico.model.Relaciones;
import com.supervielle.desafio_tecnico.repository.PersonaRepository;
import com.supervielle.desafio_tecnico.repository.RelacionesRepository;

@RestController
@RequestMapping("/api")
public class PersonaController {

	private PersonaRepository personaRepository;

	private RelacionesRepository relacionRepository;

	public PersonaController(PersonaRepository personaRepository, RelacionesRepository relacionRepository) {

		this.personaRepository = personaRepository;
		this.relacionRepository = relacionRepository;
	}

	List<Relaciones> relacionesTotal() {
		return relacionRepository.findAll();
	}

	@GetMapping("/personas")
	Collection<Persona> personas() {
		return personaRepository.findAll();
	}

	@GetMapping("/estadisticas")
	Collection<String> estadisticas() {
		List<Persona> listOfPersonas = personaRepository.findAll();
		int cant_mujeres = 0;
		int cant_hombres = 0;
		int cont_argentinos = 0;

		float porcentaje_argentinos = 0.0f;
		for (Persona persona : listOfPersonas) {

			// Para saber mujeres y hombres
			switch (persona.getSexo().toUpperCase()) {
			case "M":
				cant_hombres += 1;
				break;
			case "F":
				cant_mujeres += 1;
				break;
			default:

			}

			// Para saber el porcentaje de argentinos
			if (persona.getPais().toUpperCase().contains("ARGENTINA")) {

				cont_argentinos += 1;
			}
		}

		porcentaje_argentinos = (cont_argentinos * 100) / listOfPersonas.size();
		List<String> listadoEstadisticas = new ArrayList<>();
		listadoEstadisticas.add("Cantidad de hombres: " + cant_hombres);
		listadoEstadisticas.add("Cantidad de mujeres: " + cant_mujeres);
		listadoEstadisticas.add("Porcentaje de argentinos: " + porcentaje_argentinos + " %");

		return listadoEstadisticas;

	}

	@GetMapping("/persona/{id}")
	ResponseEntity<?> getPersona(@PathVariable Long id) {
		Optional<Persona> persona = personaRepository.findById(id);
		return persona.map(response -> ResponseEntity.ok().body(response))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
    

	@PostMapping("/persona")
	ResponseEntity<Persona> createPersona(@Valid @RequestBody Persona persona) throws URISyntaxException {
		//
		if(persona != null && persona.getEdad() >= 18 && persona.getDato_contacto() != null) {
		Persona result = personaRepository.save(persona);
		return ResponseEntity.created(new URI("/api/persona" + result.getId())).body(result);
		}else {
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		}
	}

	@PutMapping("/persona/{id}")
	ResponseEntity<Persona> updatePersona(@Valid @RequestBody Persona persona) {

		if (persona.getEdad() >= 18) {
			Persona result = personaRepository.save(persona);
			return ResponseEntity.ok().body(result);
		} else {
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		}
	}

	@DeleteMapping("/persona/{id}")
	ResponseEntity<?> deletePersona(@PathVariable Long id) {
		if(personaRepository.findById(id) != null) {
			personaRepository.deleteById(id);
		return ResponseEntity.ok().build();
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/personas/{id_1}/padre/{id_2}")
	ResponseEntity<String> relacionarPersona(@PathVariable(value = "id_1", required = true) Long id1,
			@PathVariable(value = "id_2", required = true) Long id2) {

		Optional<Persona> persona1 = personaRepository.findById(id1);

		// EXISTE EL ID1 EN LA TABLA PERSONA?
		if (persona1 != null) {
			Optional<Persona> persona2 = personaRepository.findById(id2);
			// EXISTE EL ID2 EN LA TABLA PERSONA?
			if (persona2 != null) {

				// OK DEBEMOS COMPARAR LAS LISTAS DE RELACIONES DE LOS IDs

				System.out.println(relacionesTotal().toString() + " Size: " + relacionesTotal().size());

				boolean relacionModificada = false;
				boolean relacionDesvinculada = false;

				for (Relaciones relacion_i : relacionesTotal()) {

					if (relacion_i.getId_1() == id1 && relacion_i.getId_2() == id2
							&& relacion_i.getRelacion().contains("PADRE")) {
						// SI ID1 YA ES PADRE DE ID 2
						return ResponseEntity.ok()
								.body("El id : " + relacion_i.getId_1() + " ya es PADRE de id: " + relacion_i.getId_2());
					} else if (relacion_i.getId_1() == id2 && relacion_i.getId_2() == id1
							&& relacion_i.getRelacion().contains("PADRE")) {
						// SI ID2 ES PADRE DE ID1 OSEA ID1 ES HIJO DE ID2
						return ResponseEntity.ok()
								.body("El id : " + relacion_i.getId_1() + " es HIJO de id: " + relacion_i.getId_2());
					}

					// LOOPEO INTERNO PARA VERIFICAR SI EXISTEN RELACIONES PREEXISTENTES
					for (Relaciones relacion_i2 : relacionesTotal()) {
						Relaciones relacion_rightnow = relacion_i2;

						// SI ID1 TIENE UNA RELACION DE PADRE CON OTRO ID QUE NO ES ID2...
						if (relacion_i2.getId_1() == id1 && relacion_i2.getId_2() != id2
								&& relacion_i2.getRelacion().contains("PADRE")) {

							relacion_rightnow.setRelacion("RELACION DESVINCULADA");
							relacionRepository.save(relacion_rightnow);
							relacionDesvinculada = true;
						}

						// SI ID2 TIENE UNA RELACION DE HIJO CON OTRO ID QUE SERIA EL PADRE ...
						if (relacion_i2.getId_1() != id1 && relacion_i2.getId_2() == id2
								&& relacion_i2.getRelacion().contains("PADRE")) {

							relacion_rightnow.setRelacion("RELACION DESVINCULADA");
							relacionRepository.save(relacion_rightnow);
							relacionDesvinculada = true;
						}

						// SI ID1 Y ID2 TIENEN UNA RELACION PREEXISTENTE DISTINTA A PADRE E HIJO
						if (relacion_i2.getId_1() == id1 && relacion_i2.getId_2() == id2
								|| relacion_i2.getId_1() == id2 && relacion_i2.getId_2() == id1) {

							if (relacion_i2.getRelacion().contains("HERMANO")
									|| relacion_i2.getRelacion().contains("TIO")
									|| relacion_i2.getRelacion().contains("PRIMO")) {

								Relaciones relacion_rightnow_2 = relacion_i2;
								// DEJO LA RELACION DEL ID COMO MODIFICADA Y AGREGO OTRA FILA PARA LA NUEVA
								// ESTO LO HAGO COMO UN PLUS PARA QUE QUEDE REGISTRO DE LA MODIFICACION EN LA
								// BASE DE DATOS
								relacion_rightnow_2.setRelacion("RELACION MODIFICADA");
								relacionRepository.save(relacion_rightnow_2);
								relacionModificada = true;

							}
						}
						

					}

				}

				Relaciones relacion_nueva = new Relaciones();
				// AGREGO MANUALMENTE EL ID SUMANDOLE 1 A LA CANTIDAD TOTAL DE FILAS
				relacion_nueva.setId((long) (relacionesTotal().size() + 1));
				relacion_nueva.setId_1(id1);
				relacion_nueva.setId_2(id2);
				relacion_nueva.setRelacion("PADRE");

				relacionRepository.save(relacion_nueva);

				if (relacionModificada && relacionDesvinculada) {

					return ResponseEntity.ok().body("ID: " + id1 + " con los datos: "+persona1.toString() + " ahora es padre de ID: " + id2
							+ " con los datos: "+persona2.toString()+" , se ha modificado una relación preexistente distinta de padre y desvinculado una o varias de padre");

				} else if (relacionDesvinculada) {

					return ResponseEntity.ok().body("ID: " + id1 + " con los datos: "+persona1.toString()+" ahora es padre de ID: " + id2
							+" con los datos: "+persona2.toString()+ " y se ha desvinculado una o varias relaciones preexistentes de padre");

				} else if (relacionModificada) {
					return ResponseEntity.ok().body("ID: " + id1 +" con los datos: "+persona1.toString() + " ahora es padre de ID: " + id2
							+ " con los datos: "+persona2.toString()+" y se ha modificado una relación preexistente distinta de padre");

				} else {
					return ResponseEntity.ok().body("ID: " + id1 + " con los datos: "+persona1.toString() + " ahora es padre de ID: " + id2+" con los datos: "+persona2.toString());
				}
			} else {
				return ResponseEntity.ok().body("La persona con ID: " + id1 + " no existe");
			}

		} else {
			return ResponseEntity.ok().body("La persona con ID: " + id2 + " no existe");
		}

	}

	@GetMapping("/relaciones/{id_1}/{id_2}")
	ResponseEntity<String> getRelacion(@PathVariable(value = "id_1", required = true) Long id1,
			@PathVariable(value = "id_2", required = true) Long id2) {

		Optional<Persona> persona1 = personaRepository.findById(id1);
		boolean relacionDistinta = false;
		// EXISTE EL ID1 EN LA TABLA PERSONA?
		if (persona1 != null) {
			Optional<Persona> persona2 = personaRepository.findById(id2);
			// EXISTE EL ID2 EN LA TABLA PERSONA?
			if (persona2 != null) {
				// OK AHORA ITERAMOS PARA VERIFICAR SI HAY RELACION ENTRE ESTAS 2 PERSONAS

				for (Relaciones relacion_i : relacionesTotal()) {
					//SI HAY RELACION DE HERMANO , TIO O PRIMO ...
					if (relacion_i.getId_1() == id1 && relacion_i.getId_2() == id2 || relacion_i.getId_1() == id2 && relacion_i.getId_2()== id1) {
						if (relacion_i.getRelacion().contains("TIO") || relacion_i.getRelacion().contains("PRIMO")
								|| relacion_i.getRelacion().contains("HERMANO")) {
							return ResponseEntity.ok().body("La relación de la persona: "+persona1.toString() + "\ncon la persona: "+persona2.toString()+ "\nes : " + relacion_i.getRelacion());
						}
					}
					
					//SI LA RELACIÓN ES DISTINTA DE HERMANO TIO O PRIMO
					if(relacion_i.getId_1() == id1 && relacion_i.getId_2() == id2
							&& relacion_i.getRelacion().contains("PADRE")) {
						relacionDistinta = true;
					}
					
					if(relacion_i.getId_1() == id2 && relacion_i.getId_2() == id1
							&& relacion_i.getRelacion().contains("PADRE")) {
						relacionDistinta = true;
					}
					
					

				}
				
				if(relacionDistinta) {
					return ResponseEntity.ok().body("La relación entre ambos es distinta al filtro solicitado en el desafio");
				}
				
				return ResponseEntity.ok().body("La relación entre ambos no existe");

			} else {
				return ResponseEntity.ok().body("La persona con ID: " + id2 + " no existe");
			}

		} else {
			return ResponseEntity.ok().body("La persona con ID: " + id1 + " no existe");
		}
		
	}
}