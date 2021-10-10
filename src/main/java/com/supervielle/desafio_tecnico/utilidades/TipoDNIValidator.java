package com.supervielle.desafio_tecnico.utilidades;

public class TipoDNIValidator {
	
	
	public static boolean isValid(String dni) {
		
		final String DNI = "DNI"; 
		final String LIBRETA_ENROLAMIENTO = "LIBRETAENROLAMIENTO";
		final String LIBRETA_CIVICA = "LIBRETACIVICA";
		
		if(dni.replaceAll(" ", "").toUpperCase().equals(DNI))
			return true;
		
		if(dni.replaceAll(" ", "").toUpperCase().equals(LIBRETA_ENROLAMIENTO))
			return true;
		
		if(dni.replaceAll(" ", "").toUpperCase().equals(LIBRETA_CIVICA))
			return true;
		
		return false;
	}
}
