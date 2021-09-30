/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adtdb;

import adtdb.clases.Account;
import adtdb.clases.Customer;
import adtdb.clases.Movement;
import adtdb.control.Dao;
import adtdb.control.DaoImplementation;
import adtdb.utilidades.Util;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 2dam
 */
public class ADTDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int menu;

        Dao daoI = new DaoImplementation();

        do {

            System.out.println("---------MENU---------");
            System.out.println("1.- Crear un cliente");
            System.out.println("2.- Consultar datos de un cliente");
            System.out.println("3.- Consultar cuentas de un cliente");
            System.out.println("4.- Crear cuenta para un cliente");
            System.out.println("5.- Agregar cliente a cuenta");
            System.out.println("6.- Consultar datos de una cuenta");
            System.out.println("7.- Realizar movimiento");
            System.out.println("8.- Consultar movimientos");
            System.out.println("0.- Salir");
            menu = Util.leerInt("--> ");

            while (menu < 0 || menu > 8) {
                System.out.println("Opcion no disponible. Vuelve a intentarlo");
                menu = Util.leerInt("--->");
            }

            switch (menu) {

                case 1:
                    crearCliente(daoI);
                    break;

                case 2:
                    consultarCliente(daoI);
                    //daoI.consultarCliente();
                    break;

                case 3:
                    consultarCuentasCliente(daoI);
                    break;

                case 4:
                   // daoI.crearCuenta(cust);
                    break;

                case 5:

                    //daoI.agregarClienteCuenta(acco, cust);
                    break;

                case 6:
                    //daoI.consultarDatosCuenta(acco);
                    break;

                case 7:
                   // daoI.realizarMovimiento(acco);
                    break;

                case 8:
                    consultarMovimientos(daoI);
                    break;
            }
        } while (menu != 0);
    }

    private static void consultarMovimientos(Dao daoI) {
        Account acc = new Account();
        List<Movement> mov = new ArrayList<>();

        acc.setId(Util.leerLong("Introduce el id de la cuenta"));
        mov = daoI.consultarMovimientos(acc);

        for (Movement m : mov) {
            m.getDatos();
        }
    }

    private static void consultarCuentasCliente(Dao daoI) {
        
        Customer cust = new Customer();
        List<Long> cuentas = new ArrayList();
        cust.setId(Util.leerLong("Introduce la id del cliente"));

        cuentas = daoI.consultarCuentaCliente(cust);
        System.out.println("Las ids de tus cuentas son:");
        for (Long cuenta : cuentas) {
            System.out.println(cuenta);

        }
    }

    private static void consultarCliente(Dao daoI) {
        
        Customer cust = daoI.consultarCliente();
        cust.getDatos();
        
        
    }
    
    private static void crearCliente(Dao daoI) {

        Customer cust = new Customer();

        cust.setCity(Util.introducirCadena("Introduce tu ciudad:"));
        cust.setEmail(Util.introducirCadena("Introduce tu tu email:"));
        cust.setFirstName(Util.introducirCadena("Introduce tu nombre:"));
        cust.setLastName(Util.introducirCadena("Introduce tu apellido:"));
        cust.setMiddleInitial(Util.introducirCadena("Introduce tu inicial de "
                + "segundo nombre:"));
        cust.setPhone(Util.leerLong("Introduce tu telefono:"));
        cust.setState(Util.introducirCadena("Introduce tu provincia"));
        cust.setStreet(Util.introducirCadena("Introduce tu direccion"));
        cust.setZip(Util.leerInt("Introduce tu codigo postal"));

        daoI.crearCliente(cust);
        System.out.println("Se ha creado el cliente con la siguiente "
                + "informacion:");
        cust.getDatos();

    }
    
}
