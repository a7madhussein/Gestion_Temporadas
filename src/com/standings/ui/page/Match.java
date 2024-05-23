package com.standings.ui.page;

import java.io.Serializable;

public class Match implements Serializable {

	private static final long serialVersionUID = 1L;
	private Team homeTeam;
	private Team awayTeam;
	private int roundNumber;
	private int matchNumber;

	@SuppressWarnings("unused")
	private boolean updated = false;
	private MatchResult result;
	private boolean resultsUpdated;
	private boolean isUpdated;

	public Match(Team homeTeam, Team awayTeam, int roundNumber, int matchNumber) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.roundNumber = roundNumber;
		this.matchNumber = matchNumber;
		this.resultsUpdated = false;
		// Inicializar el resultado al crear el objeto Match
		this.result = new MatchResult(0, 0);
	}

	public boolean isResultsUpdated() {
		return resultsUpdated;
	}

	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	public boolean isUpdated() {
		return this.isUpdated;
	}

	public void resetUpdated() {
		updated = false;
	}

	public void setResultsUpdated(boolean resultsUpdated) {
		this.resultsUpdated = resultsUpdated;
	}

	public void resetStatistics() {
		this.result = null;
		this.updated = false;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public Team getAwayTeam() {
		return awayTeam;
	}

	public int getRoundNumber() {
		return roundNumber;
	}

	public int getMatchNumber() {
		return matchNumber;
	}

	public MatchResult getResult() {
		return result;
	}

	public void setResult(MatchResult result) {
		this.result = result;
	}

}
