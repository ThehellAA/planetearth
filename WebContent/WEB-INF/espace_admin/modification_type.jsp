<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="fr" class="no-js">
<head>
    <c:import url="/inc/header.jsp" />
    <title>Espace Administrateur - Modification du type</title>
</head>

<body>
    <!-- Menu -->
    <c:remove var="page"/>
    <c:import url="/inc/menu.jsp" />

    <div class="container">
    
        <!-- Page Heading/Breadcrumbs -->
        
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">${ sessionUtilisateur.nom } ${ sessionUtilisateur.prenom }
                    <small>Espace Administrateur</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/index.jsp" />">Accueil</a>
                    </li>
                    <li><a href="<c:url value="/espace_administrateur"/>">Espace administrateur</a>
                    </li>
                    <li class="active">Types</li>
                </ol>
            </div>
        </div>
        <!-- /.row -->

        <hr>
        
        <div class="row">
            <!-- Sidebar Column -->
            <c:set var="pageAdmin" value="type" scope="request" />
            <c:import url="/WEB-INF/espace_admin/menu_admin.jsp" />
            
            <!-- Content Column -->
            <div class="col-md-9">
                <div class="col-md-4">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4>Modifier le type ...</h4>
                        </div>
                    <div class="panel-body">
                        <form method="post" action="<c:url value="/modificationType"/>">
                        
                            <div id="success">
		                        <c:if test="${ !empty form.erreurs }">
		                            <div class="alert alert-danger fade in">
		                                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		                                <strong><c:out value="${ form.resultat }"/></strong>
		                            </div>
		                        </c:if>
		                    </div>
                        
                            <div class="control-group form-group">
                                <div class="controls">
                                    <!-- hidden id field -->
                                    <input type="hidden" class="form-control" id="idType" name="idType" value="<c:out value="${ type.id }"/>">
                                    <!-- /.hidden id field -->
                                    <label for="nomType">Nom</label>
                                    <input type="text" class="form-control" id="nomType" name="nomType" value="<c:out value="${ type.nom }"/>" maxlength="200">
                                    <p class="help-block">${ form.erreurs['nomType'] }</p>
                                </div>
                            </div>
                            
                            <div class="control-group form-group">
                                <div class="controls">
                                    <label for="description">Description</label>
                                    <textarea rows="10" cols="100" class="form-control" id="description" name="description" maxlength="999" style="resize: vertical;"><c:out value="${ type.description }"/></textarea>
                                    <p class="help-block">${ form.erreurs['description'] }</p>
                                </div>
                            </div>
                                                            
                            <button type="submit" class="btn btn-primary">Appliquer</button>
                        </form>
                    </div>
                </div>
            </div>
            <!-- /.Pays Form -->
            <div class="col-md-7">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4>Liste des types</h4>
                    </div>
                    <c:choose>
                        <c:when test="${ empty sessionScope.types }">
                            <p class="erreur">Aucun type enregistré.</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${ sessionScope.types }" var="mapTypes">
                                <ul>
                                    <li>
                                        <strong><c:out value="${ mapTypes.value.nom }" /></strong>
                                        <ul>
                                            <li>
                                                <c:out value="${ mapTypes.value.description }" />
                                            </li>
                                        </ul>
                                        
                                        <ul>
                                            <li>
		                                        <a href="
		                                            <c:url value="/suppressionType">
		                                                <c:param name="idType" value="${ mapTypes.key }" />
		                                            </c:url>">
		                                            <i class="fa fa-trash-o" aria-hidden="true"></i>
		                                        </a>
		                                        <a href="
		                                            <c:url value="/modificationType">
		                                                <c:param name="idType" value="${ mapTypes.key }" />
		                                            </c:url>">
		                                            <i class="fa fa-pencil" aria-hidden="true"></i>
		                                        </a>
                                            </li>
                                        </ul>
                                    </li>
                                </ul>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                </div>
            </div>
        </div>
        </div>
        <!-- /.row -->

        <hr>

        <!-- Footer -->
        <c:import url="/inc/footer.jsp" />

    </div>
    <!-- /.container -->

    <!-- jQuery & Bootstrap Core JavaScript & Script to Activate the Carousel -->
    <c:import url="/inc/scripts.jsp" />
    
</body>

</html>
