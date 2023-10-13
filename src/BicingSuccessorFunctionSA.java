//imports 
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BicingSuccessorFunctionSA implements SuccessorFunction {
  public List getSuccessors(Object aState) {
    ArrayList retVal = new ArrayList();
    BicingBoard board = (BicingBoard) aState;
    Random myRandom= new Random();
    int i, j;

    //Nos ahorramos generar todos los successores escojiendo origen y destino al azar
    //De momento solo uso operador cambiar destino1;

    i = myRandom.nextInt(board.getNumFurgos());
    j = myRandom.nextInt(board.getNumEstaciones());

    BicingBoard newBoard = new BicingBoard(board.getFurgonetas());
    newBoard.cambiar_destino1(i,j);
    retVal.add(new Successor("", newBoard));

    return retVal;
  }
}
