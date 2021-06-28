package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private SimpleDirectedWeightedGraph< Player , DefaultWeightedEdge>graph;
	private Map <Integer,  Player > idMap;
	private PremierLeagueDAO dao;
	private Simulatore sim;
	private Match scelto;
	private Map <Integer,  Team > teams;
	private double maxDelta;
	
	public Model() {
		idMap= new HashMap <Integer, Player >();
		teams= new HashMap <Integer, Team >();
		dao=new PremierLeagueDAO();
	}
	
	public void creaGrafo(Match scelto) {
		graph= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.scelto=scelto;
		
		dao.loadAllVertici(idMap,scelto);
		Graphs.addAllVertices(graph, idMap.values());
		
		for(Player p: graph.vertexSet())
			setEfficienza(p);
		
		//ARCHI PER OGNI COPPIA DI VERTICI///////////////////////
		// P1 collegato a un P2 diverso con peso!=0 e di un team diverso
		for(Player p1: graph.vertexSet())
			for(Player p2: graph.vertexSet())
				if(p1.getPlayerID()>p2.getPlayerID() && p1.getEfficienza()-p2.getEfficienza()!=0.0 && p1.getTeamID()!=p2.getTeamID()) {
					if(p1.getEfficienza()>p2.getEfficienza()) {
						//double peso= (double)(t1.getPunti()-t2.getPunti());
						Graphs.addEdge (graph,p1,p2,p1.getEfficienza()-p2.getEfficienza());
					}else {
						Graphs.addEdge (graph,p2,p1,p2.getEfficienza()-p1.getEfficienza());
					}
				}
		
		//fai la mappa anche di squadre
		dao.loadAllTeams(teams);
		
	}
	
	
	public Player getTopPlayer() {
		double OUT=0.0;
		double IN=0.0;
		double delta=0.0;
		maxDelta=0.0;
		Player top=null;
		
		for(Player p:graph.vertexSet()) {
			for(DefaultWeightedEdge e: graph.outgoingEdgesOf(p))
				OUT+=graph.getEdgeWeight(e);
			for(DefaultWeightedEdge e: graph.incomingEdgesOf(p))
				IN+=graph.getEdgeWeight(e);
			delta=OUT-IN;
			if(delta>maxDelta) {
				maxDelta=delta;
				top=p;
			}
		}
		
		return top;
			
	}
	
	public double getDeltaTop() {
		return maxDelta;
	}
	
	public Integer getNVertici() {
		return graph.vertexSet().size();
	}
	
	public Integer getNArchi() {
		return graph.edgeSet().size();
	}
	

	public SimpleDirectedWeightedGraph< Player , DefaultWeightedEdge> getGraph() {
		return graph;
	}
	
	public List <Match> getMatches(){
		List <Match> matches = dao.listAllMatches();
		Collections.sort(matches);
		return matches;
	}
	
	
	public void setEfficienza(Player p) {
		double eff= dao.getEfficienza( p, scelto);
		p.setEfficienza(eff);
	}
	
	
	public void simula(Integer N) {
		sim=new Simulatore(graph,N,this.getTopPlayer(), scelto, teams);
		sim.run();
	}
	
	public String getResult() {
		return sim.getResult();
	}
	
	public String getEspulsi() {
		return sim.getEspulsi();
	}
}
