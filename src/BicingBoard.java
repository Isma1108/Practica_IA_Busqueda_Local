//todos los imports que necesitemos
import IA.Bicing.Estaciones;
import java.util.*;



public class BicingBoard {

  /*Estado
  * static Estaciones
  * Vector(F) id-Estacion Origen
  * Vector(F) id-EstacionDestino,BicisATrasladar [0..2]  (Suma de bicis <= 30)
  */

  //atributos privados:
  private static Estaciones estaciones = null;
  private int[] origenFurgoneta = {}; //  id-EstacionOrigen
  private Pair[][] destinosFurgoneta = {}; // F[i] = destino-1{, destino-2}

    //constructores

  public BicingBoard(Estaciones est, int nfurg, int estado_ini) {
    //Recorro las estaciones y me guardo informacion
    

    //Luego, en funcion de que estado_ini queremos
    if (estado_ini == 0) {

      //Construimos una solucion inicial
    }
    else {
      //u otra
    }
    if (estaciones == null) estaciones = est; //
    origenFurgoneta = new int[nfurg];
    destinosFurgoneta = new Pair[nfurg][0];
  }
};

class Pair {
  public int first;
  public int second;

  public Pair(int first, int second) {
    this.first = first;
    this.second = second;
  }

  public boolean equalsTo(Pair p) {
    return this.first == p.first && this.second == p.second;
  }

  public Pair values() {
    return new Pair(this.first, this.second);
  }
};
