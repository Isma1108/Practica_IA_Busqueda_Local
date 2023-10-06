import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class BicingDemo {

  public static void main(String[] args) {
    
    int nest = 25;
    int nbic = 1250;
    int nfurg = 5;
    int dem = Estaciones.EQUILIBRIUM;
    int seed = 1234;
    int estado_ini = 0;
    int heuristica = 0;

    Scanner sc = new Scanner(System.in);

    //Primero leemos los argumentos que el usuario pasa al programa
    //Le mostramos su uso en caso de no saber como usarlo

    if (args.length != 14) Usage();
  
    for (int i = 0; i < args.length; ++i) {
      switch(args[i]) {
        case "-e":
          try {
            nest = Integer.parseInt(args[++i]);
          } 
          catch(NumberFormatException e) {
            System.out.println(args[i] + " no es un numero entero.");
            System.exit(0);
          }

        case "-b":
          try {
            nbic = Integer.parseInt(args[++i]);
          } 
          catch(NumberFormatException e) {
            System.out.println(args[i] + " no es un numero entero.");
            System.exit(0);
          }

        case "f":
          try {
            nfurg = Integer.parseInt(args[++i]);
          } 
          catch(NumberFormatException e) {
            System.out.println(args[i] + " no es un numero entero.");
            System.exit(0);
          }

        case "-d":
          try {
            int d = Integer.parseInt(args[++i]);
            if (d == 0) dem = Estaciones.EQUILIBRIUM;
            else if (d == 1) dem = Estaciones.RUSH_HOUR;
            else {
              System.out.println(args[i] + " no es 0 o 1.");
              System.exit(0);
            }
          } 
          catch(NumberFormatException e) {
            System.out.println(args[i] + " no es un numero entero.");
            System.exit(0);
          }

        case "-s":
          try {
            seed = Integer.parseInt(args[++i]);
          } 
          catch(NumberFormatException e) {
            System.out.println(args[i] + " no es un numero entero.");
            System.exit(0);
          }
          
        case "-i":
          try {
             estado_ini = Integer.parseInt(args[++i]);
            if (estado_ini != 0 && estado_ini != 1) {
              System.out.println(args[i] + " no es 0 o 1.");
              System.exit(0);
            }
          } 
          catch(NumberFormatException e) {
            System.out.println(args[i] + " no es un numero entero.");
            System.exit(0);
          }

        case "-h":
          try {
            heuristica = Integer.parseInt(args[++i]);
            if (heuristica != 0 && heuristica != 1) {
              System.out.println(args[i] + " no es 0 o 1.");
              System.exit(0);
            }
          } 
          catch(NumberFormatException e) {
            System.out.println(args[i] + " no es un numero entero.");
            System.exit(0);
          }

        default:
          Usage();
          break;
      }
    }

    //Falta comprobar que las proporciones sean correctas:
    if (nest*50 > nbic) {
            System.out.println("La proporción entre estaciones y bicicletas debe ser como mínimo de 1 a 50");
            System.exit(0);
    }

    //En este punto, creamos una instancia de Estaciones pasandole la información del usuario,
    //para que nos genere un escenario con las n estaciones en posiciones aleatorias del mapa

    Estaciones est = new Estaciones(nest, nbic, dem, seed);
    BicingBoard board = new BicingBoard(est, estado_ini);

    //Heuristica 0 -> maximicación de lo que obtenemos por los traslados de las bicis
    //Heuristica 1 -> Heuristica 0 + minimización del coste del transporte de las bicis

    if (heuristica == 0) {
      BicingHillClimbingSearch1(board);
      BicingSimulatedAnnealingSearch1(board);
    }
    else {
      //De momento llamamos a la 1, hasta que tengamos las dos heuristicas
      BicingHillClimbingSearch1(board);
      BicingSimulatedAnnealingSearch1(board);
    }
  }

  private static void BicingHillClimbingSearch1(BicingBoard board) {
    System.out.println("\nBicing HillClimbing  -->");
    try {
      Problem problem =  new Problem(board, new BicingSuccessorFunction(), 
          new BicingGoalTest(), new BicingHeuristicFunction());
      Search search =  new HillClimbingSearch();
      SearchAgent agent = new SearchAgent(problem,search);
            
      System.out.println();
      //Aqui deberiamos mostrar la solucion que nos da el algoritmo
      printInstrumentation(agent.getInstrumentation());
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void BicingSimulatedAnnealingSearch1(BicingBoard board) {
    System.out.println("\nBicing Simulated Annealing  -->");
    try {
      Problem problem =  new Problem(board, new BicingSuccessorFunctionSA(), 
          new BicingGoalTest(), new BicingHeuristicFunction());
      SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(2000,100,5,0.001); //Parametros del SA
      SearchAgent agent = new SearchAgent(problem,search);
            
      System.out.println();
      //Aqui deberiamos mostrar la solucion que nos da el algoritmo
      printInstrumentation(agent.getInstrumentation());
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private static void printInstrumentation(Properties properties) {
    Iterator keys = properties.keySet().iterator();
    while (keys.hasNext()) {
      String key = (String) keys.next();
      String property = properties.getProperty(key);
      System.out.println(key + " : " + property);
    }
  }
    
  private static void Usage() {
    System.out.println("Flags que se deben usar: (no necesariamente en este orden)");
    System.out.println("  -e [Número de estaciones] ");
    System.out.println("  -b [Número de bicicletas]");
    System.out.println("  -f [Número de furgonetas]");
    System.out.println("  -d [Demanda (0/1)] (0 -> equilibrada y 1 -> hora punta)");
    System.out.println("  -s [Seed]");
    System.out.println("  -i [Estado inicial (0/1)]");
    System.out.println("  -h [Función heurística (0/1)]");
    System.exit(0);
  }

}
