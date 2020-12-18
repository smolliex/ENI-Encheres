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
    <main>
    <div class="col-12 text-left pl-5">
                    <p class="input" id="pseudo">Pseudo : "${user.pseudo}"</p><br>
                    
                    <p class="input" id="nom">Nom : "${user.nom}"</p><br>

                    <p class="input" id="prenom">Prénom : "${user.prenom}"</p><br>
                    
                    <p class="input" id="email">Email : "${user.email}"</p><br>
                    
                    <p class="input" id="telephone">Téléphone : "${user.telephone}"</p><br>
                    
                    <p class="input" id="rue">Rue "${user.rue}"</p><br>
                   
                    <p class="input" id="pseudo">Code Postal "${user.code_postal}"</p><br>
                    
                    <p class="input" id="pseudo">Ville "${user.ville}"</p><br>
     </div>
                    
                    <c:if test="${utilisateur.no_utilisateur == user.no_utilisateur}">
                    <div class="row my-3">
						<div class="col-12 text-center pl-5">
							<a href="${pageContext.request.contextPath}/modifier"><input type="button" class="btn btn-lg btn-primary" value="Modifier"></a>
						</div>
					</div>
					</c:if>

    </main>

</body>
</html>