package fr.eni.javaee.encheres.dal;
/*
 * 10000 à 10020 pour utilisateurs, 
 * 10021 à 10040 pour articles, 
 * 10041 à 10060 pour enchères, 
 * 10061 à 10080 pour retrait, 
 * 10081 à 10100 pour catégorie.
*/
 
/**
 * Les codes disponibles sont entre 10000 et 19999
 */
public abstract class CodesResultatDAL {
	
	/******* UTILISATEUR *******/
	public static final int INSERT_UTILISATEUR_NULL=10000;
	public static final int INSERT_UTILISATEUR_ECHEC=10001;
	public static final int UPDATE_UTILISATEUR_NULL = 10002;
	public static final int UPDATE_UTILISATEUR_ECHEC = 10003;
	public static final int DELETE_UTILISATEUR_ECHEC = 10004;
	public static final int SELECT_UTILISATEUR_ECHEC = 10005;
	public static final int INSERT_ARTICLE_NULL=10021;
	public static final int INSERT_ARTICLE_ECHEC=10022;
	public static final int UPDATE_ARTICLE_NULL = 10023;
	public static final int UPDATE_ARTICLE_ECHEC = 10024;
	public static final int DELETE_ARTICLE_ECHEC = 10025;
	public static final int SELECT_ARTICLE_ECHEC = 10026;
	public static final int BUILDER_ARTICLE_ECHEC = 10027;
	
	/******* CATEGORIE *******/
	public static final int INSERT_CATEGORIE_NULL=10081;
	public static final int INSERT_CATEGORIE_ECHEC=10082;
	public static final int UPDATE_CATEGORIE_NULL = 10083;
	public static final int UPDATE_CATEGORIE_ECHEC = 10084;
	public static final int DELETE_CATEGORIE_ECHEC = 10085;
	public static final int SELECT_CATEGORIE_ECHEC = 10086;
	public static final int BUILDER_CATEGORIE_ECHEC = 10087;
	
}












