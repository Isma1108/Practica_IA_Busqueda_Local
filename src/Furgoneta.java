import java.util.*;

public class Furgoneta {

	private int EstOrigen;
	private int EstDesti1;
	private int EstDesti2;


	//creadora
	public Furgoneta(int estOrig, int dest1, int dest2) {
		EstOrigen = estOrig;
		EstDesti1 = dest1;
		EstDesti2 = dest2;
	}

	//consultoras

	public int getOrig() {
		return EstOrigen;
	}
	public int getDest1() {
		return EstDesti1;
	}
	public int getDest2() {
		return EstDesti2;
	}
};
