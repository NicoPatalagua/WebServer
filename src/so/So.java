package so;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class So {
    public static void main(String[] args) throws IOException  {
        int port=5000;// el port de salida del socket
        ServerSocket server = new ServerSocket(port);// es el socket servidor
        byte[] datos= new byte[256];// espacio en donde se guardara la peticion del cliente
        while (true) {// en este siclo se espera la conexión 
            Socket so= server.accept(); // ser crea la conexión
            System.out.println("CONEXION ESTABLECIDAD");// mensaje cuando ya se creo la conexión
            DataInputStream entra= new DataInputStream(so.getInputStream()); // reseive las peticiones del cliente
            DataOutputStream salida= new DataOutputStream(so.getOutputStream()); // envia las respuestas al cliente
            entra.read(datos, 0, datos.length); // lee la peticion del cliente. 
            /* 
            Extrae el nombre del archivo de la peticion GET
            */
            String NombreArchivo=new String(datos).split(((char)10)+"")[0].split("/")[1];             
            NombreArchivo=NombreArchivo.substring(0, NombreArchivo.length()-5);
            //muestra el nombre del archivo solicitado por el cliente
            System.out.println("El archivo solicitado es: "+NombreArchivo); 
            // imprime la peticion del cliente
            System.out.println(new String(datos));
            
            FileReader html = null;//lee el archivo.
            String CodigoRespuesta="";// Codigo de la respuesta HTTP
            try {//captura la excepción de el archivo
                html = new FileReader (new File (NombreArchivo));//
                CodigoRespuesta="200 OK";
            } catch (FileNotFoundException ex) { //si no lo encuentra lee el archivo de html 404
                html= new FileReader ("404.html");
                CodigoRespuesta="404 not found";
            }  
            /*
            se realiza la lectura del html y se pasa a texto plano
            */
            BufferedReader br = new BufferedReader(html);
            String pagina="";
            String linea;
            while((linea=br.readLine())!=null)
                pagina+=linea;
            /*
            se crea la el encabezado HTTP y se le signa el contenido del html
            */
            String Respuesta=
                    "HTTP/1.1 "+CodigoRespuesta+"\n"+
                    "Content-Type: text/html\n"+
                    "Content-Length: "+pagina.length()+"\n"+
                    "\n\n"+pagina;
            //se envia la respuesta al cliente
            salida.write(Respuesta.getBytes(),0,Respuesta.length()); 
            // se cierran todas las conexiones y archivos.
            entra.close();
            salida.close();
            so.close();
            html.close();
            br.close();
        }
        
    }
    
}
