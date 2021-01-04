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
 * Servlet implementation class ServletAdministration
 */
@WebServlet("/Administration")
public class ServletAdministration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
		
		// Initialistion de la liste des utilisateurs et des catégories
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		List<Categorie> categories = new ArrayList<Categorie>();

		// Récupération de la liste des catégories
		List<Categorie> listeCategories = new ArrayList<Categorie>();
		listeCategories = selectionnerToutesLesCategories(listeCodesErreur);
		request.setAttribute("listeCategories", listeCategories);
		
		// Récupération de la liste des utilisateurs
		List<Utilisateur> listeUtilisateurs = new ArrayList<Utilisateur>();
		listeUtilisateurs = selectionnerTousLesUtilisateurs(listeCodesErreur);
		request.setAttribute("listeUtilisateurs", listeUtilisateurs);
		System.out.println(listeUtilisateurs);
		
		// Récupération de la liste des codes d'erreurs
		request.setAttribute("listeCodesErreur", listeCodesErreur);
		
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/administration.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------//
	// Méthodes utilisant les managers
	//--------------------------------------------------------------------------------------------------------------------------------------------------//
	public List<Categorie> selectionnerToutesLesCategories(List<Integer> listeCodesErreur){
		List<Categorie> listeCategories = new ArrayList<Categorie>();	
		CategorieManager cm = CategorieManager.getInstance();
		try {
			listeCategories = cm.getListeCategories();
		} catch (BusinessException e) {
			e.printStackTrace();
			listeCodesErreur.add(CodesResultatServlets.SELECTION_DE_TOUTES_LES_CATEGORIES_ERREUR);
			
		}

		return listeCategories;
	}
	
	public List<Utilisateur> selectionnerTousLesUtilisateurs(List<Integer> listeCodesErreur) {
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		UtilisateurManager um = UtilisateurManager.getInstance();
		try {
			utilisateurs = um.getListeUtilisateurs();
		} catch (BusinessException e) {
			e.printStackTrace();
			listeCodesErreur.add(CodesResultatServlets.SELECTION_DES_UTILISATEURS_ERREUR);
		}
		return utilisateurs;
	}

}
