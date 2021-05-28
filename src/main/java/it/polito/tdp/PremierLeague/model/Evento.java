package it.polito.tdp.PremierLeague.model;

import java.time.LocalTime;

public class Evento {
	
	public enum EventType{
		
		GOAL,
		ESPULSIONE,
		INFORTUNIO
	}
	
	private LocalTime time;
	private EventType type;
	
	public int compareTo(Evento other) {
		return this.time.compareTo(other.time);
	}

	public Evento(EventType type) {
		super();
		//this.time = time; --> non so come associarlo a numero di azioni
		this.type = type;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}
	
	public String toString() {
		return this.type.toString();
	}
	
	

}
