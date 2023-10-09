//todos los imports que necesitemos
import IA.Bicing.*;

import java.util.Random;


public class BicingBoard {

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
      furgonetas[i][0] = i;
    for (int i = 0; i < bicisSigHora.length; i++)
      bicisSigHora[i] = estaciones.get(i).getNumBicicletasNoUsadas() + estaciones.get(i).getNumBicicletasNext();
  }
  
  public void generar_solucion_random() {
    for (int i = 0; i < furgonetas.length; i++)
      furgonetas[i][0] = i;
    Random random = new Random();
    for (int i = 0; i < furgonetas.length; i++) { // Barajamos los origenes de las furgonetas
      int randomIndexToSwap = random.nextInt(furgonetas.length);
      int temp = furgonetas[randomIndexToSwap][0];
      furgonetas[randomIndexToSwap][0] = furgonetas[i][0];
      furgonetas[i][0] = temp;
    }
    for (int i = 0; i < furgonetas.length; i++) {
      destinoFullRandom(furgonetas[i], random);
    }
  }

  private void destinoFullRandom(int[] furgoneta, Random random) {
    furgoneta[1] = random.nextInt(-1, 1);
    if (furgoneta[1] != -1) {
      do furgoneta[1] = random.nextInt(0, estaciones.size());
      while (furgoneta[1] != furgonetas[i][0]);

      int bicisNoUsadas = estaciones.get(furgoneta[1]).getNumBicicletasNoUsadas();

      furgoneta[2] = random.nextInt(1, Math.min(30, bicisNoUsadas));
      estaciones.get(furgoneta[1]).setNumBicicletasNoUsadas(bicisNoUsadas - furgoneta[2]);
      bicisSigHora[furgoneta[1]] += furgoneta[2];
      furgoneta[3] = random.nextInt(-1, 1);

      if (furgoneta[3] != -1) {
        do furgoneta[3] = random.nextInt(0, estaciones.size());
        while (furgoneta[3] != furgoneta[0] && furgoneta[3] != furgoneta[1]);

        bicisNoUsadas = estaciones.get(furgoneta[1]).getNumBicicletasNoUsadas();

        furgoneta[4] = random.nextInt(1, Math.min(30, bicisNoUsadas));
        estaciones.get(furgoneta[3]).setNumBicicletasNoUsadas(bicisNoUsadas - furgoneta[4]);
        bicisSigHora[furgoneta[3]] += furgoneta[4];
        furgoneta[3] = random.nextInt(-1, 1);

      }

    }
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

  public int[][] getFurgonetas() {
  }

  public int getNumEstaciones() {
  }

  public double getBeneficio() {
  }
  public double getPrecio1() {

  }
  public double getPrecio2() {

  }
  
  public static int max(int numero1, int numero2) {
    if (numero1 > numero2) return numero1;
    else return numero2;
  }
};