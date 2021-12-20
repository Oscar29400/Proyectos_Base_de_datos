package Practica4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class Ejercicio5 {
	public static void main(String[] args) {
		Ejercicio1();
		Ejercicio1_2();
		Ejercicio2();
		Ejercicio3();
		Ejercicio4();
	}
	static void Ejercicio1() {
		Configuration cfg = new Configuration().configure();
		SessionFactory sesionFactoria = cfg.buildSessionFactory(
				new StandardServiceRegistryBuilder().configure().build());
		Session sesion = sesionFactoria.openSession();
		Transaction tx = null;
		try {
			tx = sesion.beginTransaction();
			System.out.println("\n Ejercicio 1:");
			String drop = " DROP TABLE IF EXISTS Books ";
			sesion.createSQLQuery(drop).executeUpdate();
			String sql = "CREATE TABLE Books("
					+ "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
					+ "Title varchar(100),"
					+ "Year Integer,"
					+ "Subject Integer REFERENCES course(id))";
			sesion.createSQLQuery(sql).executeUpdate();	   
			tx.commit();
			sesion.close();
			System.out.println("TABLA BOOKS CREADA");

		} catch (RuntimeException e) {
			if (tx != null) tx.rollback();
			throw e; 
		}
	}
	static void Ejercicio1_2() {
		Configuration cfg = new Configuration().configure();
		SessionFactory sesionFactoria = cfg.buildSessionFactory(
				new StandardServiceRegistryBuilder().configure().build());
		
		String csvFile = "src/res/Harry_Potter_libros.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		try {
			br = new BufferedReader(new FileReader(csvFile));
			br.readLine();
			while ((line = br.readLine()) != null) {    
				Session sesion = sesionFactoria.openSession();
				Transaction tx2 = null;
				tx2 = sesion.beginTransaction();
				String[] datos = line.split(cvsSplitBy);
				Books b = new Books();
				b.setTitle(datos[0]);
				b.setYear(Integer.parseInt(datos[1]));
				Integer p1 = (Integer)sesion.createQuery("Select id from Course where name = '"+datos[2]+"'").uniqueResult();
				b.setSubject(p1);
				sesion.save(b);
				tx2.commit();
				sesion.close();
			}
			System.out.println("\n Ejercicio 1_2");
			System.out.println("TABLA RELLENADA");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	static void Ejercicio2() {
		Configuration cfg = new Configuration().configure();
		SessionFactory sesionFactoria = cfg.buildSessionFactory(
				new StandardServiceRegistryBuilder().configure().build());
		Session sesion = sesionFactoria.openSession();
		Transaction tx = null;
		tx = sesion.beginTransaction();
		System.out.println("\n Ejercicio 2");
		Long p1 = (Long)sesion.createQuery("SELECT SUM(h.points) FROM HousePoints  h where h.personByReceiver.firstName in ('Harry', 'Ron') and h.personByGiver.firstName = 'Severus'").uniqueResult();
		System.out.println("Puntos quitados a H y R por SS: "+ p1);
		Integer p2 = (Integer)sesion.createQuery("SELECT h.points FROM HousePoints  h where h.personByReceiver.firstName = 'Harry' and h.personByGiver.firstName = 'Severus'").uniqueResult();
		Integer p3 = (Integer)sesion.createQuery("SELECT h.points FROM HousePoints  h where h.personByReceiver.firstName = 'Ron' and h.personByGiver.firstName = 'Severus'").uniqueResult();

		HousePoints hp = sesion.get(HousePoints.class, 1);
		HousePoints hp2 = sesion.get(HousePoints.class, 2);
		hp.setPoints(p2*2);
		hp2.setPoints(p3*2);
		sesion.update(hp);
		sesion.update(hp2);
		Long p4 = (Long)sesion.createQuery("SELECT SUM(h.points) FROM HousePoints  h where h.personByReceiver.firstName in ('Harry', 'Ron') and h.personByGiver.firstName = 'Severus'").uniqueResult();
		System.out.println("Puntos quitados a H y R por SS: "+ p4);

		tx.commit();
		sesion.close();
		
	}
	
	static void Ejercicio3() {
		Configuration cfg = new Configuration().configure();
		SessionFactory sesionFactoria = cfg.buildSessionFactory(
				new StandardServiceRegistryBuilder().configure().build());
		Session sesion = sesionFactoria.openSession();
		Transaction tx = null;
		tx = sesion.beginTransaction();
		System.out.println("\n Ejercicio 3");
		Long p1 = (Long)sesion.createQuery("SELECT COUNT(*) from Person p join p.courses_1 c where c.name like '%Muggle%'").uniqueResult();
		System.out.println("Hay "+p1+" alumnos que estudian Muggle Studies");
		Long p2 = (Long)sesion.createQuery("SELECT COUNT(*) from Person p join p.courses_1 c where c.name = 'Potions'").uniqueResult();
		Course c = new Course();
		c.setId(8);
		Person p;
		for (int i = 1; i <= p2 ; i++) {
			p = (Person)sesion.createQuery("from Person where id ="+i).uniqueResult();
			Set<Course> cursos = (p.getCourses_1());
			cursos.add(c);
			sesion.save(p);
		}
		tx.commit();
		Long p3 = (Long)sesion.createQuery("SELECT COUNT(*) from Person p join p.courses_1 c where c.name like '%Muggle%'").uniqueResult();
		System.out.println("Hay "+p3+" alumnos que estudian Muggle Studies");
		sesion.close();							
	}
	
	static void Ejercicio4() {
		Configuration cfg = new Configuration().configure();
		SessionFactory sesionFactoria = cfg.buildSessionFactory(
				new StandardServiceRegistryBuilder().configure().build());
		Session sesion = sesionFactoria.openSession();
		Transaction tx = null;
		tx = sesion.beginTransaction();
		System.out.println("\n Ejercicio 4");
		Long p1 = (Long)sesion.createQuery("SELECT COUNT(*) from Person p join p.courses_1 c where c.name like '%Flying%'").uniqueResult();
		System.out.println("Hay "+p1+" alumnos que estudian Flying");
		Long p2 = (Long)sesion.createQuery("SELECT COUNT(*) from Person p join p.courses_1 c where c.name = 'Transfiguration'").uniqueResult();
		Course c = new Course();
		c.setId(7);
		Person p;
		for (int i = 1; i <= p2 ; i++) {
			p = (Person)sesion.createQuery("from Person where id ="+i).uniqueResult();
			Set<Course> cursos = (p.getCourses_1());
			cursos.add(c);
			sesion.save(p);
		}
		tx.commit();
		Long p3 = (Long)sesion.createQuery("SELECT COUNT(*) from Person p join p.courses_1 c where c.name like '%Flying%'").uniqueResult();
		System.out.println("Hay "+p3+" alumnos que estudian Flying");
		sesion.close();							
	}

}
