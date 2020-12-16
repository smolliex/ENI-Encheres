package fr.eni.javaee.encheres.dal;

import java.util.List;

import fr.eni.javaee.encheres.messages.BusinessException;

public interface DAO<T> {
	
	//Sélectionner un business object
	public T getById(int id) throws BusinessException;
	
	//Sélectionner tous les business objects 
	public List<T> getAll() throws BusinessException;
	
	//Modifier les attributs d'un business object
	public void update(T obj) throws BusinessException;
	
	//Insérer un nouveau business object
	public int insert(T obj) throws BusinessException;
	
	//Supprimer un business object
	public void delete(int id) throws BusinessException;
	
}
