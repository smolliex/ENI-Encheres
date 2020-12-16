package fr.eni.javaee.encheres.bll;

/**
 * Les codes disponibles sont entre 20000 et 29999
 */
public abstract class CodesResultatBLL {
	
	/**
	 * Le format du nom d'utilisateur est incorrect. 
	 */
	public static final int REGLE_UTILISATEUR_NOM_ERREUR = 20000;
	
	/**
	 * Le format du prénom d'utilisateur est incorrect. 
	 */
	public static final int REGLE_UTILISATEUR_PRENOM_ERREUR = 20001;
	
	
	/**
	 * Le format de l'adresse email est incorrecte 
	 */
	public static final int REGLE_UTILISATEUR_EMAIL_ERREUR = 20002;
	
	/**
	 * L'adresse email fait doublon 
	 */
	public static final int REGLE_UTILISATEUR_EMAIL_DOUBLON = 20003;
	
	/**
	 * Le format du pseudo est incorrect 
	 */
	public static final int REGLE_UTILISATEUR_PSEUDO_ERREUR = 20004;

	/**
	 * L'adresse email fait doublon 
	 */
	public static final int REGLE_UTILISATEUR_PSEUDO_DOUBLON = 20005;

	public static final int REGLE_UTILISATEUR_TELEPHONE_ERREUR = 0;

	public static final int REGLE_UTILISATEUR_RUE_ERREUR = 0;

	public static final int REGLE_UTILISATEUR_CODE_POSTAL_ERREUR = 0;

	public static final int REGLE_UTILISATEUR_VILLE_ERREUR = 0;

	public static final int REGLE_UTILISATEUR_MOT_DE_PASSE_ERREUR = 0;
	
	
	
}
