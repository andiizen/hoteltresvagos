package ar.com.ada.hoteltresvagos;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.exception.ConstraintViolationException;

import ar.com.ada.hoteltresvagos.entities.*;
import ar.com.ada.hoteltresvagos.excepciones.*;
import ar.com.ada.hoteltresvagos.managers.*;

public class ABM {

    public static Scanner Teclado = new Scanner(System.in);

    protected HuespedManager ABMHuesped = new HuespedManager();
    protected ReservaManager ABMReserva = new ReservaManager();

    public void iniciar() throws Exception {

        try {

            ABMHuesped.setup();
            ABMReserva.setup();

            printOpciones();

            int opcion = Teclado.nextInt();
            Teclado.nextLine();

            while (opcion > 0) {

                switch (opcion) {
                    case 1:

                        try {
                            alta();
                        } catch (HuespedDNIException exdni) {
                            System.out.println("Error en el DNI. Indique uno valido");
                        }
                        break;

                    case 2:
                        baja();
                        break;

                    case 3:
                        modifica();
                        break;

                    case 4:
                        listar();
                        break;

                    case 5:
                        listarReserva();
                        break;
                    
                    case 6:
                        listarPorNombre();
                        break;

                    case 7:
                        listarPorNombreR();
                        break;
                    
                    case 8: 
                        mostrarImportesEstRes();
                
                    default:
                        System.out.println("La opcion no es correcta.");
                        break;
                }

                printOpciones();

                opcion = Teclado.nextInt();
                Teclado.nextLine();
            }

            // Hago un safe exit del manager
            ABMHuesped.exit();

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Que lindo mi sistema,se rompio mi sistema");
            throw e;
        } finally {
            System.out.println("Saliendo del sistema, bye bye...");

        }

    }

    public void alta() throws Exception {
        Huesped huesped = new Huesped();
        System.out.println("Ingrese el nombre:");
        huesped.setNombre(Teclado.nextLine());
        System.out.println("Ingrese el DNI:");
        huesped.setDni(Teclado.nextInt());
        Teclado.nextLine();
        System.out.println("Ingrese el domicilio:");
        huesped.setDomicilio(Teclado.nextLine());

        System.out.println("Ingrese el Domicilio alternativo(OPCIONAL):");

        String domAlternativo = Teclado.nextLine();

        if (domAlternativo != null)
            huesped.setDomicilioAlternativo(domAlternativo);
        
        //vamos a generar una reserva en el alta misma.
        Reserva reserva = new Reserva(); //creamos objeto reserva
        //declarar un importe bigdecimal antes de setear

        BigDecimal importeReserva = new BigDecimal(1000);
        reserva.setImporteReserva(importeReserva); //forma 1

        reserva.setImporteTotal(new BigDecimal(3000)); //forma 2

        reserva.setImportePagado(new BigDecimal(0)); //hay que instanciar igual el objeto

        reserva.setFechaReserva(new Date()); //fecha actual
       
        System.out.println("Ingrese la fecha de ingreso (dd/mm/yy)");
        
        Date fechaIngreso = null; //primero declaramos la variable fecha
        Date fechaEgreso = null;

        DateFormat dFormat = new SimpleDateFormat("dd/MM/yy");  //luego el formato de la fecha

        //Alternativa de leer fecha con try catch
        try {
        fechaIngreso = dFormat.parse(Teclado.nextLine()); //parsear lo que lea por teclado al formato "dd/mm/yy", verifica caracter por caracter
       
        }catch(Exception ex) {
            System.out.println("Ingreso una fecha invalida");
            System.out.println("Vuelva a empezar");
            return;
        }
    //alternativa de leer fecha a los golpes (puede tirar una exepcion)
    System.out.println("Ingrese la fecha de egreso (dd/mm/yy)");
    fechaEgreso = dFormat.parse(Teclado.nextLine());

        reserva.setFechaIngreso(fechaIngreso); //pone la fecha del dia
        reserva.setFechaEgreso(fechaEgreso); //por ahora un dia
        reserva.setEstadoId(10); //en mi caso esta pagado
        reserva.setHuesped(huesped); //recien ahora estamos haciendo la relacion bidireccional
       
        ABMHuesped.create(huesped);

        /*
         * Si concateno el OBJETO directamente, me trae todo lo que este en el metodo
         * toString() mi recomendacion es NO usarlo para imprimir cosas en pantallas, si
         * no para loguear info Lo mejor es usar:
         * System.out.println("Huesped generada con exito.  " + huesped.getHuespedId);
         */

        System.out.println("Huesped generada con exito.  " + huesped);

    }

    public void baja() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el ID de Huesped:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Huesped huespedEncontrado = ABMHuesped.read(id);

        if (huespedEncontrado == null) {
            System.out.println("Huesped no encontrado.");

        } else {

            try {

                ABMHuesped.delete(huespedEncontrado);
                System.out
                        .println("El registro del huesped " + huespedEncontrado.getHuespedId() + " ha sido eliminado.");
            } catch (Exception e) {
                System.out.println("Ocurrio un error al eliminar una huesped. Error: " + e.getCause());
            }

        }
    }

    public void bajaPorDNI() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el DNI de Huesped:");
        int dni = Teclado.nextInt();
        Huesped huespedEncontrado = ABMHuesped.readByDNI(dni);

        if (huespedEncontrado == null) {
            System.out.println("Huesped no encontrado.");

        } else {
            ABMHuesped.delete(huespedEncontrado);
            System.out.println("El registro del DNI " + huespedEncontrado.getDni() + " ha sido eliminado.");
        }
    }

    public void modifica() throws Exception {
        // System.out.println("Ingrese el nombre de la huesped a modificar:");
        // String n = Teclado.nextLine();

        System.out.println("Ingrese el ID de la huesped a modificar:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Huesped huespedEncontrado = ABMHuesped.read(id);

        if (huespedEncontrado != null) {

            // RECOMENDACION NO USAR toString(), esto solo es a nivel educativo.
            System.out.println(huespedEncontrado.toString() + " seleccionado para modificacion.");

            System.out.println(
                    "Elija qué dato de la huesped desea modificar: \n1: nombre, \n2: DNI, \n3: domicilio, \n4: domicilio alternativo");
            int selecper = Teclado.nextInt();

            switch (selecper) {
                case 1:
                    System.out.println("Ingrese el nuevo nombre:");
                    Teclado.nextLine();
                    huespedEncontrado.setNombre(Teclado.nextLine());

                    break;
                case 2:
                    System.out.println("Ingrese el nuevo DNI:");
                    Teclado.nextLine();
                    huespedEncontrado.setDni(Teclado.nextInt());
                    Teclado.nextLine();

                    break;
                case 3:
                    System.out.println("Ingrese el nuevo domicilio:");
                    Teclado.nextLine();
                    huespedEncontrado.setDomicilio(Teclado.nextLine());

                    break;
                case 4:
                    System.out.println("Ingrese el nuevo domicilio alternativo:");
                    Teclado.nextLine();
                    huespedEncontrado.setDomicilioAlternativo(Teclado.nextLine());

                    break;

                default:
                    break;
            }

            // Teclado.nextLine();

            ABMHuesped.update(huespedEncontrado);

            System.out.println("El registro de " + huespedEncontrado.getNombre() + " ha sido modificado.");

        } else {
            System.out.println("Huesped no encontrado.");
        }

    }

    public void listar() {

        List<Huesped> todos = ABMHuesped.buscarTodos();
        for (Huesped c : todos) {
            mostrarHuesped(c);
        }
    }

    public void listarReserva() {
        List<Reserva> todas = ABMReserva.buscarTodasReservas();
        for (Reserva r : todas) {
            mostrarReserva(r);
        }
    }

    public void listarPorNombre() {

        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();

        List<Huesped> huespedes = ABMHuesped.buscarPor(nombre);
        for (Huesped huesped : huespedes) {
            mostrarHuesped(huesped);
        }
    
    }

    public void listarPorNombreR() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();

        List<Reserva> reservas = ABMReserva.buscarPorNombreHuesped(nombre);
        for (Reserva reserva : reservas) {
            mostrarReserva(reserva);
        }
    
    }
    public void mostrarImportesEstRes() {
        List<Reserva> reservas = ABMReserva.informeEstadoR();
    }

    public void mostrarHuesped(Huesped huesped) {

        System.out.print("Id: " + huesped.getHuespedId() +"\nNombre: " + huesped.getNombre()
        + "\nDNI: " + huesped.getDni()
        + "\nDomicilio: " + huesped.getDomicilio());

        if (huesped.getDomicilioAlternativo() != null)
            System.out.println("\nAlternativo: " + huesped.getDomicilioAlternativo());
        else
            System.out.println();
    }

    public void mostrarReserva(Reserva reserva) {
        System.out.println("IdReserva: " + reserva.getReservaId() + "\nHuesped: " + reserva.getHuesped().getNombre()+
        "\nFecha de ingreso: " + reserva.getFechaIngreso() + "\nFecha de egreso: " + reserva.getFechaEgreso());


    }
    public static void printOpciones() {
        System.out.println("=======================================");
        System.out.println("");
        System.out.println("1. Para agregar un huesped.");
        System.out.println("2. Para eliminar un huesped.");
        System.out.println("3. Para modificar un huesped.");
        System.out.println("4. Para ver el listado de huespedes.");
        System.out.println("5. Para ver el listado de reservas.");
        System.out.println("6. Buscar un huesped por nombre especifico(SQL Injection)).");
        System.out.println("7. Buscar una reserva por nombre especifico.");
        System.out.println("8. Mostrar importes de reservas con sus estados");
        System.out.println("0. Para terminar.");
        System.out.println("");
        System.out.println("=======================================");
    }
}