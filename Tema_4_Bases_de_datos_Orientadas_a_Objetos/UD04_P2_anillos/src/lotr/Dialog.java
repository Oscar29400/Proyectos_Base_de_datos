package lotr;

public class Dialog {
	private String id;
	private String dialog;
	private Movie id_movie;
	private Character id_character;
	public Dialog(String id, String dialog, Movie id_movie, Character id_character) {
		super();
		this.id = id;
		this.dialog = dialog;
		this.id_movie = id_movie;
		this.id_character = id_character;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDialog() {
		return dialog;
	}
	public void setDialog(String dialog) {
		this.dialog = dialog;
	}
	public Movie getId_movie() {
		return id_movie;
	}
	public void setId_movie(Movie id_movie) {
		this.id_movie = id_movie;
	}
	public Character getId_character() {
		return id_character;
	}
	public void setId_character(Character id_character) {
		this.id_character = id_character;
	}
	
	
}
