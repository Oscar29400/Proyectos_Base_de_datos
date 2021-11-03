package veterinario;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Veterinaria {
	public static void main(String[] args) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:sqlite:/home/2dam/Escritorio/sqlite-tools-linux-x86-3360000/veterinaria.db");
		DatabaseMetaData dbmd = con.getMetaData();
		ResultSet resul = dbmd.getTables(null,"veterinaria",null,null);
		while (resul.next()) {
			String catalogo = resul.getString("TABLE_CAT");
			String esquema = resul.getString("TABLE_SCHEM");
			String tabla = resul.getString("TABLE_NAME");
			String tipo = resul.getString("TABLE_TYPE");
			System.out.println("Catalogo: "+ catalogo + " Esquema: "+ esquema + " tipo: " + tipo + " Nombre tabla: " + tabla);	
		}
		con.close();
	}
	


}
