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
  private int[] bicis_dejadas;
  /*
   * Si origenOcupado[i] = true significa que hay una Furgoneta que parte de
   * estacion-i hacia un destino estacion-j
  */

  //-----------------CONSTRUCTORAS---------------------

  
  public BicingBoard(Estaciones e, int nfurg) {
    estaciones = e;    

    furgonetas = new int[Math.min(nfurg, estaciones.size())][5];
    for (int i = 0; i < furgonetas.length; i++) {
      furgonetas[i][ORIGEN] = -1;
      furgonetas[i][BICIS1] = 0;
      furgonetas[i][BICIS2] = 0;
      furgonetas[i][DESTINO1] = -1;
      furgonetas[i][DESTINO2] = -1;
    }
    origenOcupado = new boolean[e.size()];
    bicis_dejadas = new int[e.size()];
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

  public BicingBoard(boolean[] orig, int[][] furgs, int[] bicis_d) {
    furgonetas = new int[furgs.length][5];
    for (int i = 0; i < furgs.length; ++i) {
      furgonetas[i][ORIGEN] = furgs[i][ORIGEN];
      furgonetas[i][DESTINO1] = furgs[i][DESTINO1];
      furgonetas[i][BICIS1] = furgs[i][BICIS1];
      furgonetas[i][DESTINO2] = furgs[i][DESTINO2];
      furgonetas[i][BICIS2] = furgs[i][BICIS2];
    }
    origenOcupado = new boolean[orig.length];
    for (int i = 0; i < orig.length; ++i) {
      origenOcupado[i] = orig[i];
    }
    bicis_dejadas = new int[bicis_d.length];
    for (int i = 0; i < bicis_d.length; ++i) {
      bicis_dejadas[i] = bicis_d[i];
    }
  }

  //------------------------------------------------------------------------------

  //----------------------SOLUCIONES INICIALES--------------------------


  // Trivial (voraz):
  public void generar_solucion_trivial() {
    /*
    Collections.sort(estaciones, new Comparator<Estacion>() {
      public int compare (Estacion a, Estacion b) {

        int aDif = Math.min(a.getNumBicicletasNoUsadas(), Math.max(0, a.getNumBicicletasNext() - a.getDemanda()));
        int bDif = Math.min(b.getNumBicicletasNoUsadas(), Math.max(0, b.getNumBicicletasNext() - b.getDemanda()));
        
        return Integer.compare(bDif, aDif);
      }
    });
    
    */
    for (int i = 0; i < furgonetas.length; i++) {
      furgonetas[i][ORIGEN] = i;
    }
  }
  

  //Full random:
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
      update_bicis_dejadas(i);
    }
  }

  private void destinoFullRandom(int[] furgoneta, Random random) {
    if (estaciones.size() <= 1) return; // Caso base: 1 o menos estaciones, la mejor opcion es no hacer nada
    // -1: no va a ninguna estacion
    //furgoneta[DESTINO1] = -random.nextInt(0, 2);
    furgoneta[DESTINO1] = 0;
    if (furgoneta[DESTINO1] != -1) {
      do furgoneta[DESTINO1] = random.nextInt(0, estaciones.size());
      while (furgoneta[DESTINO1] == furgoneta[ORIGEN] && estaciones.size() > 1);

      //furgoneta[DESTINO2] = -random.nextInt(0, 2);
      furgoneta[DESTINO2] = 0;
      if (furgoneta[DESTINO2] != -1 && estaciones.size() > 2) {
        do furgoneta[DESTINO2] = random.nextInt(0, estaciones.size());
        while (furgoneta[DESTINO2] == furgoneta[ORIGEN] && furgoneta[DESTINO2] == furgoneta[DESTINO1]);
      }
    }
  }

  //Voraz 1 (ordenar en base la demanda)
  public void generar_solucion_voraz1() {
    //Arrays.sort(SrtDemands, new DemandCompare());
    Collections.sort(estaciones, new Comparator<Estacion>() {
      public int compare (Estacion a, Estacion b) {
        return a.getDemanda() - b.getDemanda();
      }
    });

    for (int i = 0; i < furgonetas.length; ++i) {
      furgonetas[i][ORIGEN] = i;
      furgonetas[i][DESTINO1] = estaciones.size() - 1 - i;
      furgonetas[i][BICIS1] = Math.max(0, getMaxBicis(furgonetas[i][ORIGEN]));
    }

  }

  //Voraz 2 (ordenar en funcion de las bicis que puedo cojer)
   public void generar_solucion_voraz2() {
    Collections.sort(estaciones, new Comparator<Estacion>() {
      public int compare (Estacion a, Estacion b) {

        int aDif = Math.min(a.getNumBicicletasNoUsadas(), Math.max(0, a.getNumBicicletasNext() - a.getDemanda()));
        int bDif = Math.min(b.getNumBicicletasNoUsadas(), Math.max(0, b.getNumBicicletasNext() - b.getDemanda()));
        
        return Integer.compare(bDif, aDif);
      }
    });

    Random rand = new Random();
    //rand.setSeed(1234);
    for (int i = 0; i < furgonetas.length; ++i) {
      furgonetas[i][ORIGEN] = i;
      origenOcupado[i] = true;
      furgonetas[i][DESTINO1] = estaciones.size() - 1 - i;
      //destinoFullRandom(furgonetas[i], rand);
      update_bicis_dejadas(i);
    }
  }

  //---------------------------OPERADORES--------------------------
  
  //Cambirar destino 1:
  public void cambiar_destino1(int ifurg, int iest) {
    if (furgonetas[ifurg][ORIGEN] == iest) {
      //La furgoneta passa a no hacer nada
      if (furgonetas[ifurg][DESTINO1] != -1) bicis_dejadas[furgonetas[ifurg][DESTINO1]] -= furgonetas[ifurg][BICIS1];
      if (furgonetas[ifurg][DESTINO2] != -1) bicis_dejadas[furgonetas[ifurg][DESTINO2]] -= furgonetas[ifurg][BICIS2];
      furgonetas[ifurg][DESTINO1] = -1;
      furgonetas[ifurg][DESTINO2] = -1;
      furgonetas[ifurg][BICIS1] = 0;
      furgonetas[ifurg][BICIS2] = 0;
      origenOcupado[furgonetas[ifurg][ORIGEN]] = false;
    }
    else {
      clean_bicis_dejadas(ifurg);
      furgonetas[ifurg][DESTINO1] = iest;
      update_bicis_dejadas(ifurg);
    }
  }

  //Cambiar destino 2:
  public void cambiar_destino2(int ifurg, int iest) {
    if (furgonetas[ifurg][ORIGEN] == iest) {
      //Ahora no hay destino 2
      if (furgonetas[ifurg][DESTINO2] != -1) {
        bicis_dejadas[furgonetas[ifurg][DESTINO1]] += furgonetas[ifurg][BICIS2];
        bicis_dejadas[furgonetas[ifurg][DESTINO2]] -= furgonetas[ifurg][BICIS2];
        furgonetas[ifurg][DESTINO2] = -1;
        furgonetas[ifurg][BICIS1] += furgonetas[ifurg][BICIS2];
        furgonetas[ifurg][BICIS2] = 0;
      }
    }
    else {
      clean_bicis_dejadas(ifurg);
      furgonetas[ifurg][DESTINO2] = iest;
      update_bicis_dejadas(ifurg);
    }
  }
  
  public boolean puede_cambiar_destino2(int ifurg, int iest) {
    return furgonetas[ifurg][DESTINO1] != -1 && furgonetas[ifurg][DESTINO1] != iest;
  }

  // No tiene sentido cambiar origen si no tiene destino o no tiene origen

  public boolean puede_cambiar_origen(int ifurg, int iest) {
    return furgonetas[ifurg][DESTINO1] != -1 && furgonetas[ifurg][ORIGEN] != -1 && !origenOcupado[iest] 
      && furgonetas[ifurg][DESTINO1] != iest && furgonetas[ifurg][DESTINO2] != iest;
  }


  public void cambiar_origen(int ifurg, int iest) {
    origenOcupado[furgonetas[ifurg][ORIGEN]] = false;
    furgonetas[ifurg][ORIGEN] = iest;
    origenOcupado[iest] = true;
    clean_bicis_dejadas(ifurg);
    update_bicis_dejadas(ifurg);
  }

  

  public boolean puede_swap_d1(int i, int j) {
    return furgonetas[i][DESTINO1] != -1 && furgonetas[j][DESTINO1] != -1;
  }


  //Swaps destinos 1
  public void swap_d1(int i, int j) {
    clean_bicis_dejadas(i);
    clean_bicis_dejadas(j);

    int tmp = furgonetas[i][DESTINO1];
    furgonetas[i][DESTINO1] = furgonetas[j][DESTINO1];
    furgonetas[j][DESTINO1] = tmp;

    update_bicis_dejadas(i);
    update_bicis_dejadas(j);
  }
  
  public boolean puede_swap_d2(int i, int j) {
    return furgonetas[i][DESTINO2] != -1 && furgonetas[j][DESTINO2] != -1;
  }

  //Swaps destinos 2
  public void swap_d2(int i, int j) {
    clean_bicis_dejadas(i);
    clean_bicis_dejadas(j);
    
    int tmp = furgonetas[i][DESTINO2];
    furgonetas[i][DESTINO2] = furgonetas[j][DESTINO2];
    furgonetas[j][DESTINO2] = tmp;
    
    update_bicis_dejadas(i);
    update_bicis_dejadas(j);
  }
  
  public boolean puede_swap_d12(int i, int j) {
    return furgonetas[i][DESTINO1] != -1 && furgonetas[j][DESTINO2] != -1;
  }
  

  //Swap destinos 1 con destinos 2 (no sirve ?)
  public void swap_d12(int i, int j) {
    clean_bicis_dejadas(i);
    clean_bicis_dejadas(j);
    
    int tmp = furgonetas[i][DESTINO1];
    furgonetas[i][DESTINO1] = furgonetas[j][DESTINO2];
    furgonetas[j][DESTINO2] = tmp;
    
    update_bicis_dejadas(i);
    update_bicis_dejadas(j);
  }
  
  public boolean puede_swap_d21(int i, int j) {
    return furgonetas[i][DESTINO2] != -1 && furgonetas[j][DESTINO1] != -1;
  }
  

  //Swap destinos 2 con destinos 1 (no sirve ?)
  public void swap_d21(int i, int j) {
    clean_bicis_dejadas(i);
    clean_bicis_dejadas(j);
    
    int tmp = furgonetas[i][DESTINO2];
    furgonetas[i][DESTINO2] = furgonetas[j][DESTINO1];
    furgonetas[j][DESTINO1] = tmp;
    
    update_bicis_dejadas(i);
    update_bicis_dejadas(j);
  }

  public boolean puede_furgo_swap(int ifurg) {
    return furgonetas[ifurg][DESTINO1] != -1 && furgonetas[ifurg][DESTINO2] != -1;
  }

  public void swap_furgo(int ifurg) {
    clean_bicis_dejadas(ifurg);
    int tmp = furgonetas[ifurg][DESTINO1];
    furgonetas[ifurg][DESTINO1] = furgonetas[ifurg][DESTINO2];
    furgonetas[ifurg][DESTINO2] = tmp;
    update_bicis_dejadas(ifurg);
  }


  //-------------------------------------
  //Update de las bicis dejadas en D1 y D2, de forma que se dejen todo lo que se 
  //pueda y se necesite en el D1, y lo que sobre en el D2

  public void update_bicis_dejadas(int ifurg) {
      if (furgonetas[ifurg][DESTINO1] != -1) {
        furgonetas[ifurg][BICIS1] = Math.max(0, getMaxBicis(furgonetas[ifurg][ORIGEN]));
        bicis_dejadas[furgonetas[ifurg][DESTINO1]] += furgonetas[ifurg][BICIS1];
      }
     
      //Recalculo las que se pueden dejar en D2 si es que existeD2

      if (furgonetas[ifurg][DESTINO2] != -1) {
          int bicis_d1 = Math.min(furgonetas[ifurg][BICIS1], bicis_dejadas[furgonetas[ifurg][DESTINO1]] - getBicisNecesitadas(furgonetas[ifurg][DESTINO1]));
          if (bicis_d1 < 0) bicis_d1 = 0;
          furgonetas[ifurg][BICIS2] = bicis_d1;
          furgonetas[ifurg][BICIS1] -= bicis_d1;
          bicis_dejadas[furgonetas[ifurg][DESTINO2]] += bicis_d1; 
          bicis_dejadas[furgonetas[ifurg][DESTINO1]] -= bicis_d1; 
      }
  }

  public void clean_bicis_dejadas(int ifurg) {
    if (furgonetas[ifurg][DESTINO1] != -1) bicis_dejadas[furgonetas[ifurg][DESTINO1]] -= furgonetas[ifurg][BICIS1];
    if (furgonetas[ifurg][DESTINO2] != -1) bicis_dejadas[furgonetas[ifurg][DESTINO2]] -= furgonetas[ifurg][BICIS2];
    furgonetas[ifurg][BICIS1] = 0;
    furgonetas[ifurg][BICIS2] = 0;
  }



  //--------------CALIDAD DE LA SOLUCION------------------------ 

  public double getBeneficio() {
    double bnf = 0.0;
    int[] bicis_t = new int[estaciones.size()];
    int[] bicis_q = new int[estaciones.size()];
    
    
    for (int i = 0; i < furgonetas.length; ++i) {
      if (furgonetas[i][ORIGEN] != -1 && furgonetas[i][DESTINO1] != -1) {
        bicis_q[furgonetas[i][ORIGEN]] += (furgonetas[i][BICIS1] + furgonetas[i][BICIS2]);
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
      bnf += Math.min(bicis_t[i], Math.max(0, estaciones.get(i).getDemanda() - estaciones.get(i).getNumBicicletasNext()));

      //Coste
      if ((estaciones.get(i).getDemanda() - (estaciones.get(i).getNumBicicletasNext() - bicis_q[i])) > 0)
        bnf -= (bicis_q[i] - Math.max(0, estaciones.get(i).getNumBicicletasNext() - estaciones.get(i).getDemanda()));
    }
    return bnf;
  }

  //--------------------HEURISTICAS-----------------

  //Heuristica 1:
  public double getPrecio1() {
    double bnf = 0.0;
    int[] bicis_t = new int[estaciones.size()];
    int[] bicis_q = new int[estaciones.size()];
    
    
    for (int i = 0; i < furgonetas.length; ++i) {
      if (furgonetas[i][ORIGEN] != -1 && furgonetas[i][DESTINO1] != -1) {
        bicis_q[furgonetas[i][ORIGEN]] += (furgonetas[i][BICIS1] + furgonetas[i][BICIS2]);
        bicis_t[furgonetas[i][DESTINO1]] += furgonetas[i][BICIS1];
        if (furgonetas[i][DESTINO2] != -1) bicis_t[furgonetas[i][DESTINO2]] += furgonetas[i][BICIS2];
      }
    }

    //Ahora los gastos/beneficio por alejarme/acercarme a la demanda.
    for (int i = 0; i < estaciones.size(); ++i) {
      //Beneficio
      bnf += Math.min(bicis_t[i], Math.max(0, estaciones.get(i).getDemanda() - estaciones.get(i).getNumBicicletasNext()));

      //Coste
      if ((estaciones.get(i).getDemanda() - (estaciones.get(i).getNumBicicletasNext() - bicis_q[i])) > 0)
        bnf -= (bicis_q[i] - Math.max(0, estaciones.get(i).getNumBicicletasNext() - estaciones.get(i).getDemanda()));
    }
    return -bnf;
  }

  //Heuristica 2:
  public double getPrecio2() {
    return -getBeneficio();
  }



  public double getCosteTransporte() {
    double coste = 0.0;
    for (int i = 0; i < furgonetas.length; ++i) {
      if (furgonetas[i][ORIGEN] != -1 && furgonetas[i][DESTINO1] != -1) {
        //El coste del transporte
        int x_orig = estaciones.get(furgonetas[i][ORIGEN]).getCoordX();
        int y_orig = estaciones.get(furgonetas[i][ORIGEN]).getCoordY();
      
        int x_d1 = estaciones.get(furgonetas[i][DESTINO1]).getCoordX();
        int y_d1 = estaciones.get(furgonetas[i][DESTINO1]).getCoordY();
      
        int recorrido1 = dist(x_orig, y_orig, x_d1, y_d1);
        coste += ((double) (furgonetas[i][BICIS1] + furgonetas[i][BICIS2] + 9) / 10)*recorrido1/1000; 
      
        if (furgonetas[i][DESTINO2] != -1) {
          int x_d2 = estaciones.get(furgonetas[i][DESTINO1]).getCoordX();
          int y_d2 = estaciones.get(furgonetas[i][DESTINO2]).getCoordY();
          int recorrido2 = dist(x_d1, y_d1, x_d2, y_d2);
          coste += ((double) (furgonetas[i][BICIS2] + 9) / 10)*recorrido2/1000; 
        }
      }
    }
    return coste;
  }

  public int getDistancia() {
    int distancia = 0;

    for (int i = 0; i < furgonetas.length; ++i) {
      if (furgonetas[i][ORIGEN] != -1 && furgonetas[i][DESTINO1] != -1) {

        int x_orig = estaciones.get(furgonetas[i][ORIGEN]).getCoordX();
        int y_orig = estaciones.get(furgonetas[i][ORIGEN]).getCoordY();
      
        int x_d1 = estaciones.get(furgonetas[i][DESTINO1]).getCoordX();
        int y_d1 = estaciones.get(furgonetas[i][DESTINO1]).getCoordY();
      
        distancia += dist(x_orig, y_orig, x_d1, y_d1);
      
        if (furgonetas[i][DESTINO2] != -1) {
          int x_d2 = estaciones.get(furgonetas[i][DESTINO1]).getCoordX();
          int y_d2 = estaciones.get(furgonetas[i][DESTINO2]).getCoordY();
          distancia += dist(x_d1, y_d1, x_d2, y_d2);
        }
      }
    }
    return distancia;
  }


  //---------------GETTERS-----------------

  public int[][] getFurgonetas() {
    return furgonetas;
  }

  public boolean[] getOrigenesOcupados() {
    return origenOcupado;
  }
  
  public int[] getBicisDejadas() {
    return bicis_dejadas;
  }

  public int getNumFurgos() {
    return furgonetas.length;
  }

  public int getNumEstaciones() {
    return estaciones.size();
  }


  public int getMaxBicis(int iest) {
    int num = Math.min(estaciones.get(iest).getNumBicicletasNoUsadas(), estaciones.get(iest).getNumBicicletasNext() - estaciones.get(iest).getDemanda());

    if (num > 30) return 30;
    else return num;
  }

  public int getBicisNecesitadas(int iest) {
    return estaciones.get(iest).getDemanda() - estaciones.get(iest).getNumBicicletasNext();
  }
  // --------------------------------------------
  

  private static int dist(int x1, int y1, int x2, int y2) {
    return Math.abs(x2 - x1) + Math.abs(y2 - y1);
  }

}


