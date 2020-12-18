package fr.eni.javaee.encheres.servlets;

import java.io.IOException;

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
 * Servlet implementation class ServletProfil
 */
@WebServlet(urlPatterns= {
		"/profil",
		"/mon_profil"
	 })
public class ServletProfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int no_utilisateur = Integer.parseInt(request.getParameter("user"));
		UtilisateurManager manager = new UtilisateurManager();
		Utilisateur user = new Utilisateur();
		try {
			user = manager.getUtilisateur(no_utilisateur);
		} catch (BusinessException e) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatServlets.SELECTION_UTILISATEUR_ERREUR);
			e.printStackTrace();
		}
		request.setAttribute("user", user);
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/profil.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
