package lotr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.And;
import org.neodatis.odb.core.query.criteria.ICriterion;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

public class Ejercicio2 {
	public static void main(String[] args) {
		ODB odb = ODBFactory.open("lotr.db");

		//APARTADO 1
		System.out.println("Apartado 1");
		ICriterion criterio = Where.isNotNull("spouse"); 
		IQuery character_query = new CriteriaQuery(Character.class, criterio);
		Objects<Character> objects_character =odb.getObjects(character_query);		
		System.out.println("Hay "+objects_character.size()+" personajes casados.");

		//APARTADO 1.2
		System.out.println("\nApartado 1.2");
		criterio = Where.isNull("spouse"); 
		character_query = odb.criteriaQuery(Character.class, criterio);
		objects_character =odb.getObjects(character_query);
		System.out.println("Hay "+objects_character.size()+" personajes no casados.");

		//APARTADO 2
		System.out.println("\nApartado 2");
		criterio = Where.like("name","%Baggins%"); 
		character_query = odb.criteriaQuery(Character.class, criterio);
		objects_character =odb.getObjects(character_query);
		int c = 0;
		System.out.println("Nº  ID    Nombre");
		while(objects_character.hasNext()){
			Character personaje= objects_character.next();
			c++;
			System.out.println(c+" "+personaje.getName());
		}
		System.out.println("Hay "+c+" miembros del clan Baggins.");

		//APARTADO 3
		System.out.println("\nApartado 3");
		criterio = new And().add(Where.gt("budgetInMillions", 100))
				.add(Where.lt("rottenTomatoesScore", 70)); 
		IQuery pelicula_query = odb.criteriaQuery(Movie.class, criterio);
		Objects<Movie> objects_movie =odb.getObjects(pelicula_query);
		System.out.println("ID | Nombre | Tiempo | Presupuesto | Beneficio | Nominaciones | Premios | Ganados | RottenTomatoes | Libro");
		while(objects_movie.hasNext()){
			Movie pelicula= objects_movie.next();
			System.out.println(pelicula.getId()+" | "+pelicula.getName()+ " | "+pelicula.getRuntimeInMinutes()+
					" | "+pelicula.getBudgetInMillions()+" | "+pelicula.getBoxOfficeRevenueInMillions()+
					" | "+pelicula.getAcademyAwardNominations()+" | "+pelicula.getAcademyAwardWins()+
					" | "+pelicula.getRottenTomatoesScore()+" | "+pelicula.getLibro().getTitle());
		}

		//APARTADO 4
		System.out.println("\nApartado 4");
		criterio = Where.like("name","%Valinor%"); 
		IQuery reino_query = odb.criteriaQuery(Realm.class, criterio);
		Objects<Realm> objects_reino =odb.getObjects(reino_query);
		System.out.println("Consulta de Realm");
		while(objects_reino.hasNext()){
			Realm reino = objects_reino.next();
			System.out.print("Reino - "+ reino.getName());
			System.out.print(" | Personajes: ");
			for (Character personaje : reino.getPersonaje()) {
				System.out.println(personaje.getName());
			}
		}
		System.out.println("\nApartado 4.2");
		criterio = Where.like("id_realm.name","%Valinor%"); 
		IQuery	char_query = odb.criteriaQuery(Character.class, criterio);
		Objects<Character> objects_char =odb.getObjects(char_query);

		while(objects_char.hasNext()){
			Character personaje = objects_char.next();
			System.out.print("Personaje: "+ personaje.getName());
			System.out.println(" | Reino: "+personaje.getId_realm().getName());

		}
		//APARTADO 5
		System.out.println("\nApartado 5");
		criterio = Where.like("title","%Hobbit%"); 
		IQuery	book_query = odb.criteriaQuery(Book.class, criterio);
		Objects<Book> objects_book =odb.getObjects(book_query);

		while(objects_book.hasNext()){
			Book libro = objects_book.next();
			for (Movie pelicula : libro.getPelicula()) {
				System.out.println("Titulo: "+ pelicula.getName());				
			}	
		}

		//APARTADO 6
		System.out.println("\nApartado 6");		

		criterio = Where.like("title","%The Return Of The King%");
		IQuery consulta = new CriteriaQuery(Realm.class);
		Objects<Realm> objeto= odb.getObjects(consulta);
		List<String> listaReino = new ArrayList<String>();
		while (objeto.hasNext()) {
			Realm reino= objeto.next();
			criterio=Where.equal("title", "The Return Of The King");
			consulta = new CriteriaQuery(Book.class,criterio);
			Objects<Book> objeto_libro= odb.getObjects(consulta);

			while (objeto_libro.hasNext()) {
				Book libro = objeto_libro.next();
				for (Movie pelicula : libro.getPelicula()) {
					for (Dialog dialogo : pelicula.getDialogo()) {
						if (dialogo.getDialog().matches(".*"+reino.getName()+".*")) {
							listaReino.add(reino.getName());
						}
					}
				}
			}
		}

		Set<String> set = new HashSet<String>(listaReino);
		listaReino.clear();
		listaReino.addAll(set);
		System.out.println("Reinos: "+listaReino);
		System.out.println("Total: "+listaReino.size());

	}
}
