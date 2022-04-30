
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class ServerPostGreSql implements IServer {
	public Statement statement = null;
	public ResultSet result = null;
	public Connection connection = null;
	String dataBaseUrl; 
	String usuarioDB = "postgres";
	String password = "masterkey";
	String driverName = "org.postgresql.Driver";
	
	public ServerPostGreSql(String ip) {
		dataBaseUrl = "jdbc:postgresql://" + ip + ":5432/personal";
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
			statement.  executeUpdate(query);
			estado = true;
		} catch (Exception e) {System.out.println(e);}
		
		return estado;
	}

	@Override
	public ResultSet query(String query) throws SQLException {
		statement = connection.createStatement();
		try {
     result = statement.executeQuery(query);
		} catch (Exception e) {System.out.println(e);}
		
		return result;
	}
}
