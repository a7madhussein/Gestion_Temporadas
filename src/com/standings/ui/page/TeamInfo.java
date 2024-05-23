package com.standings.ui.page;

import java.util.ArrayList;
import java.util.List;

//Clase para almacenar información de un equipo
	public class TeamInfo {
	    private String teamName;
	    private String teamLogoUrl;
	    private List<PlayerInfo> playerNames;

	    public TeamInfo(String teamName, String teamLogoUrl) {
	        this.teamName = teamName;
	        this.teamLogoUrl = teamLogoUrl;
	        this.playerNames = new ArrayList<>();
	    }

	    public String getTeamName() {
	        return teamName;
	    }

	    public String getTeamLogoUrl() {
	        return teamLogoUrl;
	    }

	    public List<PlayerInfo> getPlayerNames() {
	        return playerNames;
	    }

	    public void setPlayerNames(List<PlayerInfo> playerNames) {
	        this.playerNames = playerNames;
	    }
	}