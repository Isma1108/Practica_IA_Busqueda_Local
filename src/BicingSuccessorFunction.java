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
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas());

        newBoard.cambiar_destino1(i,j);
        retVal.add(new Successor("change destination1 of furgo " + i + " to " + j, newBoard));
      }
    }    

    //Cambiar el destino 2
    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumEstaciones(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas()); //Copia

        if (newBoard.puede_cambiar_destino2(i, j)) {
          newBoard.cambiar_destino2(i,j);
          retVal.add(new Successor("change destination2 of furgo " + i + " to " + j, newBoard));
        }
      }
    }

    
    

    //Cambiar origen

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumEstaciones(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas()); //Copia

        if (newBoard.puede_cambiar_origen(i)) {
          newBoard.cambiar_origen(i,j);
          retVal.add(new Successor("origin of furgo " + i + " changed to " + j, newBoard));
        }
      }
    }

    
    
    //Swap destins
    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumFurgos(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas()); //Copia
        if (newBoard.puede_swap_d1(i, j)) {
          newBoard.swap_d1(i, j);
          retVal.add(new Successor("swap dest1 of " + i + " with " + j, newBoard));
        }
      }
    }

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumFurgos(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas()); //Copia
        if (newBoard.puede_swap_d2(i, j)) {
          newBoard.swap_d2(i, j);
          retVal.add(new Successor("swap dest2 of " + i + " with " + j, newBoard));
        }
      }
    }

    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumFurgos(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas()); //Copia
        if (newBoard.puede_swap_d12(i, j)) {
          newBoard.swap_d12(i, j);
          retVal.add(new Successor("swap dest1 of " + i + " with dest2 of " + j, newBoard));
        }
      }
    }
    
    for (int i = 0; i < board.getNumFurgos(); ++i) {
      for (int j = 0; j < board.getNumFurgos(); ++j) {
        BicingBoard newBoard = new BicingBoard(board.getOrigenesOcupados(), board.getFurgonetas()); //Copia
        if (newBoard.puede_swap_d21(i, j)) {
          newBoard.swap_d21(i, j);
          retVal.add(new Successor("swap dest2 of " + i + " with dest1 of " + j, newBoard));
        }
      }
    }


    return retVal;
  }
}