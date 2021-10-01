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
    private final String insertarMovimiento = "INSERT INTO movement (amount,balance,description,timestamp,account_id) VALUES (?,?,?,?,?)";
    private final String buscarCliente = "SELECT * FROM customer WHERE id = ?";
    private final String buscarCuenta = "SELECT * FROM account WHERE id = ?";
    private final String buscarMovimientos = "select * from movement where account_id = ?";
    private final String consultarIdCuentaCliente = "select accounts_id from customer_account where customers_id=?";
    private final String ultimaIdCliente = "SELECT id FROM customer ORDER BY id DESC LIMIT 1";
    private final String ultimaIdCuenta = "SELECT id FROM account ORDER BY id DESC LIMIT 1";
    private final String ultimaIdMovimiento = "SELECT id FROM movement ORDER BY id DESC LIMIT 1";
    private final String agregarIdCuentaCliente = "INSERT INTO customer_account VALUES (?,?)";
    private final String crearCuenta = "insert into account (balance,beginBalance,beginBalanceTimeStamp,creditLine,description,type) values (?,?,?,?,?,?)";

    public void openConnection() {

        try {
            con = DriverManager.getConnection(this.url, this.user, this.pass);
        } catch (SQLException e) {
            System.out.println("Error al intentar abrir la BD" + e);
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

    @Override
    public void crearCliente(Customer cust) {

        this.openConnection();

        try {

            stat = con.prepareStatement(insertarCliente);

            stat.setString(1, cust.getCity());
            stat.setString(2, cust.getEmail());
            stat.setString(3, cust.getFirstName());
            stat.setString(4, cust.getLastName());
            stat.setString(5, cust.getMiddleInitial());
            stat.setLong(6, cust.getPhone());
            stat.setString(7, cust.getState());
            stat.setString(8, cust.getStreet());
            stat.setInt(9, cust.getZip());

            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        insertarUltimaIdCliente(cust);

    }

    public void insertarUltimaIdCliente(Customer cust) {

        ResultSet rs = null;
        this.openConnection();

        try {

            stat = con.prepareStatement(ultimaIdCliente);
            rs = stat.executeQuery();
            if (rs.next()) {
                cust.setId(rs.getLong("id"));
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
    }

    @Override
    public Customer consultarCliente(Customer cli) {

        Customer cust = null;
        ResultSet rs = null;
        this.openConnection();

        try {

            stat = con.prepareStatement(buscarCliente);
            stat.setLong(1, cli.getId());
            rs = stat.executeQuery();

            if (rs.next()) {
                cust = new Customer();
                cust.setId(rs.getLong("id"));
                cust.setCity(rs.getString("city"));
                cust.setEmail(rs.getString("email"));
                cust.setFirstName(rs.getString("firstName"));
                cust.setLastName(rs.getString("lastName"));
                cust.setMiddleInitial(rs.getString("middleInitial"));
                cust.setPhone(rs.getLong("phone"));
                cust.setState(rs.getString("state"));
                cust.setStreet(rs.getString("street"));
                cust.setZip(rs.getInt("zip"));
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
        return cust;
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
                    ac.setId(rs.getLong("accounts_id"));
                    li.add(ac.getId());
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return li;
    }

    @Override
    public void crearCuenta(Account acc, Customer cust) {

        this.openConnection();

        try {

            stat = con.prepareStatement(crearCuenta);

            stat.setFloat(1, acc.getBalance());
            stat.setFloat(2, acc.getBeginBalance());
            stat.setTimestamp(3, acc.getBeginBalanceTimestamp());
            stat.setFloat(4, acc.getCreditLine());
            stat.setString(5, acc.getDescription());
            if (acc.getType() == AccountType.STANDARD) {
                stat.setInt(6, 0);
            } else {
                stat.setInt(6, 1);
            }

            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        insertarUltimaIdCuenta(acc);
        agregarIdCuentaCliente(acc, cust);

    }

    public void insertarUltimaIdCuenta(Account acc) {

        ResultSet rs = null;
        this.openConnection();

        try {

            stat = con.prepareStatement(ultimaIdCuenta);
            rs = stat.executeQuery();
            if (rs.next()) {
                acc.setId(rs.getLong("id"));
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
    }

    public void agregarIdCuentaCliente(Account acc, Customer cust) {

        this.openConnection();

        try {

            stat = con.prepareStatement(agregarIdCuentaCliente);
            stat.setLong(1, cust.getId());
            stat.setLong(2, acc.getId());

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
    public void agregarClienteCuenta(Account acc, Customer cust) {

        agregarIdCuentaCliente(acc, cust);

    }

    @Override
    public Account consultarCuenta(Account acco) {

        Account acc = null;
        ResultSet rs = null;
        this.openConnection();

        try {

            stat = con.prepareStatement(buscarCuenta);
            stat.setLong(1, acco.getId());
            rs = stat.executeQuery();

            if (rs.next()) {
                acc = new Account();
                acc.setId(rs.getLong("id"));
                acc.setBalance(rs.getFloat("balance"));
                acc.setBeginBalance(rs.getFloat("beginBalance"));
                acc.setBeginBalanceTimestamp(rs.getTimestamp("beginBalanceTimestamp"));
                acc.setCreditLine(rs.getFloat("creditLine"));
                acc.setDescription(rs.getString("description"));
                if (rs.getInt("type") == 1) {
                    acc.setType(AccountType.CREDIT);
                } else {
                    acc.setType(AccountType.STANDARD);
                }
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
        return acc;
    }

    @Override
    public void realizarMovimiento(Movement mov, Account acco) {

        this.openConnection();

        try {

            stat = con.prepareStatement(insertarMovimiento);

            stat.setFloat(1, mov.getAmount());
            stat.setFloat(2, mov.getBalance());
            stat.setString(3, mov.getDescription());
            stat.setTimestamp(4, mov.getDatabaseDate());
            stat.setLong(5, acco.getId());

            stat.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        insertarUltimaIdMovimiento(mov);
        
    }

    public void insertarUltimaIdMovimiento(Movement mov) {

        ResultSet rs = null;
        this.openConnection();

        try {

            stat = con.prepareStatement(ultimaIdMovimiento);
            rs = stat.executeQuery();
            if (rs.next()) {
                mov.setId(rs.getLong("id"));
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
    }
    
    @Override
    public List consultarMovimientos(Account acco) {
        List<Movement> movements = new ArrayList<>();
        Movement mov;
        ResultSet rs = null;

        this.openConnection();
        try {

            stat = con.prepareStatement(buscarMovimientos);
            stat.setLong(1, acco.getId());
            rs = stat.executeQuery();

            while (rs.next()) {

                mov = new Movement();

                mov.setId(rs.getLong("id"));
                mov.setAmount(rs.getLong("amount"));
                mov.setBalance(rs.getFloat("balance"));
                mov.setDescription(rs.getString("description"));
                mov.setDatabaseDate(rs.getTimestamp("timestamp"));

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

}
