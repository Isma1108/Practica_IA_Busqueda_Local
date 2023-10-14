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
        BicingBoard newBoard = new BicingBoard(board.getFurgonetas(), board.getLibres()); //Copia

        if (board.validChoice(i,j)) {
          newBoard.cambiarDestino1(i,j);
          retVal.add(new Successor("", newBoard));
        }
      }
    }

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumEstaciones(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getFurgonetas(), board.getLibres()); //Copia

        if (board.getSinUsar(i) > 0 && board.validChoice(i,j)) {
          newBoard.cambiarDestino2(i,j);
          retVal.add(new Successor("", newBoard));
        }
      }
    }

    

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumFurgos(); ++j) {

        if (i != j) { 
          BicingBoard newBoard = new BicingBoard(board.getFurgonetas(), board.getLibres()); //Copia
          newBoard.swap_origins(i,j);
          retVal.add(new Successor("", newBoard));
        }
      }
    }

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumFurgos(); ++j) {

        if (i != j) {
          BicingBoard newBoard = new BicingBoard(board.getFurgonetas(), board.getLibres()); //Copia
          if (newBoard.getDestino1(i) != newBoard.getDestino1(j))  {
            newBoard.swap_destination1(i,j);
            retVal.add(new Successor("", newBoard));
          }
        }
      }
    }

    return retVal;
  }
}
