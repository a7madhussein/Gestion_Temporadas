package com.standings.ui.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Season implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Team> teams;
    private List<Match> matches;

    public Season(List<String> teamNames) {
        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();

        for (String teamName : teamNames) {
            Team team = new Team(teamName);
            teams.add(team);
        }

        generateMatches();
    }
    
    public List<String> getTeamNames() {
      List<String> teamNames = new ArrayList<>();
      for (Team team : teams) {
          teamNames.add(team.getName());
      }
      return teamNames;
  }

    public Season(List<String> teamNames, int year) {
      this.teams = new ArrayList<>();
      this.matches = new ArrayList<>();

      for (String teamName : teamNames) {
          Team team = new Team(teamName);
          teams.add(team);
      }

      generateMatches();
      // Puedes hacer algo con el año si es necesario
  }
    public List<Team> getTeams() {
        return teams;
    }

    public List<Match> getMatches() {
        return matches;
    }

    private void generateMatches() {
      // Genera partidos para las 10 jornadas con equipos aleatorios
      for (int roundNumber = 1; roundNumber <= 10; roundNumber++) {
          List<Team> shuffledTeams = new ArrayList<>(teams);
          // Baraja los equipos para que los partidos sean aleatorios
          java.util.Collections.shuffle(shuffledTeams);

          for (int matchNumber = 1; matchNumber <= 3; matchNumber++) {
              Team homeTeam = shuffledTeams.get((matchNumber - 1) * 2);
              Team awayTeam = shuffledTeams.get((matchNumber - 1) * 2 + 1);
              matches.add(new Match(homeTeam, awayTeam, roundNumber, matchNumber));
          }
      }
  }

}
