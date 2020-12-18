package fr.eni.javaee.encheres.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;

/**
 * Servlet implementation class ServletConnexion
 */
@WebServlet("/Connexion")
public class ServletConnexion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("login")){
					String login = cookie.getValue();
					session.setAttribute("login", login);
				}
				if (cookie.getName().equals("password")) {
					String mot_de_passe = cookie.getValue();
					session.setAttribute("password", mot_de_passe);
				}
			}
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp");
			rd.forward(request, response);
			
		}else {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//gestion de la connexion avec un cookie si "seRappeler" est sélectionné
		HttpSession session = request.getSession();
		Utilisateur utilisateur = new Utilisateur();
		String pseudo = request.getParameter("pseudo");
		String mot_de_passe = request.getParameter("mot_de_passe");
		UtilisateurManager manager = new UtilisateurManager();
		utilisateur.setPseudo("pseudo");
		try {
			utilisateur = manager.getUtilisateurLogin(pseudo);
		} catch (BusinessException e) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatServlets.UTILISATEUR_INCONNU);
			e.printStackTrace();
		}
		if(!mot_de_passe.equals(utilisateur.getMot_de_passe())){
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatServlets.MOT_DE_PASSE_INCORRECT);
		}else {
			if(request.getParameter("souvenir") == "ok") {
				Cookie login = new Cookie("login", pseudo);
				login.setMaxAge(10000);
				response.addCookie(login);
				Cookie password = new Cookie("password", mot_de_passe);
				login.setMaxAge(10000);
				response.addCookie(password);
			}
			session.setAttribute("utilisateur", utilisateur);
			RequestDispatcher rd = request.getRequestDispatcher(request.getContextPath()+"/ListeEncheres");
			rd.forward(request, response);
		}
	}

}
