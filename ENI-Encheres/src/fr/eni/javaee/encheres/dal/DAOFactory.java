package fr.eni.javaee.encheres.dal;

import fr.eni.javaee.encheres.dal.UtilisateurDAO;

public class DAOFactory {
	
	public static UtilisateurDAO getUtilisateurDAO() {
		UtilisateurDAO utilisateurDAO = null;
		try {
			utilisateurDAO = (UtilisateurDAO) Class.forName("fr.eni.javaee.encheres.dal.ArticleDAOJdbcImpl").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return utilisateurDAO;
		
	}

}
