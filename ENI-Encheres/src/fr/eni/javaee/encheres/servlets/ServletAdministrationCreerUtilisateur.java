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
 * Servlet implementation class ServletAdministrationCreerUtilisateur
 */
@WebServlet("/AdministrationCreerUtilisateur")
public class ServletAdministrationCreerUtilisateur extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/administrationCreerUtilisateur.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Encodage des caractères.
		request.setCharacterEncoding("UTF-8");
		
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
		BusinessException businessException = new BusinessException();
		
		// Ajout de l'utilisateur
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setPseudo(request.getParameter("pseudo"));
		utilisateur.setNom(request.getParameter("nom"));
		utilisateur.setPrenom(request.getParameter("prenom"));
		utilisateur.setEmail(request.getParameter("email"));
		utilisateur.setRue(request.getParameter("rue"));
		utilisateur.setTelephone("0699887766");
		utilisateur.setCode_postal(request.getParameter("code_postal"));
		utilisateur.setVille(request.getParameter("ville"));
		utilisateur.setMot_de_passe("password");
		utilisateur.setAdministrateur(false);
		utilisateur.setCredit(0);
		
		creerUtilisateur(utilisateur, businessException);
		
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

	public void creerUtilisateur(Utilisateur utilisateur,BusinessException businessException){	
			UtilisateurManager um = UtilisateurManager.getInstance();
			try {
				um.ajouterUtilisateur(utilisateur);
			} catch (BusinessException e) {
				e.printStackTrace();
				businessException.ajouterErreur(CodesResultatServlets.CREER_UTILISATEUR_ERREUR);		
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
