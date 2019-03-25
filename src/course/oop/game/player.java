package course.oop.game;

public class player {

	private String name;
	private String marker;
	
	public player(String name, String marker) {
		this.name = name;
		this.marker = marker;
	}
	
	public String getPlayerName() {
		return name;
	}
	
	public String getPlayerMarker() {
		return marker;
	}
	
	public String toString() {
		return marker;
	}
}
