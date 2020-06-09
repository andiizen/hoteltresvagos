package ar.com.ada.hoteltresvagos.managers;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import ar.com.ada.hoteltresvagos.entities.*;

public class ReservaManager {

    protected SessionFactory sessionFactory;

    public void setup() {

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures settings
                                                                                                  // from
                                                                                                  // hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception ex) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw ex;
        }

    }

    public void exit() {
        sessionFactory.close();
    }

    public void create(Reserva reserva) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(reserva);

        session.getTransaction().commit();
        session.close();
    }

    public Reserva read(int reservaId) {
        Session session = sessionFactory.openSession();

        Reserva reserva = session.get(Reserva.class, reservaId);

        session.close();

        return reserva;
    }

    // public Huesped readByDNI(int dni) {
    // Session session = sessionFactory.openSession();

    // Huesped huesped = session.byNaturalId(Huesped.class).using("dni",
    // dni).load();

    // session.close();

    // return huesped;
    // }

    public void update(Reserva reserva) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(reserva);

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Reserva reserva) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(reserva);

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Este metodo en la vida real no debe existir ya qeu puede haber miles de
     * usuarios
     * 
     * @return
     */
    public List<Reserva> buscarTodasReservas() {

        Session session = sessionFactory.openSession();

        /// NUNCA HARCODEAR SQLs nativos en la aplicacion.
        // ESTO es solo para nivel educativo
        Query query = session.createNativeQuery("SELECT * FROM reserva", Reserva.class);
        // query = session.createQuery("From Obse")
        List<Reserva> todas = query.getResultList();

        return todas;

    }
    //manager consultan a la base de datos y devuelve un objet. van metodos qu hagan los query
    //reportes, cuando mapeemos el cueri va a ser un objeto de reporte.
    //dentro de entities crear paquete reporte y subcarpetas reporte.
    /**
     * Busca una lista de huespedes por el nombre completo Esta armado para que se
     * pueda generar un SQL Injection y mostrar commo NO debe programarse.
     * 
     * @param nombre
     * @return
     */
    public List<Reserva> buscarPorNombreHuesped(String nombre) {

        Session session = sessionFactory.openSession();

        // SQL Injection vulnerability exposed.
        // Deberia traer solo aquella del nombre y con esto demostrarmos que trae todas
        // si pasamos
        // como nombre: "' or '1'='1"*
        // NUNCA HACERLO DE LA MANERA DE ABAJO
        // Query query = session.createNativeQuery
        // ("SELECT * FROM reserva r inner join huesped h on h.huesped_id = r.huesped_id
        // where nombre = '" + nombre + "'", Reserva.class);

        // Forma sql query nativa con parametros
        // Query queryForma1 = session.createNativeQuery(
        // "SELECT * FROM reserva r inner join huesped h on h.huesped_id = r.huesped_id
        // where nombre = ?", Reserva.class);
        // queryForma1.setParameter(1, nombre);

        // Forma query utilizando JPQL (a traves del lenguaje de java). select sobre los
        // objetos.
        Query queryForma2 = session.createQuery(
                "Select r from Reserva r where r.huesped.nombre like CONCAT('%',:nombre,'%')", Reserva.class);
        queryForma2.setParameter("nombre", nombre);

        List<Reserva> reservas = queryForma2.getResultList();

        return reservas;

    }

    // Armar un informe que devuelva un resumen por estado de reservas con sus
    // diferentes importes

    public List<Reserva> informeEstadoR() {

        Session session = sessionFactory.openSession();

        Query queryEstR = session.createQuery(
                "Select t.estado_descripcion sum(r.importe_reserva) total_reservas,sum(r.importe_total) importe_total, sum(r.importe_pagado) importes_entregados from tipo_estado_pago t inner join reserva r on r.estado_id = t.estado_id group by t.estado_descripcion",
                Reserva.class);

        List<Reserva> reservas = queryEstR.getResultList();

        return reservas;

    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
