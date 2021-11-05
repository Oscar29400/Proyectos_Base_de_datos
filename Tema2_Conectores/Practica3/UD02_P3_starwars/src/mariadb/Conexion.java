package mariadb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {

	public static void main(String[] args) throws SQLException {
		System.out.println("INFORMACION DE LOS PLANETAS: ");
		System.out.println("-----------------------------------------------------");
		mostrar();
		System.out.println("-----------------------------------------------------");
		System.out.println("INSERCION DE LAS PELICULAS: ");
		insertar(7,"Episode VII","The Force Awakening");
		insertar(8,"Episode VIII","The Last Jedi");
		insertar(9,"Episode IX","The Raise Of Skywalker");
		System.out.println("-----------------------------------------------------");
		System.out.println("PERSONAJES QUE PERTENECEN A LA ORDEN JEDI:");
		jedi();
		System.out.println("-----------------------------------------------------");
		System.out.println("LOS MUERTOS DEL EPISODIO III SON: ");
		muertos();



	}
	public static void insertar(int id, String episodio, String titulo) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/starwars?","star","wars");
		Statement st = con.createStatement();
		ResultSet existe = st.executeQuery ("SELECT id FROM films where id="+id);
		if (existe.next()) {
			System.out.println("La pelicula "+titulo + " ya existe, por lo que no se introducirá.");
		} else {
			int insertar = st.executeUpdate("INSERT INTO films VALUES ("+id+",'"+episodio+"','"+titulo+"')");
			System.out.println("Se ha insertado la pelicula " + titulo + " correctamente.");
		}
		
			
	
	}
	
	public static void mostrar() throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/starwars?","star","wars");
		Statement st = con.createStatement();
		ResultSet columnas = st.executeQuery ("SELECT column_name FROM information_schema.columns where table_name='planets'");
		int numero= 1;
		while (columnas.next()) {
			numero++;
		}
		ResultSet query = st.executeQuery ("SELECT * FROM planets");
		while (query.next())
		{
			for (int i = 1; i < numero; i++) {
				System.out.print(query.getString(i)+ " | ");
			}
			System.out.println("");
		}
	}
	
	public static void jedi() throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/starwars?","star","wars");
		Statement st = con.createStatement();
		ResultSet jedi = st.executeQuery ("Select name "
				+ "FROM characters charac "
				+ "join character_affiliations characaf on charac.id = characaf.id_character "
				+ "join affiliations aff on characaf.id_affiliation = aff.id where characaf.id_affiliation = 1");
		while (jedi.next()) {
			System.out.println("Jedi: "+jedi.getString(1));
		}
	}
	public static void muertos() throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/starwars?","star","wars");
		Statement st = con.createStatement();
		ResultSet muertos = st.executeQuery ("Select charac.name, "
				+ "ch2.name "
				+ "FROM characters charac "
				+ "join deaths dea on charac.id = dea.id_character join characters ch2 on  dea.id_killer = ch2.id "
				+ "where dea.id_film = 3;");
		while (muertos.next()) {
			System.out.println("Muerto: "+muertos.getString(1)+ " Asesino: " + muertos.getString(2));
		}
	}

}


