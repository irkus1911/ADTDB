/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adtdb.control;

import adtdb.clases.Account;
import adtdb.clases.Customer;
import adtdb.clases.Movement;
import adtdb.utilidades.Util;
import java.net.ConnectException;
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
public class DaoImplementation implements Dao{
    
    
    
    
     private ResourceBundle configFile;
    private String url, user, pass;
    
    public DaoImplementation() {
        
        configFile = ResourceBundle.getBundle("bancoadt.conection.config");
        url = configFile.getString("Conn");
        user = configFile.getString("DBUser");
        pass = configFile.getString("DBPass");
        
    }
    
    public Connection openConnection() throws ConnectException {
        
        Connection con = null;
        
        try {
            con = DriverManager.getConnection(url,user,pass);
        } catch (SQLException ex) {
            throw new ConnectException("Error al conectar con la base de datos");
        }
        
        return con;
        
    }
    
    public void closeConnection() throws ConnectException {
        
        if (stat != null || con != null) {
            try {
                stat.close();
                con.close();
            } catch (SQLException ex) {
                throw new ConnectException("Error al cerrar con la base de datos"); 
            }
        }
        
        
    }
    
    
    
    
    
    
    
    
    
    private Connection con;
    private PreparedStatement stat;
    
    
    private final String insertarCliente = "insert into customer (city,email,firstName,lastName,middleInitial,phone,state,street,zip) values (?,?,?,?,?,?,?,?,?)";
    private final String buscarIdCliente = "select * from customer where id = ?";
    private final String buscarMovimientos = "select * from movement where account_id = ?";
    private final String consultarIdCuentaCliente= "select accounts_id from customer_account where customers_id=?";
    private final String realizarMovimiento = "insert into movement (id, amount, balance, description, timestamp, account_id) values (?, ?, ?, ?, ?, ?)";
    private final String buscarIdCuenta = "select * from account where id = ?";
   
  
   
    private Customer comprobarIdCliente(Customer cli) {
        
        ResultSet rs = null;
       
          try {
          this.openConnection();
        } catch (ConnectException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        
        try {
            this.closeConnection();
        } catch (ConnectException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cli;
    }
    
    private Long comprobarIdCuenta(Account acc){
        
        ResultSet rs = null;
        long id = 0;
       
          try {
            con = this.openConnection();
        } catch (ConnectException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            
            stat = con.prepareStatement(buscarIdCuenta);
            stat.setLong(1, acc.getId());
            rs = stat.executeQuery();
            if (rs == null) {
                return null;
            } else {
                id=rs.getLong("account.id");
                
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    @Override
    public void crearCliente() {
        
        ResultSet rs = null;     
        
        try {
            this.openConnection();
        } catch (ConnectException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            
            rs = stat.executeQuery();
            
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            this.closeConnection();
        } catch (ConnectException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }

    @Override
    public Customer consultarCliente() {
        
        Customer cli = new Customer();
        cli.setId(Util.leerLong("Introduce la id del cliente a buscar:"));
               
        if (comprobarIdCliente(cli) == null) {
            System.out.println("El cliente no se ha encontrado");
        } 
        return cli;
    }

    @Override
    public List consultarCuentaCliente(Customer cust) {

        List li =new ArrayList<Long>();
        ResultSet rs=null;
        try {
            this.openConnection();
        } catch (ConnectException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            stat=con.prepareStatement(consultarIdCuentaCliente);
            stat.setLong(1, cust.getId());
            rs=stat.executeQuery();
            if(rs!=null){
                while (rs.next()){
                    Account ac=new Account();
                    ac.setId( rs.getLong("customer_account.accounts_id"));
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Account consultarDatosCuenta(Account acco) {
        //Lo hace adrian
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void realizarMovimiento(Account acco) {
        ResultSet rs = null;    
        Account acc = new Account();
        
        try {
            con = this.openConnection();
        } catch (ConnectException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            
            stat = con.prepareStatement(realizarMovimiento);
            
            stat.setString(1, Util.introducirCadena("Introduce la id: "));
            stat.setString(2, Util.introducirCadena("Introduce la cantidad: "));
            stat.setString(3, Util.introducirCadena("Introduce el balance: "));
            stat.setString(4, Util.introducirCadena("Introduce una descripcion: "));
            stat.setString(5, Util.introducirCadena("Introduce la fecha y hora: "));
            
            acc.setId(Util.leerLong("Introduce el id de la cuenta: "));
            
            while(comprobarIdCuenta(acc)==null || comprobarIdCuenta(acc)==0){
                System.out.println("No existe esa id de cuenta, vuelva a intentarlo");
                acc.setId(Util.leerLong("Introduce el id de la cuenta: "));
            }
            
            stat.setLong(6, Util.leerLong("Introduce el id de la cuenta: "));
            
            rs = stat.executeQuery();
            
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            this.closeConnection();
        } catch (ConnectException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List consultarMovimientos(Account acco) {
        
        List <Movement> movements = new ArrayList<>();
        ResultSet rs = null;
       
          try {
            this.openConnection();
        } catch (ConnectException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            
            stat = con.prepareStatement(buscarMovimientos);
            stat.setLong(1, acco.getId());
            rs = stat.executeQuery();
            
            while(rs.next()){
                
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
        } catch (ConnectException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return movements;
    }
    
    
    
    
    
}
