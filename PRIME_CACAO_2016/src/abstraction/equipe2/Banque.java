package abstraction.equipe2;

import java.util.HashMap;

import abstraction.commun.*;

public class Banque {
	
	private double banque;
	private HashMap<IProducteur,Double> prixdevente;	
	
	public Banque(double banque){
	this.banque=banque;
	this.prixdevente=new HashMap<IProducteur, Double>();
	}
	
	public void addPrixdevente(IProducteur p, Double prix) {
		this.prixdevente.put(p,prix);
	}
	
	public HashMap<IProducteur, Double> getPrixdevente() {
		return prixdevente;
	}	
	
	public double getBanque() {
		return banque;
	}

/* void setBanque(double banque) {
		this.banque = Constante.TRESORERIE_INITIALE;
	} Sert a rien ?*/ 
	
	public static double CoutIntermediaireUnitaire (Produit produit, Achat achat, Tarif tarif){
		return Constante.CHARGES_FIXES + achat.getDernierecommandeachetee().getCommandesProd()
				*(Constante.COUT_DE_TRANSFORMATION   /* + Cout d'achat */)// + cout stock;
	}
	
}
