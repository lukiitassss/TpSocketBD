
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class ServerFirebird implements IServer{
	private String dataBaseUrl;
	private String usuarioDB = "sysdba";
	private String password = "masterkey";
	private String driverName = "org.firebirdsql.jdbc.FBDriver";
	public Statement statement = null;
	public ResultSet result = null;
	public Connection connection = null;
	
	public ServerFirebird(String ip) {
		dataBaseUrl = "jdbc:firebirdsql:"+ip+"/3050:C:\\FACTURACION.fdb";
		//jdbc:firebirdsql:localhost/3050:C:\\PRUEBA.FDB
	}
	
	@Override
	public boolean conectar() {
		boolean estado = false;
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(dataBaseUrl,usuarioDB,password);
			estado = true;
		} catch (Exception e) {e.printStackTrace();}		
		return estado;
	}
	
	@Override
	public boolean desconectar() throws SQLException {
		boolean estado = false;
		if (connection != null) {
			connection.close();
			estado = true;
		}
		
		return estado;
	}

	@Override
	public boolean queryModificador(String query) {
		boolean estado = false;
		try  {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			estado = true;
			
		} catch (Exception e) {System.out.println(e);}
		
		
		return estado;
	}

	@Override
	public ResultSet query(String query) throws SQLException {
		result=null;
		statement = connection.createStatement();
		try {
		 result = statement.executeQuery(query);
		} catch (Exception e) {return result;}
		
		return result;
	}
}
