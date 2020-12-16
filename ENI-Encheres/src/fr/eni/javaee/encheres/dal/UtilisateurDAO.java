package fr.eni.javaee.encheres.dal;

import java.util.List;

import fr.eni.javaee.encheres.messages.BusinessException;
import fr.eni.javaee.encheres.bo.Utilisateur;

public interface UtilisateurDAO {
	
	public void insert(Utilisateur utilisateur) throws BusinessException;
	public void delete(int id) throws BusinessException;
	public void update(Utilisateur utilisateur) throws BusinessException;
	public List<Utilisateur> selectAll() throws BusinessException;
	public Utilisateur selectById(int id) throws BusinessException;

}
