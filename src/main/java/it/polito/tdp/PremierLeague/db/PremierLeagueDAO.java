package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	
	
	
	public List<Team> listAllTeams(){
		String sql = "SELECT * FROM Teams";
		List<Team> result = new ArrayList<Team>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				result.add(team);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public double getEfficienza(Player p,Match m){
		String sql = "SELECT ((`TotalSuccessfulPassesAll`+assists)/timePlayed) as eff "
				+ "from Actions "
				+ "where PlayerID=? and MatchID=?";
		double efficienza=0.0;
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, p.getPlayerID());
			st.setInt(2, m.getMatchID());
			ResultSet res = st.executeQuery();
			if (res.first()) {
				
				efficienza=res.getDouble("eff");
				
				
			}
			conn.close();
			return efficienza;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	public void loadAllVertici(Map <Integer, Player> idMap, Match scelto){
		
		
		String sql = "SELECT DISTINCT p.PlayerID as id, p.Name as nome, a.TeamID as team "
				+ "from Players as p, Actions as a "
				+ "where p.PlayerID=a.PlayerID and a.MatchID=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, scelto.getMatchID());
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {
					Player player = new Player(res.getInt("id"), res.getString("nome"), res.getInt("team"));
					idMap.put(res.getInt("id"),player );
					System.out.println(player);
				}//if
				
			}//while
			
			conn.close();
			return  ;

		} catch (SQLException e) {
			e.printStackTrace();
			return  ;
		}

	}


public void loadAllTeams(Map <Integer, Team> teams){
		
		
		String sql = "SELECT * from Teams" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(!teams.containsKey(res.getInt("TeamID"))) {
					Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
					teams.put(res.getInt("TeamID"),team );
					//System.out.println();
				}//if
				
			}//while
			
			conn.close();
			return  ;

		} catch (SQLException e) {
			e.printStackTrace();
			return  ;
		}

	}


}
	
	
	
	
	
	
	
	
	

