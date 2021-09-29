/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adtdb.control;

import adtdb.clases.Account;
import adtdb.clases.AccountType;
import adtdb.clases.Customer;
import adtdb.clases.Movement;
import adtdb.utilidades.Util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Steven,Irkus,Unai y Adrian
 */
public class DaoImplementation implements Dao {

    // ATRIBUTOS DE LA CONEXION A BD
    private Connection con;
    private PreparedStatement stat;

    /* CONDFIGURACION */
    private ResourceBundle configFile;
    private String url, user, pass;

    public DaoImplementation() {

        configFile = ResourceBundle.getBundle("adtdb.control.config");
        url = configFile.getString("Conn");
        user = configFile.getString("DBUser");
        pass = configFile.getString("DBPass");

    }

    //SENTENCIAS SQL
    private final String insertarCliente = "insert into customer (city,email,firstName,lastName,middleInitial,phone,state,street,zip) values (?,?,?,?,?,?,?,?,?)";
    private final String buscarIdCliente = "select * from customer where id = ?";
    private final String buscarMovimientos = "select * from movement where account_id = ?";
    private final String consultarIdCuentaCliente = "select accounts_id from customer_account where customers_id=?";

    private final String mostrarCuentas = "select * from account";

    public void openConnection() {

        try {
            con = DriverManager.getConnection(this.url, this.user, this.pass);
        } catch (SQLException e) {
            System.out.println("Error al intentar abrir la BD");
        }

    }

    public void closeConnection() throws SQLException {

        if (stat != null) {
            stat.close();
        }
        if (con != null) {
            con.close();
        }

    }

    private Customer comprobarIdCliente(Customer cli) throws SQLException {

        ResultSet rs = null;

        this.openConnection();
        try {

            stat = con.prepareStatement(buscarIdCliente);
            stat.setLong(1, cli.getId());
            rs = stat.executeQuery();
            if (rs == null) {
                return null;
            } else {
                Customer cust = new Customer();
                cust.setId(rs.getLong("customer.id"));
                cust.setCity(rs.getString("customer.city"));
                cust.setEmail(rs.getString("customer.email"));
                cust.setFirstName(rs.getString("customer.firstName"));
                cust.setLastName(rs.getString("customer.lastName"));
                cust.setMiddleInitial(rs.getString("customer.middleName"));
                cust.setPhone(rs.getLong("customer.phone"));
                cust.setState(rs.getString("customer.state"));
                cust.setStreet(rs.getString("customer.street"));
                cust.setZip(rs.getInt("customer.zip"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.closeConnection();
        return cli;
    }

    @Override
    public void crearCliente() {

        this.openConnection();
        
        try {

            stat = con.prepareStatement(insertarCliente);

            stat.setString(1, Util.introducirCadena("Introduce tu ciudad:"));
            stat.setString(2, Util.introducirCadena("Introduce tu tu email:"));
            stat.setString(3, Util.introducirCadena("Introduce tu nombre:"));
            stat.setString(4, Util.introducirCadena("Introduce tu apellido:"));
            stat.setString(5, Util.introducirCadena("Introduce tu inicial de segundo nombre:"));
            stat.setLong(6, Util.leerLong("Introduce tu telefono:"));
            stat.setString(7, Util.introducirCadena("Introduce tu provincia"));
            stat.setString(8, Util.introducirCadena("Introduce tu direccion"));
            stat.setString(9, Util.introducirCadena("Introduce tu codigo postal"));

            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Customer consultarCliente() {

        Customer cli = new Customer();
        cli.setId(Util.leerLong("Introduce la id del cliente a buscar:"));

        try {
            if (comprobarIdCliente(cli) == null) {
                System.out.println("El cliente no se ha encontrado");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cli;
    }

    @Override
    public List consultarCuentaCliente(Customer cust) {

        List<Long> li = new ArrayList<>();
        ResultSet rs = null;
        this.openConnection();

        try {
            stat = con.prepareStatement(consultarIdCuentaCliente);
            stat.setLong(1, cust.getId());
            rs = stat.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    Account ac = new Account();
                    ac.setId(rs.getLong("customer_account.accounts_id"));
                    li.add(ac.getId());
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return li;
    }

    @Override
    public void crearCuenta(Customer cust) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void agregarClienteCuenta(Account acco, Customer cust) {
        
    }

    @Override
    public Account consultarDatosCuenta(Account acco) {
        //Lo hace adrian
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void realizarMovimiento(Account acco) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List consultarMovimientos(Account acco) {

        List<Movement> movements = new ArrayList<>();
        ResultSet rs = null;

        this.openConnection();
        try {

            stat = con.prepareStatement(buscarMovimientos);
            stat.setLong(1, acco.getId());
            rs = stat.executeQuery();

            while (rs.next()) {

                Movement mov = new Movement();

                mov.setId(rs.getLong("movement.id"));
                mov.setAmount(rs.getLong("movement.amount"));
                mov.setBalance(rs.getFloat("movement.balance"));
                mov.setDescription(rs.getString("movement.description"));
                mov.setDatabaseDate(rs.getTimestamp("movement.timestamp"));

                movements.add(mov);

            }

        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return movements;
    }

    //MOSTRAR TODAS LAS CUENTAS
    /*
    @Override
    public List mostrarCuentas() {

        List<Account> cuentas = new ArrayList<>();
        Account acc = null;
        ResultSet rs = null;

        this.openConnection();
        
        try {
            stat = con.prepareStatement(mostrarCuentas);
            rs = stat.executeQuery();

            while (rs.next()) {             
                acc = new Account();
                acc.setId(rs.getLong("id"));
                acc.setDescription(rs.getString("description"));
                acc.setBalance(rs.getFloat("balance"));
                acc.setCreditLine(rs.getFloat("creditLine"));
                acc.setBeginBalance(rs.getFloat("beginBalance"));
                acc.setBeginBalanceTimestamp(rs.getTimestamp("beingBalanceTimestamp"));          
                if (rs.getInt("type") == 1) {
                    acc.setType(AccountType.CREDIT);
                } else {
                    acc.setType(AccountType.STANDARD);
                }        
                cuentas.add(acc);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            this.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cuentas;

    }
    */

}
