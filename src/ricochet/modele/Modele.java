package ricochet.modele;

import java.util.ArrayList;
import java.util.List;

import ricochet.vue.Observateur;

public class Modele {
	
	private List<Observateur> observateurs = new ArrayList<Observateur>();
	private Configuration configInitiale;
	private ArrayList<Configuration> parcours;
	private static Modele instance = null;
	
	//On remplace le constructeur par défaut public par un constructeur privé pour garantir qu'il n'y ai qu'une seul instance de Modele
	private Modele() {
		super();
	}
	
	/**
	 * Récupère l'instance unique du modèle car celui-ci est en pattern singleton (instance unique récupérable avec cette méthode)
	 * @return instance unique du modèle
	 */
	public static Modele getInstance() {
		if (instance == null) instance = new Modele();
		return instance;
	}
	
	public void ajoutObservateur(Observateur observateur) {
		observateurs.add(observateur);
	}

	public void retireObservateur(Observateur observateur) {
		observateurs.remove(observateur);
	}
	
	public void notifier() {
		for (Observateur ob : observateurs) {
			ob.actualise();
		}
	}
	
	public List<Observateur>getObservateurs() {
		return observateurs;
	}

	public Configuration getConfigInitiale() {
		return configInitiale;
	}

	public void setConfigInitiale(Configuration configInitiale) {
		this.configInitiale = configInitiale;
	}

	public ArrayList<Configuration> getParcours() {
		return parcours;
	}

	public void setParcours(ArrayList<Configuration> parcours) {
		this.parcours = parcours;
	}
}
