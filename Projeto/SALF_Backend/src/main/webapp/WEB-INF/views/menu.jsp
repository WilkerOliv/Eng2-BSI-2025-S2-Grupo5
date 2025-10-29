<%--
  Created by IntelliJ IDEA.
  User: wilker
  Date: 28/10/2025
  Time: 23:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Menu Principal - SALF</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="#">SALF - Sistema de Armazenamento Lar Filomena</a>
    </div>
</nav>

<div class="container mt-5">
    <div class="text-center mb-4">
        <h2 class="fw-bold">Menu Principal</h2>
        <p class="text-muted">Selecione a funcionalidade desejada abaixo</p>
    </div>

    <div class="row justify-content-center g-4">
        <!-- RF-B2: Gerenciar Categorias -->
        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <h5 class="card-title fw-bold">Gerenciar Categorias</h5>
                    <p class="card-text text-muted">Crie, edite e exclua categorias de produtos.</p>
                    <a href="categorias" class="btn btn-primary w-100">Acessar</a>
                </div>
            </div>
        </div>

        <!-- RF-F5: Criar Lista de Compras -->
        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <h5 class="card-title fw-bold">Lista de Compras</h5>
                    <p class="card-text text-muted">Gerencie listas de compras e seus produtos.</p>
                    <a href="listas" class="btn btn-success w-100">Acessar</a>
                </div>
            </div>
        </div>

        <!-- RF-F10: Registrar Necessidades -->
        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <h5 class="card-title fw-bold">Registrar Necessidades</h5>
                    <p class="card-text text-muted">Cadastre e acompanhe as necessidades de pessoas carentes.</p>
                    <a href="necessidades" class="btn btn-warning w-100">Acessar</a>
                </div>
            </div>
        </div>
    </div>
</div>

<footer class="text-center mt-5 py-3 bg-white border-top">
    <small class="text-muted">&copy; 2025 - Sistema de Armazenamento Lar Filomena (SALF)</small>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

