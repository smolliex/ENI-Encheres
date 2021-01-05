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

import fr.eni.javaee.encheres.bll.CategorieManager;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;

/**
 * Servlet implementation class ServletAdministrationCategorie
 */
@WebServlet("/AdministrationDesCategories")
public class ServletAdministrationCategorie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Récupération de l'action à éffectuer
		String action = request.getParameter("action");
		System.out.println(action);
		
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
		BusinessException businessException = new BusinessException();
		
		if (action.equals("creer")) {
			request.setAttribute("creer", action);
		}
		if (action.equals("modifier") && request.getParameter("categorie") != null) {
			request.setAttribute("modifier", action);
			int noCategorie = Integer.parseInt(request.getParameter("categorie"));
			//Récupération catégorie choisie
			if (noCategorie > -1) {
				Categorie categorie = selectionnerUneCategorie(noCategorie,businessException);
				System.out.println(categorie);
				request.setAttribute("categorie", categorie);
			}
		}
		
		// Récupération de la liste des catégories
		List<Categorie> listeCategories = new ArrayList<Categorie>();
		listeCategories = selectionnerToutesLesCategories(businessException);
		request.setAttribute("listeCategories", listeCategories);
		
		// Récupération de la liste des utilisateurs
		List<Utilisateur> listeUtilisateurs = new ArrayList<Utilisateur>();
		listeUtilisateurs = selectionnerTousLesUtilisateurs(businessException);
		request.setAttribute("listeUtilisateurs", listeUtilisateurs);
		
		// Récupération de la liste des codes d'erreurs
		listeCodesErreur = businessException.getListeCodesErreur();
		request.setAttribute("listeCodesErreur", listeCodesErreur);
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/administrationCategorie.jsp");
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Encodage des caractères.
		request.setCharacterEncoding("UTF-8");
		
		// Récupération de l'action à éffectuer
		String action = request.getParameter("action");
				;
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
		BusinessException businessException = new BusinessException();
		
		if (action.equals("creer")) {
			Categorie categorie = new Categorie();
			categorie.setLibelle(request.getParameter("libelle"));
			creerCategorie(categorie,businessException);
		}
		if (action.equals("modifier") && request.getParameter("categorie") != null) {
			int noCategorie;
			Categorie categorie = null;
			noCategorie = Integer.parseInt(request.getParameter("categorie"));
			//Récupération catégorie
			if (noCategorie > 0) {
				categorie = selectionnerUneCategorie(noCategorie,businessException);
			}
			
			categorie.setLibelle(request.getParameter("libelle"));
			updateCategorie(categorie,businessException);
		}
		
		// Récupération de la liste des catégories
		List<Categorie> listeCategories = new ArrayList<Categorie>();
		listeCategories = selectionnerToutesLesCategories(businessException);
		request.setAttribute("listeCategories", listeCategories);
		
		// Récupération de la liste des utilisateurs
		List<Utilisateur> listeUtilisateurs = new ArrayList<Utilisateur>();
		listeUtilisateurs = selectionnerTousLesUtilisateurs(businessException);
		request.setAttribute("listeUtilisateurs", listeUtilisateurs);
		
		// Récupération de la liste des codes d'erreurs
		listeCodesErreur = businessException.getListeCodesErreur();
		request.setAttribute("listeCodesErreur", listeCodesErreur);
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/administration.jsp");
		rd.forward(request, response);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------//
	// Méthodes utilisant les managers
	//--------------------------------------------------------------------------------------------------------------------------------------------------//

	public void creerCategorie(Categorie categorie, BusinessException businessException){	
			CategorieManager cm = CategorieManager.getInstance();
			try {
				cm.createCategorie(categorie);
			} catch (BusinessException e) {
				e.printStackTrace();
				businessException.ajouterErreur(CodesResultatServlets.CREER_CATEGORIE_ERREUR);		
			}
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
	
	public void updateCategorie(Categorie categorie, BusinessException businessException){	
		CategorieManager cm = CategorieManager.getInstance();
		try {
			cm.updateCategorie(categorie);
		} catch (BusinessException e) {
			e.printStackTrace();
			businessException.ajouterErreur(CodesResultatServlets.UPDATE_CATEGORIE_ERREUR);
		}
	}
	
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
	
	public List<Utilisateur> selectionnerTousLesUtilisateurs(BusinessException businessException) {
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		UtilisateurManager um = UtilisateurManager.getInstance();
		try {
			utilisateurs = um.getListeUtilisateurs();
		} catch (BusinessException e) {
			e.printStackTrace();
			businessException.ajouterErreur(CodesResultatServlets.SELECTION_DES_UTILISATEURS_ERREUR);
		}
		return utilisateurs;
	}

}
