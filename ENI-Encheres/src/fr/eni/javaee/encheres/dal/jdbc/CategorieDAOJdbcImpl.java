package fr.eni.javaee.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.dal.CategorieDAO;
import fr.eni.javaee.encheres.dal.CodesResultatDAL;
import fr.eni.javaee.encheres.messages.BusinessException;

public class CategorieDAOJdbcImpl implements CategorieDAO{
	
	private static final String INSERT = "INSERT INTO CATEGORIES libelle VALUES libelle=?";
	private static final String UPDATE ="UPDATE CATEGORIES SET libelle=? WHERE no_categorie=?";
	private static final String DELETE ="DELETE FROM CATEGORIES WHERE no_categorie=?";
	private static final String SELECT_BY_ID ="SELECT no_categorie, libelle FROM CATEGORIES WHERE no_categorie=?";
	private static final String SELECT_ALL ="SELECT no_categorie, libelle FROM CATEGORIES";

	@Override
	public Categorie selectById(int id) throws BusinessException {
		Categorie categorie = new Categorie();
		try(Connection cnx = ConnectionProvider.getConnection())
		{
			PreparedStatement pstmt = cnx.prepareStatement(SELECT_BY_ID);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			categorie.setNo_categorie(rs.getInt("no_categorie"));
			categorie.setLibelle(rs.getString("libelle"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECT_CATEGORIE_ECHEC);
			throw businessException;
		}
		return categorie;
	}

	@Override
	public List<Categorie> selectAll() throws BusinessException {
		List<Categorie>categories = new ArrayList<Categorie>();
		Categorie categorie = new Categorie();
		try(Connection cnx = ConnectionProvider.getConnection())
		{
			PreparedStatement pstmt = cnx.prepareStatement(SELECT_ALL);
			ResultSet rs = pstmt.executeQuery();
			categorie.setNo_categorie(rs.getInt("no_categorie"));
			categorie.setLibelle(rs.getString("libelle"));
			categories.add(categorie);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECT_CATEGORIE_ECHEC);
			throw businessException;
		}
		return categories;
	}

	@Override
	public void update(Categorie categorie) throws BusinessException {
		if(categorie==null)
		{
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_CATEGORIE_NULL);
			throw businessException;
		}
		try(Connection cnx = ConnectionProvider.getConnection())
		{
			PreparedStatement pstmt;
			pstmt = cnx.prepareStatement(UPDATE);
			pstmt.setString(1, categorie.getLibelle());
			pstmt.setInt(2, categorie.getNo_categorie());
			pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_CATEGORIE_ECHEC);
			throw businessException;
		}
	}

	@Override
	public void insert(Categorie categorie) throws BusinessException {
		if(categorie==null)
		{
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_CATEGORIE_NULL);
			throw businessException;
		}
		try(Connection cnx = ConnectionProvider.getConnection())
		{
			PreparedStatement pstmt;
			ResultSet rs;
			pstmt = cnx.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, categorie.getLibelle());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				categorie.setNo_categorie(rs.getInt(1));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_CATEGORIE_ECHEC);
			throw businessException;
		}
		
	}

	@Override
	public void delete(int id) throws BusinessException {
		try(Connection cnx = ConnectionProvider.getConnection())
		{
			PreparedStatement pstmt;
			pstmt = cnx.prepareStatement(DELETE);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_CATEGORIE_ECHEC);
			throw businessException;
		}
	}

}
