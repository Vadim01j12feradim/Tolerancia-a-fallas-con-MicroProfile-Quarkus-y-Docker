package org.acme;

import org.eclipse.microprofile.faulttolerance.*;

import javax.swing.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;
@Path("/MyExample")
public class MyExample {

    Logger LOGGER = Logger.getLogger("My Example");
    public MyExample() throws IOException {
    }

    @GET
    //@Timeout(value = 5000L)
    //@Retry(maxRetries = 2)//Ejecuta hasta que se den el numero de errores
    //@CircuitBreaker(failureRatio = 0.1,delay = 100L)//Si uno de cada 10 falla considera que intenta en 10 segundos
    @Bulkhead(value = 2)//Cantidad de clientes
    @Fallback(fallbackMethod = "Restore")
    public String Execuye() throws InterruptedException, IOException {
        LOGGER.warning("Ejecutando el programa");
        File fichero = new File("/home/sensei/Escritorio/code-with-quarkus/src/main/java/org/acme/file.txt");

        Scanner s = null;
        String ret="";
            s = new Scanner(fichero);
            int i=0;
            ret="";
            while (s.hasNextLine()) {
                String linea = s.nextLine(); 	// Guardamos la linea en un String
                ret+=linea;
                ret+="*";
                i++;
            }
        //Escribo
        String lineas = ret;
        FileWriter fw  = new FileWriter("/home/sensei/Escritorio/code-with-quarkus/src/main/java/org/acme/file.txt");
        String dev ="<div style='color:red;text-align: center;" +
                "font-size: 40px;" +
                "background-color: rgb(196, 190, 233);'>";
            dev+="<div style='color:blue;'>Nueros generados </div><br>";
            //JOptionPane.showMessageDialog(null,"value "+lineas[0]);
            String tem="";
            String next="";
            if (lineas.length()==0)
                next="-1";
            for(i=0;i<lineas.length()-1;i++) {

                if (lineas.charAt(i)=='*'){
                    fw.write(tem + "*");
                    dev+="<div style='border-style:groove;display: " +
                            "inline-block;border-width: 30px;" +
                            "border-color:rgb(88, 172, 40);" +
                            " cursor: pointer;" +
                            "border-radius: 10px;'>";
                    dev+=tem;
                    dev+="</div><br><div style='color:black;'>|</div>";
                    next=tem;
                    //JOptionPane.showMessageDialog(null,"Entre "+next);
                    tem="";
                }

                else{
                    tem+=lineas.charAt(i);
                    //JOptionPane.showMessageDialog(null,"recolecto "+tem);

                }
            }

            next = String.valueOf(Integer.parseInt(next)+1);
            fw.write(next + "*");

            dev+=next;
            dev+="</div>";
            fw.close();
        String getter = dev;
        Thread.sleep(5000);
        //getter +="<script>location.reload();</script>";
        return getter;
    }
    public String Restore() throws IOException {

        String path = "/home/sensei/Escritorio/code-with-quarkus/src/main/java/org/acme/Script.sh";
        //String path = "/home/refactorizando/script.sh"
        String[] command = {"sh",path};
        Runtime.getRuntime().exec(command);
        //process.destroy();
        LOGGER.warning("Error");
        return "<script>" +
                "document.write('Ejecutando Script');alert('Ejecutando Script.sh para recrear el fichero');location.reload();</script>";
    }
}

