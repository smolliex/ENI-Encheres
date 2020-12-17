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
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Retrait;
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
		
		//HttpSession session = request.getSession();
		List<Enchere> listeEncheres = new ArrayList<Enchere>();
		List<Categorie> listeCategories = new ArrayList<Categorie>();
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		
		
		
		
		
		listeCategories = selectionnerToutesLesCategories();
		listeArticlesVendus = selectionnerTousLesArticlesVendus();
		System.out.println(listeCategories);
		System.out.println(listeArticlesVendus);
		/*listeEncheres = selectionnerToutesLesEncheres();*/
			
		request.setAttribute("listeCategories", listeCategories);
		request.setAttribute("listeArticlesVendus", listeArticlesVendus);
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/listeEncheres.jsp");
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		String nomArticle;
		String nomCategorie;
		
		request.setCharacterEncoding("UTF-8");
	
		if (request.getParameter("article") != null && !request.getParameter("article").contentEquals("") && request.getParameter("categorie") != null) {
			nomArticle = request.getParameter("article");
			nomCategorie = request.getParameter("categorie");
			List<Enchere> listeEncheres = new ArrayList<Enchere>();
			listeEncheres = rechercherParCategorieEtNomDArticle(nomArticle, nomCategorie);
		}
		if (request.getParameter("article") == null && request.getParameter("article").contentEquals("") && request.getParameter("categorie") != null) {
			nomCategorie = request.getParameter("categorie");
			List<Enchere> listeEncheres = new ArrayList<Enchere>();
			listeEncheres = rechercherParNomDeCategorie(nomCategorie);
		}
		*/
	}
	/*
	public List<Enchere> rechercherParNomDeCategorieEtNomDArticle(String nomArticle,String nomCategorie){
		EnchereManager em;
		List<Enchere> listeEncheres = em.rechercherParNomDeCategorieEtNomDArticle(nomArticle, nomCategorie);
		return listeEncheres;
	}
	public List<Enchere> rechercherParCategorie(String nomCategorie){
		EnchereManager em;
		List<Enchere> listeEncheres = em.rechercherParNomDeCategorie(nomCategorie);
		return listeEncheres;
	}*/
	/*
	public List<Enchere> selectionnerToutesLesEncheres(){
		EnchereManager em;
		List<Enchere> listeEncheres = em.selectionnerToutesLesEncheres();
		return listeEncheres;
	}*/
	
	public List<Categorie> selectionnerToutesLesCategories(){
			List<Categorie> listeCategories = new ArrayList<Categorie>();	
			CategorieManager cm = CategorieManager.getInstance();
			try {
				listeCategories = cm.getListeCategories();
			} catch (BusinessException e) {
				e.printStackTrace();
			}

		return listeCategories;
	}
	
	public List<ArticleVendu> selectionnerTousLesArticlesVendus(){
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		try {
			listeArticlesVendus = avm.getListeArticlesVendu();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return listeArticlesVendus;
	}
	
	

}
