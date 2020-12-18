package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;

/**
 * Servlet implementation class ServletArticle
 */
@WebServlet("/vente")
public class ServletVente extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Charge l'article 
		String strId = request.getParameter("no_article");
		int no_article=0;
		
		if(strId!=null && !strId.isEmpty()) {

			no_article = Integer.valueOf(strId);
			
			ArticleVenduManager articleVenduManager = ArticleVenduManager.getInstance();
			ArticleVendu articleVendu = null;
			
			try{
				articleVendu = articleVenduManager.getArticleVendu(no_article);
				
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			request.setAttribute("article", articleVendu);
		}
		
		// charge les categories
		CategorieManager categorieManager = CategorieManager.getInstance();
		List<Categorie> categories = null;
		try {
			categories = categorieManager.getListeCategories();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("categories", categories);
		
		// date minimum
		Date date = new Date();  
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		String dateMin = dateFormat.format(date);  

		request.setAttribute("dateMin", dateMin);
		
		// affiche la page
		RequestDispatcher rd=null;
		if (no_article==0) {
			// nouveau
			rd=request.getRequestDispatcher("/WEB-INF/jsp/vente.jsp");			
		}else
		{
			// existant
			rd=request.getRequestDispatcher("/WEB-INF/jsp/vente.jsp?no_article=" + no_article);			
		}
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		////// a enlever ////
		RequestDispatcher rd=null;
		rd=request.getRequestDispatcher("/WEB-INF/jsp/listeEncheres.jsp");			
		rd.forward(request, response);
		//////////
		
		// encodage
		request.setCharacterEncoding("UTF-8");
		
		//recupere l'utilisateur connecté = vendeur
		Utilisateur vendeur=null;
		
		HttpSession session=request.getSession();
		if(session.getAttribute("utilisateur")!=null) {
			vendeur = (Utilisateur)session.getAttribute("utilisateur");
		}
		
		//recupere valeurs et les caste si nécessaire
		int no_article =0;
		String strId = request.getParameter("no_article");
		if(strId!=null && !strId.isEmpty()) {
			no_article = Integer.valueOf(strId);
		}
		
		String nom_article = request.getParameter("nom");
		String description = request.getParameter("description");
		
		String no_categorie = request.getParameter("categorie");
		int noCategorie = Integer.valueOf(no_categorie);
		
		String prix_initial = request.getParameter("prixInitial");
		int prixInitial = Integer.valueOf(prix_initial);
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		String date_debut_encheres = request.getParameter("dateDebut");
		LocalDate dateDebutEncheres = LocalDate.parse(date_debut_encheres, dtf);
		
		String date_fin_encheres = request.getParameter("dateFin");
		LocalDate dateFinEncheres = LocalDate.parse(date_fin_encheres, dtf);
		
		//recupere la categorie
		Categorie categorie = new Categorie();
		categorie.setNo_categorie(noCategorie);
		
		//construit l'article
		ArticleVenduManager articleVenduManager;
		articleVenduManager = ArticleVenduManager.getInstance();
		
		ArticleVendu articleVendu = new ArticleVendu();	

		if(no_article==0) {
			articleVendu.setVendeur(vendeur);	
		}else
		{
			articleVendu.setNo_article(no_article);
		}
		articleVendu.setNom_article(nom_article);
		articleVendu.setDescription(description);
		articleVendu.setCategorie(categorie);
		articleVendu.setPrix_initial(prixInitial);
		articleVendu.setDate_debut_encheres(dateDebutEncheres);
		articleVendu.setDate_fin_encheres(dateFinEncheres);

		// enregistre
		try {
			if (no_article>0) {
				//modifie
				articleVenduManager.updateArticleVendu(articleVendu);
			}else
			{
				//crée
				articleVenduManager.createArticleVendu(articleVendu);
			}

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
