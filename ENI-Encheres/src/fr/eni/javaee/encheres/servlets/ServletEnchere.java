package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eni.javaee.encheres.bll.ArticleVenduManager;
import fr.eni.javaee.encheres.bll.CategorieManager;
import fr.eni.javaee.encheres.bll.RetraitManager;
import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.bo.Retrait;
import fr.eni.javaee.encheres.messages.BusinessException;
import fr.eni.javaee.encheres.messages.LecteurMessage;


/**
 * Servlet implementation class ServletArticle
 */
@WebServlet("/enchere")

public class ServletEnchere extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Integer> listeCodesErreur = new ArrayList<>();
		Retrait retrait = null;
		
		try {
			
			int no_article=0;
			no_article = lireParametreInt(request, "no_article", listeCodesErreur);
				
			if(no_article>0)
			{
				// charge l'article 
				ArticleVenduManager articleVenduManager = ArticleVenduManager.getInstance();
				ArticleVendu articleVendu = articleVenduManager.getArticleVendu(no_article);
				if(articleVendu==null) {
					listeCodesErreur.add(CodesResultatServlets.VENTE_INCONNUE);
				}
				
				// charge l'adresse de retrait
				RetraitManager retraitManager = RetraitManager.getInstance();
				retrait = retraitManager.getRetrait(articleVendu.getNo_article());
				
				// passe les attributs à la page
				request.setAttribute("article", articleVendu);
				request.setAttribute("retrait", retrait);
				request.setAttribute("categories", listeCategories(listeCodesErreur));
				request.setAttribute("isModifiable", isModifiable(articleVendu));
				
			}else
			{	
				listeCodesErreur.add(CodesResultatServlets.VENTE_INCONNUE);
			}
			
		} catch (BusinessException e) {
			for (int err : e.getListeCodesErreur()) {
				listeCodesErreur.add(err);
			}
		}
		
		// transmet la liste des erreurs
		request.setAttribute("ListeLibellesErreurs",ListeLibellesErreurs(listeCodesErreur));

		// affiche la page
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/enchere.jsp");			
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		List<Integer> listeCodesErreur = new ArrayList<>();
		
		ArticleVendu articleVendu = null;
		Boolean modifiable = Boolean.FALSE;

		// Récupère les valeurs du formulaire
		articleVendu = recupererSaisieArticle(request, listeCodesErreur);
					
		modifiable = lireParametreBoolean(request, "isModifiable", listeCodesErreur);
		
		// controle et enregistre l'enchere

		
		if(listeCodesErreur.size()>0)
		{
			// recharge la page en affichant les erreurs
			request.setAttribute("categories", listeCategories(listeCodesErreur));
			request.setAttribute("article", articleVendu);
			request.setAttribute("isModifiable", modifiable);
			request.setAttribute("ListeLibellesErreurs",ListeLibellesErreurs(listeCodesErreur));
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/enchere.jsp");			
			rd.forward(request, response);
			
		}else {
			// redirige vers la liste
			response.sendRedirect(request.getContextPath() + "/ListeEncheres");
		}
		
	}
	
	private int lireParametreInt(HttpServletRequest request, String parametre, List<Integer> listeCodesErreur) {
		// Renvoie la valeur du parametre de type INT
		int valeur=0;
		try
		{
			if(request.getParameter(parametre)!=null && !request.getParameter(parametre).isEmpty())
			{
				valeur = Integer.parseInt(request.getParameter(parametre));
			}
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			listeCodesErreur.add(CodesResultatServlets.LECTURE_PARAMETRE_VENTE);
		}
		return valeur;
	}
	
	private Boolean lireParametreBoolean(HttpServletRequest request, String parametre, List<Integer> listeCodesErreur) {
		// Renvoie la valeur du parametre de type Boolean
		Boolean valeur=Boolean.FALSE;
		try
		{
			if(request.getParameter(parametre)!=null && !request.getParameter(parametre).isEmpty())
			{
				valeur = Boolean.parseBoolean(request.getParameter(parametre));
			}
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			listeCodesErreur.add(CodesResultatServlets.LECTURE_PARAMETRE_VENTE);
		}
		return valeur;
	}
	
	private ArticleVendu recupererSaisieArticle(HttpServletRequest request, List<Integer> listeCodesErreur) {
		
		//recupere les parametres du formulaire
		int no_article = lireParametreInt(request, "no_article", listeCodesErreur);
		int prixVente = lireParametreInt(request, "prixVente", listeCodesErreur);

		
		//construit l'article
		ArticleVendu articleVendu = new ArticleVendu();	
		
		articleVendu.setNo_article(no_article);
		articleVendu.setPrix_vente(prixVente);
		
		return articleVendu;
	}
	
	private List<String> ListeLibellesErreurs (List<Integer> listeCodesErreur){
		List<String> liste=new ArrayList<>();
		if(listeCodesErreur.size()>0)
			for(int code:listeCodesErreur) {
				liste.add(LecteurMessage.getMessageErreur(code));
			}
		return liste;
	}
	
	private List<Categorie> listeCategories(List<Integer> listeCodesErreur) {
		List<Categorie> categories=null;
		try {
			CategorieManager categorieManager = CategorieManager.getInstance();
			categories = categorieManager.getListeCategories();
		} catch (BusinessException e) {
			for (int err : e.getListeCodesErreur()) {
				listeCodesErreur.add(err);
			}
		}
		return categories;
	}
	
	private Boolean isModifiable(ArticleVendu articleVendu) {
		Boolean modifiable = Boolean.TRUE;
		if(articleVendu!=null) {
			LocalDate today = LocalDate.now(); 
			if( today.isBefore(articleVendu.getDate_debut_encheres())
				|| today.isAfter(articleVendu.getDate_fin_encheres())) 
			{
				modifiable = Boolean.FALSE;
			}			
		}
		return modifiable;
	}
	
}
