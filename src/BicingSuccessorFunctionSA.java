//imports 
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import aima.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BicingSuccessorFunctionSA implements SuccessorFunction {
  @SuppressWarnings("unchecked")
  public List getSuccessors(Object aState) {
    ArrayList retVal = new ArrayList();
    BicingBoard board = (BicingBoard) aState;
    Random myRandom= new Random();
    BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas(), board.getBicisDejadas());
    int i, j;
    int nest = board.getNumEstaciones();


    List<Pair> operadoresValidos;

    operadoresValidos = new ArrayList<>();
    //Escojemos una furgoneta aleatoria a la que le aplicaremos un operador
    i = myRandom.nextInt(board.getNumFurgos());

    //De todos los posibles operadores que podemos aplicar a la furgoneta i, escogemos uno aleatorio
    
    for (j = 0; j < nest; ++j) operadoresValidos.add(new Pair("d1", j));
    
    for (j = 0; j < nest; ++j) {
      if (newBoard.puede_cambiar_destino2(i, j)) operadoresValidos.add(new Pair("d2", j));
    }

    for (j = 0; j < nest; ++j) {
      if (newBoard.puede_cambiar_origen(i, j)) operadoresValidos.add(new Pair("origen", j));
    }

    int ind = myRandom.nextInt(operadoresValidos.size());
    Pair op = operadoresValidos.get(ind);

    if (op.getFirst().equals("d1")) newBoard.cambiar_destino1(i, (Integer)op.getSecond());
    else if (op.getFirst().equals("d2")) newBoard.cambiar_destino2(i, (Integer)op.getSecond());
    else newBoard.cambiar_origen(i,(Integer)op.getSecond());

    retVal.add(new Successor("", newBoard));

    return retVal;
  }
}
