package fr.eni.javaee.encheres.servlets;

/**
 * Les codes disponibles sont entre 30000 et 39999
 */
public abstract class CodesResultatServlets {
	
	// Connexion
	public static final int FORMAT_ID_UTILISATEUR_ERREUR = 30000;
	public static final int MOTS_DE_PASSE_DIFFERENTS = 30001;
	public static final int MODIFICATION_UTILISATEUR_ERREUR = 30002;
	public static final int CREATION_UTILISATEUR_ERREUR = 30003;
	public static final int SUPPRESSION_UTILISATEUR_ERREUR = 30004;
	public static final int SELECTION_UTILISATEUR_ERREUR = 30005;
	public static final int MOT_DE_PASSE_INCORRECT = 30006;
	public static final int UTILISATEUR_INCONNU = 30007;
	
	// Ventes
	public static final int LECTURE_PARAMETRE_VENTE = 30100;
	public static final int VENTE_INCONNUE = 30101;
	
	
}