package com.standings.ui.page;

public class Player {
  private int numFicha;
  private String nomJug;
  private String apeJug;
  private int edad;
  private float peso;
  private float altura;
  private String dniJug;
  private byte[] foto;

  public Player() {
      // Constructor vacío
  }

  public int getNumFicha() {
      return numFicha;
  }

  public void setNumFicha(int numFicha) {
      this.numFicha = numFicha;
  }

  public String getNomJug() {
      return nomJug;
  }

  public void setNomJug(String nomJug) {
      this.nomJug = nomJug;
  }

  public String getApeJug() {
      return apeJug;
  }

  public void setApeJug(String apeJug) {
      this.apeJug = apeJug;
  }

  public int getEdad() {
      return edad;
  }

  public void setEdad(int edad) {
      this.edad = edad;
  }

  public float getPeso() {
      return peso;
  }

  public void setPeso(float peso) {
      this.peso = peso;
  }

  public float getAltura() {
      return altura;
  }

  public void setAltura(float altura) {
      this.altura = altura;
  }

  public String getDniJug() {
      return dniJug;
  }

  public void setDniJug(String dniJug) {
      this.dniJug = dniJug;
  }

  public byte[] getFoto() {
      return foto;
  }

  public void setFoto(byte[] foto) {
      this.foto = foto;
  }
}
