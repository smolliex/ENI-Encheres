package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
import fr.eni.javaee.encheres.messages.LecteurMessage;


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
		
		List<Integer> listeCodesErreur = new ArrayList<>();
		
		try {
			
			int no_article=0;
			no_article = lireParametreInt(request, "no_article", listeCodesErreur);
				
			if(no_article>0)
			{
				// charge l'article 
				ArticleVenduManager articleVenduManager = ArticleVenduManager.getInstance();
				ArticleVendu articleVendu = null;
				articleVendu = articleVenduManager.getArticleVendu(no_article);
				if(articleVendu==null) {
					listeCodesErreur.add(CodesResultatServlets.VENTE_INCONNUE);
				}
				
				// transmet à la page
				request.setAttribute("article", articleVendu);
				request.setAttribute("isModifiable", isModifiable(articleVendu));
				request.setAttribute("isSupprimable", isSupprimable(articleVendu));
			}else
			{
				request.setAttribute("isModifiable", Boolean.TRUE);
				request.setAttribute("isSupprimable", Boolean.FALSE);
			}
			
			request.setAttribute("dateDuJour", dateDuJour());
			request.setAttribute("categories", listeCategories(listeCodesErreur));
			
			
		} catch (BusinessException e) {
			for (int err : e.getListeCodesErreur()) {
				listeCodesErreur.add(err);
			}
		}
		
		// transmet la liste des erreurs
		request.setAttribute("ListeLibellesErreurs",ListeLibellesErreurs(listeCodesErreur));

		// affiche la page
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/vente.jsp");			
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		List<Integer> listeCodesErreur = new ArrayList<>();
		ArticleVendu articleVendu = null;
		
		try {
			
			// Récupère les valeurs du formulaire
			articleVendu = recupererSaisieArticle(request, listeCodesErreur);
			
			// controle et enregistre
			ArticleVenduManager articleVenduManager = ArticleVenduManager.getInstance();
			
			if (articleVendu.getNo_article()>0) {
				articleVenduManager.updateArticleVendu(articleVendu);
			}else
			{
				articleVenduManager.createArticleVendu(articleVendu);
			}
			
		} catch (BusinessException e) {
			for (int err : e.getListeCodesErreur()) {
				listeCodesErreur.add(err);
			}
		}
		
		if(listeCodesErreur.size()>0)
		{
			// recharge la page en affichant les erreurs
			request.setAttribute("dateMin", dateDuJour());
			request.setAttribute("categories", listeCategories(listeCodesErreur));
			request.setAttribute("article", articleVendu);
			request.setAttribute("isModifiable", Boolean.TRUE);
			request.setAttribute("isSupprimable", isSupprimable(articleVendu));
			request.setAttribute("ListeLibellesErreurs",ListeLibellesErreurs(listeCodesErreur));
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/vente.jsp");			
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
	
	private String lireParametreString(HttpServletRequest request, String parametre, List<Integer> listeCodesErreur) {
		// Renvoie la valeur du parametre de type STRING
		String valeur = request.getParameter(parametre);
		if(valeur==null)
		{
			listeCodesErreur.add(CodesResultatServlets.LECTURE_PARAMETRE_VENTE);
		}
		return valeur;
	}
	
	private LocalDate lireParametreLocalDate(HttpServletRequest request, String parametre, List<Integer> listeCodesErreur) {
		// Renvoie la valeur du parametre de type LocalDate
		LocalDate valeur=null;
		try
		{
			String string = request.getParameter(parametre);
			if(string!=null && !string.isEmpty())
			{
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				valeur = LocalDate.parse(string, dtf);
			}
		}
		catch(DateTimeParseException e)
		{
			e.printStackTrace();
			listeCodesErreur.add(CodesResultatServlets.LECTURE_PARAMETRE_VENTE);
		}
		return valeur;
	}
	
	private ArticleVendu recupererSaisieArticle(HttpServletRequest request, List<Integer> listeCodesErreur) {
		
		//recupere les parametres du formulaire
		int no_article = lireParametreInt(request, "no_article", listeCodesErreur);

		Utilisateur vendeur=null;
		
		String no_utilisateur = lireParametreString(request, "no_utilisateur", listeCodesErreur);
		
/// en attendant d'avoir la session utilisateur //////////////////////////
vendeur=new Utilisateur();
vendeur.setNo_utilisateur(3);
//////////////////////////////////////////////////////////////

		if(no_utilisateur==null || no_utilisateur.isEmpty()) {
			// Le vendeur est l'utilisateur connecté
			HttpSession session=request.getSession();
			if(session.getAttribute("utilisateur")!=null) {
				vendeur = (Utilisateur)session.getAttribute("utilisateur");
			}

			
		}else
		{
			vendeur = new Utilisateur();
			vendeur.setNo_utilisateur(lireParametreInt(request, "no_utilisateur", listeCodesErreur));
		}
		
		String nom = lireParametreString(request, "nom", listeCodesErreur);
		String description = lireParametreString(request, "description", listeCodesErreur);
		int prixInitial = lireParametreInt(request, "prixInitial", listeCodesErreur);
		LocalDate dateDebut = lireParametreLocalDate(request, "dateDebut", listeCodesErreur);
		LocalDate dateFin = lireParametreLocalDate(request, "dateFin", listeCodesErreur);

		int no_categorie = lireParametreInt(request, "categorie", listeCodesErreur);
		Categorie categorie = new Categorie();
		categorie.setNo_categorie(no_categorie);
		
		//construit l'article
		ArticleVendu articleVendu = new ArticleVendu();	
		
		articleVendu.setNo_article(no_article);
		articleVendu.setVendeur(vendeur);	
		articleVendu.setNom_article(nom);
		articleVendu.setDescription(description);
		articleVendu.setCategorie(categorie);
		articleVendu.setPrix_initial(prixInitial);
		articleVendu.setDate_debut_encheres(dateDebut);
		articleVendu.setDate_fin_encheres(dateFin);
		
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
	
	private String dateDuJour() {
		Date date = new Date();  
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		return dateFormat.format(date);
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
	
	private Boolean isModifiable (ArticleVendu articleVendu) {
		Boolean modifiable = Boolean.FALSE;
		if(articleVendu!=null) {
			LocalDate today = LocalDate.now(); 
			if(	   (today.isEqual(articleVendu.getDate_debut_encheres()) || today.isAfter(articleVendu.getDate_debut_encheres()))
				&& (today.isEqual(articleVendu.getDate_fin_encheres()) || today.isBefore(articleVendu.getDate_fin_encheres()))) 
			{
				modifiable = Boolean.TRUE;
			}			
		}
		return modifiable;
	}
	
	private Boolean isSupprimable (ArticleVendu articleVendu) {
		Boolean supression = Boolean.FALSE;
		if(articleVendu!=null) {
			if( isModifiable(articleVendu) && articleVendu.getPrix_vente()==0 ) 
			{
				supression = Boolean.TRUE;
			}
		}
		return supression;
	}
	
}
