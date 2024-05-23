package com.standings.ui.page;

import java.util.ArrayList;
import java.util.List;

class Temporada {
    private String año;
    private List<Jornada> jornadas;
    private List<Equipo> equipos;

    public Temporada(String año) {
        this.año = año;
        this.jornadas = new ArrayList<>();
        this.equipos = new ArrayList<>();
    }

    public void setJornadas(List<Jornada> jornadas) {
        this.jornadas = jornadas;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    public String getAño() {
        return año;
    }

    public List<Jornada> getJornadas() {
        return jornadas;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }
}

class Jornada {
    private int numero;
    private List<Partido> partidos;

    public Jornada(int numero) {
        this.numero = numero;
        this.partidos = new ArrayList<>();
    }

    public void addPartido(Partido partido) {
        this.partidos.add(partido);
    }

    public int getNumero() {
        return numero;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }
}

class Partido {
    private String local;
    private String visitante;
    private int golesLocal;
    private int golesVisitante;

    public Partido(String local, String visitante, int golesLocal, int golesVisitante) {
        this.local = local;
        this.visitante = visitante;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
    }

    public String getLocal() {
        return local;
    }

    public String getVisitante() {
        return visitante;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }
}

class Equipo {
    private String nombre;
    private String escudoURL;
    private List<Jugador> jugadores;

    public Equipo(String nombre, String escudoURL) {
        this.nombre = nombre;
        this.escudoURL = escudoURL;
        this.jugadores = new ArrayList<>();
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEscudoURL() {
        return escudoURL;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }
}

class Jugador {
    private String nombre;
    private String apellido;
    private String rutaFoto;

    public Jugador(String nombre, String apellido, String rutaFoto) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rutaFoto = rutaFoto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }
}
