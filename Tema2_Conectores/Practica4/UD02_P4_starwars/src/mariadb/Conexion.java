package mariadb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Scanner;

public class Conexion {

	public static void main(String[] args) throws SQLException {
		mostrar();
		insertar();
		muertos();
	}

	public static void mostrar() throws SQLException {
		Scanner leer = new Scanner(System.in);
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/starwars?","star","wars");
		Statement st = con.createStatement();
		ResultSet columnas = st.executeQuery ("SELECT column_name FROM information_schema.columns where table_name='planets'");
		int colum= 1;
		while (columnas.next()) {
			colum++;
		}

		for (int i = 0; i < 3; i++) {
			System.out.println("\nINFORMACION DE LOS PLANETAS: ");
			System.out.println("-----------------------------------------------------");
			System.out.println("Seleccione el rango del diametro de los planetas");
			System.out.print("Seleccione el rango minimo: ");
			int minimo = leer.nextInt();
			System.out.print("Seleccione el rango maximo: ");
			int maximo = leer.nextInt();

			String consulta = "SELECT * FROM planets p where p.diameter BETWEEN ? AND ? ";
			PreparedStatement sentencia= con.prepareStatement(consulta);
			sentencia.setInt(1, minimo);
			sentencia.setInt(2, maximo);
			ResultSet rs = sentencia.executeQuery();

			while (rs.next()) {
				System.out.print("Planeta: ");
				for (int j = 1; j < colum; j++) {
					System.out.print(rs.getString(j)+ " | ");
				}
				System.out.println();
			}
		}
		con.close();
	}

	public static void insertar() throws SQLException {
		String[][] personas = {{"Rey","170","54","black","white","brown","15DBY","female"},
				{"Finn","178","73","black","dark","dark","11DBY","male"},
				{"Kylo Ren","189","89","black","white","brown","5DBY","male"}};

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/starwars?","star","wars");
		String ultimo_planeta = "SELECT id FROM planets WHERE id = (SELECT MAX(id) FROM planets)";
		String ultimo_character = "SELECT id FROM characters WHERE id = (SELECT MAX(id) from characters)";
		Statement st = con.createStatement();
		ResultSet ultimop = st.executeQuery(ultimo_planeta);
		int nuevo_planeta = 0;
		while (ultimop.next()) {
			nuevo_planeta = Integer.parseInt(ultimop.getString(1))+1;
		}
		ResultSet ultimoc = st.executeQuery(ultimo_character);
		int nuevo_char = 0;
		while (ultimoc.next()) {
			nuevo_char = Integer.parseInt(ultimoc.getString(1))+1;
		}
		String[] planeta = {"Jakku","Kamino","Chandrila"};
		String[] planetaInt = new String[3];
		int contador = 0;
		ResultSet planetas_int = null;
		for (int i = 0; i < planeta.length; i++) {
			String planetacual = "SELECT p.id FROM planets p  WHERE p.name = ?";
			PreparedStatement sentenciaplanet = con.prepareStatement(planetacual);
			sentenciaplanet.setString(1, planeta[i]);
			planetas_int = sentenciaplanet.executeQuery();
			while(planetas_int.next())
			{
				planetaInt[contador] = planetas_int.getString(1);
				contador++;
			}
		}
		String consulta = "INSERT INTO planets VALUES("+nuevo_planeta+",'Jakku2','23','304','10465','arid','1 standard','desert','1','200000',NULL,NULL,NULL)";
		int jakku = st.executeUpdate(consulta);
		int j = 0;

		for (int i = 0; i < 3; i++) {
			String personajes = "INSERT INTO characters VALUES("+(nuevo_char+i)+",?,?,?,?,?,?,?,?,?,NOW(),NOW(),null)";
			PreparedStatement sentencia= con.prepareStatement(personajes);

			sentencia.setString(1, personas[i][0]);
			sentencia.setString(2, personas[i][1]);
			sentencia.setString(3, personas[i][2]);
			sentencia.setString(4, personas[i][3]);
			sentencia.setString(5, personas[i][4]);
			sentencia.setString(6, personas[i][5]);
			sentencia.setString(7, personas[i][6]);
			sentencia.setString(8, personas[i][7]);
			sentencia.setString(9, planetaInt[i]);
			int personajes_lista = sentencia.executeUpdate();
		}
		System.out.println("\nSe han insertado los personajes correctamente.");
		con.close();
	}

	public static void muertos() throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/starwars?","star","wars");
		String pelis = "SELECT episode,title FROM films WHERE id = ?";
		String muertes = "Select charac.name, ch2.name "
				+ "FROM characters charac join deaths dea on charac.id = dea.id_character "
				+ "join characters ch2 on  dea.id_killer = ch2.id where dea.id_film = ?";
		PreparedStatement st = con.prepareStatement(muertes);
		PreparedStatement st2 = con.prepareStatement(pelis);
		for (int i = 1; i <= 6; i++) {
			st2.setInt(1, i);
			ResultSet rs2 = st2.executeQuery();
			rs2.next();
			System.out.println("\n"+rs2.getString(1)+  ": " +rs2.getString(2));
			st.setInt(1, i);
			ResultSet muertos_query = st.executeQuery();
			while (muertos_query.next()) {
				System.out.println("Muerto: "+muertos_query.getString(1)+ " Asesino: " + muertos_query.getString(2));
			}
		}
		con.close();

	}
}



