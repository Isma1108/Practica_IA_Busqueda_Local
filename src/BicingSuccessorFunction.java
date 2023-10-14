//imports 
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class BicingSuccessorFunction implements SuccessorFunction {
  @SuppressWarnings("unchecked")
  public List getSuccessors(Object aState) {
    ArrayList retVal = new ArrayList();
    BicingBoard board = (BicingBoard) aState;

    
    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumEstaciones(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getFurgonetas());

        if (newBoard.puede_cambiar_destino1(i, j)) {
          newBoard.cambiar_destino1(i,j);
          retVal.add(new Successor("", newBoard));
        }
      }
    }    

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumEstaciones(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getFurgonetas()); //Copia

        if (newBoard.puede_cambiar_destino2(i, j)) {
          newBoard.cambiar_destino2(i,j);
          retVal.add(new Successor("", newBoard));
        }
      }
    }

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumEstaciones(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getFurgonetas()); //Copia

        if (newBoard.getDestino2(i) != j && newBoard.getOrigen(i) != j) newBoard.add_2ndDestination(i,j);
        
        retVal.add(new Successor("", newBoard));
      }
    }

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = i + 1; j < board.getNumFurgos(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getFurgonetas()); //Copia
        newBoard.swap_origins(i,j);
        retVal.add(new Successor("", newBoard));
      }
    }

    return retVal;
  }
}
