package mariadb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Conexion {

	public static void main(String[] args) throws SQLException {
		Scanner leer = new Scanner(System.in);
		System.out.println("¿Quieres ver los datos por pantalla[1] o guardarlos en un archivo[2]?");
		int respuesta = leer.nextInt();
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/starwars?","star","wars");
		DatabaseMetaData metaData = con.getMetaData();
		Statement st = con.createStatement();
		int c2 =0;
		ResultSet query3 = st.executeQuery ("SELECT table_name FROM information_schema.tables WHERE table_schema = 'starwars'");
		while (query3.next())
		{
			c2++;
		}
		ResultSet query = st.executeQuery ("SELECT table_name FROM information_schema.tables WHERE table_schema = 'starwars'");
		int col;
		ResultSetMetaData rsmetadatos;
		String tabla[] = new String[c2];
		int c = 0;

		while (query.next())
		{
			tabla[c] = query.getString(1);
			c++;
		}
		ResultSet pkey = null; 
		String[] primary = new String[tabla.length];
		for (int i = 0; i < tabla.length; i++) {
			pkey = metaData.getPrimaryKeys(null, null, tabla[i]);
			while (pkey.next()) {
				primary[i] = pkey.getString(4);
			}
		}
		ResultSet query2;
		BufferedWriter bw = null;
		if (respuesta == 2) {
			JFileChooser fc = new JFileChooser();
			String filename;
			fc.setDialogTitle("¿Donde quieres guardar el archivo?");
			fc.setSelectedFile(null);
			int opcion = fc.showSaveDialog(null);
			filename = fc.getSelectedFile().toString();
			try {
				File fichero = new File(filename);

				System.out.println(fichero.getCanonicalPath()); 

				bw = new BufferedWriter(new FileWriter(fichero));
				for (int j = 0; j < tabla.length; j++) {
					query2 = st.executeQuery ("SELECT * FROM "+tabla[j]);
					rsmetadatos =  query2.getMetaData();
					bw.write("\n\nNOMBRE TABLA: "+tabla[j]);
					bw.write("\nClave primaria: " + primary[j]);
					col = rsmetadatos.getColumnCount();
					bw.write("\nColumnas: "+col);
					for(int i=1;i<=col;i++){
						bw.write("\n"+rsmetadatos.getColumnName(i) + " - " + rsmetadatos.getColumnTypeName(i));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bw.close(); 
				} catch (Exception e) {
				}
			}
		} else if (respuesta == 1) {
			for (int j = 0; j < tabla.length; j++) {
				query2 = st.executeQuery ("SELECT * FROM "+tabla[j]);
				rsmetadatos =  query2.getMetaData();
				System.out.println ("\nNOMBRE TABLA: "+tabla[j]);
				System.out.println("Clave primaria: " + primary[j]);
				col = rsmetadatos.getColumnCount();
				System.out.println("Columnas: "+col);
				for(int i=1;i<=col;i++){
					System.out.print(rsmetadatos.getColumnName(i) + " - " + rsmetadatos.getColumnTypeName(i) + "\n");
				}
			}
		}






		con.close();

	}
} 
