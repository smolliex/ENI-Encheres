<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
	    <meta name="description" content="">
	    <meta name="author" content="">
	    
	    <link rel="icon" href="favicon.ico">
		<link rel="stylesheet" href="vendor/bootstrap/css/bootstrap_yeti.min.css">
				
		<style type="text/css">
			label { display: inline-block; width: 130px; }
			input[type='number']{text-align: right;}
			.card {	padding: 10px 10px 10px 10px;
					margin: 0px 0px 15px 0px
					}
			button {margin: 10px 0px 10px 0px}
		</style>
		
		<title>Vente</title>
		
	</head>
	
	<body>
		<main class="container-fluid">
			
			<!-- titre de la page -->
			<div class="row text-center">
				<div class="col">
					<c:if test="${empty article}">	
						<h1 class="my-5">Nouvelle vente</h1>
					</c:if>
					<c:if test="${!empty article}">	
						<h1 class="my-5">Ma vente</h1>
					</c:if>
				</div>
			</div>
		
			<!-- Messages d'erreur -->
			<c:if test="${!empty ListeLibellesErreurs}">
				<div class="alert alert-danger" role="alert">
					<c:forEach var="e" items="${ListeLibellesErreurs}">
						${e}
					</c:forEach>
				</div>
			</c:if>
			
			<!-- Formulaire -->
			<div>
				<form method="post" action="${pageContext.request.contextPath}/vente">
		
					<% 
					String disabled="";
					if(!(Boolean)request.getAttribute("isModifiable")) {
						disabled="disabled";
					}
					%>
					
					<div class="row">
						
						<!-- image -->
						<div class="col-xs-12 col-md-4">
							<h3>Photo</h3>
							<div class="card">
								<!-- TODO -->
								<div>
									<img class="card-img-left img-fluid" src="https://via.placeholder.com/250x150" alt="image">
								</div>
								
								<c:if test="${isModifiable}">	
									<button type="button" class="btn btn-secondary btn-xs">Upload</button>
								</c:if>
							</div>
						</div>
	
						<!-- inputs -->
						<div class="col-xs-12 col-md-8">
												
							<input type="hidden" id="no_article" name="no_article" value="${article.no_article}">
							<input type="hidden" id="no_utilisateur" name="no_utilisateur" value="${article.vendeur.no_utilisateur}">
				
							<div>
								<h3>Article</h3>
								<div class="card col-xs-12 col-md-6">
									<div>
										<label for="nom">Nom</label>
										<input type="text" id="nom" name="nom" value="${article.nom_article}" placeholder="Nom de l'article" required <%=disabled %>>
									</div>
								
									<div>
										<label for="description">Description</label>
										<textarea id="description" name="description" required  <%=disabled %>>${article.description}</textarea>
									</div>		
								
									<div>
										<label for="categorie">Catégorie</label>
										<select id="categorie" name="categorie"  <%=disabled %>>
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
										<label for="prixInitial">Mise à prix</label>
										<input type="number" id="prixInitial" name="prixInitial" value="${article.prix_initial}" min="0" max="10000" step="5" required <%=disabled %>>
										<label>Points</label>
									</div>
						
									<div>
										<label for="prixVente">Prix de vente</label>
										<input type="number" id="prixVente" name="prixVente" value="${article.prix_vente}" min="0" max="10000" step="5" disabled>
										<label>Points</label>
									</div>
												
									<div>
										<label for="dateDebut">Début de l'enchère</label>
										<input type="date" id="dateDebut" name="dateDebut" value="${article.date_debut_encheres}" min="${dateDuJour}" <%=disabled %>> 
									</div>
									
									<div>
										<label for="dateFin">Fin de l'enchère</label>
										<input type="date" id="dateFin" name="dateFin" value="${article.date_fin_encheres}" min="${dateDuJour}" <%=disabled %>> 
									</div>		
								</div>		
							</div>		
							
							<div>
								<h3>Retrait</h3>
								<div class="card col-xs-12 col-md-6">
									<div>
										<label for="rue">Rue</label>
										<input type="text" id="rue" name="rue" value="A FAIRE" placeholder="Rue" required <%=disabled %>>
									</div>
									<div>
										<label for="code_postal">Code postal</label>
										<input type="text" id="code_postal" name="rue" value="A FAIRE" placeholder="Code postal" required <%=disabled %>>
									</div>
									<div>
										<label for="ville">Ville</label>
										<input type="text" id="ville" name="ville" value="A FAIRE" placeholder="Ville" required <%=disabled %>>
									</div>
								</div>
							</div>
							
						</div>
					</div>
											
					<!-- bouton -->
					<div class="row text-center">
						<div class="col-xs-12 col-md-12">
							<c:if test="${isModifiable}">	
								<button type="submit" class="btn btn-primary btn-md">Enregistrer</button>
							</c:if>
							
							<a href="${pageContext.request.contextPath}/ListeEncheres"><button type="button" class="btn btn-primary btn-md">Retour à la liste</button></a>
							
							<c:if test ="${isSupprimable}">
								<a href="${pageContext.request.contextPath}/ListeEncheres"><button type="button" class="btn btn-primary btn-md">Annuler la vente</button></a>
							</c:if>
						</div>
					</div>
				</form>
			</div>
			
		</main>
		
		<!-- Pied de page -->
	    <footer class="py-5 bg-secondary">
	      <div class="container">
	        <p class="m-0 text-center text-black">Copyright &copy; ENI - 2020</p>
	      </div>
	    </footer>
		
	    <!-- Placed at the end of the document so the pages load faster -->
	        <!-- Placed at the end of the document so the pages load faster -->
	    <script src="vendor/jquery/jquery.min.js"></script>
	    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
    
	</body>
</html>