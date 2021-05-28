package it.polito.tdp.PremierLeague.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.PremierLeague.model.Evento.EventType;
import javafx.event.Event;

public class Simulatore {

	//private PriorityQueue <Evento> queue;
	private List <Evento> queue;
	
	//VARIABILI
	private int N;
	private double probability;
	private Team bestTeam;
	private Match match;
	
	//OUTPUT
	private int goal1; //squadra di casa
	private int goal2;
	private int espulsi1;
	private int espulsi2;
	
	//SIMULAZIONE
	public Integer[] run(int N, Team bestTeam, Match match) {
		this.N=N; //numero di azioni che generano eventi 
		//this.queue= new PriorityQueue<Evento>();
		this.queue=new ArrayList <Evento>();
		goal1=0;
		goal2=0;
		espulsi1=0;
		espulsi2=0;
		this.bestTeam=bestTeam;
		this.match=match;
		
		for(int i=0;i<N;i++) {
			this.addEvento(); //inserisco le N azioni salienti in coda
		}//for
		
		//while(!this.queue.isEmpty()) {
			//Evento e = this.queue.poll();
		int i=0;
		while(queue.get(i)!=null) {
			Evento e = queue.get(i);
			processEvent (e);
			i++;
		}
		
		Integer[] results = new Integer[4];
		results[0] = goal1;
		results[1] = goal2;
		results[2] = espulsi1;
		results[3] = espulsi2;
		return results;
	}
	
	/**
	 * Inserisce AZIONI SALIENTI che generano eventi
	 */
	public void addEvento () {
		probability = Math.random();
		if(probability<=0.5)
			queue.add(new Evento(EventType.GOAL));
		else if(probability>0.5 && probability<=0.8)
			queue.add(new Evento(EventType.ESPULSIONE));
		else
			queue.add(new Evento(EventType.INFORTUNIO));
		System.out.println(queue.get(queue.size()-1));
	}

	
	private void processEvent(Evento e) {
		switch (e.getType()) {
			case GOAL:
				//stesso n°players --> goal a squadra con best
				if(espulsi1== espulsi2) {
					if(bestTeam.getTeamID()==match.getTeamHomeID())
						goal1++;
					else
						goal2++;
				} else { //goal al team con meno espulsi
					if(espulsi1>espulsi2)
						goal2++;
					else
						goal1++;
				}
				break;
				
			case ESPULSIONE:
				double prob = Math.random();
				if (prob<=0.6) { //coinvolge teamBest
					if(bestTeam.getTeamID()==match.getTeamHomeID())
						espulsi1++;
					else
						espulsi2++;
				} else { //coinvolge squadra senza best
					if(bestTeam.getTeamID()==match.getTeamHomeID())
						espulsi2++;
					else
						espulsi1++;
				}
				break;
				
			case INFORTUNIO:
				double prob2 = Math.random();
				if(prob2<=0.5) {
					this.addEvento();
					this.addEvento();
					N+=2;
				} else {
					this.addEvento();
					this.addEvento();
					this.addEvento();
					N+=3;
				}
						
				
		}//switch
		
	}
	
}
