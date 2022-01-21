package lotr;

public class Chapter {
	private String id;
	private String chapter_name;
	private Book id_book;

	public Chapter(String id, String chapter_name, Book id_book) {
		super();
		this.id = id;
		this.chapter_name = chapter_name;
		this.id_book = id_book;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChapter_name() {
		return chapter_name;
	}
	public void setChapter_name(String chapter_name) {
		this.chapter_name = chapter_name;
	}
	public Book getId_book() {
		return id_book;
	}
	public void setId_book(Book id_book) {
		this.id_book = id_book;
	}


}
