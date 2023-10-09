//todos los imports que necesitemos
import IA.Bicing.*;

import java.util.Random;


public class BicingBoard {

  private static int ORIGEN = 0;
  private static int DESTINO1 = 1;
  private static int BICIS1 = 2;
  private static int DESTINO2 = 3;
  private static int BICIS2 = 4;


  private static Estaciones estaciones;
  private int[][] furgonetas;

  private boolean[] origenOcupado;
  /*
   * Si origenOcupado[i] = true significa que hay una Furgoneta que parte de
   * estacion-i hacia un destino estacion-j
  */

  private int[] bicisSigHora; // Contar las bicis que habran en la siguiente hora

  //constructores
  
  public BicingBoard(Estaciones e, int nfurg) {
    estaciones = e;
    bicisSigHora = new int[e.size()];
    for (int i = 0; i < bicisSigHora.length; i++) {
      bicisSigHora[i] = 0;
    }
    furgonetas = new int[Math.min(nfurg, estaciones.size())][5];
    for (int i = 0; i < nfurg; i++) {
      furgonetas[i][0] = -1;
      furgonetas[i][1] = -1;
      furgonetas[i][3] = -1;
    }
    origenOcupado = new boolean[e.size()];
    // By default boolean is false
    // for (int i = 0; i < origenOcupado.length; i++) origenOcupado[i] = false;
  }


  //Ninguna furgoneta se usa
  public void generar_solucion_trivial() {
    for (int i = 0; i < furgonetas.length; i++)
      furgonetas[i][ORIGEN] = i;
    for (int i = 0; i < bicisSigHora.length; i++)
      bicisSigHora[i] = estaciones.get(i).getNumBicicletasNoUsadas() + estaciones.get(i).getNumBicicletasNext();
  }
  
  public void generar_solucion_random() {
    for (int i = 0; i < furgonetas.length; i++)
      furgonetas[i][ORIGEN] = i;
    Random random = new Random();
    for (int i = 0; i < furgonetas.length; i++) { // Barajamos los origenes de las furgonetas
      int randomIndexToSwap = random.nextInt(furgonetas.length);
      int temp = furgonetas[randomIndexToSwap][ORIGEN];
      furgonetas[randomIndexToSwap][ORIGEN] = furgonetas[i][ORIGEN];
      furgonetas[i][ORIGEN] = temp;
    }
    for (int i = 0; i < furgonetas.length; i++) {
      destinoFullRandom(furgonetas[i], random);
    }
  }

  private void destinoFullRandom(int[] furgoneta, Random random) {
    // -1: no va a ninguna estacion
    furgoneta[DESTINO1] = -random.nextInt(0, 2);
    if (furgoneta[DESTINO1] != -1) {
      do furgoneta[DESTINO1] = random.nextInt(0, estaciones.size());
      while (furgoneta[DESTINO1] == furgoneta[ORIGEN]);

      int bicisNoUsadas = estaciones.get(furgoneta[ORIGEN]).getNumBicicletasNoUsadas();

      furgoneta[BICIS1] = random.nextInt(0, Math.min(30, bicisNoUsadas) + 1);
      estaciones.get(furgoneta[ORIGEN]).setNumBicicletasNoUsadas(bicisNoUsadas - furgoneta[2]);
      bicisSigHora[furgoneta[DESTINO1]] += furgoneta[BICIS1];

      furgoneta[DESTINO2] = -random.nextInt(0, -1);
      if (furgoneta[DESTINO2] != -1) {
        do furgoneta[DESTINO2] = random.nextInt(0, estaciones.size());
        while (furgoneta[DESTINO2] == furgoneta[ORIGEN] && furgoneta[DESTINO2] == furgoneta[DESTINO1]);

        bicisNoUsadas = estaciones.get(furgoneta[ORIGEN]).getNumBicicletasNoUsadas();

        furgoneta[BICIS2] = random.nextInt(0, Math.min(30, bicisNoUsadas) + 1);
        estaciones.get(furgoneta[ORIGEN]).setNumBicicletasNoUsadas(bicisNoUsadas - furgoneta[BICIS2]);
        bicisSigHora[furgoneta[DESTINO2]] += furgoneta[BICIS2];
      }
    }
  }

  private void destinoGreedyRandom(int[] furgoneta, Random random) {
    
  }
  
  public void generar_solucion_voraz() {


  }

  //Operadores:
  //Python suca blyad


  //Calidad de la solución + heurísticas

  public double getBeneficio() {
    return 0.0; 
  }

  public double getPrecio1() {
    return 0.0; 
  }
  public double getPrecio2() {
    return 0.0; 
  }
  

  //Maximo de 2 numeros
  public static int max(int numero1, int numero2) {
    if (numero1 > numero2) return numero1;
    else return numero2;
  }
};
