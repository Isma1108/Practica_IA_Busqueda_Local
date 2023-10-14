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
  private int[] furgosLibres; // aun no han sido usadas todas las bicis

  private boolean[] origenOcupado;
  /*
   * Si origenOcupado[i] = true significa que hay una Furgoneta que parte de
   * estacion-i hacia un destino estacion-j
  */

  //constructores

  
  public BicingBoard(Estaciones e, int nfurg) {
    estaciones = e;    

    furgonetas = new int[Math.min(nfurg, estaciones.size())][5];
    furgosLibres = new int[furgonetas.length];
    for (int i = 0; i < furgonetas.length; i++) {
      furgonetas[i][ORIGEN] = -1;
      furgonetas[i][DESTINO1] = -1;
      furgonetas[i][DESTINO2] = -1;
      furgosLibres[i] = 0;
    }
    origenOcupado = new boolean[e.size()];
    // By default boolean is false
    // for (int i = 0; i < origenOcupado.length; i++) origenOcupado[i] = false;
  }

  public BicingBoard(int[][] furgs, int[] furgLib) {
    furgonetas = new int[furgs.length][5];
    furgosLibres = new int[furgs.length];
    for (int i = 0; i < furgs.length; ++i) {
      furgonetas[i][ORIGEN] = furgs[i][ORIGEN];
      furgonetas[i][DESTINO1] = furgs[i][DESTINO1];
      furgonetas[i][BICIS1] = furgs[i][BICIS1];
      furgonetas[i][DESTINO2] = furgs[i][DESTINO2];
      furgonetas[i][BICIS2] = furgs[i][BICIS2];
      furgosLibres[i] = furgLib[i];
    }
  }

  //Ninguna furgoneta se usa
  public void generar_solucion_trivial() {
    for (int i = 0; i < furgonetas.length; i++)
      furgonetas[i][ORIGEN] = i;
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
      destinoGreedyRandom(furgonetas[i], random, i);
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
      bicisNoUsadas = bicisNoUsadas - furgoneta[BICIS1];

      furgoneta[DESTINO2] = -random.nextInt(0, 1);
      if (furgoneta[DESTINO2] != -1 && estaciones.size() > 1) {
        do furgoneta[DESTINO2] = random.nextInt(0, estaciones.size());
        while (furgoneta[DESTINO2] == furgoneta[ORIGEN] && furgoneta[DESTINO2] == furgoneta[DESTINO1]);
        furgoneta[BICIS2] = random.nextInt(0, Math.min(30, bicisNoUsadas) + 1);
      
      }
    }
  }

  private void destinoGreedyRandom(int[] furgoneta, Random random, int i) {
    furgoneta[DESTINO1] = -random.nextInt(0, 2);
    if (furgoneta[DESTINO1] != -1 && estaciones.size() > 1) {
      do furgoneta[DESTINO1] = random.nextInt(0, estaciones.size());
      while (furgoneta[DESTINO1] == furgoneta[ORIGEN] && estaciones.size() > 1);

      int bicisNoUsadas = estaciones.get(furgoneta[ORIGEN]).getNumBicicletasNoUsadas();
      int bicisSigHora = estaciones.get(furgoneta[ORIGEN]).getNumBicicletasNext();
      int demanda = estaciones.get(furgoneta[ORIGEN]).getDemanda();
      int diff = bicisNoUsadas + bicisSigHora - demanda;

      furgoneta[BICIS1] = random.nextInt(0, Math.max(0,Math.min(bicisNoUsadas, diff)) + 1);
      bicisNoUsadas = bicisNoUsadas - furgoneta[BICIS1];
      diff = diff - furgoneta[BICIS1];

      furgosLibres[i] = bicisNoUsadas;

      furgoneta[DESTINO2] = -random.nextInt(0, 1);
      if (furgoneta[DESTINO2] != -1 && estaciones.size() > 2) {
        do furgoneta[DESTINO2] = random.nextInt(0, estaciones.size());
        while (furgoneta[DESTINO2] == furgoneta[ORIGEN] && furgoneta[DESTINO2] == furgoneta[DESTINO1]);
        furgoneta[BICIS2] = random.nextInt(0, Math.max(0,Math.min(bicisNoUsadas, diff)) + 1);

      }
    }
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

    for (int i = 0; i < furgonetas.length; ++i) {
      furgonetas[i][ORIGEN] = i;

      /*
      furgonetas[i][DESTINO1] = furgonetas.length - 1 - i;
      furgonetas[i][BICIS1] = estaciones.get(furgonetas[i][ORIGEN]).getNumBicicletasNoUsadas();

      */

      int puedeUsar = calcula_diffOrig(estaciones.get(i));

      int cargaTemp = Math.max(0, Math.min(puedeUsar,30)); // lo que se lleva del origen

      int destinoBicis = calcula_diffDest(estaciones.get(furgonetas.length - 1 - i));   // si es positivo, enviamos

      if (cargaTemp - destinoBicis <= 0) {
        furgonetas[i][DESTINO1] = -1;
        furgonetas[i][BICIS1] = 0;
        furgosLibres[i] = cargaTemp;
      }

      else {
        furgonetas[i][DESTINO1] = furgonetas.length - 1 - i;
        furgonetas[i][BICIS1] = Math.min(cargaTemp, destinoBicis);
        furgosLibres[i] = Math.max(0, cargaTemp - destinoBicis);
      }
    }

  }

   public void generar_solucion_voraz2() {
    Collections.sort(estaciones, new Comparator<Estacion>() {
      public int compare (Estacion a, Estacion b) {

        int aDif = a.getDemanda() - a.getNumBicicletasNext();
        int bDif = b.getDemanda() - b.getNumBicicletasNext();

        return aDif - bDif;
      }
    });

    for (int i = 0; i < furgonetas.length; ++i) {
      furgonetas[i][ORIGEN] = i;

      /*
      furgonetas[i][DESTINO1] = furgonetas.length - 1 - i;
      furgonetas[i][BICIS1] = estaciones.get(furgonetas[i][ORIGEN]).getNumBicicletasNoUsadas();

      */

      int puedeUsar = calcula_diffOrig(estaciones.get(i));

      int cargaTemp = Math.max(0, Math.min(puedeUsar,30)); // lo que se lleva del origen

      int destinoBicis = calcula_diffDest(estaciones.get(furgonetas.length - 1 - i));   // si es positivo, enviamos

      if (cargaTemp - destinoBicis <= 0) {
        furgonetas[i][DESTINO1] = -1;
        furgonetas[i][BICIS1] = 0;
        furgosLibres[i] = cargaTemp;
      }

      else {
        furgonetas[i][DESTINO1] = furgonetas.length - 1 - i;
        furgonetas[i][BICIS1] = Math.min(cargaTemp, destinoBicis);
        furgosLibres[i] = Math.max(0, cargaTemp - destinoBicis);
      }
    }
  }

  public int calcula_diffOrig(Estacion est) {
    return est.getNumBicicletasNoUsadas() - (est.getDemanda() - est.getNumBicicletasNext());
  }

  public int calcula_diffDest(Estacion est) {
    return est.getDemanda() - est.getNumBicicletasNext();
  }

  //Operadores:
  
  public void cambiarDestino1(int ifurg, int iest) {
    furgonetas[ifurg][DESTINO1] = iest;
  }

  public boolean puede_cambiar_destino1(int ifurg, int iest) {
    return furgonetas[ifurg][DESTINO1] != -1;
  }  

  public void cambiarDestino2(int ifurg, int iest) {

    furgonetas[ifurg][DESTINO2] = iest;
    furgonetas[ifurg][BICIS2] = furgosLibres[ifurg];
  }

  public void swap_origins(int ifurg1, int ifurg2) {
    int temp = furgonetas[ifurg1][ORIGEN];
    furgonetas[ifurg1][ORIGEN] = furgonetas[ifurg2][ORIGEN];
    furgonetas[ifurg1][BICIS1] = estaciones.get(furgonetas[ifurg1][ORIGEN]).getNumBicicletasNoUsadas();
    furgonetas[ifurg2][ORIGEN] = temp;
    furgonetas[ifurg2][BICIS1] = estaciones.get(furgonetas[ifurg2][ORIGEN]).getNumBicicletasNoUsadas();
  }

  public void swap_destination1(int ifurg1, int ifurg2) {
    int temp = furgonetas[ifurg1][DESTINO1];
    furgonetas[ifurg1][DESTINO1] = furgonetas[ifurg2][DESTINO1];
    furgonetas[ifurg2][DESTINO1] = temp;
  }

  public void eliminarDestino2(int ifurg) {
    furgonetas[ifurg][DESTINO2] = -1;
    furgonetas[ifurg][BICIS2] = 0;
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
        //bnf -= ((double) (furgonetas[i][BICIS1] + furgonetas[i][BICIS2] + 9) / 10)*recorrido1/1000; 
      

        if (furgonetas[i][DESTINO2] != -1) {
          int x_d2 = estaciones.get(furgonetas[i][DESTINO1]).getCoordX();
          int y_d2 = estaciones.get(furgonetas[i][DESTINO2]).getCoordY();
          int recorrido2 = dist(x_d1, y_d1, x_d2, y_d2);
          //bnf -= ((double) (furgonetas[i][BICIS2] + 9) / 10)*recorrido2/1000; 
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
    double bnf = 0.0;
    int[] bicis_t = new int[estaciones.size()];
    int[] bicis_q = new int[estaciones.size()];
    
    
    for (int i = 0; i < furgonetas.length; ++i) {
      if (furgonetas[i][ORIGEN] != -1 && furgonetas[i][DESTINO1] != -1) {
        bicis_q[furgonetas[i][ORIGEN]] -= (furgonetas[i][BICIS1] + furgonetas[i][BICIS2]);
        bicis_t[furgonetas[i][DESTINO1]] += furgonetas[i][BICIS1];
        if (furgonetas[i][DESTINO2] != -1) bicis_t[furgonetas[i][DESTINO2]] += furgonetas[i][BICIS2];
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
    return -bnf;
  }
  public double getPrecio2() {
    return -getBeneficio();
  }

  //Getters
  public int[][] getFurgonetas() {
    return furgonetas;
  }
  public int[] getLibres() {
    return furgosLibres;
  }


  public int getNumFurgos() {
    return furgonetas.length;
  }

  public int getNumEstaciones() {
    return estaciones.size();
  }

  public int getDestino1(int ifurg) {
    return furgonetas[ifurg][DESTINO1];
  }

  public int getDestino2(int ifurg) {
    return furgonetas[ifurg][DESTINO2];
  }

  public int getOrigen(int ifurg) {
    return furgonetas[ifurg][ORIGEN];
  }

  public boolean validChoice(int ifurg, int iest) {
    return furgonetas[ifurg][ORIGEN] != iest && furgonetas[ifurg][DESTINO1] != iest && furgonetas[ifurg][DESTINO2] != iest;
  }

  public Estaciones getEstaciones() {
    return estaciones;
  }

  public final int getBicisDestino1(int ifurg) {
    return furgonetas[ifurg][BICIS1];
  }

  public final int getBicisDestino2(int ifurg) {
    return furgonetas[ifurg][BICIS2];
  }

  public int getSinUsar(int ifurg) {
    return furgosLibres[ifurg];
  }


  

  private static int dist(int x1, int y1, int x2, int y2) {
    return Math.abs(x2 - x1) + Math.abs(y2 - y1);
  }
};
