package com.standings.ui.page;

//Clase para almacenar información de un jugador
	public class PlayerInfo {
	    private String playerName;
	    private String playerPhotoUrl;

	    public PlayerInfo(String playerName, String playerPhotoUrl) {
	        this.playerName = playerName;
	        this.playerPhotoUrl = playerPhotoUrl;
	    }

	    public String getPlayerName() {
	        return playerName;
	    }

	    public String getPlayerPhotoUrl() {
	        return playerPhotoUrl;
	    }
	}
