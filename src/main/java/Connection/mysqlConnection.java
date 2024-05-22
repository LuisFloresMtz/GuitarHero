package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class mysqlConnection {
    String url =  "jdbc:mysql://localhost:3306/consultorios";

    public mysqlConnection(){
        try{
            Connection conexion = DriverManager.getConnection(url,"root","od4ExKo*gqVm");
            System.out.println("Conexión exitosa");
            conexion.close();
        }catch(SQLException ex){
            ex.printStackTrace(System.out);
        }
    }
}
