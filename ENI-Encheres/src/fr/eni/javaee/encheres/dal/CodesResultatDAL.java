package fr.eni.javaee.encheres.dal;

/**
 * Les codes disponibles sont entre 10000 et 19999
 */
public abstract class CodesResultatDAL {
	
	/**
	 * Echec général quand tentative d'ajouter un objet null
	 */
	public static final int INSERT_UTILISATEUR_NULL=10000;
	
	/**
	 * Echec général quand erreur non gérée à l'insertion 
	 */
	public static final int INSERT_UTILISATEUR_ECHEC=10001;
	
	/**
	 * Echec général quand tentative d'update d'un objet null 
	 */
	public static final int UPDATE_UTILISATEUR_NULL = 10002;
	
	/**
	 * Echec général quand erreur non gérée à la mise à jour 
	 */
	public static final int UPDATE_UTILISATEUR_ECHEC = 10003;
	
	/**
	 * Echec général quand erreur non gérée à la suppression 
	 */
	public static final int DELETE_UTILISATEUR_ECHEC = 10004;
	
	/**
	 * Echec général quand erreur non gérée à la selection 
	 */
	public static final int SELECT_UTILISATEUR_ECHEC = 10005;
	
	

	public static final int SELECT_CATEGORIE_ECHEC = 10080;

	public static final int UPDATE_CATEGORIE_NULL = 10081;

	public static final int UPDATE_CATEGORIE_ECHEC = 10082;

	public static final int DELETE_CATEGORIE_ECHEC = 10083;

	public static final int INSERT_CATEGORIE_NULL = 10084;

	public static final int INSERT_CATEGORIE_ECHEC = 10085;



	
}












