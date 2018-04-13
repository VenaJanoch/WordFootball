package com.example.venaj.slovnifotbal;

public class Hrac {

    private String jmeno;
    private boolean jineSlovo, pocitacuvTah, hraje;

    public Hrac(String jmeno){
        this.jmeno = jmeno;
        jineSlovo = true;
        pocitacuvTah = true;
        hraje = true;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public boolean isPocitacuvTah() {
        return pocitacuvTah;
    }

    public void setPocitacuvTah(boolean pocitacuvTah) {
        this.pocitacuvTah = pocitacuvTah;
    }

    public boolean isJineSlovo() {
        return jineSlovo;
    }

    public void setJineSlovo(boolean jineSlovo) {
        this.jineSlovo = jineSlovo;
    }

    public boolean isHraje() {
        return hraje;
    }

    public void setHraje(boolean hraje) {
        this.hraje = hraje;
    }
}
