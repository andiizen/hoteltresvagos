package ar.com.ada.hoteltresvagos.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;


@Entity //crear objetos que van a actuar como entidad. No van a terminar siempre siendo una tabla en la base de datos.
@Table(name = "reserva") // define que es dentro de la base de datos.
public class Reserva {
    
    //cual es el campo que va a ser el identificador unico? = y va a ser una columna. Van justo arriba del atributo las anotaciones.
    @Id
    @Column(name= "reserva_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservaId;
    @Column(name = "fecha_reserva")
    private Date fechaReserva;
    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;
    @Column(name = "fecha_egreso")
    private Date fechaEgreso;
    private Integer habitacion; //no le pongo anotacion porque se llaman igual en la db.
    @Column(name = "importe_reserva")
    private BigDecimal importeReserva;
    @Column(name = "importe_total")
    private BigDecimal importeTotal;
    @Column(name = "importe_pagado")
    private BigDecimal importePagado;
    @Column(name = "estado_id")
    private int estadoId; //Por ahora vamos a crear este como int. tipoEstadoPago.
    @ManyToOne //muchas reservas que pueden apuntar a un mismo huesped.
    @JoinColumn(name = "huesped_id", referencedColumnName = "huesped_id") //foring key. clave foranea
    private Huesped huesped;

    public int getReservaId() {
        return reservaId;
    }

    public void setReservaId(int reservaId) {
        this.reservaId = reservaId;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(Date fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }

    public Integer getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Integer habitacion) {
        this.habitacion = habitacion;
    }

    public BigDecimal getImporteReserva() {
        return importeReserva;
    }

    public void setImporteReserva(BigDecimal importeReserva) {
        this.importeReserva = importeReserva;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public BigDecimal getImportePagado() {
        return importePagado;
    }

    public void setImportePagado(BigDecimal importePagado) {
        this.importePagado = importePagado;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public void setHuesped(Huesped huesped) {
        this.huesped = huesped;
        this.huesped.getReservas().add(this); //aca recien se hace la relacion bidireccional, agregando.
    }

    
    

}