<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
	    <meta name="description" content="">
	    <meta name="author" content="">
	    <link rel="icon" href="favicon.ico">
		<link rel="stylesheet" href="vendor/css/listeEncheres.css">
		<link rel="stylesheet" href="vendor/bootstrap/css/bootstrap_yeti.min.css">
		<title>Listes des enchères</title>
	</head>
	<body>
	
		<!-- Entete de page -->
	<header>
		
		<!-- Navigation -->
		<nav class="navbar navbar-expand-lg navbar-dark bg-primary static-top">
		  <div class="container-fluid">
			<a class="navbar-brand" href="#" id="link-title">ENI Enchères</a>
			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
			  <span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarResponsive">
			  <ul class="navbar-nav ml-auto">
				<li class="nav-item active">
				  <a class="nav-link" href="#" id="link-connect">Se connecter - S'inscrire
					<span class="sr-only">(current)</span>
				  </a>
				</li>
			  </ul>
			</div>
		  </div>
		</nav>
	
	</header>
	
	<main class="container-fluid">
		<div class="row text-center">
			<div class="col">
				<h1 class="my-5">Liste des enchères</h1> 
			</div>
		</div>

			<form class="row mx-md-5">
				<div class="col-12 col-md-6 px-md-5">
					<h2 class="my-3">Filtres :</h2>
					<div class="input-group flex-nowrap">
					  <span class="input-group-text" id="addon-wrapping"><jsp:include page="../icons/loupe.html" /></span>
					  <input type="text" class="form-control" name="article" id="article" placeholder="Le nom de l'article contient" aria-label="article" aria-describedby="addon-wrapping">
					</div>
					<div class="row align-items-center my-3">
					  <div class="col-auto">
					    <label for="categorie" class="col-form-label">Catégorie :</label>
					  </div>
					  <div class="col-auto">
					    <select class="form-control">
					    <c:if test="${!empty listeCategories}">
						    <option value="${listeCategories[0].libelle}" selected>${listeCategories[0].libelle}</option>
						    <c:if test="${fn:length(listeCategories) > 1}">
							    <c:forEach var="categorie" begin="1" items="${listeCategories}">
							    	<option value="${categorie.libelle}">${categorie.libelle}</option>
							    </c:forEach>			    
						    </c:if>				    
					    </c:if>
					    <c:if test="${empty listeCategories}">
					    	<option value="null">Pas de categories disponible</option>
					    </c:if>
						</select>
					  </div>
					</div>	
					<!--  
					<div class="row">
					 	<div class="col form-check">
							<input class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1">
							<label class="form-check-label" for="flexRadioDefault1">Achats</label>
							<div class="row mx-3 my-3">
								<div class="form-check">
								  <input class="form-check-input" type="checkbox" id="defaultCheckbox1" value="option1">
								  <label class="form-check-label" for="defaultCheckbox1">enchères ouvertes</label>
								</div>
								<div class="form-check">
								  <input class="form-check-input" type="checkbox" id="defaultCheckbox2" value="option2">
								  <label class="form-check-label" for="defaultCheckbox2">mes enchères en cours</label>
								</div>
								<div class="form-check">
								  <input class="form-check-input" type="checkbox" id="defaultCheckbox3" value="option3">
								  <label class="form-check-label" for="defaultCheckbox3">mes enchères remportées</label>
								</div>												 						
							</div>
					 	</div>
					 	<div class="col">
							<input class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2">
							<label class="form-check-label" for="flexRadioDefault2">Mes ventes</label>	
								<div class="row mx-3 my-3">
								<div class="form-check">
								  <input class="form-check-input" type="checkbox" id=defaultCheckbox1" value="option1">
								  <label class="form-check-label" for="defaultCheckbox1">mes ventes en cours</label>
								</div>
								<div class="form-check">
								  <input class="form-check-input" type="checkbox" id="defaultCheckbox2" value="option2">
								  <label class="form-check-label" for="defaultCheckbox2">ventes non débutées</label>
								</div>
								<div class="form-check">
								  <input class="form-check-input" type="checkbox" id="defaultCheckbox3" value="option3">
								  <label class="form-check-label" for="defaultCheckbox3">ventes terminées</label>
								</div>	
							</div>									
					 	</div>
					</div>	-->			
				</div>
				<div class="col-12 col-md-6 text-center m-auto">
					<button type="submit" class="btn btn-primary btn-lg" id="btn-rechercher">Rechercher</button>
				</div>	
			</form>
			
			<div class="row my-5 mx-md-5">
				<c:if test="${!empty listeArticlesVendus}">
					<c:forEach var="articleVendu" items="${listeArticlesVendus}">
						<div class="col-12 col-md-6 my-2">
							<div class="card">
								<!--Card content-->
								<div class="card-body row">
									<div class="col-md-5">
										<img class="card-img-left img-fluid" src="https://via.placeholder.com/250x150" alt="image">
										</div>
									<div class="col-md-7">
										<!--Title-->
										<h3 class="card-title mt-2 mt-md-0"><a href="">${articleVendu.nom_article}</a></h3>
										<!--Text-->
										<p class="card-text">Prix: ${articleVendu.prix_initial}</p>
										<p class="card-text">Fin de l'enchère: ${articleVendu.date_fin_encheres}</p>
										<p class="card-text">Vendeur : <a href="">${articleVendu.vendeur.pseudo}</a></p>    
									</div>			
								</div>
							</div>
						</div>
				    </c:forEach>
				</c:if>
				<c:if test="${empty listeArticlesVendus}">
					<p>Pas d'articles vendus</p>
				</c:if>
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