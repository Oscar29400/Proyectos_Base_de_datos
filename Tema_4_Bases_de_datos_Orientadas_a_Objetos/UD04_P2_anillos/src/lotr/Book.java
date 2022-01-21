package lotr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Book {
	private int id;
	private String title;
	private Set<Movie> pelicula = new HashSet<Movie>();
	private Set<Chapter> capitulo = new HashSet<Chapter>();
	
	
	
	public Book(int id, String title) {
		super();
		this.id = id;
		this.title = title;		
	}

	public Set<Chapter> getCapitulo() {
		return capitulo;
	}

	public void setCapitulo(Chapter capitulo) {
		this.capitulo.add(capitulo);
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public Set<Movie> getPelicula() {
		return pelicula;
	}
	public void setPelicula(Movie pelicula) {
		this.pelicula.add(pelicula);
	}
	

}
