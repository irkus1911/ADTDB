/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adtdb.control;

import adtdb.clases.Account;
import adtdb.clases.Customer;
import java.util.List;

/**
 *
 * @author 2dam
 */
public interface Dao {
     public void crearCliente();
    public Customer consultarCliente();
    public List consultarCuentaCliente(Customer cust);
    public void crearCuenta(Customer cust);
    public void agregarClienteCuenta(Account acco, Customer cust);
    public Account consultarDatosCuenta(Account acco);
    public void realizarMovimiento(Account acco);
    public List consultarMovimientos(Account acco);
    
}
