package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable <Event>{
	
	public enum EventType{
		GOAL,
		ESPULSIONE,
		INFORTUNIO
	}
	
	private Integer id;
	private EventType type;
	
	@Override
	public int compareTo(Event other) {
		return this.id-other.id;
	}

	public Event(Integer id, EventType type) {
		super();
		this.id = id;
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}
	
	
}
