package ES14.ProjetoES;

public class Regra {

	String metrica;
	String op;
	int valor;
	String opLogico;
	
	public Regra(String metrica, String op, int valor, String opLogico) {
		this.metrica=metrica;
		this.op=op;
		this.valor=valor;
		this.opLogico=opLogico;
	}

	public String getMetrica() {
		return metrica;
	}

	public String getOp() {
		return op;
	}

	public int getValor() {
		return valor;
	}

	public String getOpLogico() {
		return opLogico;
	}
	
	
}
