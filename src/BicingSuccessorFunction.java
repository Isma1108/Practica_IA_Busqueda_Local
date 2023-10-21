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

    
    //Cambiar el destino 1

    
    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumEstaciones(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas(), board.getBicisDejadas());

        newBoard.cambiar_destino1(i,j);
        retVal.add(new Successor("change destination1 of furgo " + i + " to " + j, newBoard));
      }
    }    

    //Cambiar el destino 2
    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumEstaciones(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas(), board.getBicisDejadas()); //Copia

        if (newBoard.puede_cambiar_destino2(i, j)) {
          newBoard.cambiar_destino2(i,j);
          retVal.add(new Successor("change destination2 of furgo " + i + " to " + j, newBoard));
        }
      }
    }
    
    //Cambiar origen

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumEstaciones(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas(), board.getBicisDejadas()); //Copia

        if (newBoard.puede_cambiar_origen(i, j)) {
          newBoard.cambiar_origen(i,j);
          retVal.add(new Successor("cambio origen", newBoard));
        }
      }
    }

    
    //Swap destins
    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumFurgos(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas(), board.getBicisDejadas()); //Copia
        if (newBoard.puede_swap_d1(i, j)) {
          newBoard.swap_d1(i, j);
          retVal.add(new Successor("swap d1", newBoard));
        }
      }
    }

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumFurgos(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas(), board.getBicisDejadas()); //Copia
        if (newBoard.puede_swap_d2(i, j)) {
          newBoard.swap_d2(i, j);
          retVal.add(new Successor("swap d2", newBoard));
        }
      }
    }

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas(), board.getBicisDejadas()); //Copia
      if (newBoard.puede_furgo_swap(i)) newBoard.swap_furgo(i);
      retVal.add(new Successor("swap furgo", newBoard));
    }

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumFurgos(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas(), board.getBicisDejadas()); //Copia
        if (newBoard.puede_swap_d12(i, j)) {
          newBoard.swap_d12(i, j);
          retVal.add(new Successor("swap d12", newBoard));
        }
      }
    }
    
    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumFurgos(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas(), board.getBicisDejadas()); //Copia
        if (newBoard.puede_swap_d21(i, j)) {
          newBoard.swap_d21(i, j);
          retVal.add(new Successor("swap d21", newBoard));
        }
      }
    }

    
    return retVal;
  }
}
