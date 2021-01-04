<%@ page import="fr.eni.javaee.encheres.messages.LecteurMessage" %>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                                                                                                                                                                                        
                                                                                                                                                                                        
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
    <link rel="stylesheet" href="vendor/css/profil.css">
    <link rel="stylesheet" href="vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="vendor/bootstrap/css/bootstrap_yeti.min.css">
    <title>Profil de l'utilisateur</title>
</head>

<body>
    <!-- Entete de page -->
    <header>

        <!-- Navigation -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary static-top">
            <div class="container">
                <a class="navbar-brand" href="#">ENI Enchères</a>
            </div>
        </nav>

    </header>
    
    
    <main class="container-fluid">
        <div class="row text-center">
            <div class="col">
    
       			<div class="container">
       			<div class="row my-5 mx-5">
					<div class="col-6 text-left pl-5">
				 		<p>Pseudo : </p>
					</div>
					<div class="col-6 text-left pl-5">
			 			<p>${user.pseudo}</p><br>
					</div>
					<div class="col-6 text-left pr-5">
						 <p>Nom : </p>
					</div>
					<div class="col-6 text-left pl-5">
					 	<p>${user.nom}</p><br>
					</div>
			
			
			
				</div>
				</div>		
        	
        	
        	<div class="informations">
                    <p id="pseudo">Pseudo : ${user.pseudo}</p><br>
                    
                    <p id="nom">Nom : ${user.nom}</p><br>

                    <p id="prenom">Prénom : ${user.prenom}</p><br>
                    
                    <p id="email">Email : ${user.email}</p><br>
                    
                    <p id="telephone">Téléphone : ${user.telephone}</p><br>
                    
                    <p id="rue">Rue ${user.rue}</p><br>
                   
                    <p id="pseudo">Code Postal ${user.code_postal}</p><br>
                    
                    <p id="pseudo">Ville ${user.ville}</p><br>
			</div>
                    
                    <c:if test="${utilisateur.no_utilisateur == user.no_utilisateur}">
                    <div class="row my-3">
						<div class="col-12 text-center pl-5">
							<a href="${pageContext.request.contextPath}/modifier"><input type="button" class="btn btn-lg btn-primary" value="Modifier"></a>
						</div>
					</div>
					</c:if>
</div>
</div>
    </main>

</body>
</html>