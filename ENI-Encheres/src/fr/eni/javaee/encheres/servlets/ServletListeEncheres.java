package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.javaee.encheres.bll.ArticleVenduManager;
import fr.eni.javaee.encheres.bll.CategorieManager;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.bo.Categorie;

import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;



/**
 * Servlet implementation class ServletListeEncheres
 */
@WebServlet("/ListeEncheres")
public class ServletListeEncheres extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
		BusinessException businessException = new BusinessException();

	
		// Récupération de la liste des catégories
		List<Categorie> listeCategories = new ArrayList<Categorie>();
		listeCategories = selectionnerToutesLesCategories(businessException);
		request.setAttribute("listeCategories", listeCategories);
		
		// Récupération de la liste de toutes les enchères en cours
		List<ArticleVendu> listeDeToutesLesEncheresEnCours = new ArrayList<ArticleVendu>();		
		listeDeToutesLesEncheresEnCours = selectionnerToutesLesEncheresEnCours(-1, "", businessException);
		request.setAttribute("liste", listeDeToutesLesEncheresEnCours);
		
		// Récupération de la liste des codes d'erreurs
		listeCodesErreur = businessException.getListeCodesErreur();
		request.setAttribute("listeCodesErreur", listeCodesErreur);
		
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/listeEncheres.jsp");
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
		BusinessException businessException = new BusinessException();
		
		// Encodage des caractères.
		request.setCharacterEncoding("UTF-8");
		
		// Récupération de la session.
		HttpSession session = request.getSession();
		
		// Initialisation des paramètres.
		String nomArticle = "";
		int noCategorie = -1;
		String choixUtilisateur = null;
		String choixAchat = null;
		String choixVente = null;
		List<ArticleVendu> listeAvecParamètres;
		
		// Recheche avec paramètres en mode connecté avec la présence d'un utilisateur en session.
		if(session.getAttribute("utilisateur")!=null) {
			try {
				// Récupération de l'identifiant de l'utilisateur
				Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
				int noUtilisateur = utilisateur.getNo_utilisateur();
				
				// Récuparation du choix de l'utilisateur
				choixUtilisateur = request.getParameter("choix");
				request.setAttribute("choixUtilisateur", choixUtilisateur);
				choixAchat = request.getParameter("choixAchat");
				request.setAttribute("choixAchat", choixAchat);
				choixVente = request.getParameter("choixVente");
				request.setAttribute("choixVente", choixVente);
				
				//Récupération du nom de l'article à rechercher
				nomArticle = request.getParameter("article");
				request.setAttribute("nomArticleSaisi", nomArticle);
				
				if (request.getParameter("categorie") != null) {
					noCategorie = Integer.parseInt(request.getParameter("categorie"));		
					//Récupération catégorie choisie
					if (noCategorie > -1) {
						Categorie categorieSelectionnee = selectionnerUneCategorie(noCategorie,businessException);
						request.setAttribute("categorieSelectionnee", categorieSelectionnee);
					}				
				}
				
				//Récupération liste des catégories
				List<Categorie> listeCategories = new ArrayList<Categorie>();
				listeCategories = selectionnerToutesLesCategories(businessException);
				request.setAttribute("listeCategories", listeCategories);
				
				// Si le choix concerne les achats
				if (choixUtilisateur.equalsIgnoreCase("achats")) {
					switch (choixAchat) {
					case "encheresOuvertes":
						listeAvecParamètres = selectionnerLesEncheresOuvertesAvecUnUtilisateurConnecte(noUtilisateur, noCategorie, nomArticle, businessException);
						// Récupération de la liste des codes d'erreurs
						listeCodesErreur = businessException.getListeCodesErreur();
						request.setAttribute("listeCodesErreur", listeCodesErreur);
						request.setAttribute("liste", listeAvecParamètres);
						break;
					case "encheresEnCours":
						listeAvecParamètres = selectionnerLesEncheresEnCoursDeLUtilisateurConnecte(noUtilisateur, noCategorie, nomArticle, businessException);
						// Récupération de la liste des codes d'erreurs
						listeCodesErreur = businessException.getListeCodesErreur();
						request.setAttribute("listeCodesErreur", listeCodesErreur);
						request.setAttribute("liste", listeAvecParamètres);				
						break;
					case "encheresRemportees":
						listeAvecParamètres = selectionnerLesEncheresRemporteesParLUtilisateur(noUtilisateur, noCategorie, nomArticle, businessException);
						// Récupération de la liste des codes d'erreurs
						listeCodesErreur = businessException.getListeCodesErreur();
						request.setAttribute("listeCodesErreur", listeCodesErreur);
						request.setAttribute("liste", listeAvecParamètres);				
						break;
					}
				}
				// Si le choix concerne les ventes
				if (choixUtilisateur.equalsIgnoreCase("ventes")) {
					switch (choixVente) {
					case "ventesEncours":
						listeAvecParamètres = selectionnerLesVentesEnCoursDeLUtilisateur(noUtilisateur, noCategorie, nomArticle, businessException);
						// Récupération de la liste des codes d'erreurs
						listeCodesErreur = businessException.getListeCodesErreur();
						request.setAttribute("listeCodesErreur", listeCodesErreur);
						request.setAttribute("liste", listeAvecParamètres);
						break;
					case "ventesNonDebutees":
						listeAvecParamètres = selectionnerLesVentesNonDebuteesDeLUtilisateur(noUtilisateur, noCategorie, nomArticle, businessException);
						// Récupération de la liste des codes d'erreurs
						listeCodesErreur = businessException.getListeCodesErreur();
						request.setAttribute("listeCodesErreur", listeCodesErreur);
						request.setAttribute("liste", listeAvecParamètres);
						break;
					case "ventesTerminees":
						listeAvecParamètres = selectionnerLesVentesTermineesDeLUtilisateur(noUtilisateur, noCategorie, nomArticle, businessException);
						// Récupération de la liste des codes d'erreurs
						listeCodesErreur = businessException.getListeCodesErreur();
						request.setAttribute("listeCodesErreur", listeCodesErreur);
						request.setAttribute("liste", listeAvecParamètres);
						break;
			
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
		// Recherche avec paramètres sans utilisateur connecté.
			try {
				//Récupération du nom de l'article à rechercher
				nomArticle = request.getParameter("article");
				request.setAttribute("nomArticleSaisi", nomArticle);
				
				noCategorie = Integer.parseInt(request.getParameter("categorie"));
				//Récupération catégorie choisie
				if (noCategorie > -1) {
					Categorie categorieSelectionnee = selectionnerUneCategorie(noCategorie,businessException);
					request.setAttribute("categorieSelectionnee", categorieSelectionnee);
				}
				
				//Récupération liste des catégories
				List<Categorie> listeCategories = new ArrayList<Categorie>();
				listeCategories = selectionnerToutesLesCategories(businessException);
				request.setAttribute("listeCategories", listeCategories);
				
				//Récupération des listes
				listeAvecParamètres = selectionnerToutesLesEncheresEnCours(noCategorie, nomArticle, businessException);
				// Récupération de la liste des codes d'erreurs
				listeCodesErreur = businessException.getListeCodesErreur();
				request.setAttribute("listeCodesErreur", listeCodesErreur);
				request.setAttribute("liste", listeAvecParamètres);
			} catch(NumberFormatException e) {
				// Récupération de la liste des codes d'erreurs
				listeCodesErreur = businessException.getListeCodesErreur();
				listeCodesErreur.add(CodesResultatServlets.FORMAT_NUMERO_CATEGORIE_ERREUR);
				request.setAttribute("listeCodesErreur", listeCodesErreur);
			}	
		}
		
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/listeEncheres.jsp");
		rd.forward(request, response);
	}
	
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------//
	// Méthodes utilisant les managers
	//--------------------------------------------------------------------------------------------------------------------------------------------------//

	public List<Categorie> selectionnerToutesLesCategories(BusinessException businessException){
			List<Categorie> listeCategories = new ArrayList<Categorie>();	
			CategorieManager cm = CategorieManager.getInstance();
			try {
				listeCategories = cm.getListeCategories();
			} catch (BusinessException e) {
				e.printStackTrace();
				businessException.ajouterErreur(CodesResultatServlets.SELECTION_DE_TOUTES_LES_CATEGORIES_ERREUR);
				
			}

		return listeCategories;
	}
	
	public Categorie selectionnerUneCategorie(int noCategorie,BusinessException businessException){
		Categorie categorie = null;	
		CategorieManager cm = CategorieManager.getInstance();
		try {
			categorie = cm.getCategorie(noCategorie);
		} catch (BusinessException e) {
			e.printStackTrace();
			businessException.ajouterErreur(CodesResultatServlets.SELECTION_DUNE_CATEGORIE_ERREUR);
		}

	return categorie;
	}
	
	public List<ArticleVendu> selectionnerToutesLesEncheresEnCours(int noCategorie,String nomArticle, BusinessException businessException){
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		try {
			listeArticlesVendus = avm.getListeEncheresEncoursToutes(noCategorie,nomArticle);
		} catch (BusinessException e) {
			e.printStackTrace();
			businessException.ajouterErreur(CodesResultatServlets.SELECTION_DE_TOUTES_LES_ENCHERES_EN_COURS_ERREUR);
		}
		return listeArticlesVendus;
	}
	
	public Utilisateur selectionnerUnUtilisateur(int no_utilisateur, BusinessException businessException) {
		Utilisateur utilisateur = null;
		UtilisateurManager um = UtilisateurManager.getInstance();
		try {
			utilisateur = um.getUtilisateur(no_utilisateur);
		} catch (BusinessException e) {
			e.printStackTrace();
			businessException.ajouterErreur(CodesResultatServlets.SELECTION_DUN_UTILISATEUR_ERREUR);
		}
		return utilisateur;
	}

	public List<ArticleVendu> selectionnerLesEncheresOuvertesAvecUnUtilisateurConnecte(int no_utilisateur, int no_categorie, String nom_article, BusinessException businessException) {
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		try {
			listeArticlesVendus = avm.getListeEncheresEncoursAutresVendeurs(no_utilisateur, no_categorie, nom_article);
		} catch (BusinessException e) {
			e.printStackTrace();
			businessException.ajouterErreur(CodesResultatServlets.SELECTION_DE_TOUTES_LES_ENCHERES_EN_COURS_EN_MODE_CONNECTE_ERREUR);
		}
		return listeArticlesVendus;
	}
	
	public List<ArticleVendu> selectionnerLesEncheresEnCoursDeLUtilisateurConnecte(int no_utilisateur, int no_categorie, String nom_article, BusinessException businessException) {
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		try {
			listeArticlesVendus = avm.getListeEncheresEncoursUtilisateur(no_utilisateur, no_categorie, nom_article);
		} catch (BusinessException e) {
			e.printStackTrace();
			businessException.ajouterErreur(CodesResultatServlets.SELECTION_DES_ENCHERES_EN_COURS_EN_MODE_CONNECTE_ERREUR);
		}
		return listeArticlesVendus;
	}
	
	public List<ArticleVendu> selectionnerLesEncheresRemporteesParLUtilisateur(int no_utilisateur, int no_categorie, String nom_article, BusinessException businessException) {
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		try {
			listeArticlesVendus = avm.getListeEncheresRemporteesUtilisateur(no_utilisateur, no_categorie, nom_article);
		} catch (BusinessException e) {
			e.printStackTrace();
			businessException.ajouterErreur(CodesResultatServlets.SELECTION_DES_ENCHERES_REMPORTEES_EN_MODE_CONNECTE_ERREUR);
		}
		return listeArticlesVendus;
	}
	
	public List<ArticleVendu> selectionnerLesVentesEnCoursDeLUtilisateur(int no_utilisateur, int no_categorie, String nom_article, BusinessException businessException) {
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		try {
			listeArticlesVendus = avm.getListeVentesEncoursUtilisateur(no_utilisateur, no_categorie, nom_article);
		} catch (BusinessException e) {
			e.printStackTrace();
			businessException.ajouterErreur(CodesResultatServlets.SELECTION_DES_VENTES_EN_COURS_EN_MODE_CONNECTE_ERREUR);
		}
		return listeArticlesVendus;
	}
	
	public List<ArticleVendu> selectionnerLesVentesNonDebuteesDeLUtilisateur(int no_utilisateur, int no_categorie, String nom_article, BusinessException businessException) {
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		try {
			listeArticlesVendus = avm.getListeVentesAVenirsUtilisateur(no_utilisateur, no_categorie, nom_article);
		} catch (BusinessException e) {
			e.printStackTrace();
			businessException.ajouterErreur(CodesResultatServlets.SELECTION_DES_VENTES_NON_DEBUTEES_EN_MODE_CONNECTE_ERREUR);
		}
		return listeArticlesVendus;
	}
	
	public List<ArticleVendu> selectionnerLesVentesTermineesDeLUtilisateur(int no_utilisateur, int no_categorie, String nom_article, BusinessException businessException) {
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		try {
			listeArticlesVendus = avm.getListeVentesTermineesUtilisateur(no_utilisateur, no_categorie, nom_article);
		} catch (BusinessException e) {
			e.printStackTrace();
			businessException.ajouterErreur(CodesResultatServlets.SELECTION_DES_VENTES_TERMINEES_EN_MODE_CONNECTE_ERREUR);
		}
		return listeArticlesVendus;
	}

}
