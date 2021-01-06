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

		<link rel="stylesheet" href="vendor/css/vente.css">
		<link rel="stylesheet" href="vendor/bootstrap/css/bootstrap_yeti.min.css">
		
		<title>Enchère</title>
		
	</head>
	
	<body>
	
		<!-- Entete de page -->
		<header>
			<!-- Navigation -->
			<%@include file="navigation.jsp" %>
		</header>
		
		<!--  Corps de la page  -->
		
		<main class="container-fluid">
			
			<div class="row text-center">
				<div class="col">
				
					<!-- titre de la page -->
					<h1 class="my-5">Enchère</h1>
		
					<!-- Messages d'erreur -->
					<c:if test="${!empty ListeLibellesErreurs}">
						<div class="alert alert-danger" role="alert">
							<c:forEach var="e" items="${ListeLibellesErreurs}">
								${e}
							</c:forEach>
						</div>
					</c:if>
					
					<!-- Formulaire -->
					<div class="text-left">
						<c:if test="${!empty article}">
							<form name="formSaisie" method="post" action="${pageContext.request.contextPath}/enchere">
								
								<div class="row">
									
									<!-- image -->
									<div class="col-md-4">
										<h3>Photo</h3>
										<div class="card">
											<!-- TODO -->
											<div>
												<img class="card-img-left img-fluid" src="https://via.placeholder.com/250x150" alt="image">
											</div>
											
										</div>
									</div>
				
									<!-- inputs -->
									<div class="col-md-8">
															
										<input type="hidden" id="no_article" name="no_article" value="${article.no_article}">
										<input type="hidden" id="isModifiable" name="isModifiable" value="${isModifiable}">
	
										<div>
											<h3>${article.nom_article}</h3>
											<div class="card">
											
												<div>
													<label>Description :</label>
													<span>${article.description}</span>
												</div>		
																					
												<div>
													<label>Meilleure offre :</label>
													<span class="prix">[prix] points</span>
													<span>par [pseudo]</span>
												</div>
																					
												<div>
													<label>Mise à prix :</label>
													<span>${article.prix_initial} points</span>
												</div>		
																				
												<div>
													<label>Fin de l'enchère :</label>
													<span>${article.date_fin_encheres}</span>
												</div>		
												
												<div>
													<label class="aligntop">retrait :</label>
													<label>
														${retrait.rue}<br>
														${retrait.code_postal}<br>
														${retrait.ville}
													</label>
												</div>		
	
												<div>
													<label class="aligntop">Vendeur :</label>
													<label>
														${article.vendeur.pseudo}<br>
														${article.vendeur.telephone}
													</label>
												</div>		
												
												<div>
													<label for="prix_vente">Ma proposition :</label>
													<input type="number" id="prix_vente" name="prix_vente" value="${article.prix_vente}" min="${article.prix_vente}" max="10000" step="5" disabled>
													<label>Points</label>
												</div>
													
											</div>
										</div>
																																		
									</div>
										
								</div>
								
								<!-- bouton -->
								<div class="row text-center">
									<div class="col">
										<c:if test="${isModifiable}">	
											<button type="submit" class="btn btn-primary btn-md">Enchérir</button>
										</c:if>
										
										<c:if test ="${isModifiable}">
											<button type="button" class="btn btn-primary btn-md">Retrait effectué</button>
										</c:if>
									</div>
								</div>							
							</form>
						</c:if>
						<div class="row text-center">
							<div class="col">
								<a href="${pageContext.request.contextPath}/ListeEncheres"><button type="button" class="btn btn-primary btn-md">Retour à la liste</button></a>
							</div>
						</div>
					</div>		
				</div>
			</div>			
		</main>
		
		<!-- Pied de page -->
	    <footer">
	    	<p>
				<%@include file="pied_de_page.jsp" %>
			</p>
	    </footer>

	    <script src="vendor/jquery/jquery.min.js"></script>
	    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
		
	</body>
	
</html>