package fr.eni.javaee.encheres.bo;

import java.io.Serializable;

public class Categorie implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String libelle;
	
	public Categorie() {
	}

	public Categorie(String libelle) {
		super();
		this.libelle = libelle;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Categorie [libelle=");
		builder.append(libelle);
		builder.append("]");
		return builder.toString();
	}
	
	
}
