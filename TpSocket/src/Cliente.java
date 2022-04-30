

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public final class Cliente {
	private String hostproxy = "192.168.0.131";
	private int puertoproxy = 8000;
	private boolean bucle;
	private DataInputStream recibeServidor;//tomaremos los datos que vienen desde el cliente
	private DataOutputStream desdecliente;//estos datos devuelve el servidor
	private Scanner scanner;

	 public static void main(String args[]) {
		    new Cliente();
		  }
	 
	 
	 
	 
	 public Cliente() {
		    scanner = new Scanner(System.in);
	try {
		Socket miproxy = new Socket(hostproxy,puertoproxy);
		recibeServidor = new DataInputStream(miproxy.getInputStream());
		desdecliente = new DataOutputStream(miproxy.getOutputStream());
		System.out.println("Cliente iniciado.");
		/*desdeclien
		System.out.println("el servidor contesto al cliente: "+ mensaje);
		miproxy.close();
		*/

		menu();
		desdecliente.writeUTF("exit");
		desdecliente.close();
	    recibeServidor.close();
	    miproxy.close();
		System.out.println("Cliente finalizado");
		
		
	} catch (IOException e) {
		 System.out.println("Servidor no conectado");
		e.printStackTrace();
	}
		
 	
	}
	 
	 

	private void menu() {

		bucle=true;
		String opcion;
		 while(bucle) {
			 System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
			 System.out.println("*  Bienvenido a nuestro programa cliente  *");
			 System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
			 System.out.println("-------------------------------------------");
			 System.out.println("|En este sistema solo se sabe la existencia|");
			 System.out.println("|de 2 BD: FACTURACION Y PERSONAL.          |");
			 System.out.println("|------------------------------------------|");
			 System.out.println("|Para realizar su operacion primero debe   |");
			 System.out.println("|escribir 'F: ' o 'P: ' para indicar si usa|");
			 System.out.println("|FACTURACION o PERSONAL respectivamente    |");
			 System.out.println("|------------------------------------------|");
			 System.out.println("|Solo se pueden realizar operaciones de    |");
			 System.out.println("|               ABM y Select               |");
			 System.out.println("--------------------------------------------");
			 System.out.println();
			 System.out.println("Ingresar operacion o 'exit' para salir");
			 System.out.println();
			 System.out.print(">");
			 opcion  = scanner.nextLine(); 
			 if (opcion.toLowerCase().equals("exit")) {
				 bucle = false;
			 } else {
				try {
					desdecliente.writeUTF(opcion);
					System.out.println(recibeServidor.readUTF());
					System.out.println();
					System.out.println();
					System.out.println();
				} catch (IOException e) {
					// TODO Bloque catch generado automáticamente
					e.printStackTrace();
				}
				
				
			 }
	
	
		 }
	

}






	
	
	
	
}
