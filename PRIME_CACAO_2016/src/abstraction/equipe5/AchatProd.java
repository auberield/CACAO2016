package abstraction.equipe5;

import java.util.ArrayList;
import java.util.List;

import abstraction.commun.CommandeDistri;
import abstraction.commun.CommandeProduc;
import abstraction.commun.MarcheProducteur;

class CommandeInterne {
	private double quantite, prix;
	
	public CommandeInterne(double q, double p) {
		this.quantite=q;
		this.prix=p;
	}
	
	public double getQuantite() {
		return this.quantite;
	}
	
	public double getPrix() {
		return this.prix;
	}
}

public class AchatProd {
	private HistoriqueCommandeProduc histP;
	private HistoriqueCommandeDistri histD;
	private Lindt lindt;
	private Stock stockCacao;
	private Tresorerie treso;
	private double quantiteDemandee;
	private double quantiteRecue;

	
	public AchatProd(HistoriqueCommandeProduc histP, HistoriqueCommandeDistri histD, Lindt lindt, Stock stockCacao, Tresorerie treso) {
		this.histP = histP;
		this.histD = histD;
		this.lindt = lindt;
		this.stockCacao = stockCacao;
		this.treso = treso;
	}
	
	public HistoriqueCommandeProduc getHistP() {
		return this.histP;
	}

	public HistoriqueCommandeDistri getHistD() {
		return this.histD;
	}
	
	public Tresorerie getTreso() {
		return this.treso;
	}
	
	public Stock getStock() {
		return this.stockCacao;
	}
	
	
	/** Fonction qui calcul la quantite que l on va demander aux producteurs
	 * @param histP
	 * @param histD
	 * @return la quantite que l on va demander aux producteurs
	 */
	public CommandeInterne calculQuantiteDemandee(HistoriqueCommandeProduc histP, HistoriqueCommandeDistri histD){
		double besoinCacao=0;
		// Creation de la liste des commandes au step n,n-1 et n-2
		List<CommandeDistri> listeCommandesDist= new ArrayList<CommandeDistri>();
		for (int i=0 ; i<histD.getHist().size(); i++){
			if (histD.getCommande(i).getStepLivraison()==Constante.stepCourant()
					||histD.getCommande(i).getStepLivraison()==Constante.stepPrecedent()
					||histD.getCommande(i).getStepLivraison()==Constante.step2() 
					||histD.getCommande(i).getStepLivraison()==Constante.step3())
				listeCommandesDist.add(histD.getCommande(i));
		}
		// Calcul du besoin en cacao pour les 3 prochains step
		for (CommandeDistri c : listeCommandesDist){
			for (int i=0; i<Constante.LISTE_PRODUIT.length ; i++){
				if (c.getProduit().equals(Constante.LISTE_PRODUIT[i]))
					besoinCacao += c.getQuantite()*Constante.LISTE_PRODUIT[i].getRatioCacao();
		}}
		// StockCacao-StockChoco c est pas optimal comme solution
		double commandeP=0;
		for (int i=0; i<lindt.getProducteurs().size() ; i++){
			commandeP+= histP.getHist().get(histP.getHist().size()-i-1).getQuantite();
		}
		commandeP=commandeP*2/3; // Pour rajouter P3
		double stockCacao=lindt.getStockCacao().getStock()+commandeP
				+ lindt.getStockChocolat50().getStock()*Constante.LISTE_PRODUIT[0].getRatioCacao()
				+ lindt.getStockChocolat60().getStock()*Constante.LISTE_PRODUIT[1].getRatioCacao()
				+ lindt.getStockChocolat70().getStock()*Constante.LISTE_PRODUIT[2].getRatioCacao();

		if (stockCacao-Constante.STOCK_MINIMAL<besoinCacao){
			besoinCacao=besoinCacao-stockCacao+Constante.STOCK_MINIMAL;
		}
		double prixDemande;
		if (quantiteDemandee < quantiteRecue) {
			prixDemande = MarcheProducteur.LE_MARCHE.getCours()*1.2;
		}
		else {
			prixDemande=0.95*MarcheProducteur.LE_MARCHE.getCours();
		}
		return new CommandeInterne(besoinCacao, prixDemande);
	}
	
	/**
	 * Indique la quantite demandee au producteur autre que P3.
	 */
	
	public double annonceQuantiteDemandee(){ 
		this.quantiteDemandee = this.calculQuantiteDemandee(this.getHistP(),this.getHistD()).getQuantite();
		return 0.6*this.calculQuantiteDemandee(this.getHistP(),this.getHistD()).getQuantite();
	}// On met *0.6 car on prend 60% au prod et 40% au reste du monde
	
	public void notificationVente(CommandeProduc c) {
		this.quantiteRecue = c.getQuantite();
		this.getHistP().ajouter(c);
		System.out.println("avant "+this.getStock().getStock()+" --> "+c.getQuantite());
		this.getStock().ajouterStock(c.getQuantite());
		System.out.println("apres "+this.getStock().getStock());
		System.out.println("c "+c);
		System.out.println("treso"+this.getTreso());
		this.getTreso().retrait(c.getQuantite()*c.getPrixTonne());
	}
	
	/**
	 * Indique le prix propose au producteur .
	 */
	public double getPrix(){
		return this.calculQuantiteDemandee(this.getHistP(),this.getHistD()).getPrix();
	}
	
	//on achete 40% de la quantite demandee au producteur 3
	public double quantiteProduc3() {
		return 0.4*this.calculQuantiteDemandee(this.getHistP(),this.getHistD()).getQuantite(); 
	}
}