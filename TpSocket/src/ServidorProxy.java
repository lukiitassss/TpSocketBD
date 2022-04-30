

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServidorProxy {


	public static void main(String[] args) throws SQLException {
		ServerSocket servidor = null;
		Socket cliente =null;
		final int puerto = 8000;// mi puerto de escucha
		DataInputStream desdecliente;//tomaremos los datos que vienen desde el cliente
		DataOutputStream desdeservidor;//estos datos devuelve el servidor
		IServer serverFirebird = null;
		IServer serverPostGresql = null;
		String ipFirebird = "192.168.0.136";
		String ipPostGreSql = "192.168.0.119";
		ResultSet resultado = null;
		try {
			servidor = new ServerSocket(puerto);//creo el socket del servidor
			System.out.println("Servidor iniciado");
			serverFirebird = new ServerFirebird(ipFirebird);
			serverPostGresql = new ServerPostGreSql(ipPostGreSql);
			if (serverFirebird.conectar()) {System.out.println("firebird conextado");}
			if (serverPostGresql.conectar()) {System.out.println("postgresql conextado");}
			
			
			while (true) {
				
				cliente = servidor.accept();//aca es donde nos quedamos a la espera de recibir peticiones
				System.out.println("Un cliente se acaba de conectar al servidor");
				desdecliente = new DataInputStream(cliente.getInputStream());
				desdeservidor = new DataOutputStream(cliente.getOutputStream());
				String mensaje = desdecliente.readUTF();//queda a la espera de que el cliente el cual ya me conecte me envie algo y meguardo el primer msj
				mensaje = mensaje.toUpperCase();// forzamos mayusculas
				System.out.println("esto se envio desde el cliente hacia el servidor: " + mensaje);//mostramos en el servidor el msj recibido
				//aca va la interaccion con los gestores de bd
				while (!mensaje.equals("EXIT"))//ACA DESCONECTO AL CLIENTE TOTALMENTE SI EL MSJ RECIBIDO ES 'EXIT'
				{
					boolean verificarF = mensaje.startsWith("F: ");
					boolean verificarP = mensaje.startsWith("P: ");
					if ((verificarF && !verificarP)||(!verificarF && verificarP) ) {
						//CODIGO CON EL GESTOR
						//primero sacar al msj los primer 3 caracteres
						mensaje = mensaje.substring(3, mensaje.length());
						if (verificarF) {//aca verificamos que el gestor seleccionado es firebird
							if (mensaje.startsWith("SELECT")){
								try {
									resultado = serverFirebird.query(mensaje);
									desdeservidor.writeUTF(codificar(resultado));

								} catch (SQLException e) {
									desdeservidor.writeUTF("consulta sin exito!!");
								}
								
							}else {
								
									boolean verifoperacion = serverFirebird.queryModificador(mensaje);
									if (verifoperacion) {
										desdeservidor.writeUTF("ABM con exito!!");
									}else {
										desdeservidor.writeUTF("ABM sin exito!!");
									}

							}
							

						} else {
							//operaciones para el gestor postgresql
							if (mensaje.startsWith("SELECT")){
								try {
									resultado = serverPostGresql.query(mensaje);
									desdeservidor.writeUTF(codificar(resultado));

								} catch (SQLException e) {
									desdeservidor.writeUTF("consulta sin exito!!");
								}
								
							}else {
								
									boolean verifoperacion =  serverPostGresql.queryModificador(mensaje);
									if (verifoperacion) {
										desdeservidor.writeUTF("ABM con exito!!");
									}else {
										desdeservidor.writeUTF("ABM sin exito!!");
									}

							}

						}


					}else {
						desdeservidor.writeUTF("error en comandos");
					}
					mensaje = desdecliente.readUTF();//queda a la espera de que el cliente el cual ya me conecte me envie algo de nuevo
					mensaje = mensaje.toUpperCase();
					System.out.println("esto se envio desde el cliente hacia el servidor: " + mensaje +"\n");
				}
				
				cliente.close();//cerramos el cliente
				System.out.println("Un cliente se acaba de desconectar del servidor");
				
			}

			
//fin del servidor
			
		} catch (IOException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
		}serverFirebird.desconectar();
		serverPostGresql.desconectar();

	}
	
	
	
	
	

	private static String codificar(ResultSet resultado) throws SQLException {
		String respuesta;

		respuesta = "";
		int j = 1;
		if (resultado !=null){
		while (j <= resultado.getMetaData().getColumnCount()) { // con este while recorro todos los campos que contenga la tabla resultado
			respuesta += resultado.getMetaData().getColumnName(j) + "     |   ";
			j++;
		}
		respuesta +="\n";
		while (resultado.next()) { // cargo cada fila que de como resultado

			j = 1;
			while (j <= resultado.getMetaData().getColumnCount()) {
				respuesta += resultado.getString(resultado.getMetaData().getColumnName(j)) +"     |   " ;
				j++;
			}

			respuesta +="\n";

		}

		return respuesta;
	}
		else {return respuesta="comando mal realizado";}

	
	
	}
	
	
	
	
}
