package mariadb;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class Conexion_sqlite {
	public static void main(String[] args) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:sqlite:C:/Users/2dam/Documents/sqlite-tools-win32-x86-3360000/sqlite-tools-win32-x86-3360000/veterinaria.db");
		DatabaseMetaData dbmd = con.getMetaData();
		ResultSet resul = dbmd.getTables(null,"veterinaria",null,null);
		Statement st = con.createStatement();
		String tablas = "";
		while (resul.next()) {
			String tabla = resul.getString("TABLE_NAME");
			tablas += tabla + "-";
		}

		String arrayTablas[] = tablas.split("-");
		String fk = "";
		for (int i = 0; i < arrayTablas.length; i++) {
			ResultSet foreignkey = st.executeQuery("PRAGMA foreign_key_list("+arrayTablas[i]+")");
			while (foreignkey.next()) {
				fk = foreignkey.getString(4);
			}
			ResultSet columnas = st.executeQuery ("PRAGMA table_info("+arrayTablas[i]+")");
			System.out.println("Nombre de tabla: "+ arrayTablas[i]);
			while (columnas.next()){

				if (columnas.getString(6).equals("1")) {
					System.out.println(" Columnas: "+columnas.getString(2)+" Tipo: "+columnas.getString(3)+" PRIMARY KEY");
				} else if(columnas.getString(2).equals(fk)) {
					System.out.println(" Columnas: "+columnas.getString(2)+" Tipo: "+columnas.getString(3)+" FOREIGN KEY");
				} else {
					System.out.println(" Columnas: "+columnas.getString(2)+" Tipo: "+columnas.getString(3)+" ");

				}

			}
			System.out.println();
		}


		con.close();
	}	
}
