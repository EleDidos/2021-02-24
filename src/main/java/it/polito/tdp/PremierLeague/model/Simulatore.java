package it.polito.tdp.PremierLeague.model;

import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.model.Event.EventType;

public class Simulatore { 
	
	private PriorityQueue<Event> queue;
	private SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>graph;
	private Player top;
	private Team topTeam;
	private Integer N; //azioni
	private Match match;
	private Team t1;
	private Team t2;
	private Map <Integer,  Team > teams;
	private int goal1;
	private int goal2;
	private int espulsi1;
	private int espulsi2;
	private int numeroEventi;
	
	public Simulatore( SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>graph, Integer N, Player top, Match match,Map <Integer,  Team > teams) {
		queue = new PriorityQueue<Event> ();
		this.graph=graph;
		this.match=match;
		this.top=top;
		this.teams=teams;
		this.N=N;
		goal1=0;
		goal2=0;
		espulsi1=0;
		espulsi2=0;
		numeroEventi=0;
		
		t1= teams.get(match.getTeamHomeID()) ;
		t2= teams.get(match.getTeamAwayID());
		
		if(top.getTeamID()==t1.getTeamID())
			topTeam=t1;
		else
			topTeam=t2;
		
		//creo N azioni salienti
		for(int i=0;i<N;i++)
			this.creaEvento(i);;
	
		
	}
		
	
	public void creaEvento(Integer id) {
		double prob=Math.random();
		Event e;
		if(prob<=0.5)
			e = new Event(id,EventType.GOAL);
		else if(prob>0.5 & prob<=0.8)
			e= new Event(id,EventType.ESPULSIONE);
		else
			e = new Event(id,EventType.INFORTUNIO);
		
		queue.add(e);
		numeroEventi++;
	}
	
	
	public void run() {
		while(!queue.isEmpty()) {
			Event e = this.queue.poll();
			processEvent(e);
		}//while
	}
	
	
	private void processEvent(Event e) {
		switch(e.getType()) {
				
			case GOAL:
				
				if(t1.getNGiocatori()>t2.getNGiocatori()) {
					goal1++;
					
				}else if(t2.getNGiocatori()>t1.getNGiocatori()) {
					goal2++;
					
				}else { //goal alla squadra del top
					if(t1.equals(topTeam))
						goal1++;
					else
						goal2++;
				}
				
				
				break;
				
				
			case ESPULSIONE: 
				
				double prob2=Math.random();
				
				if(prob2<=0.6) {
					//espello da topTeam
					if(t1.equals(topTeam)) {
						espulsi1++;
						t1.minusGiocatore();
					}else {
						espulsi2++;
						t2.minusGiocatore();
					}
				}
				else { //espello dall'altra squadra
					if(t1.equals(topTeam)) {
						espulsi2++;
						t2.minusGiocatore();
					}else {
						espulsi1++;
						t1.minusGiocatore();
					}
					
				}
				break;
				
				
			case INFORTUNIO: //aggiungo azioni 
				double prob3= Math.random();
				if(prob3<=0.5) {
					this.creaEvento(this.numeroEventi+1); //indice della nuova azione = l'ultimo+1
					this.creaEvento(this.numeroEventi+1);
				}else {
					this.creaEvento(this.numeroEventi+1);
					this.creaEvento(this.numeroEventi+1);
					this.creaEvento(this.numeroEventi+1);
				}
				break;
				
			default:
				break;
		}
	}
	
	public String getResult() {
		return goal1+" - "+goal2;
	}
	
	public String getEspulsi() {
		return "Espulsi dalla squadra 1: "+espulsi1+"\nEspulsi dalla squadra 2: "+espulsi2;
	}
	

}
