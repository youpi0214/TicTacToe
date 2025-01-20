package modele;

public enum nivSelect {
	ALEATOIRE("Aléatoire"), FACILE("Facile"), DIFFICILE("IA"), JOUEUR("2 Joueurs");

	String nom;

	private nivSelect(String pNom) {
		nom = pNom;
	}
}