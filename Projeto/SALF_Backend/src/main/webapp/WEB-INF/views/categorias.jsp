<%--
  Created by IntelliJ IDEA.
  User: wilker
  Date: 28/10/2025
  Time: 21:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Gerenciar Categorias - SALF</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-4">
    <h2 class="mb-3">Gerenciar Categorias</h2>

    <form id="formCategoria" class="row g-3">
        <input type="hidden" id="catCod">
        <div class="col-md-6">
            <input type="text" id="catDescr" class="form-control" placeholder="Descrição da categoria" required>
        </div>
        <div class="col-md-6">
            <button type="submit" class="btn btn-success">Salvar</button>
            <button type="reset" class="btn btn-secondary">Limpar</button>
        </div>
    </form>

    <hr>

    <table class="table table-striped" id="tabelaCategorias">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Descrição</th>
            <th>Ações</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>

<script>
    const apiUrl = 'http://localhost:8080/api/categorias';

    async function carregarCategorias() {
        const res = await fetch(apiUrl);
        const data = await res.json();
        const tbody = document.querySelector("tbody");
        tbody.innerHTML = "";
        data.forEach(cat => {
            tbody.innerHTML += " " +
                "<tr>"+
            "<td>"+ cat.catCod +"</td>"+
            "<td>"+ cat.catDescr +"</td>"+
            "<td>" +
                "<button class='btn btn-warning btn-sm' onclick='editar("+ JSON.stringify(cat) +")''>Editar</button> " +
            "<button class='btn btn-danger btn-sm' onclick='excluir("+ cat.catCod +")'>Excluir</button> " +
            "</td> " +
            "</tr>";
        });
    }

    function editar(cat) {
        document.getElementById("catCod").value = cat.catCod;
        document.getElementById("catDescr").value = cat.catDescr;
    }

    async function excluir(id) {
        if (!confirm("Deseja excluir esta categoria?")) return;
        await fetch(apiUrl+"/"+id, { method: "DELETE" });
        carregarCategorias();
    }

    document.getElementById("formCategoria").addEventListener("submit", async (e) => {
        e.preventDefault();
        const id = document.getElementById("catCod").value;
        const descr = document.getElementById("catDescr").value.trim();
        const categoria = { catCod: id ? parseInt(id) : null, catDescr: descr };

        const method = id ? "PUT" : "POST";
        const url = id ? apiUrl+"/"+id : apiUrl;
        await fetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(categoria)
        });
        e.target.reset();
        carregarCategorias();
    });

    carregarCategorias();
</script>
</body>
</html>

