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
		this.validerUtilisateur(utilisateur, businessException);
		if(!businessException.hasErreurs()) {
			this.utilisateurDAO.insert(utilisateur);
		}
		else
		{
			throw businessException;
		}
	}
	
	public void modifierUtilisateur(Utilisateur utilisateur) throws BusinessException {
		BusinessException businessException = new BusinessException();
		this.validerUtilisateur(utilisateur, businessException);
		if(!businessException.hasErreurs()) {
			this.utilisateurDAO.update(utilisateur);
		}
		else
		{
			throw businessException;
		}
	}
	
	public void supprimerUtilisateur(int id) throws BusinessException {
		this.utilisateurDAO.delete(id);
	}
	
	public List<Utilisateur> getListeUtilisateurs(List<Utilisateur> utilisateurs) throws BusinessException {
		return this.utilisateurDAO.selectAll();
	}
	
	public Utilisateur getUtilisateur(int no_utilisateur) throws BusinessException {
		return utilisateurDAO.selectById(no_utilisateur);
		}
	
	
	
	private void validerUtilisateur (Utilisateur utilisateur, BusinessException businessException) throws BusinessException {
		this.validerNom(utilisateur.getNom(), businessException);
		this.validerPrenom(utilisateur.getPrenom(), businessException);
		this.validerEmail(utilisateur.getEmail(), businessException);
		this.validerPseudo(utilisateur.getPseudo(), businessException);
		this.validerTelephone(utilisateur.getPseudo(), businessException);
		this.validerRue(utilisateur.getPseudo(), businessException);
		this.validerCodePostal(utilisateur.getPseudo(), businessException);
		this.validerVille(utilisateur.getPseudo(), businessException);
		this.validerMotDePasse(utilisateur.getPseudo(), businessException);
	}
	
	private void validerNom (String nom, BusinessException businessException) {
		if(nom==null || nom.trim().length() <1 || nom.length()>30)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_NOM_ERREUR);
		}
	}
	
	private void validerPrenom (String prenom, BusinessException businessException) {
		if(prenom==null || prenom.trim().length() <1 || prenom.length()>30)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PRENOM_ERREUR);
		}
	}
	
	private void validerEmail (String email, BusinessException businessException) throws BusinessException {
		if(email==null || email.length()>20)
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
		if(pseudo==null || pseudo.length()>30 ||!pseudo.matches("[A-Za-z0-9]*"))
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
	
	private void validerTelephone (String telephone, BusinessException businessException) throws BusinessException {
		if(telephone.length() >15 || !telephone.matches("[0-9]*")) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_TELEPHONE_ERREUR);
		}
	}
	
	private void validerRue (String rue, BusinessException businessException) throws BusinessException {
		if(rue.length() >30 || rue == null || rue.trim().length() < 1) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_RUE_ERREUR);
		}
	}
	
	private void validerCodePostal (String codePostal, BusinessException businessException) throws BusinessException {
		if(codePostal.trim().length() >5 || codePostal == null || !codePostal.matches("[0-9]*")) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_CODE_POSTAL_ERREUR);
		}
	}
	
	private void validerVille (String ville, BusinessException businessException) throws BusinessException {
		if(ville.length() >30 || ville == null) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_VILLE_ERREUR);
		}
	}
	
	private void validerMotDePasse (String mdp, BusinessException businessException) throws BusinessException {
		if(mdp.length() >30 || mdp == null) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_MOT_DE_PASSE_ERREUR);
		}
	}
	
}
