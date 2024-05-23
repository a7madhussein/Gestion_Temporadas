package com.standings.ui.page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataExtractor {

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "");
	}

	public List<Temporada> getTemporadas() throws SQLException {
		List<Temporada> temporadas = new ArrayList<>();
		String query = "SELECT * FROM temporada WHERE Estado = 'Finalizado'";

		try (Connection connection = getConnection();
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				String año = rs.getString("Año");
				Temporada temporada = new Temporada(año);
				temporada.setJornadas(getJornadas(año));
				temporada.setEquipos(getEquipos(año));
				temporadas.add(temporada);
			}
		}
		return temporadas;
	}

	public List<Jornada> getJornadas(String año) throws SQLException {
		List<Jornada> jornadas = new ArrayList<>();
		String query = "SELECT * FROM jornada WHERE Año = ?";

		try (Connection connection = getConnection(); PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, año);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int numJor = rs.getInt("NumJor");
					Jornada jornada = new Jornada(numJor);
					jornada.addPartido(new Partido(rs.getString("Partido1Local"), rs.getString("Partido1Visitante"),
							rs.getInt("GolesLocalPartido1"), rs.getInt("GolesVisitantePartido1")));
					jornada.addPartido(new Partido(rs.getString("Partido2Local"), rs.getString("Partido2Visitante"),
							rs.getInt("GolesLocalPartido2"), rs.getInt("GolesVisitantePartido2")));
					jornada.addPartido(new Partido(rs.getString("Partido3Local"), rs.getString("Partido3Visitante"),
							rs.getInt("GolesLocalPartido3"), rs.getInt("GolesVisitantePartido3")));
					jornadas.add(jornada);
				}
			}
		}
		return jornadas;
	}

	public List<Equipo> getEquipos(String año) throws SQLException {
		List<Equipo> equipos = new ArrayList<>();
		String query = "SELECT DISTINCT NomEq, EscudoURL FROM jugador_equipo_historial WHERE Año = ?";

		try (Connection connection = getConnection(); PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, año);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					String nombre = rs.getString("NomEq");
					String escudoURL = rs.getString("EscudoURL");
					Equipo equipo = new Equipo(nombre, escudoURL);
					equipo.setJugadores(getJugadores(nombre, año));
					equipos.add(equipo);
				}
			}
		}
		return equipos;
	}

	public List<Jugador> getJugadores(String equipoNombre, String año) throws SQLException {
		List<Jugador> jugadores = new ArrayList<>();
		String query = "SELECT NomJug, ApeJug, ruta_foto FROM jugador_equipo_historial WHERE NomEq = ? AND Año = ?";

		try (Connection connection = getConnection(); PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, equipoNombre);
			pstmt.setString(2, año);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					String nombre = rs.getString("NomJug");
					String apellido = rs.getString("ApeJug");
					String rutaFoto = rs.getString("ruta_foto");
					jugadores.add(new Jugador(nombre, apellido, rutaFoto));
				}
			}
		}
		return jugadores;
	}
}
