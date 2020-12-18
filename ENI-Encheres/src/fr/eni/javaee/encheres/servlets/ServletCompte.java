package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;

/**
 * Servlet implementation class ServletCompte
 */
@WebServlet(urlPatterns= {
							"/creer",
							"/modifier",
							"/supprimer"
						 })
public class ServletCompte extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getServletPath().equals("/creer"))
		{
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/compte.jsp");
			rd.forward(request, response);
		}else if (request.getServletPath().equals("/modifier")){
			HttpSession session = request.getSession();
			Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
			UtilisateurManager manager = new UtilisateurManager();
			int id = utilisateur.getNo_utilisateur();
			try {
				utilisateur = manager.getUtilisateur(id);
			} catch (BusinessException e) {
				e.printStackTrace();
				BusinessException businessException = new BusinessException(); 
				businessException.ajouterErreur(CodesResultatServlets.FORMAT_ID_UTILISATEUR_ERREUR);
			}
			session.setAttribute("utilisateur", utilisateur);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/compte.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		if(request.getServletPath().equals("/supprimer")){
			UtilisateurManager manager = new UtilisateurManager();
			Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
			try {
				manager.supprimerUtilisateur(utilisateur.getNo_utilisateur());
				response.sendRedirect(request.getContextPath()+"/ListeEncheres");
			} catch (BusinessException e) {
				BusinessException businessException = new BusinessException(); 
				businessException.ajouterErreur(CodesResultatServlets.SUPPRESSION_UTILISATEUR_ERREUR);
				e.printStackTrace();
			}
		}else {
			Utilisateur utilisateur = new Utilisateur();
			utilisateur.setPseudo(request.getParameter("pseudo"));
			utilisateur.setNom(request.getParameter("nom"));
			utilisateur.setPrenom(request.getParameter("prenom"));
			utilisateur.setEmail(request.getParameter("email"));
			utilisateur.setTelephone(request.getParameter("telephone"));
			utilisateur.setRue(request.getParameter("rue"));
			utilisateur.setCode_postal(request.getParameter("code_postal"));
			utilisateur.setVille(request.getParameter("ville"));
			//vérification du mot de passe
			String mdp1 = request.getParameter("mot_de_passe");
			String mdp2 = request.getParameter("confirmation");
			if(!mdp1.equals(mdp2)) {
				BusinessException businessException = new BusinessException(); 
				businessException.ajouterErreur(CodesResultatServlets.MOTS_DE_PASSE_DIFFERENTS);
				request.setAttribute("Exception", businessException);
				RequestDispatcher rd = request.getRequestDispatcher(request.getServletPath());
				rd.forward(request, response);
			}else {
				utilisateur.setMot_de_passe(request.getParameter("mot_de_passe"));
				UtilisateurManager manager = new UtilisateurManager();
				if(request.getServletPath().equals("/creer"))
				{
					try {
						manager.ajouterUtilisateur(utilisateur);
					} catch (BusinessException e) {
						BusinessException businessException = new BusinessException(); 
						businessException.ajouterErreur(CodesResultatServlets.CREATION_UTILISATEUR_ERREUR);
						e.printStackTrace();
					}
				}else if (request.getServletPath().equals("/modifier")){
					try {
						manager.modifierUtilisateur(utilisateur);
					} catch (BusinessException e) {
						BusinessException businessException = new BusinessException(); 
						businessException.ajouterErreur(CodesResultatServlets.MODIFICATION_UTILISATEUR_ERREUR);
						e.printStackTrace();
					}
				}
				session.setAttribute("utilisateur", utilisateur);
				response.sendRedirect(request.getContextPath()+"/ListeEncheres");
			}
		}
		
		
		
	}

}
