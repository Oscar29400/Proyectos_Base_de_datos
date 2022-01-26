package lotr;


import java.math.BigDecimal;
import java.math.BigInteger;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.ICriterion;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

public class P4_EJ2 {

	public static void main(String[] args) {
		consultas();

	}

	public static void consultas() {
		System.out.println("Ejercicio 2");
		System.out.println("Apartado 1");
		ICriterion criterio = Where.like("name", "%Lord of the Ring%");
		ODB odb = ODBFactory.open("lotr.db");
		Values valores= odb.getValues( new ValuesCriteriaQuery(Movie.class, criterio).sum("runtimeInMinutes"));
		try {
			while (valores.hasNext()) {
				ObjectValues valorObjetos=(ObjectValues)valores.next();
				System.out.println("Total en Minutos: "+valorObjetos.getByAlias("runtimeInMinutes"));               
			}

		} catch (IndexOutOfBoundsException e) {
			System.out.println("Ninguna pelicula encontrada");
		}
		odb.close();
		System.out.println("Apartado 2");
		odb = ODBFactory.open("lotr.db");
		valores= odb.getValues( new ValuesCriteriaQuery(Book.class).size("capitulo").field("title"));
		try {
			while (valores.hasNext()) {

				ObjectValues valorObjetos=(ObjectValues)valores.next();

				System.out.println("Titulo: "+valorObjetos.getByAlias("title")+" Capitulos: "+valorObjetos.getByAlias("capitulo"));               
			}

		} catch (IndexOutOfBoundsException e) {
			System.out.println("Ningun libro encontrado");

		}
		odb.close();
		System.out.println("Apartado 3");

		odb = ODBFactory.open("lotr.db");
		criterio = Where.like("name", "%Lord of the Ring%");
		valores= odb.getValues( new ValuesCriteriaQuery(Movie.class,criterio).avg("rottenTomatoesScore"));

		try {			
			ObjectValues valorObjetos=(ObjectValues)valores.nextValues();
			BigDecimal maxima = (BigDecimal)valorObjetos.getByAlias("rottenTomatoesScore");
			System.out.println("Nota media trilogia Lord of the Rings: "+ maxima );               

		} catch (IndexOutOfBoundsException e) {
			System.out.println("Ninguna pelicula encontrado");
		}
		System.out.println("Apartado 4");

		criterio = Where.like("name","The Hobbit%");
		valores = odb.getValues(new ValuesCriteriaQuery(Movie.class,criterio)
				.sum("rottenTomatoesScore").count("rottenTomatoesScore"));
		try {			
			ObjectValues valorObjetos = valores.nextValues();
			BigDecimal sumatorio = (BigDecimal) valorObjetos.getByIndex(0);
			BigInteger cantidad = (BigInteger) valorObjetos.getByIndex(1);
			double media = sumatorio.floatValue() / cantidad.intValue();
			System.out.println("Nota media trilogia del Hobbit: "+ String.format("%.2f", media)+ ".");


		} catch (IndexOutOfBoundsException e) {
			System.out.println("Ninguna pelicula encontrado");
		}
		System.out.println("Ejercicio 3");

		criterio = Where.like("race","%Hobbit%");
		try {
			valores = odb.getValues(new ValuesCriteriaQuery(Dialog.class)
					.field("id").field("dialog").field("id_character"));
			while (valores.hasNext()) {
				ObjectValues valorObjetos = (ObjectValues) valores.next();
				Character personaje = (Character)valorObjetos.getByAlias("id_character");
				IQuery character_query = new CriteriaQuery(Character.class, criterio);
				Objects<Character> objects = odb.getObjects(character_query);
				while(objects.hasNext()) {
					Character personajes = (Character) objects.next();
					if ( valorObjetos.getByAlias("dialog").toString().matches(".*"+personajes.getName()+".*")) {
						System.out.println("Dialogo: "+valorObjetos.getByAlias("dialog") + "| Personaje:  " +
								personaje.getName() + "| Raza: " + personajes.getRace());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		odb.close();
	}
}
