package practica2;
// Generated 26 nov. 2021 14:55:04 by Hibernate Tools 5.5.7.Final

/**
 * HousePoints generated by hbm2java
 */
public class HousePoints implements java.io.Serializable {

	private Integer id;
	private Person personByGiver;
	private Person personByReceiver;
	private Integer points;

	public HousePoints() {
	}

	public HousePoints(Person personByGiver, Person personByReceiver, Integer points) {
		this.personByGiver = personByGiver;
		this.personByReceiver = personByReceiver;
		this.points = points;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Person getPersonByGiver() {
		return this.personByGiver;
	}

	public void setPersonByGiver(Person personByGiver) {
		this.personByGiver = personByGiver;
	}

	public Person getPersonByReceiver() {
		return this.personByReceiver;
	}

	public void setPersonByReceiver(Person personByReceiver) {
		this.personByReceiver = personByReceiver;
	}

	public Integer getPoints() {
		return this.points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

}
