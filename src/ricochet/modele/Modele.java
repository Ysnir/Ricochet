package ricochet.modele;

import java.util.ArrayList;
import java.util.List;

import ricochet.vue.Observateur;

public class Modele {
	
	private List<Observateur> observateurs = new ArrayList<Observateur>();
	private Configuration configInitiale;
	private ArrayList<Configuration> parcours;
	private static Modele instance = null;
	
	//On remplace le constructeur par d�faut public par un constructeur priv� pour garantir qu'il n'y ai qu'une seul instance de Modele
	private Modele() {
		super();
	}
	
	/**
	 * R�cup�re l'instance unique du mod�le car celui-ci est en pattern singleton (instance unique r�cup�rable avec cette m�thode)
	 * @return instance unique du mod�le
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
