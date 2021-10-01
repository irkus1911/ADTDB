/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adtdb;

import adtdb.clases.Account;
import adtdb.clases.AccountType;
import adtdb.clases.Customer;
import adtdb.clases.Movement;
import adtdb.control.Dao;
import adtdb.control.DaoImplementation;
import adtdb.utilidades.Util;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
            System.out.println("7.- Realizar movimiento sobre una cuenta");
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
                    consultarDatosCliente(daoI);
                    break;

                case 3:
                    consultarCuentasCliente(daoI);
                    break;

                case 4:
                    crearCuentaCliente(daoI);
                    break;

                case 5:
                    agregarClienteCuenta(daoI);
                    break;

                case 6:
                    consultarDatosCuenta(daoI);
                    break;

                case 7:
                    realizarMovimientoCuenta(daoI);
                    break;
                case 8:
                    //falta las comprobaciiones igual que en los demas
                    consultarMovimientos(daoI);
                    break;
            }
        } while (menu != 0);
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

    private static void consultarDatosCliente(Dao daoI) {

        Customer cust = new Customer();
        cust.setId(Util.leerLong("Introduce la id del cliente a buscar:"));
        cust = daoI.consultarCliente(cust);

        if (cust != null) {
            cust.getDatos();
        } else {
            System.out.println("El cliente no se ha encontrado");
        }

    }

    private static void consultarCuentasCliente(Dao daoI) {

        Account acc = null;
        Customer cust = new Customer();
        List<Long> cuentas = new ArrayList();
        cust.setId(Util.leerLong("Introduce la id del cliente"));

        cuentas = daoI.consultarCuentaCliente(cust);
        System.out.println("Tus cuentas son:");
        for (Long cuenta : cuentas) {
            System.out.println(cuenta);
        }
    }

    private static void crearCuentaCliente(Dao daoI) {

        Account acc = new Account();
        Customer cust = null;
        boolean encontrado = false;
        boolean continuar = true;

        while (!encontrado && continuar) {
            cust = new Customer();
            cust.setId(Util.leerLong("Introduce el id del cliente para crearle una nueva cuenta:"));
            cust = daoI.consultarCliente(cust);
            if (cust != null) {
                encontrado = true;
            } else {
                System.out.println("El cliente no se ha encontrado");
                System.out.println("¿Quieres continuar buscando un cliente? Si (S/s) o No (N/n)");
                continuar = Util.esBoolean();
            }

        }

        if (encontrado) {
            acc.setDescription(Util.introducirCadena("Introduce la descripcion:"));
            acc.setBalance(Util.leerFloat("Introduce el balance:"));
            acc.setCreditLine(Util.leerFloat("Introduce la linea de credito:"));
            acc.setBeginBalance(Util.leerFloat("Introduce el balance inicial:"));
            LocalDate ld = Util.leerFecha("Introduce la fecha del balance inicial:");
            LocalDateTime ldt
                    = ld.atTime(Util.leerInt("Introduce la hora:"),
                            Util.leerInt("Introduce el minuto:"),
                            Util.leerInt("Introduce el segundo:"));
            acc.setBeginBalanceTimestamp(Timestamp.valueOf(ldt));
            int accType = Util.leerInt("Introduce el tipo de cuenta, 1 para CREDITO o 2 para ESTANDAR ", 1, 2);
            if (accType == 1) {
                acc.setType(AccountType.CREDIT);
            } else if (accType == 2) {
                acc.setType(AccountType.STANDARD);
            }
            daoI.crearCuenta(acc, cust);
            System.out.println("Se ha creado la cuenta con la siguiente "
                    + "informacion:");
            System.out.println("---------Id cliente: " + cust.getId() + "---------");
            acc.getDatos();
        }

    }

    private static void agregarClienteCuenta(Dao daoI) {

        Account acc = null;
        Customer cust = null;
        boolean encontradoCliente = false;
        boolean continuarCliente = true;

        boolean encontradoCuenta = false;
        boolean continuarCuenta = true;

        while (!encontradoCliente && continuarCliente) {
            cust = new Customer();
            cust.setId(Util.leerLong("Introduce el id de un cliente:"));
            cust = daoI.consultarCliente(cust);
            if (cust != null) {
                encontradoCliente = true;
            } else {
                System.out.println("El cliente no se ha encontrado");
                System.out.println("¿Quieres continuar buscando un cliente? Si (S/s) o No (N/n)");
                continuarCliente = Util.esBoolean();
            }

        }

        while (!encontradoCuenta && continuarCuenta) {
            acc = new Account();
            acc.setId(Util.leerLong("Introduce el id de la cuenta que quiere agregar:"));
            acc = daoI.consultarCuenta(acc);
            if (acc != null) {
                encontradoCuenta = true;
            } else {
                System.out.println("La cuenta no se ha encontrado");
                System.out.println("¿Quieres continuar buscando una cuenta? Si (S/s) o No (N/n)");
                continuarCuenta = Util.esBoolean();
            }

        }

        if (encontradoCliente && encontradoCuenta) {

            daoI.agregarClienteCuenta(acc, cust);
            System.out.println("Se ha agregado el CLIENTE con ID "
                    + cust.getId() + " a la CUENTA con ID " + acc.getId());

        }

    }

    private static void consultarDatosCuenta(Dao daoI) {

        Account acc = new Account();
        acc.setId(Util.leerLong("Introduce el id de la cuenta:"));
        acc = daoI.consultarCuenta(acc);
        if (acc != null) {
            acc.getDatos();
        } else {
            System.out.println("La cuenta no se ha encontrado");
        }

    }

    private static void realizarMovimientoCuenta(Dao daoI) {

        Account acc = null;
        Movement mov = null;
        boolean encontradoCuenta = false;
        boolean continuarCuenta = true;

        while (!encontradoCuenta && continuarCuenta) {
            acc = new Account();
            acc.setId(Util.leerLong("Introduce el id de la cuenta para realizar un movimiento:"));
            acc = daoI.consultarCuenta(acc);
            if (acc != null) {
                encontradoCuenta = true;
            } else {
                System.out.println("La cuenta no se ha encontrado");
                System.out.println("¿Quieres continuar buscando una cuenta? Si (S/s) o No (N/n)");
                continuarCuenta = Util.esBoolean();
            }

        }

        if (encontradoCuenta) {

            mov = new Movement();
            LocalDate ld = Util.leerFecha("Introduce la fecha del movimiento:");
            LocalDateTime ldt
                    = ld.atTime(Util.leerInt("Introduce la hora:"),
                            Util.leerInt("Introduce el minuto:"),
                            Util.leerInt("Introduce el segundo:"));
            mov.setDatabaseDate(Timestamp.valueOf(ldt));
            mov.setAmount(Util.leerFloat("Introduce la cantidad:"));
            mov.setBalance(Util.leerFloat("Introduce el balance:"));
            mov.setDescription(Util.introducirCadena("Introduce la descripcion:"));

            daoI.realizarMovimiento(mov, acc);
            System.out.println("---------Id cuenta: " + acc.getId() + "---------");
            mov.getDatos();
        }

    }

    private static void consultarMovimientos(Dao daoI) {

        Account acc = null;
        List<Movement> mov = new ArrayList<>();
        boolean encontradoCuenta = false;
        boolean continuarCuenta = true;

        while (!encontradoCuenta && continuarCuenta) {
            acc = new Account();
            acc.setId(Util.leerLong("Introduce el id de la cuenta para consultar todos sus movimientos:"));
            acc = daoI.consultarCuenta(acc);
            if (acc != null) {
                encontradoCuenta = true;
            } else {
                System.out.println("La cuenta no se ha encontrado");
                System.out.println("¿Quieres continuar buscando una cuenta? Si (S/s) o No (N/n)");
                continuarCuenta = Util.esBoolean();
            }

        }

        if (encontradoCuenta) {

            mov = daoI.consultarMovimientos(acc);
            System.out.println("---------Id cuenta: " + acc.getId() + "---------");
            for (Movement m : mov) {
                m.getDatos();
            }

        }

    }

}
