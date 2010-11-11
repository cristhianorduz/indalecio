

package aeropuerto;
import java.util.*;
import java.io.*;
/**
 *
 * @author Indalecio Abril Palanco
 */
public class Aeropuerto {
    // ------------------- ATRIBUTOS -------------------------
    
    //Aviones que están aterrizados esperando a despegar
    private Set<Vuelo> vuelos;
    //Compañias con sus acrónimos
    private static Map<String, String> compañias = new HashMap<String,String>();
    private int capacidad;
    private int tamSlot;
    
    //--------------------- CONSTRUCTORES --------------------
    /**
     * 
     * @param capac
     * @param tamSlot
     */
    public Aeropuerto(int capac,int tamSlot){
        if(compañias.isEmpty()){ //si esta vacío, cargamos el fichero en la estruct
            try{
                cargarCompañias("Companias.txt");
            }catch(IllegalArgumentException e){
                System.err.print(e.getMessage());
            }
         }
        capacidad = capac;
        this.tamSlot = tamSlot;
        vuelos = new HashSet<Vuelo>();
    }
    /**
     * 
     * @param capac
     */
    public Aeropuerto(int capac){
        if(compañias.isEmpty()){ //si esta vacío, cargamos el fichero en la estruct
            try{
                cargarCompañias("Companias.txt");
            }catch(IllegalArgumentException e){
                System.err.print(e.getMessage());
            }
         }
        capacidad = capac;
        this.tamSlot = 2;
        vuelos = new HashSet<Vuelo>();
    }
    // ------------------- METODOS --------------------
    /**
     * @param nomfich
     * Consideraremos que habrá un fichero en el directorio: companias.txt donde
     * tendremos el nombre de la compañia y de ahí obtendremos cada acrónimo
     */
    private void cargarCompañias (String nomfich){
        BufferedReader br = null;
        try{
            br= new BufferedReader(new FileReader(nomfich));
            String nombre;
            while((nombre =br.readLine())!=null){
                //String nombre = sc.nextLine();
                String [] nombres = nombre.split(" ");
                String acro = "";
                if(nombres.length == 1){ //Si la compañia solo tiene un nombre
                    acro = nombres[0].substring(0, 2).toUpperCase();
                }else if(nombres.length >1){
                    acro = nombres[0].substring(0, 1).toUpperCase()+
                            nombres[1].substring(0, 1).toUpperCase();
                }
                if(nombre.equals(" ") || compañias.containsValue(acro.toUpperCase()))
                    throw new IllegalArgumentException("La compañia "+
                                nombre+" NO tiene acrónimo");
                else{
                    compañias.put(nombre, acro); //Insertamos en el mapa
                    //System.out.println(nombre+" ,"+acro+" \n");
                }
            }

        }catch(FileNotFoundException e){
                System.err.print(e.getMessage());
        }catch(IOException e2){
            System.err.print(e2.getMessage());
        }finally{
            try{
                br.close();
            }catch(IOException e3){
                System.err.print(e3.getMessage());
            }
        }

    }
    /**
     * Devuelve un vuelo aleatorio entre las compañias existentes
     * Para ello usaremos un nº aleatorio entero que se comparará con el código hash
     * si coinciden, esa será la compañia que generará el vuelo
     *
     * @return Vuelo
     */
    public Vuelo generaVuelo(){
        Set<String> companys = compañias.keySet(); //obtenemos el cjto de claves, las compañias
        //Random r = new Random(); //Objeto aleatorio
        int rand = (int)(Math.random()*companys.size());
        String clave = "";
        
        /*while(clave.equals("")){ //No ha encontrado aún una compañia con hashcode == aleatorio
            int aleatorio = r.nextInt();
            for(String company:companys){ //recorremos el cjto
                if(company.hashCode() == aleatorio)
                    clave = company;
            }
        }*/

        Iterator<String> it = companys.iterator();
        int i=1;
        while(i<rand){
            clave = it.next();
            i++;
        }
        //Obtenemos el acrónimo y construimos el Vuelo
        return (new Vuelo(compañias.get(it.next())));
    }
    /**
     * @param salida
     * @param intentos
     * @param slots
     * Donde Salida será el nombre del fichero donde se volcará la salida
     * Intentos el nº máx de aviones que podrán intentar aterrizar en cada slot
     * y slots el nº total de slots del aeropuerto
     * 
     * En cada slot de tiempo habrá peticiones de aterrizaje y despegues y todo 
     * dependerá del nº de aviones que haya en cada momento en el airport
     */
    public void simulación(String salida,int intentos, int slots){
       BufferedWriter bfw=null;
        try{
           //creamos recurso para escribir en fichero
           bfw = new BufferedWriter(new FileWriter(salida));
           int contador = 0; //para controlar los movimientos en cada slot y compararlo con tamSlot

            for(int slot=1;slot<=slots;slot++){  //para cada slot de tiempo
                bfw.write("Slot "+slot+":\n"); //Escribimos el slot en el fichero
                bfw.write("Aviones en el Aeropuerto: [");
                int j=1;
                for(Vuelo vuelo:vuelos){
                    if((vuelos.size() == 1) || (j == vuelos.size())){
                       bfw.write(vuelo.toString());
                       break;
                     }
                     else{
                        bfw.write(vuelo.toString()+", ");
                        j++;
                     }
                }
                bfw.write("] ("+vuelos.size()+")\n"); //Cierra el aeropuerto y dice nº aviones en el
                //----------- Aterrizajes ---------
                bfw.write("Peticiones de Aterrizaje: ");
                //nº de vuelos que intentan aterrizar
                int inAir = (int)(Math.random()*intentos);
                bfw.write(inAir+" Intento/s\n");
                contador = 0;
                for(j=1;j<=inAir;j++){ //para todos los vuelos q intentar aterrizar
                    Vuelo v = generaVuelo(); //crea el vuelo
                    if(estaLleno())
                        bfw.write(v.toString()+" Desviado a otro Aeropuerto\n");
                    else{ // PERO NO pueden aterrizar mas que el tamSlot del airport
                        if(contador<tamSlot){
                            v.aterriza(slot);
                            vuelos.add(v);
                            bfw.write(v.toString()+" Aterriza\n");
                            contador++; //para saber que los q aterrizan no superen el tamSlot
                            //bfw.write("Contador "+contador+"\n");
                        }
                    }
                }
                //------------- Despegues ---------------
                 //nº de vuelos que intentan despegar
                bfw.write("Despegues: ");
                int inEarth = (int)(Math.random()*intentos);
                bfw.write(inEarth+" Intento/s\n");
                //bfw.write("Contador "+contador+"\n");

                    for(j=1;j<=inEarth;j++){
                        if((contador < tamSlot) && (vuelos.size()>0)){
                            Iterator<Vuelo> it = vuelos.iterator();
                            Vuelo vu = it.next();
                            vu.despega(slot);
                            bfw.write(vu.toString()+" Despega\n");
                            vuelos.remove(vu);
                            contador++;
                            //bfw.write("Contador "+contador+"\n");
                        }
                    }
           }
       }catch(IOException ioe){
            System.err.print(ioe.getMessage());
       }finally{
           try{
            bfw.close();
            }catch(IOException e2){
                System.err.print(e2.getMessage());
            }
       }
    }

    public boolean estaLleno(){
        return capacidad == vuelos.size();
    }

}
