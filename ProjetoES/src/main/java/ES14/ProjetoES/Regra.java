package ES14.ProjetoES;

public class Regra {

	private String metrica;
	private String op;
	private double valor;
	private String opLogico;

	public Regra(String metrica, String op, double valor, String opLogico) {
		this.metrica = metrica;
		this.op = op;
		this.valor = valor;
		this.opLogico = opLogico;
	}

	public void setOpLogico(String opLogico) {
		this.opLogico = opLogico;
	}

	public String getMetrica() {
		return metrica;
	}

	public String getOp() {
		return op;
	}

	public double getValor() {
		return valor;
	}

	public String getOpLogico() {
		return opLogico;
	}

	public boolean checkRule(String s) {
		return s.equals(metrica + op + valor);
	}

}
