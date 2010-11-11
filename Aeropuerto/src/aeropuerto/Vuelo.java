

package aeropuerto;

/**
 *
 * @author Indalecio Abril Palanco
 */
public class Vuelo {

    private String código;
    private int aterriza;  //Slot en el que aterriza
    private int despega;  //Slot en el que despega

    //Se usa para garantizar la unicidad del id de la compañía
    private static int id=0;

    public Vuelo(String acro){
        Vuelo.id++; //Se incrementa el identificador ESTATICO que comparten TODOS
        //Los otros dos atributos, al ser primitivos, quedarán inicializados
        //por defecto, al ser atributos de la clase
         código = acro+id;
    }

    public int getAterriza(){
        return aterriza;
    }

    public int getDespega(){
        return despega;
    }

    public String getCódigo(){
        return código;
    }

    public void aterriza(int slot){
        aterriza = slot;
    }

    public void despega(int slot){
        despega = slot;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Vuelo)) return false;

        return código.equalsIgnoreCase(((Vuelo)o).getCódigo());
    }

    @Override
    public int hashCode(){
        return código.hashCode();
    }

    @Override
    public String toString(){
        return "["+código+" ,"+(aterriza == 0 ? "?" :aterriza)+
                " ,"+(despega == 0 ? "?":despega)+"]";
    }
    
}
