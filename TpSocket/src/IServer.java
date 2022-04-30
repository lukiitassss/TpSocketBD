
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IServer {
	public boolean conectar();
	public boolean desconectar() throws SQLException;
	public boolean queryModificador(String query);
	public ResultSet query(String query) throws SQLException;
}
