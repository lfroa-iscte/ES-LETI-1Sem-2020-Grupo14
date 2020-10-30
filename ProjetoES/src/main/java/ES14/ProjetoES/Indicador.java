package ES14.ProjetoES;

public class Indicador {

	private int dci;
	private int dii;
	private int adci;
	private int adii;
	
	public Indicador() {
		dci=0;
		dii=0;
		adci=0;
		adii=0;
	}
	
	public int getDci() {
		return dci;
	}
	public int getDii() {
		return dii;
	}
	public int getAdci() {
		return adci;
	}
	public int getAdii() {
		return adii;
	}
	public void setDci(int dci) {
		this.dci = dci;
	}
	public void setDii(int dii) {
		this.dii = dii;
	}
	public void setAdci(int adci) {
		this.adci = adci;
	}
	public void setAdii(int adii) {
		this.adii = adii;
	}
	
}
