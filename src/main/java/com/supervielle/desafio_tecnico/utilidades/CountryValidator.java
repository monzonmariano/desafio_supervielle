package com.supervielle.desafio_tecnico.utilidades;

import java.util.Locale;

public class CountryValidator {
	


	
	public static boolean isValid(String country) {
		

		
		//LISTA DE TODOS LOS PAISES Y SUS CODIGOS DE PAIS
		String[] locales = Locale.getISOCountries();
		
		for (String countryCode : locales) {
            
			//ESTE OBJETO REPRESENTA UNA REGION GEOGRÁFICA,POLÍTICA O CULTURAL ESPECIFICA
			//PARA PERSONALIZAR LA INFORMACIÓN DEL USER
			Locale obj = new Locale("", countryCode);
			
			//SACAMOS LOS ESPACIOS EN BLANCO Y CONVERTIMOS EN MAYÚSCULAS TODO
			//PARA COMPARAR CON LA LISTA DE PAISES SI EXISTE EL INGRESADO EN EL REQUEST BODY
            if(country.replaceAll(" ", "").toUpperCase().equals(obj.getDisplayCountry().replaceAll(" ", "").toUpperCase()))
            	return true;
            
//			System.out.println(
//					"Country Code = " + obj.getCountry() + ", Country Name = " + obj.getDisplayCountry(locale));

		}
		
		
		return false;
	}
}
