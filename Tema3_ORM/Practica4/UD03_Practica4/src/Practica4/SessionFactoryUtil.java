package Practica4;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class SessionFactoryUtil {
	public static void main(String[] args) {
		Configuration cfg = new Configuration().configure();
		SessionFactory sesionFactoria = cfg.buildSessionFactory(
				new StandardServiceRegistryBuilder().configure().build());
		Session sesion = sesionFactoria.openSession();
		Ejercicio1(sesion);
		Ejercicio2(sesion);
		Ejercicio3(sesion);
		Ejercicio4_1(sesion);
		Ejercicio4_2(sesion);
		sesion.close();

	}
	static void Ejercicio1(Session s) {
		Query q = s.createQuery("from Person where lastName = 'Potter'");
		List<Person> persona1 =  q.getResultList();
		System.out.println("Ejercicio 1: ");
		for(Person p: persona1) System.out.println(p.getFirstName()+ " "+ p.getLastName()+ " - "+ p.getHouse().getName());
	}

	static void Ejercicio2(Session s) {
		System.out.println("\nEjercicio 2: ");
		ScrollableResults sc = s.createQuery("from Person AS p, Course AS c where p.firstName not in (select p.firstName from p.courses) group by p.id order by p.lastName ").scroll();
		while (sc.next()) {
			Person p = (Person) sc.get(0);
			Course c = (Course) sc.get(1);
			System.out.println(p.getLastName()+", "+p.getFirstName());
		}
		Long p1 = (Long)s.createQuery("Select count(*) from Person p join p.courses_1 c group by c.id").uniqueResult();
		System.out.println("Hay un total de "+ p1+" alumnos");

	}
	static void Ejercicio3(Session s) {
		System.out.println("\nEjercicio 3: ");
		List<HousePoints> persona1 = s.createQuery("from HousePoints h "
				+ "where h.personByReceiver.firstName in (:NameHarry, :NameRon, :NameHermione)")
				.setParameter("NameHarry", "Harry").setParameter("NameRon", "Ron")
				.setParameter("NameHermione", "Hermione")
				.getResultList(); 
		for(HousePoints p: persona1) System.out.println(p.getPersonByGiver().getFirstName()+ " -> "+ 
				p.getPoints()+ " puntos para " + p.getPersonByReceiver().getFirstName()  );
		Long p1 = (Long)s.createQuery("Select SUM(h.points) from HousePoints h join h.personByGiver g join h.personByReceiver r where r.firstName in ('Harry','Ron', 'Hermione')").uniqueResult();
		System.out.println("Hay un total de "+ p1+" puntos");

	}

	static void Ejercicio4_1(Session s) {
		System.out.println("\nEjercicio 4_1: ");
		ScrollableResults sc = s.createQuery("from Person p inner join p.courses_1 c inner join c.person p2  where c.name = 'Potions' and p.house.name = 'Gryffindor'").scroll();
		while (sc.next()) {
			Person p = (Person) sc.get(0);
			Course c = (Course) sc.get(1);
			Person p2 = (Person) sc.get(2);
			System.out.println(p.getFirstName()+" "+p.getLastName()+ " estudia "+ 
					c.getName()+ " con "+ p2.getFirstName()+ " "+ p2.getLastName());
		}
		Long p1 = (Long)s.createQuery("Select count(*) from Person p join p.courses_1 c where c.name ='Potions' and p.house.name = 'Gryffindor'").uniqueResult();
		System.out.println("Hay un total de "+ p1+" estudiantes");

	}
	static void Ejercicio4_2(Session s) {
		System.out.println("\nEjercicio 4_2: ");
		Query q = s.createQuery("from Person p join fetch p.courses_1 c join fetch c.person p2  where c.name = 'Potions' and p.house.name = 'Gryffindor'");
		List<Person> personas =  q.getResultList();
		for(Person p: personas)
			System.out.println(p.getFirstName()+" "+p.getLastName() +" estudia Potions con Severus Snape");

		Long p1 = (Long)s.createQuery("Select count(*) from Person p join p.courses_1 c where c.name ='Potions' and p.house.name = 'Gryffindor'").uniqueResult();
		System.out.println("Hay un total de "+ p1+" estudiantes");

	}

}
