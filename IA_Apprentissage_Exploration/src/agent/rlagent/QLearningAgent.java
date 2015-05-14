package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import environnement.Action;
import environnement.Environnement;
import environnement.Etat;

/**
 * 
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {

	// attribut QLearning : un double pour chaque etat/action
	private Map<Etat, Map<Action, Double>> qTable;

	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param Environnement
	 */
	public QLearningAgent(double alpha, double gamma, Environnement _env) {
		super(alpha, gamma, _env);
		qTable = new HashMap<Etat, Map<Action, Double>>();
	}

	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  
	 *  renvoi liste vide si aucunes actions possibles dans l'etat 
	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		ArrayList<Action> resultat = new ArrayList<Action>();
		if (qTable.isEmpty() || qTable.get(e) == null) {
			return this.getActionsLegales(e);
		}
		Set set = qTable.get(e).entrySet();
		double max = -1;
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry<Action, Double> couple = (Map.Entry<Action, Double>) iterator.next();
			if (couple.getValue() > max) {
				resultat.clear();
				max = couple.getValue();
				resultat.add(couple.getKey());
			}
			if (couple.getValue() == max) {
				resultat.add(couple.getKey());
			}
		}
		return resultat;
	}

	/**
	 * @return la valeur d'un etat
	 */
	@Override
	public double getValeur(Etat e) {
		if (e.estTerminal() || qTable.isEmpty() || qTable.get(e) == null) {
			return 0.0;
		} else {
			Set set = qTable.get(e).entrySet();
			double max = -1;
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry<Action, Double> couple = (Map.Entry<Action, Double>) iterator.next();
				if (couple.getValue() > max) {
					max = couple.getValue();
				}
			}
			return max;
		}
	}

	/**
	 * 
	 * @param e
	 * @param a
	 * @return Q valeur du couple (e,a)
	 */
	@Override
	public double getQValeur(Etat e, Action a) {
		if (!qTable.isEmpty() && (qTable.get(e) != null) && (qTable.get(e).get(a)!=null)) {
			return qTable.get(e).get(a);
		}
		return 0.0;
	}

	/**
	 * setter sur Q-valeur
	 */
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		if (qTable.get(e) == null) {
			qTable.put(e, new HashMap<Action, Double>());
		}
		qTable.get(e).put(a, d);
		this.notifyObs();

	}

	/**
	 *
	 * mise a jour de la Q-valeur du couple (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		// Q(e,a) = (1-alpha)Q(e,a) + (alpha [reward + gamma *
		// getValeur(esuivant)])
			Double d = (1 - alpha) * getQValeur(e, a) + alpha * (reward + gamma * getValeur(esuivant));
			this.setQValeur(e, a, d);
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	/**
	 * reinitialise les Q valeurs
	 */
	@Override
	public void reset() {
		this.episodeNb = 0;
		qTable.clear();
	}

}
