package fr.eni.javaee.encheres.bll;

import java.util.ArrayList;
import java.util.List;

import fr.eni.javaee.encheres.messages.BusinessException;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.dal.UtilisateurDAO;

public class UtilisateurManager {
	
	private UtilisateurDAO utilisateurDAO;
	private static UtilisateurManager instance;
	
	private UtilisateurManager() {
		this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
	}
	
	public static UtilisateurManager getInstance() {
		if(instance == null){
			instance = new UtilisateurManager();
		}
		return instance;
	}
	
	public void ajouterUtilisateur(Utilisateur utilisateur) throws BusinessException {
		BusinessException businessException = new BusinessException();
		this.validerNomUtilisateur(utilisateur.getNom(), businessException);
		this.validerPrenomUtilisateur(utilisateur.getPrenom(), businessException);
		this.validerEmail(utilisateur.getEmail(), businessException);
		this.validerPseudo(utilisateur.getPseudo(), businessException);
		if(!businessException.hasErreurs()) {
			this.utilisateurDAO.insert(utilisateur);
		}
		else
		{
			throw businessException;
		}
	}
		
	public List<Utilisateur> getListeUtilisateurs(List<Utilisateur> utilisateurs) throws BusinessException {
		return this.utilisateurDAO.selectAll();
	}
	
	
	
	private void validerNomUtilisateur (String nomUtilisateur, BusinessException businessException) {
		if(nomUtilisateur==null || nomUtilisateur.trim().length()>50)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_NOM_ERREUR);
		}
	}
	
	private void validerPrenomUtilisateur (String prenomUtilisateur, BusinessException businessException) {
		if(prenomUtilisateur==null || prenomUtilisateur.trim().length()>50)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PRENOM_ERREUR);
		}
	}
	
	private void validerEmail (String email, BusinessException businessException) throws BusinessException {
		if(email==null || email.trim().length()>100)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_EMAIL_ERREUR);
		}
		List<Utilisateur>utilisateurs = new ArrayList<Utilisateur>();
		utilisateurs = utilisateurDAO.selectAll();
		for (Utilisateur utilisateur : utilisateurs) {
			if(email.trim().equals(utilisateur.getEmail())) {
				businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_EMAIL_DOUBLON);
				break;
			}
		}
	}
	
	private void validerPseudo (String pseudo, BusinessException businessException) throws BusinessException {
		if(pseudo==null || pseudo.trim().length()>30)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PSEUDO_ERREUR);
		}
		List<Utilisateur>utilisateurs = new ArrayList<Utilisateur>();
		utilisateurs = utilisateurDAO.selectAll();
		for (Utilisateur utilisateur : utilisateurs) {
			if(pseudo.trim().equals(utilisateur.getPseudo())) {
				businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PSEUDO_DOUBLON);
				break;
			}
		}
	}
	
}
