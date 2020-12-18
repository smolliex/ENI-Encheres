package fr.eni.javaee.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import fr.eni.javaee.encheres.bll.CategorieManager;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.dal.ArticleVenduDAO;
import fr.eni.javaee.encheres.dal.CodesResultatDAL;
import fr.eni.javaee.encheres.messages.BusinessException;
import fr.eni.javaee.encheres.utils.ErrorLogger;

public class ArticleVenduDAOJdbcImpl implements ArticleVenduDAO{
	
	private static Logger logger = ErrorLogger.getLogger("ArticleVenduDAOJdbcImpl");
	
	private UtilisateurManager utilisateurManager = UtilisateurManager.getInstance();
	private CategorieManager categorieManager = CategorieManager.getInstance();
	
	private static final String SQL_SELECT_BY=
			"SELECT * FROM ARTICLES_VENDUS WHERE no_article=?;";
	private static final String SQL_SELECT_ALL=
			"SELECT * FROM ARTICLES_VENDUS ORDER BY nom_article;";
	private static final String SQL_SELECT_ALL_ENCOURS=
			"SELECT * FROM ARTICLES_VENDUS WHERE date_debut_encheres<=? AND date_fin_encheres>=?;";
	private static final String SQL_SELECT_ALL_ENCOURS_CATEGORIE=
			"SELECT * FROM ARTICLES_VENDUS WHERE no_categorie=? AND date_debut_encheres<=? AND date_fin_encheres>=?;";
	private static final String SQL_SELECT_ENCHERES_ENCOURS=	
			"SELECT * FROM ARTICLES_VENDUS WHERE no_utilisateur<>? AND date_debut_encheres<=? AND date_fin_encheres>=? ORDER BY nom_article;";
	private static final String SQL_SELECT_ENCHERES_ENCOURS_CATEGORIE=	
			"SELECT * FROM ARTICLES_VENDUS WHERE no_utilisateur<>? AND no_categorie=? AND date_debut_encheres<=? AND date_fin_encheres>=? ORDER BY nom_article;";
	private static final String SQL_SELECT_ENCHERES_UTILISATEUR=
			"SELECT a.* FROM ARTICLES_VENDUS a " + 
			"INNER JOIN ENCHERES e on a.no_article = e.no_article " + 
			"WHERE e.no_utilisateur=? AND date_debut_encheres<=? AND date_fin_encheres>=? ORDER BY nom_article;";
	private static final String SQL_SELECT_ENCHERES_UTILISATEUR_CATEGORIE=
			"SELECT a.* FROM ARTICLES_VENDUS a " + 
			"INNER JOIN ENCHERES e on a.no_article = e.no_article " + 
			"WHERE e.no_utilisateur=? AND no_categorie=? AND date_debut_encheres<=? AND date_fin_encheres>=? ORDER BY nom_article;";
	private static final String SQL_SELECT_ENCHERES_REMPORTEES=
			"SELECT a.* FROM ARTICLES_VENDUS a " + 
			"INNER JOIN ENCHERES e on a.no_article = e.no_article " + 
			"WHERE e.no_utilisateur=? AND e.montant_enchere=a.prix_vente AND date_fin_encheres<? ORDER BY nom_article;";
	private static final String SQL_SELECT_ENCHERES_REMPORTEES_CATEGORIE=
			"SELECT a.* FROM ARTICLES_VENDUS a " + 
			"INNER JOIN ENCHERES e on a.no_article = e.no_article " + 
			"WHERE e.no_utilisateur=? AND no_categorie=? AND e.montant_enchere=a.prix_vente AND date_fin_encheres<? ORDER BY nom_article;";
	private static final String SQL_SELECT_VENTES_ENCOURS=
			"SELECT * FROM ARTICLES_VENDUS WHERE no_utilisateur=? AND date_debut_encheres<=? AND date_fin_encheres>=? ORDER BY nom_article;";
	private static final String SQL_SELECT_VENTES_ENCOURS_CATEGORIE=
			"SELECT * FROM ARTICLES_VENDUS WHERE no_utilisateur=? AND no_categorie=? AND date_debut_encheres<=? AND date_fin_encheres>=? ORDER BY nom_article;";
	private static final String SQL_SELECT_VENTES_A_VENIR=
			"SELECT * FROM ARTICLES_VENDUS WHERE no_utilisateur=? AND date_debut_encheres>?  ORDER BY nom_article;";
	private static final String SQL_SELECT_VENTES_A_VENIR_CATEGORIE=
			"SELECT * FROM ARTICLES_VENDUS WHERE no_utilisateur=? AND no_categorie=? AND date_debut_encheres>?  ORDER BY nom_article;";
	private static final String SQL_SELECT_VENTES_TERMINEES=
			"SELECT * FROM ARTICLES_VENDUS WHERE no_utilisateur=? AND date_fin_encheres<? ORDER BY nom_article;";
	private static final String SQL_SELECT_VENTES_TERMINEES_CATEGORIE=
			"SELECT * FROM ARTICLES_VENDUS WHERE no_utilisateur=? AND no_categorie=? AND date_fin_encheres<? ORDER BY nom_article;";
	private static final String SQL_INSERT=
			"INSERT INTO ARTICLES_VENDUS (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie ) VALUES (?,?,?,?,?,?,?,?);";
	private static final String SQL_UPDATE=
			"UPDATE ARTICLES_VENDUS SET nom_article=?, description=?, date_debut_encheres=?, date_fin_encheres=?, prix_initial=?, prix_vente=?, no_utilisateur=?, no_categorie=? WHERE no_article=?;";
	private static final String SQL_DELETE=
			"DELETE FROM ARTICLES_VENDUS WHERE no_article=?;";
	
	@Override
	public ArticleVendu selectById(int no_article) throws BusinessException {
		
		ArticleVendu articleVendu=null;
		
		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_SELECT_BY);
			stmt.setInt(1, no_article);
			ResultSet rs=stmt.executeQuery();
			if (rs.next()) {
				articleVendu=articleBuilder(rs);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECT_ARTICLE_ECHEC);
			throw businessException;
		}
		
		return articleVendu;
	}

	private List<ArticleVendu> select(String sql, int no_utilisateur, int no_categorie) throws BusinessException {
		
		List<ArticleVendu> liste=new ArrayList<>();
		
		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(sql);
			
			//recupere la date du jour
			Date date = new Date();
			java.sql.Date today = java.sql.Date.valueOf(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			
			//passe les parametres selon le contexte
			switch(sql) {
			
				case SQL_SELECT_ALL_ENCOURS :
					stmt.setDate(1, today);
					stmt.setDate(2, today);		
					break;
					
				case SQL_SELECT_ALL_ENCOURS_CATEGORIE :
					stmt.setInt(1, no_categorie);
					stmt.setDate(2, today);
					stmt.setDate(3, today);
					break;
					
				case SQL_SELECT_ENCHERES_ENCOURS :
					stmt.setInt(1, no_utilisateur);
					stmt.setDate(2, today);
					stmt.setDate(3, today);
					break;
					
				case SQL_SELECT_ENCHERES_ENCOURS_CATEGORIE :
					stmt.setInt(1, no_utilisateur);
					stmt.setInt(2, no_categorie);
					stmt.setDate(3, today);
					stmt.setDate(4, today);
					break;
					
				case SQL_SELECT_ENCHERES_UTILISATEUR :
					stmt.setInt(1, no_utilisateur);
					stmt.setDate(2, today);
					stmt.setDate(3, today);
					break;
					
				case SQL_SELECT_ENCHERES_UTILISATEUR_CATEGORIE :
					stmt.setInt(1, no_utilisateur);
					stmt.setInt(2, no_categorie);
					stmt.setDate(3, today);
					stmt.setDate(4, today);
					break;
					
				case SQL_SELECT_ENCHERES_REMPORTEES :
					stmt.setInt(1, no_utilisateur);
					stmt.setDate(2, today);
					break;			
					
				case SQL_SELECT_ENCHERES_REMPORTEES_CATEGORIE :
					stmt.setInt(1, no_utilisateur);
					stmt.setInt(2, no_categorie);
					stmt.setDate(3, today);
					break;
					
				case SQL_SELECT_VENTES_ENCOURS :
					stmt.setInt(1, no_utilisateur);
					stmt.setDate(2, today);
					stmt.setDate(3, today);
					break;
					
				case SQL_SELECT_VENTES_ENCOURS_CATEGORIE :
					stmt.setInt(1, no_utilisateur);
					stmt.setInt(2, no_categorie);
					stmt.setDate(3, today);
					stmt.setDate(4, today);
					break;
					
				case SQL_SELECT_VENTES_A_VENIR :
					stmt.setInt(1, no_utilisateur);
					stmt.setDate(2, today);
					break;					
					
				case SQL_SELECT_VENTES_A_VENIR_CATEGORIE :
					stmt.setInt(1, no_utilisateur);
					stmt.setInt(2, no_categorie);
					stmt.setDate(3, today);
					break;
					
				case SQL_SELECT_VENTES_TERMINEES :
					stmt.setInt(1, no_utilisateur);
					stmt.setDate(2, today);
					break;	
					
				case SQL_SELECT_VENTES_TERMINEES_CATEGORIE :
					stmt.setInt(1, no_utilisateur);
					stmt.setInt(2, no_categorie);
					stmt.setDate(3, today);
					break;
			}
			
			//execute la requete
			ResultSet rs=stmt.executeQuery();
			while (rs.next()) {
				ArticleVendu articleVendu=new ArticleVendu();
				articleVendu=articleBuilder(rs);
				liste.add(articleVendu);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECT_ARTICLE_ECHEC);
			throw businessException;
		}
		
		return liste;
	}


	@Override
	public void insert(ArticleVendu obj) throws BusinessException {

		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_INSERT);
			stmt.setString(1, obj.getNom_article());
			stmt.setString(2, obj.getDescription());
			stmt.setDate(3, java.sql.Date.valueOf(obj.getDate_debut_encheres()));
			stmt.setDate(4, java.sql.Date.valueOf(obj.getDate_fin_encheres()));
			stmt.setInt(5, obj.getPrix_initial());
			stmt.setInt(6, obj.getPrix_vente());
			stmt.setInt(7, obj.getVendeur().getNo_utilisateur());
			stmt.setInt(8, obj.getCategorie().getNo_categorie());
			
			stmt.executeUpdate();
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECT_ARTICLE_ECHEC);
			throw businessException;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_ARTICLE_ECHEC);
			throw businessException;
		}
		
	}


	@Override
	public void update(ArticleVendu obj) throws BusinessException {

		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_UPDATE);
			stmt.setString(1, obj.getNom_article());
			stmt.setString(2, obj.getDescription());
			stmt.setDate(3, java.sql.Date.valueOf(obj.getDate_debut_encheres()));
			stmt.setDate(4, java.sql.Date.valueOf(obj.getDate_fin_encheres()));
			stmt.setInt(5, obj.getPrix_initial());
			stmt.setInt(6, obj.getPrix_vente());
			stmt.setInt(7, obj.getVendeur().getNo_utilisateur());
			stmt.setInt(8, obj.getCategorie().getNo_categorie());
			stmt.setInt(9, obj.getNo_article());
			
			stmt.executeUpdate();
		
		} catch (NullPointerException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_ARTICLE_NULL);
			throw businessException;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_ARTICLE_ECHEC);
			throw businessException;
		}
		
	}
	

	@Override
	public void delete(int no_article) throws BusinessException {

		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_DELETE);
			stmt.setInt(1, no_article);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_ARTICLE_ECHEC);
			throw businessException;
		}
		
	}

	//Charge l'objet à partir du ResultSet
	private ArticleVendu articleBuilder(ResultSet rs) throws BusinessException, SQLException {
		
		ArticleVendu articleVendu = new ArticleVendu();
		
		try {
			articleVendu.setNo_article(rs.getInt("no_article"));
			articleVendu.setNom_article(rs.getString("nom_article"));
			articleVendu.setDescription(rs.getString("description"));
			articleVendu.setDate_debut_encheres(rs.getDate("date_debut_encheres").toLocalDate());
			articleVendu.setDate_fin_encheres(rs.getDate("date_fin_encheres").toLocalDate());
			articleVendu.setPrix_initial(rs.getInt("prix_initial"));
			articleVendu.setPrix_vente(rs.getInt("prix_vente"));
			
			Utilisateur vendeur = utilisateurManager.getUtilisateur(rs.getInt("no_utilisateur"));
			articleVendu.setVendeur(vendeur);
			
			Categorie categorie = categorieManager.getCategorie(rs.getInt("no_categorie"));
			articleVendu.setCategorie(categorie);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.BUILDER_ARTICLE_ECHEC);
			throw businessException;
		}

		return articleVendu;
	}

	@Override
	public List<ArticleVendu> selectAll() throws BusinessException {
		// Liste de tous les articles en base de données
		List<ArticleVendu> articles = select(SQL_SELECT_ALL,-1,-1);
		return articles;
	}
	
	@Override
	public List<ArticleVendu> selectAllEnCours() throws BusinessException {
		//Toutes les encheres en cours de toutes les catégories
		List<ArticleVendu> articles = select(SQL_SELECT_ALL_ENCOURS, -1, -1);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectAllEnCours(int no_categorie) throws BusinessException {
		//Toutes les encheres en cours selon la catégorie indiquée
		List<ArticleVendu> articles = select(SQL_SELECT_ALL_ENCOURS_CATEGORIE, -1, no_categorie);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectEncheresEnCours(int no_utilisateur) throws BusinessException {
		//Encheres en-cours toutes categories, sauf celles du user connecté
		List<ArticleVendu> articles = select(SQL_SELECT_ENCHERES_ENCOURS, no_utilisateur, -1);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectEncheresEnCours(int no_utilisateur, int no_categorie) throws BusinessException {
		//Encheres en-cours de la categorie sauf celles du user connecté 
		List<ArticleVendu> articles = select(SQL_SELECT_ENCHERES_ENCOURS_CATEGORIE, no_utilisateur, no_categorie);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectEncheresEnCoursUtilisateur(int no_utilisateur) throws BusinessException {
		//Encheres en cours toutes categories pour le user connecte
		List<ArticleVendu> articles = select(SQL_SELECT_ENCHERES_UTILISATEUR, no_utilisateur, -1);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectEncheresEnCoursUtilisateur(int no_utilisateur, int no_categorie) throws BusinessException {
		//Encheres en cours pour la categorie pour le user connecte
		List<ArticleVendu> articles = select(SQL_SELECT_ENCHERES_UTILISATEUR_CATEGORIE, no_utilisateur, no_categorie);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectEncheresRemporteesUtilisateur(int no_utilisateur)
			throws BusinessException {
		//Encheres remportées toutes categories pour le user connecte
		List<ArticleVendu> articles = select(SQL_SELECT_ENCHERES_REMPORTEES, no_utilisateur, -1);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectEncheresRemporteesUtilisateur(int no_utilisateur, int no_categorie)
			throws BusinessException {
		//Encheres remportées pour la categorie pour le user connecte
		List<ArticleVendu> articles = select(SQL_SELECT_ENCHERES_REMPORTEES_CATEGORIE, no_utilisateur, no_categorie);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectVentesEnCoursUtilisateur(int no_utilisateur) throws BusinessException {
		//Ventes en cours du user toutes ctegories
		List<ArticleVendu> articles = select(SQL_SELECT_VENTES_ENCOURS, no_utilisateur, -1);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectVentesEnCoursUtilisateur(int no_utilisateur, int no_categorie)
			throws BusinessException {
		//Ventes en cours du user pour la categorie
		List<ArticleVendu> articles = select(SQL_SELECT_VENTES_ENCOURS_CATEGORIE, no_utilisateur, no_categorie);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectVentesAVenirUtilisateur(int no_utilisateur) throws BusinessException {
		//Ventes à venir du user  toutes ctegories
		List<ArticleVendu> articles = select(SQL_SELECT_VENTES_A_VENIR, no_utilisateur, -1);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectVentesAVenirUtilisateur(int no_utilisateur, int no_categorie)
			throws BusinessException {
		//Ventes à venir du user  pour la categorie
		List<ArticleVendu> articles = select(SQL_SELECT_VENTES_A_VENIR_CATEGORIE, no_utilisateur, no_categorie);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectVentesTermineesUtilisateur(int no_utilisateur) throws BusinessException {
		//Ventes terminées du user  toutes ctegories
		List<ArticleVendu> articles = select(SQL_SELECT_VENTES_TERMINEES_CATEGORIE, no_utilisateur, -1);
		return articles;
	}

	@Override
	public List<ArticleVendu> selectVentesTermineesUtilisateur(int no_utilisateur, int no_categorie)
			throws BusinessException {
		//Ventes terminées du user  pour la categorie
		List<ArticleVendu> articles = select(SQL_SELECT_VENTES_TERMINEES_CATEGORIE, no_utilisateur, no_categorie);
		return articles;
	}

}
