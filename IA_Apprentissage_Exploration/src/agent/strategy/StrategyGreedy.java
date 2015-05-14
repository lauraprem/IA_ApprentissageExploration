package agent.strategy;

import java.util.ArrayList;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;

/**
 * Strategie qui renvoit une action aleatoire avec probabilite epsilon, une action gloutonne (qui suit la politique de l'agent) sinon
 * Cette classe a acces a un RLAgent par l'intermediaire de sa classe mere.
 * @author lmatignon
 *
 */
public class StrategyGreedy extends StrategyExploration {

	/**
	 * probabilité d'exploration
	 */
	private double epsilon;

	private Random rand = new Random();

	public StrategyGreedy(RLAgent agent, double epsilon) {
		super(agent);
		this.epsilon = epsilon;
	}

	/**
	 * @return action selectionnee par la strategie d'exploration
	 */
	@Override
	public Action getAction(Etat _e) {
		// choix de deux actions aléatoires
		ArrayList<Action> listeAction = (ArrayList) this.agent.getActionsLegales(_e);
		Action aLegale = listeAction.get(this.rand.nextInt(listeAction.size()));
		listeAction = (ArrayList) this.agent.getPolitique(_e);
		Action aPolitique = listeAction.get(this.rand.nextInt(listeAction.size()));

		// proba epsilon de prendre aLegale et proba 1-epsilon de prendre
		// aPolitique
		int r = rand.nextInt(99);
		double epsiPourcent = epsilon * 100;
		if (r > (100 - epsiPourcent)) {
			return aLegale;
		} else {
			return aPolitique;
		}
	}

	public void setEpsilon(double epsilon) {
		// VOTRE CODE
		this.epsilon = epsilon;
	}

}
