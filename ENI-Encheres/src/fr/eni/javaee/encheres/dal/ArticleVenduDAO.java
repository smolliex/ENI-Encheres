package fr.eni.javaee.encheres.dal;

import java.util.List;

import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.messages.BusinessException;

public interface ArticleVenduDAO extends DAO<ArticleVendu>{

	//Sélectionner Toutes les encheres en cours selon la catégorie
	public List<ArticleVendu> selectAllEnCours() throws BusinessException;
	public List<ArticleVendu> selectAllEnCours(int no_categorie) throws BusinessException;
	
	//Encheres en-cours sauf celles du user 
	public List<ArticleVendu> selectEncheresEnCours(int no_utilisateur) throws BusinessException;
	public List<ArticleVendu> selectEncheresEnCours(int no_utilisateur, int no_categorie) throws BusinessException;
	
	//Encheres en cours pour le user 
	public List<ArticleVendu> selectEncheresEnCoursUtilisateur(int no_utilisateur) throws BusinessException;
	public List<ArticleVendu> selectEncheresEnCoursUtilisateur(int no_utilisateur, int no_categorie) throws BusinessException;
	
	//Encheres remportées pour le user 
	public List<ArticleVendu> selectEncheresRemporteesUtilisateur(int no_utilisateur) throws BusinessException;
	public List<ArticleVendu> selectEncheresRemporteesUtilisateur(int no_utilisateur, int no_categorie) throws BusinessException;
	
	//Ventes en cours du user 
	public List<ArticleVendu> selectVentesEnCoursUtilisateur(int no_utilisateur) throws BusinessException;
	public List<ArticleVendu> selectVentesEnCoursUtilisateur(int no_utilisateur, int no_categorie) throws BusinessException;
	
	//Ventes à venir du user 
	public List<ArticleVendu> selectVentesAVenirUtilisateur(int no_utilisateur) throws BusinessException;
	public List<ArticleVendu> selectVentesAVenirUtilisateur(int no_utilisateur, int no_categorie) throws BusinessException;
	
	//Ventes terminées du user 
	public List<ArticleVendu> selectVentesTermineesUtilisateur(int no_utilisateur) throws BusinessException;
	public List<ArticleVendu> selectVentesTermineesUtilisateur(int no_utilisateur, int no_categorie) throws BusinessException;
	
	
}
