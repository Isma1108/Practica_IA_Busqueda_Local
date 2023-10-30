import IA.Bicing.*;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.*;

public class Main {
  private static int nest = 25;
  private static int nbic = 1250;
  private static int nfurg = 5;
  private static int dem = Estaciones.EQUILIBRIUM;
  private static int seed = 333;
  private static int solucion = 3;
  private static int heuristica = 2;
  private static boolean HillClimbing = false;
  private static int k = 1;
  private static double lambda = 0.0001;
   
  public static void main(String[] args){
    defVars(); 
    iniciar_experimento();
  }

  public static void defVars() {
    Scanner sc = new Scanner(System.in);

    System.out.print("Indica el número de estaciones: ");
    nest = sc.nextInt();

    System.out.print("Indica el número de bicicletas totales: ");
    nbic = sc.nextInt();

    if (nest*50 > nbic) {
            System.out.println("La proporción entre estaciones y bicicletas debe ser como mínimo de 1 a 50");
            System.exit(-1);
    }

    System.out.print("Indica el número de furgonetas: ");
    nfurg = sc.nextInt();
    
    System.out.print("Indica el tipo de demanda {1: equiilibrada, 2: hora punta}: ");
    dem = sc.nextInt();
    dem = (dem == 2) ? Estaciones.RUSH_HOUR : Estaciones.EQUILIBRIUM;
    
    System.out.print("Indica la seed {-1: seed aleatoria}: ");
    seed = sc.nextInt();
    if (seed == -1) {
      Random rand = new Random();
      seed = rand.nextInt(0, 100000);
    }

    System.out.print("Selecciona la solución inicial {1: Trivial, 2: Random, 3: Voraz}: ");
    solucion = sc.nextInt();
    if (solucion < 1 || solucion > 3) System.exit(-1);

    System.out.print("Selecciona la heurística {1: Max. beneficio por traslado, 2: 1 + transporte}: ");
    heuristica = sc.nextInt();
    if (heuristica != 1 && heuristica != 2) System.exit(-1);

    System.out.print("Selecciona el algoritmo de búsqueda local {1: HillClimbing, 2: SA}: ");
    HillClimbing = sc.nextInt() == 1;

    if (!HillClimbing) {
      System.out.print("Indica el valor de k para el simulated annealing: ");
      k = sc.nextInt();
      System.out.print("Indica el valor de lambda para el simulated annealing: ");
      lambda = sc.nextDouble();
    }
  }

  public static void iniciar_experimento() {
    Estaciones est = new Estaciones(nest, nbic, dem, seed);
    BicingBoard board = new BicingBoard(est, nfurg);

    switch (solucion) {
      case 1:
        board.generar_solucion_trivial();
        break;
      case 2:
        board.generar_solucion_random();
        break;
      case 3:
        board.generar_solucion_voraz();
        break;
      default:
        board.generar_solucion_voraz();
        break;
    }
    //Si la sol. inicial es trivial el benefico será obviamente de 0, y mucho mejor en la greedy.
    System.out.println();
    System.out.println("Antes de la búsqueda el benefico es de : " + board.getBeneficio());
    System.out.println("Antes de la búsqueda el benefico (el transporte es gratis) es de : " + -board.getPrecio1());
    System.out.println("La seed es " + seed);
    System.out.println();


    if (heuristica == 1) {
      if (HillClimbing) BicingHillClimbingSearch1(board);
      else BicingSimulatedAnnealingSearch1(board);
    }
    else {
      if (HillClimbing) BicingHillClimbingSearch2(board);
      else BicingSimulatedAnnealingSearch2(board);
    }
  }
  
  private static void BicingHillClimbingSearch1(BicingBoard board) {
    System.out.println("\nBicing HillClimbing (with heuristic 1)  -->");
    try {
      long time = System.currentTimeMillis();
     
      Problem problem =  new Problem(board, new BicingSuccessorFunction(), 
          new BicingGoalTest(), new BicingHeuristicFunction());
      Search search =  new HillClimbingSearch();
      SearchAgent agent = new SearchAgent(problem,search);
      
      time = System.currentTimeMillis() - time;
      BicingBoard newboard = (BicingBoard) search.getGoalState(); 
      System.out.println("Después de la búsqueda el benefico es de : " + newboard.getBeneficio());
      System.out.println("Antes de la búsqueda el benefico (el transporte es gratis) es de : " + -newboard.getPrecio1());
      System.out.println("El coste del transporte es de " + newboard.getCosteTransporte());
      System.out.println("La distancia recorrida es de " + newboard.getDistancia());
      System.out.println("Tiempo transcurrido: " + time + " ms");

      printInstrumentation(agent.getInstrumentation());
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void BicingSimulatedAnnealingSearch1(BicingBoard board) {
    System.out.println("\nBicing Simulated Annealing (with heuristic 1) -->");
    try {
      long time = System.currentTimeMillis();
      
      Problem problem =  new Problem(board, new BicingSuccessorFunctionSA(), 
          new BicingGoalTest(), new BicingHeuristicFunction());
      SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(5000,100,k,lambda); //Parametros del SA
      SearchAgent agent = new SearchAgent(problem,search);
            
      time = System.currentTimeMillis() - time;
      BicingBoard newboard = (BicingBoard) search.getGoalState(); 
      System.out.println("Después de la búsqueda el benefico es de : " + newboard.getBeneficio());
      System.out.println("Después de la búsqueda el benefico (el transporte es gratis) es de : " + -newboard.getPrecio1());
      System.out.println("El coste del transporte es de " + newboard.getCosteTransporte());
      System.out.println("La distancia recorrida es de " + newboard.getDistancia());
      System.out.println("Tiempo transcurrido: " + time + " ms");
      
      printInstrumentation(agent.getInstrumentation());
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void BicingHillClimbingSearch2(BicingBoard board) {
    System.out.println("\nBicing HillClimbing (with heuristic 2)  -->");
    try {
      long time = System.currentTimeMillis();
      
      Problem problem =  new Problem(board, new BicingSuccessorFunction(), 
          new BicingGoalTest(), new BicingHeuristicFunction2());
      Search search =  new HillClimbingSearch();
      SearchAgent agent = new SearchAgent(problem,search);
            
      time = System.currentTimeMillis() - time;
      BicingBoard newboard = (BicingBoard) search.getGoalState(); 
      System.out.println("Después de la búsqueda el benefico es de : " + newboard.getBeneficio());
      System.out.println("Después de la búsqueda el benefico (el transporte es gratis) es de : " + -newboard.getPrecio1());
      System.out.println("El coste del transporte es de " + newboard.getCosteTransporte());
      System.out.println("La distancia recorrida es de " + newboard.getDistancia());
      System.out.println("Tiempo transcurrido: " + time + " ms");
      
      printInstrumentation(agent.getInstrumentation());
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void BicingSimulatedAnnealingSearch2(BicingBoard board) {
    System.out.println("\nBicing Simulated Annealing (with heuristic 2) -->");
    try {
      long time = System.currentTimeMillis();
      
      Problem problem =  new Problem(board, new BicingSuccessorFunctionSA(), 
          new BicingGoalTest(), new BicingHeuristicFunction2());
      SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(5000,100,k,lambda); //Parametros del SA
      SearchAgent agent = new SearchAgent(problem,search);
            
      time = System.currentTimeMillis() - time;
      
      
      BicingBoard newboard = (BicingBoard) search.getGoalState(); 
      System.out.println("Después de la búsqueda el benefico es de : " + newboard.getBeneficio());
      System.out.println("Después de la búsqueda el benefico (el transporte es gratis) es de : " + -newboard.getPrecio1());
      System.out.println("El coste del transporte es de " + newboard.getCosteTransporte());
      System.out.println("La distancia recorrida es de " + newboard.getDistancia());
      System.out.println("Tiempo transcurrido: " + time + " ms");
      
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
  private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
  }
}
