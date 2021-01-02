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
import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.messages.BusinessException;

/**
 * Servlet implementation class ServletListeEncheresDeconnecter
 */
@WebServlet("/SeDeconnecter")
public class ServletListeEncheresDeconnecter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletListeEncheresDeconnecter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		HttpSession session = request.getSession();
		
		// Suppression de l'utilisateur en session
		if (session.getAttribute("utilisateur")!=null) {
			session.setAttribute("utilisateur", null);
		}
		
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
			
		// Récupération de la liste des catégories
		List<Categorie> listeCategories = new ArrayList<Categorie>();
		listeCategories = selectionnerToutesLesCategories(listeCodesErreur);
		request.setAttribute("listeCategories", listeCategories);
		
		// Récupération de la liste de toutes les enchères en cours
		List<ArticleVendu> listeDeToutesLesEncheresEnCours = new ArrayList<ArticleVendu>();		
		listeDeToutesLesEncheresEnCours = selectionnerToutesLesEncheresEnCours(-1, "", listeCodesErreur);
		request.setAttribute("liste", listeDeToutesLesEncheresEnCours);
		
		// Récupération de la liste des codes d'erreurs
		request.setAttribute("listeCodesErreur", listeCodesErreur);
		
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/listeEncheres.jsp");
		rd.forward(request, response);
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
	
	public List<ArticleVendu> selectionnerToutesLesEncheresEnCours(int noCategorie,String nomArticle, List<Integer> listeCodesErreur){
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		try {
			listeArticlesVendus = avm.getListeEncheresEncoursToutes(noCategorie,nomArticle);
		} catch (BusinessException e) {
			e.printStackTrace();
			listeCodesErreur.add(CodesResultatServlets.SELECTION_DE_TOUTES_LES_ENCHERES_EN_COURS_ERREUR);
		}
		return listeArticlesVendus;
	}


}
