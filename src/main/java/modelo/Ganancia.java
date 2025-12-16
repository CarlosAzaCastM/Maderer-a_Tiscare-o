package modelo;

import java.util.Date;

public class Ganancia {
    private Date fechaInicio;
    private Date fechaFin;
    private double gananciaBruta;
    private double totalGastos;
    private double gananciaNeta;
    private int idUsuario;

    public Ganancia() {
    }

    // Getters y Setters
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getGananciaBruta() {
        return gananciaBruta;
    }

    public void setGananciaBruta(double gananciaBruta) {
        this.gananciaBruta = gananciaBruta;
    }

    public double getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(double totalGastos) {
        this.totalGastos = totalGastos;
    }

    public double getGananciaNeta() {
        return gananciaNeta;
    }

    public void setGananciaNeta(double gananciaNeta) {
        this.gananciaNeta = gananciaNeta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    // Método para calcular ganancia neta
    public void calcularGananciaNeta() {
        this.gananciaNeta = this.gananciaBruta - this.totalGastos;
    }
}