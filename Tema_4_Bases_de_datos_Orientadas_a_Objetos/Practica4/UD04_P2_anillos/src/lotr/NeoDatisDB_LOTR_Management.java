package lotr;

import org.neodatis.odb.ClassRepresentation;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.ODBRuntimeException;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Values;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

public class NeoDatisDB_LOTR_Management {
	public static void main(String[] args) {
		ODB odb = ODBFactory.open("lotr.db");
		//CREACION DE INDICES
		creaIndex(odb,odb.getClassRepresentation(Realm.class),"idRealmIndex","id");
		creaIndex(odb,odb.getClassRepresentation(Book.class),"idBookIndex","id");
		creaIndex(odb,odb.getClassRepresentation(Character.class),"idCharacterIndex","id");
		creaIndex(odb,odb.getClassRepresentation(Chapter.class),"idChapterIndex","id");
		creaIndex(odb,odb.getClassRepresentation(Movie.class),"idMovieIndex","id");
		creaIndex(odb,odb.getClassRepresentation(Dialog.class),"idDialogIndex","id");
		creaIndex(odb,odb.getClassRepresentation(Realm.class),"nameRealmIndex","name");
		
		odb.close();

		//CREACION DE OBJETOS
		//OBJETO CON ID NUEVO
		guardaObjeto(new Character("1", null, null, null, null, null, null, null, null, null, null));
		//OBJETO CON ID YA NUEVO
		guardaObjeto(new Movie("1", null, 0, 0, 0, 0, 0, 0, null));
		//OBJETO CON ID YA EXISTENTE
		guardaObjeto(new Movie("1", null, 0, 0, 0, 0, 0, 0, null));
		//OBJETO CON ID Y NOMBRE YA EXISTENTE
		guardaObjeto(new Realm(5000015, "Dale,Laketown", 0, 0));
		//OBJETO CON ID NUEVO Y NOMBRE YA EXISTENTE
		guardaObjeto(new Realm(1, "Dale,Laketown", 0, 0));
		//OBJETO CON ID YA EXISTENTE Y NOMBRE NUEVO
		guardaObjeto(new Realm(5000015, "Mi casiiiita", 0, 0));
		//OBJETO CON ID NUEVO Y NOMBRE NUEVO
		guardaObjeto(new Realm(14245, "Mi casaVoladora", 0, 0));
	}

	public static void creaIndex(ODB odb, ClassRepresentation classRepresentation, String nombreIndice, String campo) {
		if(!classRepresentation.existIndex(nombreIndice)) {
			String [] fieldIndex = {campo};
			classRepresentation.addUniqueIndexOn(nombreIndice, fieldIndex, true);
			for(String mensaje : classRepresentation.getIndexDescriptions()) {
				System.out.println(mensaje);
			}
			System.out.println();
		} else {
			System.out.println("El indice "+ nombreIndice + " ya existe");
		}		
	}
	public static void guardaObjeto(Object objeto) {
		ODB odb = ODBFactory.open("lotr.db");
		try {
			odb.store(objeto);
			odb.commit();
			System.out.println("Se ha creado el objeto con exito");
		} catch (ODBRuntimeException e) {
			System.out.println("El objeto ya existe "+ e.getLocalizedMessage());
		}
		odb.close();
	
	}
	

}

