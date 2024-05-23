package com.standings.ui.page;

import java.io.Serializable;
import java.util.List;

public class Round implements Serializable {
    private static final long serialVersionUID = 1L;

    private int roundNumber;
    private List<Match> matches;
    private String startDate; 

    public Round(int roundNumber, List<Match> matches, String startDate) {
        this.roundNumber = roundNumber;
        this.matches = matches;
        this.startDate = startDate;
    }

    // Getters y setters

		public int getRoundNumber() {
			return roundNumber;
		}

		public void setRoundNumber(int roundNumber) {
			this.roundNumber = roundNumber;
		}

		public List<Match> getMatches() {
			return matches;
		}

		public void setMatches(List<Match> matches) {
			this.matches = matches;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		
		@Override
    public String toString() {
        return "Round{" +
                "roundNumber=" + roundNumber +
                ", matches=" + matches +
                ", startDate='" + startDate + '\'' +
                '}';
    }
}
