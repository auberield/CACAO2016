package abstraction.equipe2;
import java.util.HashMap;

import abstraction.commun.Produit;

public class StockChocolats {
	private HashMap<Produit, Double> stockschocolats;
	
	public StockChocolats() {
		this.stockschocolats = new HashMap<Produit, Double>();
		this.stockschocolats.put(Constante.PRODUIT_50, 0.);
		this.stockschocolats.put(Constante.PRODUIT_60, 0.);
		this.stockschocolats.put(Constante.PRODUIT_70, 0.);
	}

	public HashMap<Produit, Double> getStockschocolats() {
		return stockschocolats;
	}
	
	public void add(Produit p) {
		this.stockschocolats.put(p, 0.0);
	}
	
	public void AjouterStockProduit(Produit p, Production prod) {
		this.stockschocolats.put(p, prod.getProduction().get(p)+this.stockschocolats.get(p));
	}

	public void RetirerStockProduit(Produit p, Vente vente) {
		this.stockschocolats.put(p, -vente.getVentes().get(p)+this.stockschocolats.get(p));
	}
	
	public double CoutStockChocolat() {
		double resultat = 0;
		for (Double stock : this.getStockschocolats().values()) {
			resultat +=stock;
		}
		return resultat;
	}

}
