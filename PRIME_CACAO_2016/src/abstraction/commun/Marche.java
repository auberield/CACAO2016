package abstraction.commun;
	
	import java.util.ArrayList;
	import abstraction.commun.IDistributeur;
	import abstraction.commun.IProducteur;
	import abstraction.commun.ITransformateur;
	import abstraction.fourni.Acteur;
	import abstraction.fourni.Monde;

	public class Marche implements Acteur {
		public static Marche LE_MARCHE;	
		public static final double COURS = 3000.0;
		
		private double quantiteMiseEnVente;

		private ArrayList<IProducteur> producteurs;
		private ArrayList<ITransformateur> transformateurs;
		private ArrayList<IDistributeur> distributeurs;

		public Marche() {
			this.producteurs = new ArrayList<IProducteur>();
			this.transformateurs = new ArrayList<ITransformateur>();
			this.distributeurs = new ArrayList<IDistributeur>();
			this.quantiteMiseEnVente = 0.0;
		}
		
		public double getCours() {
			return Marche.COURS;
		}
		public double vendre(double quantite) {
			this.quantiteMiseEnVente += quantite;
			return quantite*Marche.COURS;
		}
		public String getNom() {
			return "Marche du cacao";
		}
		public void next() {
			double[] quantitesEnVenteP = new double[this.producteurs.size()];
			double totalQuantitesEnVenteP =0.0;
			for (ITransformateur t : this.transformateurs) {
				int i =0;
				for (IProducteur p : this.producteurs){
					quantitesEnVenteP[i] =+ p.annonceQuantiteMiseEnVente(t);
					i++;
					totalQuantitesEnVenteP+=p.annonceQuantiteMiseEnVente(t);
			}}
			double[] quantitesEnVenteT = new double[this.transformateurs.size()];
			double totalQuantitesEnVenteT =0.0;
			for (IDistributeur d : this.distributeurs) {
				int i =0;
				for (ITransformateur t : this.transformateurs){
					quantitesEnVenteT[i] =+ t.annonceQuantiteMiseEnVente(d);
					i++;
					totalQuantitesEnVenteT+=t.annonceQuantiteMiseEnVente(d);
			}}
/////////////////////////////// finit
			double[] quantitesDemandeesT = new double[this.transformateurs.size()];
			for (int i=0; i<this.transformateurs.size(); i++) {
				quantitesDemandeesT[i] = this.transformateurs.get(i).quantiteSouhaitee();
			}
			double totalQuantitesDemandees =0.0;
			for (double d : quantitesDemandeesT) {
				totalQuantitesDemandees+=d;
			}
			double[] quantitesReellementAchettes  = new double[this.transformateurs.size()]; 
			double[] quantitesReellementVendues  = new double[this.producteurs.size()]; 
			if (totalQuantitesDemandees>totalQuantitesEnVente) { // demande > offre
				for (int i=0; i<this.transformateurs.size(); i++) {
					quantitesReellementAchettes[i]=quantitesDemandeesT[i]*totalQuantitesEnVente/totalQuantitesDemandees;
				}
				for (int i=0; i<this.producteurs.size(); i++) {
					quantitesReellementVendues[i]=quantitesEnVente[i];
				}
				
			} else {// offre >= demande
				for (int i=0; i<this.transformateurs.size(); i++) {
					quantitesReellementAchettes[i]=quantitesDemandeesT[i];
				}
				for (int i=0; i<this.producteurs.size(); i++) {
					quantitesReellementVendues[i]=quantitesEnVente[i]*totalQuantitesDemandees/totalQuantitesEnVente;
				}
			}
			//////////////////////////////////////////////
			for (int i=0; i<this.transformateurs.size(); i++) {
				this.transformateurs.get(i).notificationVente(quantitesReellementAchettes[i]);
			}
			for (int i=0; i<this.producteurs.size(); i++) {
				this.producteurs.get(i).notificationVente(quantitesReellementVendues[i]);
			}
			
			
		}
	}	



}
