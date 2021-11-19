package mariadb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

public class Ejercicio2_y_3_Conexion {

	public static void main(String[] args) throws SQLException {
		crearTablas();
		rellenarTablas();
	}

	public static void rellenarTablas() throws SQLException {
		Connection consqlite = DriverManager.getConnection("jdbc:sqlite:C:/Users/2dam/Documents/sqlite-tools-win32-x86-3360000/sqlite-tools-win32-x86-3360000/veterinaria.db");
		DatabaseMetaData dbmd = consqlite.getMetaData();
		ResultSet resul = dbmd.getTables(null,"veterinaria",null,null);
		Statement stlite = consqlite.createStatement();
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/","star","wars");
		Statement st = con.createStatement();
		st.execute("USE veterinaria");
		String tablas = "";
		while (resul.next()) {
			String tabla = resul.getString("TABLE_NAME");
			tablas += tabla + "-";
		}
		String[] tabla = tablas.split("-");
		for (int i = tabla.length-1; i >= 0; i--) {
			int contador = 0;
			ResultSet columnas = stlite.executeQuery ("PRAGMA table_info("+tabla[i]+")");
			while(columnas.next()) {
				contador++;
			}
			ResultSet columnas_nombre = stlite.executeQuery ("PRAGMA table_info("+tabla[i]+")");
			String columname = "";
			while(columnas_nombre.next()) {
				columname += columnas_nombre.getString(2)+",";

			}

			ResultSet columnas_tipo = stlite.executeQuery ("SELECT * FROM "+tabla[i]);
			String columtipo ="";

			while(columnas_tipo.next()) {
				for (int j = 1; j <= contador; j++) {
					columtipo += columnas_tipo.getString(j)+",";
				}
			}
			String[] columnanombre_array = columname.split(",");
			String[] columnatipo_array = columtipo.split(",");
			for (int j = 0; j <= contador-1; j++) {
				if (j == 0) {
					int insertar = st.executeUpdate("INSERT INTO "+ tabla[i] + " ("+columnanombre_array[j]+") VALUES ("+columnatipo_array[j]+")");
				} else {
					if (j == 3 && i == 1) {
						String[] fecha = columnatipo_array[3].split("/");
						int insertar = st.executeUpdate("UPDATE "+ tabla[i] + " SET "+columnanombre_array[j]+" = '"+fecha[2]+"-"+fecha[1]+"-"+fecha[0]+"' WHERE id = "+columnatipo_array[0]);
					} else {
						int insertar = st.executeUpdate("UPDATE "+ tabla[i] + " SET "+columnanombre_array[j]+" = '"+columnatipo_array[j]+"' WHERE id = "+columnatipo_array[0]);

					}			
				}

			}
		}
		System.out.println("LAS TABLAS HAN SIDO RELLENADAS CON EXITO");
		con.close();
	}



	public static void crearTablas() throws SQLException {
		Connection consqlite = DriverManager.getConnection("jdbc:sqlite:C:/Users/2dam/Documents/sqlite-tools-win32-x86-3360000/sqlite-tools-win32-x86-3360000/veterinaria.db");
		DatabaseMetaData dbmd = consqlite.getMetaData();
		ResultSet resul = dbmd.getTables(null,"veterinaria",null,null);
		Statement stlite = consqlite.createStatement();
		String tablas = "";
		while (resul.next()) {
			String tabla = resul.getString("TABLE_NAME");
			tablas += tabla + "-";
		}
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/","star","wars");
		Statement st = con.createStatement();
		st.execute("DROP DATABASE IF EXISTS veterinaria");
		st.execute("CREATE DATABASE veterinaria");
		st.execute("USE veterinaria");
		String[] tabla = tablas.split("-");
		for (int i = tabla.length-1; i >= 0; i--) {
			ResultSet columnas = stlite.executeQuery ("PRAGMA table_info("+tabla[i]+")");
			columnas.next();
			st.execute("CREATE TABLE "+tabla[i]+"("+columnas.getString(2)+" "+columnas.getString(3)+")");
			if (columnas.getString(6).equals ("1")) 
				st.execute("ALTER TABLE "+ tabla[i] + " ADD PRIMARY KEY ("+columnas.getString(2)+")");
			
			while (columnas.next()){
				if (columnas.getString(6).equals ("1")) {
					st.execute("ALTER TABLE "+ tabla[i] + " ADD PRIMARY KEY ("+columnas.getString(2)+")");
				} else {
					st.execute("ALTER TABLE "+ tabla[i] + " ADD "+columnas.getString(2)+" "+columnas.getString(3));
				}
				
			}
			if (i == 0) {
				st.executeUpdate("ALTER TABLE mascota ADD FOREIGN KEY (cliente) REFERENCES propietario(id)");
			}

		}
		System.out.println("LAS TABLAS HAN SIDO CREADAS CON EXITO");
		con.close();
	}
}




