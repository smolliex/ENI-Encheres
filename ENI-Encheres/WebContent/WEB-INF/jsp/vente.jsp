<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style type="text/css">
	label { display: inline-block; width: 130px; }
	div {padding: 5px 5px 5px 5px}
</style>
<title>Vente</title>
</head>
<body>

<main>

	<h1>Article vendu</h1>
	
	<div>
		<form method="post" action="${pageContext.request.contextPath}/vente">

			<fieldset>
				<!-- TODO -->
				<legend>Photo</legend>
				<div>Image ici</div>
				<button>Upload</button>
			</fieldset>
			
			<div>
				<label for="nom">Article : </label>
				<input type="text" id="nom" name="nom" value="${article.nom_article}" placeholder="Nom de l'article"required>
			</div>
		
			<div>
				<label for="description">Description : </label>
				<textarea id="description" name="description" required>${article.description}</textarea>
			</div>		
		
			<div>
				<label for="categorie">Catégorie : </label>
				<select id="categorie" name="categorie">
					<c:if test="${!empty categories}">
						<c:forEach var="c" items="${categories}">
							<c:if test="${c.no_categorie == article.categorie.no_categorie}">
					  			<option value="${c.no_categorie}" selected>${c.libelle}</option>
					  		</c:if>
					  		<c:if test="${c.no_categorie != article.categorie.no_categorie}">
					  			<option value="${c.no_categorie}">${c.libelle}</option>
					  		</c:if>
						</c:forEach>
					</c:if>
				</select>
			</div>

			<div>
				<label for="prixInitial">Mise à prix : </label>
				<input type="number" id="prixInitial" name="prixInitial" value="${article.prix_initial}" min="0" max="10000" step="5" required>
			</div>
			
			<div>
				<label for="dateDebut">Début de l'enchère : </label>
				<input type="date" id="dateDebut" name="dateDebut" value="${article.date_debut_encheres}" min="${dateMin}" > 
			</div>
			
			<div>
				<label for="dateDebut">Fin de l'enchère : </label>
				<input type="date" id="dateDebut" name="dateDebut" value="${article.date_fin_encheres}" min="${dateMin}" > 
			</div>		
			
			<div>
				<fieldset>
					<legend>Retrait:</legend>
					<!-- TODO -->
					rue : <br>
					Code postal : <br>
					Ville : <br>
				</fieldset>
			</div>
			
			<div>
				<button type="submit">Enregistrer</button>
				<a href="${pageContext.request.contextPath}/ListeEncheres"><button type="button">Annuler</button></a>
				<!-- TODO -->
				<a href="${pageContext.request.contextPath}/ListeEncheres"><button type="button">Annuler la vente</button></a>
			</div>
			
		</form>
	</div>

</main>

</body>
</html>