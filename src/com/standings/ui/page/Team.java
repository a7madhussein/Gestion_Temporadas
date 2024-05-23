package com.standings.ui.page;

import java.io.Serializable;

public class Team implements Serializable, Comparable<Team> {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private String name;
	private int points;
	private int goalsScored;
	private int goalsConceded;
	private int matchesPlayed;
	private int wins;
	private int draws;
	private int losses;

	public Team(String name) {
		this.name = name;
		this.points = 0;
		this.goalsScored = 0;
		this.goalsConceded = 0;
		this.matchesPlayed = 0;
		this.wins = 0;
		this.draws = 0;
		this.losses = 0;
	}

	public void resetStatistics() {
		// Solo reiniciar estadísticas si es la primera vez
		if (matchesPlayed == 0) {
			this.points = 0;
			this.matchesPlayed = 0;
			this.wins = 0;
			this.draws = 0;
			this.losses = 0;
			this.goalsScored = 0;
			this.goalsConceded = 0;
		}
	}

	public String getName() {
		return name;
	}

	public int getPoints() {
		return points;
	}

	public int getGoalsScored() {
		return goalsScored;
	}

	public int getGoalsConceded() {
		return goalsConceded;
	}

	public int getMatchesPlayed() {
		return matchesPlayed;
	}

	public int getWins() {
		return wins;
	}

	public int getDraws() {
		return draws;
	}

	public int getLosses() {
		return losses;
	}

	public int getGoalDifference() {
		return goalsScored - goalsConceded;
	}

	public void updateStatistics(int goalsScored, int goalsConceded, boolean isUpdated, int oldGoalsScored,
			int oldGoalsConceded) {
		// Restar las estadísticas anteriores si el partido ya ha sido actualizado
		if (isUpdated) {
			this.goalsScored -= oldGoalsScored;
			this.goalsConceded -= oldGoalsConceded;

			if (oldGoalsScored > oldGoalsConceded) {
				points -= 3;
				wins--;
			} else if (oldGoalsScored == oldGoalsConceded) {
				points -= 1;
				draws--;
			} else {
				losses--;
			}
		}

		// Agregar las nuevas estadísticas
		this.goalsScored += goalsScored;
		this.goalsConceded += goalsConceded;

		// Incrementar matchesPlayed si es la primera vez que se actualiza el partido
		if (!isUpdated) {
			this.matchesPlayed++;
		}

		if (goalsScored > goalsConceded) {
			points += 3;
			wins++;
		} else if (goalsScored == goalsConceded) {
			points += 1;
			draws++;
		} else {
			losses++;
		}
	}

	public void incrementMatchesPlayed() {
		matchesPlayed++;
	}

	@Override
	public int compareTo(Team other) {
		// Compare based on points, goal difference, and then goals scored
		if (this.points != other.points) {
			return Integer.compare(other.points, this.points); // Higher points first
		} else {
			int thisGoalDifference = this.goalsScored - this.goalsConceded;
			int otherGoalDifference = other.goalsScored - other.goalsConceded;

			if (thisGoalDifference != otherGoalDifference) {
				return Integer.compare(otherGoalDifference, thisGoalDifference); // Higher goal difference first
			} else {
				// If points and goal difference are equal, compare based on goals scored
				return Integer.compare(other.goalsScored, this.goalsScored); // Higher goals scored first
			}
		}
	}
}
