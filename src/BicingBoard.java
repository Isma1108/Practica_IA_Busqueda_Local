//todos los imports que necesitemos
import IA.Bicing.*;

import java.util.*;


public class BicingBoard {

  //atributos privados:
<<<<<<< HEAD
  
  private static int num_furgonetas;

  private static int num_estaciones;

  private static Estaciones est;

=======
  private static Estaciones estaciones = null;
  private int[] origenFurgonetas = {};
  private Pair[][] destinoFurgonetas = {};
>>>>>>> origin

  //constructores
  
  public BicingBoard(Estaciones e, int nfurg) {
  }


  //Ninguna furgoneta se usa
  public void generar_solucion_trivial() {
  }
  
  public void generar_solucion_random() {


  }
  
  public void generar_solucion_voraz() {


  }

  //Operador de prueba:
  //Cambiar destino y descargar todas las bicis

  public void cambiar_destino(int ifurg, int iest) {
  }

  //Comprovacion de poder usar el operador:
  public boolean puede_ir(int ifurg, int iest) {
  }

  public int getNumFurgos() {
  }

  public ArrayList<Furgoneta> getFurgonetas() {
  }

  public int getNumEstaciones() {
  }

  public double getBeneficio() {

<<<<<<< HEAD
  }
  public double getPrecio1() {

  }
  public double getPrecio2() {

  }
  
  public static int max(int numero1, int numero2) {
    if (numero1 > numero2) return numero1;
    else return numero2;
  }
}
=======
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
>>>>>>> origin
