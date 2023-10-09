//todos los imports que necesitemos
import IA.Bicing.*;

import java.util.*;

public class BicingBoard {

  //atributos privados:
  
  private static int num_furgonetas;

  private static int num_estaciones;

  private static Estaciones est;


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
