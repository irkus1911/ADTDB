/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adtdb.control;

import adtdb.clases.Account;
import adtdb.clases.Customer;
import adtdb.clases.Movement;
import java.util.List;

/**
 *
 * @author 2dam
 */
public interface Dao {
    
    public void crearCliente(Customer cust); //LISTO
    public Customer consultarCliente(Customer cust); //LISTO
    public List consultarCuentaCliente(Customer cust); //LISTO
    public void crearCuenta(Account acc, Customer cust); //LISTO
    public void agregarClienteCuenta(Account acc, Customer cust); //LISTO
    public Account consultarCuenta(Account acco); //LISTO
    public void realizarMovimiento(Movement mov, Account acco);
    public List consultarMovimientos(Account acco);

    
}
