import aima.search.framework.HeuristicFunction;

public class BicingHeuristicFunction implements HeuristicFunction {
  
  public double getHeuristicValue(Object state) {
    BicingBoard board = (BicingBoard) state;
    return board.getPrecio1();
  }

}
