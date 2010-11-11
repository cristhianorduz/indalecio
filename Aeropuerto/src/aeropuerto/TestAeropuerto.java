
package aeropuerto;
import java.io.*;
/**
 *
 * @author Indalecio Abril Palanco
 */
public class TestAeropuerto {
    static public void main(String[] args) throws IOException {
        Aeropuerto AGP = new Aeropuerto(5, 2);
        // También se debe poder obtener la salida en pantalla:
         //PrintWriter informe = new PrintWriter(System.out, true);
         //AGP.simulación(informe, 4, 24);
         //informe.close();
        AGP.simulación("informe.txt", 4, 24);
   }
}
