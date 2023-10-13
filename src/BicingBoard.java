//todos los imports que necesitemos
import IA.Bicing.*;

import java.util.Random;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;


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
    for (int i = 0; i < furgonetas.length; i++) {
      furgonetas[i][ORIGEN] = -1;
      furgonetas[i][DESTINO1] = -1;
      furgonetas[i][DESTINO2] = -1;
    }
    origenOcupado = new boolean[e.size()];
    // By default boolean is false
    // for (int i = 0; i < origenOcupado.length; i++) origenOcupado[i] = false;
  }

  public BicingBoard(int[][] furgs) {
    furgonetas = new int[furgs.length][5];
    for (int i = 0; i < furgs.length; ++i) {
      furgonetas[i][ORIGEN] = furgs[i][ORIGEN];
      furgonetas[i][DESTINO1] = furgs[i][DESTINO1];
      furgonetas[i][BICIS1] = furgs[i][BICIS1];
      furgonetas[i][DESTINO2] = furgs[i][DESTINO2];
      furgonetas[i][BICIS2] = furgs[i][BICIS2];
    }
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
    if (estaciones.size() <= 1) return; // Caso base: 1 o menos estaciones, la mejor opcion es no hacer nada
    // -1: no va a ninguna estacion
    furgoneta[DESTINO1] = -random.nextInt(0, 2);
    if (furgoneta[DESTINO1] != -1) {
      do furgoneta[DESTINO1] = random.nextInt(0, estaciones.size());
      while (furgoneta[DESTINO1] == furgoneta[ORIGEN] && estaciones.size() > 1);

      int bicisNoUsadas = estaciones.get(furgoneta[ORIGEN]).getNumBicicletasNoUsadas();

      furgoneta[BICIS1] = random.nextInt(0, Math.min(30, bicisNoUsadas) + 1);
      bicisSigHora[furgoneta[DESTINO1]] += furgoneta[BICIS1];
      bicisNoUsadas = bicisNoUsadas - furgoneta[BICIS1];

      furgoneta[DESTINO2] = -random.nextInt(0, 1);
      if (furgoneta[DESTINO2] != -1 && estaciones.size() > 1) {
        do furgoneta[DESTINO2] = random.nextInt(0, estaciones.size());
        while (furgoneta[DESTINO2] == furgoneta[ORIGEN] && furgoneta[DESTINO2] == furgoneta[DESTINO1]);
        furgoneta[BICIS2] = random.nextInt(0, Math.min(30, bicisNoUsadas) + 1);
      
        bicisSigHora[furgoneta[DESTINO2]] += furgoneta[BICIS2];
      }
    }
  }

  private void destinoGreedyRandom(int[] furgoneta, Random random) {
    
  }

  // genera una solución voraz: Furgoneta en estación con menos demanda se dirige a la que tiene más demanda
  // La segunda con menos demanda a la segunda con más, y así
  
  public void generar_solucion_voraz1() {
    //Arrays.sort(SrtDemands, new DemandCompare());
    Collections.sort(estaciones, new Comparator<Estacion>() {
      public int compare (Estacion a, Estacion b) {
        return a.getDemanda() - b.getDemanda();
      }
    });

    for (int i = 0; i < estaciones.size(); ++i) {
      System.out.println(estaciones.get(i).getDemanda());
    }

    for (int i = 0; i < furgonetas.length; ++i) {
      furgonetas[i][ORIGEN] = i;
      furgonetas[i][DESTINO1] = furgonetas.length - 1 - i;
      furgonetas[i][BICIS1] = estaciones.get(furgonetas[i][ORIGEN]).getNumBicicletasNoUsadas();
      bicisSigHora[furgonetas[i][DESTINO1]] = furgonetas[i][BICIS1] + estaciones.get(furgonetas[i][DESTINO1]).getNumBicicletasNext();
    }

  }

   public void generar_solucion_voraz2() {
    //Arrays.sort(SrtDemands, new DemandCompare());
    Collections.sort(estaciones, new Comparator<Estacion>() {
      public int compare (Estacion a, Estacion b) {

        int aDif = a.getDemanda() - a.getNumBicicletasNext();
        int bDif = b.getDemanda() - b.getNumBicicletasNext();

        return aDif - bDif;
      }
    });

    for (int i = 0; i < furgonetas.length; ++i) {
      furgonetas[i][ORIGEN] = i;
      furgonetas[i][DESTINO1] = furgonetas.length - 1 - i;
      furgonetas[i][BICIS1] = estaciones.get(furgonetas[i][ORIGEN]).getNumBicicletasNoUsadas();
      bicisSigHora[furgonetas[i][DESTINO1]] = furgonetas[i][BICIS1] + estaciones.get(furgonetas[i][DESTINO1]).getNumBicicletasNext();
    }

  }

  //Operadores:
  
  public void cambiar_destino1(int ifurg, int iest) {
    furgonetas[ifurg][DESTINO1] = iest;
  }

  public void cambiar_destino2(int ifurg, int iest) {
    furgonetas[ifurg][DESTINO2] = iest;
  }

  public boolean puede_cambiar_destino1(int ifurg, int iest) {
    return furgonetas[ifurg][ORIGEN] != -1;
  }
  
  public boolean puede_cambiar_destino2(int ifurg, int iest) {
    return furgonetas[ifurg][DESTINO2] != -1;
  }

  


  //Calidad de la solución + heurísticas

  public double getBeneficio() {
    double bnf = 0.0;
    int[] bicis_t = new int[estaciones.size()];
    int[] bicis_q = new int[estaciones.size()];
    
    
    for (int i = 0; i < furgonetas.length; ++i) {
      if (furgonetas[i][ORIGEN] != -1 && furgonetas[i][DESTINO1] != -1) {
        bicis_q[furgonetas[i][ORIGEN]] -= (furgonetas[i][BICIS1] + furgonetas[i][BICIS2]);
        bicis_t[furgonetas[i][DESTINO1]] += furgonetas[i][BICIS1];
        if (furgonetas[i][DESTINO2] != -1) bicis_t[furgonetas[i][DESTINO2]] += furgonetas[i][BICIS2];

        //El coste del transporte
        int x_orig = estaciones.get(furgonetas[i][ORIGEN]).getCoordX();
        int y_orig = estaciones.get(furgonetas[i][ORIGEN]).getCoordY();
      
        int x_d1 = estaciones.get(furgonetas[i][DESTINO1]).getCoordX();
        int y_d1 = estaciones.get(furgonetas[i][DESTINO1]).getCoordY();
      
        int recorrido1 = dist(x_orig, y_orig, x_d1, y_d1);
        bnf -= ((double) (furgonetas[i][BICIS1] + furgonetas[i][BICIS2] + 9) / 10)*recorrido1/1000; 
      

        if (furgonetas[i][DESTINO2] != -1) {
          int x_d2 = estaciones.get(furgonetas[i][DESTINO1]).getCoordX();
          int y_d2 = estaciones.get(furgonetas[i][DESTINO2]).getCoordY();
          int recorrido2 = dist(x_d1, y_d1, x_d2, y_d2);
          bnf -= ((double) (furgonetas[i][BICIS2] + 9) / 10)*recorrido2/1000; 
        }
      }
    }

    //Ahora los gastos/beneficio por alejarme/acercarme a la demanda.
    for (int i = 0; i < estaciones.size(); ++i) {
      //Beneficio
      if (estaciones.get(i).getDemanda() - (estaciones.get(i).getNumBicicletasNext() + bicis_t[i]) <= 0) 
        bnf += Math.max(0, estaciones.get(i).getDemanda() - estaciones.get(i).getNumBicicletasNext());
      else bnf += bicis_t[i];

      //Coste
      if (estaciones.get(i).getDemanda() - (estaciones.get(i).getNumBicicletasNext() - bicis_q[i]) > 0)
        bnf -= (bicis_q[i] - Math.max(0, estaciones.get(i).getNumBicicletasNext() - estaciones.get(i).getDemanda()));
    }
    return bnf;
  }

  public double getPrecio1() {
    return 0.0; 
  }
  public double getPrecio2() {
    return -getBeneficio();
  }

  //Getters
  public int[][] getFurgonetas() {
    return furgonetas;
  }

  public int getNumFurgos() {
    return furgonetas.length;
  }

  public int getNumEstaciones() {
    return estaciones.size();
  }

  

  private static int dist(int x1, int y1, int x2, int y2) {
    return Math.abs(x2 - x1) + Math.abs(y2 - y1);
  }
};
