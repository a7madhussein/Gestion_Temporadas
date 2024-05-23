package com.standings.ui.page;

import java.io.Serializable;

public class MatchResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private int goalsHomeTeam;
	private int goalsAwayTeam;

	public MatchResult(int goalsHomeTeam, int goalsAwayTeam) {
		this.goalsHomeTeam = goalsHomeTeam;
		this.goalsAwayTeam = goalsAwayTeam;
	}

	// Getters y setters

	public int getGoalsHomeTeam() {
		return goalsHomeTeam;
	}

	public void setGoalsHomeTeam(int goalsHomeTeam) {
		this.goalsHomeTeam = goalsHomeTeam;
	}

	public int getGoalsAwayTeam() {
		return goalsAwayTeam;
	}

	public void setGoalsAwayTeam(int goalsAwayTeam) {
		this.goalsAwayTeam = goalsAwayTeam;
	}

	@Override
	public String toString() {
		return "MatchResult{" + "goalsHomeTeam=" + goalsHomeTeam + ", goalsAwayTeam=" + goalsAwayTeam + '}';
	}
	public MatchResult() { 
}

}