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
	PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer,Player> idMap;
	private Player best;
	private Simulatore sim;
	private Match match;
	
	private int goal1; //squadra di casa
	private int goal2;
	private int espulsi1;
	private int espulsi2;

	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<Integer,Player>();
		this.dao.listAllPlayers(idMap);
		this.sim=new Simulatore();
	}
	
	public void creaGrafo(Match m) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.match=m;
		
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(m, idMap));
		
		//aggiungo gli archi
		
		for(Adiacenza a : dao.getAdiacenze(m, idMap)) {
			if(a.getPeso() >= 0) {
				//p1 meglio di p2
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), 
							a.getP2(), a.getPeso());
				}
			} else {
				//p2 meglio di p1
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), 
							a.getP1(), (-1) * a.getPeso());
				}
			}
		}
	}
	
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Graph<Player,DefaultWeightedEdge> getGrafo() {
		return this.grafo;
	}
	
	public List<Match> getTuttiMatch(){
		List<Match> matches = dao.listAllMatches();
		Collections.sort(matches, new Comparator<Match>() {

			@Override
			public int compare(Match o1, Match o2) {
				return o1.getMatchID().compareTo(o2.matchID);
			}
			
		});
		return matches;
	}
	
	public Player getMigliore() {
		if(grafo == null) {
			return null;
		}
		
		best = null;
		Double maxDelta = (double) Integer.MIN_VALUE;
		
		for(Player p : this.grafo.vertexSet()) {
			// calcolo la somma dei pesi degli archi uscenti
			double pesoUscente = 0.0;
			for(DefaultWeightedEdge edge : this.grafo.outgoingEdgesOf(p)) {
				pesoUscente += this.grafo.getEdgeWeight(edge);
			}
			
			// calcolo la somma dei pesi degli archi entranti
			double pesoEntrante = 0.0;
			for(DefaultWeightedEdge edge : this.grafo.incomingEdgesOf(p)) {
				pesoEntrante += this.grafo.getEdgeWeight(edge);
			}
			
			double delta = pesoUscente - pesoEntrante;
			if(delta > maxDelta) {
				best = p;
				maxDelta = delta;
			}
		}
		
		return best;
		
	}
	
	
	public void simula(Integer N, Player best) {
		Team bestTeam = this.dao.getTeam(best);
		
		Integer[]results = sim.run(N, bestTeam, match);
		goal1=results[0]; //squadra di casa
		goal2=results[1];
		espulsi1=results[2];
		espulsi2=results[3];
		
	}

	public PremierLeagueDAO getDao() {
		return dao;
	}

	public void setDao(PremierLeagueDAO dao) {
		this.dao = dao;
	}

	public int getGoal1() {
		return goal1;
	}

	public void setGoal1(int goal1) {
		this.goal1 = goal1;
	}

	public int getGoal2() {
		return goal2;
	}

	public void setGoal2(int goal2) {
		this.goal2 = goal2;
	}

	public int getEspulsi1() {
		return espulsi1;
	}

	public void setEspulsi1(int espulsi1) {
		this.espulsi1 = espulsi1;
	}

	public int getEspulsi2() {
		return espulsi2;
	}

	public void setEspulsi2(int espulsi2) {
		this.espulsi2 = espulsi2;
	}

	public Map<Integer, Player> getIdMap() {
		return idMap;
	}

	public Player getBest() {
		return best;
	}

	public Simulatore getSim() {
		return sim;
	}

	public Match getMatch() {
		return match;
	}
	
	
	
}
