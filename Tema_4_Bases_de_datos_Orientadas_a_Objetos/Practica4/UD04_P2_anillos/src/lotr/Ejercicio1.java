package lotr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

public class Ejercicio1 {

	public static void main(String[] args) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/lotr?","star","wars");
		ODB odb = ODBFactory.open("lotr.db");
		realm(con,odb);
		character(con,odb);
		espouse(con,odb);
		book(con,odb);
		chapter(con,odb);
		movie(con,odb);
		dialog(con,odb);
		rellenar(con,odb);
		odb.close();
		con.close();
	}

	public static void realm(Connection con,ODB odb) throws SQLException {
		Statement st = con.createStatement();
		ResultSet query = st.executeQuery ("SELECT * FROM realms");
		while (query.next())
		{
			Realm reino = new Realm(query.getInt(1),query.getString(2),query.getInt(3),query.getInt(4));
			odb.store(reino);
		}
		st.close();
		System.out.println("Se han añadido los Reinos con exito.");
		odb.commit();
	}
	public static void character(Connection con,ODB odb) throws SQLException {
		Statement st = con.createStatement();
		ResultSet query = st.executeQuery ("SELECT * FROM characters");
		while (query.next())
		{
			if (query.getInt(10) == 0) {
				Character personaje = new Character(query.getString(1),query.getString(2),query.getString(3),
						query.getString(4),query.getString(5),query.getString(6),query.getString(7),query.getString(8),
						query.getString(9),null, null);
				odb.store(personaje);
			} else {
				IQuery reino_query = new CriteriaQuery(Realm.class, Where.equal("id", query.getInt(10)));
				Objects<Realm> objects = odb.getObjects(reino_query);
				Realm reino = (Realm) objects.next();			
				Character personaje = new Character(query.getString(1),query.getString(2),query.getString(3),
						query.getString(4),query.getString(5),query.getString(6),query.getString(7),query.getString(8),
						query.getString(9),reino, null);
				odb.store(personaje);
			}
		}
		st.close();
		odb.commit();


		System.out.println("Se han añadido los Personajes con exito.");
		odb.commit();
	}
	public static void espouse(Connection con,ODB odb) throws SQLException {
		Statement st = con.createStatement();
		ResultSet query = st.executeQuery ("SELECT * FROM marriage");
		while(query.next()) {
			if (query.getString(2) == null) {
				IQuery consulta = new CriteriaQuery(Character.class,Where.equal("id",query.getString(1)));
				Objects <Character> objetos = odb.getObjects(consulta);
				Character personaje = objetos.next();
				personaje.setSpouse("Unknown person");
				odb.store(personaje);
			} else {
				IQuery consulta = new CriteriaQuery(Character.class,Where.equal("id",query.getString(1)));
				Objects <Character> objetos = odb.getObjects(consulta);
				Character personaje = objetos.next();
				personaje.setSpouse(query.getString(2));
				odb.store(personaje);
			}
		}
		st.close();
		System.out.println("Se han añadido las Spouse de los personajes con exito.");
		odb.commit();
	}

	public static void book(Connection con,ODB odb) throws SQLException {
		Statement st = con.createStatement();
		ResultSet query = st.executeQuery ("SELECT * FROM books");
		while (query.next())
		{
			Book libro = new Book(query.getInt(1), query.getString(2));	
			odb.store(libro);
		}
		st.close();
		odb.commit();
	}

	public static void chapter(Connection con,ODB odb) throws SQLException {
		Statement st = con.createStatement();
		ResultSet query = st.executeQuery ("SELECT * FROM chapters");
		while (query.next())
		{
			IQuery book_query = new CriteriaQuery(Book.class, Where.equal("id", query.getInt(3)));
			Objects<Book> objects = odb.getObjects(book_query);
			Book libro = (Book) objects.next();
			Chapter capitulo = new Chapter(query.getString(1), query.getString(2), libro);	
			odb.store(capitulo);
		}
		st.close();
		odb.commit();
		Objects<Book> objects =odb.getObjects(Book.class);
		while(objects.hasNext()){
			Book libro= objects.next();
			Objects<Chapter> objects_chapter =odb.getObjects(Chapter.class);
			while (objects_chapter.hasNext()) {
				Chapter capitulo = objects_chapter.next();
				if (capitulo.getId_book()==libro) {
					libro.setCapitulo(capitulo);
					odb.store(capitulo);
				}
			}
		}
		System.out.println("Se han añadido los Capitulos con exito.");
		odb.commit();
	}

	public static void movie(Connection con,ODB odb) throws SQLException {
		Statement st = con.createStatement();
		ResultSet query = st.executeQuery ("SELECT * FROM movies");
		while (query.next())
		{
			Movie pelicula = new Movie(query.getString(1), query.getString(2), query.getInt(3), query.getInt(4),
					query.getInt(5), query.getInt(6), query.getInt(7), query.getInt(8), null);
			odb.store(pelicula);
		}
		st.close();

		Statement st2 = con.createStatement();
		ResultSet query_idBooks = st2.executeQuery ("SELECT * FROM books_movies");
		while (query_idBooks.next())
		{
			Objects<Movie> objects =odb.getObjects(Movie.class);
			while(objects.hasNext()){
				Movie pelicula= objects.next();
				Objects<Book> objects_libro =odb.getObjects(Book.class);
				while(objects_libro.hasNext()){
					Book libro= objects_libro.next();
					if ((libro.getId() == query_idBooks.getInt(3)) && pelicula.getId().equals(query_idBooks.getString(2))) {
						pelicula.setLibro(libro);
						odb.store(pelicula);
					}
				}
			}
		}
		st2.close();
		odb.commit();
		Objects<Book> objects =odb.getObjects(Book.class);
		while(objects.hasNext()){
			Book libro= objects.next();
			Objects<Movie> objects_movie =odb.getObjects(Movie.class);
			while (objects_movie.hasNext()) {
				Movie pelicula = objects_movie.next();
				if (pelicula.getLibro()==libro) {
					libro.setPelicula(pelicula);
					odb.store(pelicula);
				}
			}
		}
		System.out.println("Se han añadido las Peliculas con exito.");
		odb.commit();
	}

	public static void dialog(Connection con,ODB odb) throws SQLException {
		Statement st = con.createStatement();
		ResultSet query = st.executeQuery ("SELECT * FROM dialogs");
		while (query.next())
		{
			IQuery movie_query = new CriteriaQuery(Movie.class, Where.equal("id", query.getString(3)));
			Objects<Movie> objects_movie =odb.getObjects(movie_query);
			Movie pelicula = (Movie) objects_movie.next();

			IQuery character_query = new CriteriaQuery(Character.class, Where.equal("id", query.getString(4)));
			Objects<Character> objects_character =odb.getObjects(character_query);
			Character personaje = (Character) objects_character.next();

			Dialog dialogo = new Dialog(query.getString(1), query.getString(2), pelicula, personaje);
			odb.store(dialogo);
		}
		st.close();
		odb.commit();
		Objects<Movie> objects =odb.getObjects(Movie.class);
		while(objects.hasNext()){
			Movie pelicula= objects.next();
			Objects<Dialog> objects_dialog =odb.getObjects(Dialog.class);
			while (objects_dialog.hasNext()) {
				Dialog dialogo = objects_dialog.next();
				if (dialogo.getId_movie()== pelicula) {
					pelicula.setDialogo(dialogo);
					odb.store(pelicula);
				}
			}
		}
		System.out.println("Se han añadido las Dialogos con exito.");
		odb.commit();
	}
	public static void rellenar(Connection con,ODB odb) throws SQLException {
		Objects<Realm> objects =odb.getObjects(Realm.class);
		while(objects.hasNext()){
			Realm reino= objects.next();
			Objects<Character> objects_character =odb.getObjects(Character.class);
			while (objects_character.hasNext()) {
				Character personaje = objects_character.next();
				if (personaje.getId_realm() == reino) {
					reino.setPersonaje(personaje);
					odb.store(reino);
				}
			}
		}
		odb.commit();
	}

}


