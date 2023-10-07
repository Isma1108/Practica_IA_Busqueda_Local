//todos los imports que necesitemos
import IA.Bicing.Estaciones;

import java.util.*;


public class BicingBoard {

  //atributos privados:
  private static Estaciones estaciones = null;
  private int[] origenFurgonetas = {};
  private Pair[][] destinoFurgonetas = {};

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
  
  }
  


};

class Pair {
  public int first;
  public int second;

  public Pair(int f, int s) {
    this.first = f;
    this.second = second;
  }

  public boolean equalsTo(Pair p) {
    return this.first == p.first && this.second == p.second;
  }

  public Pair values() {
    return new Pair(this.first, this.second);
  }
};
