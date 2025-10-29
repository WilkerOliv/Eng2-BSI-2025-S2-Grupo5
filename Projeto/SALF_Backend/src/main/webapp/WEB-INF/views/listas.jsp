<%--
  Created by IntelliJ IDEA.
  User: wilker
  Date: 28/10/2025
  Time: 21:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Lista de Compras - SALF</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-4">
    <h2>Criar / Gerenciar Lista de Compras</h2>

    <form id="formLista" class="row g-3">
        <input type="hidden" id="lcCod">
        <div class="col-md-4">
            <input type="text" id="funcionarioCpf" class="form-control" placeholder="CPF do Funcionário" required>
        </div>
        <div class="col-md-4">
            <input type="date" id="dataCriacao" class="form-control" required>
        </div>
        <div class="col-md-4">
            <input type="text" id="descricao" class="form-control" placeholder="Descrição da lista" required>
        </div>
        <div class="col-md-3">
            <input type="number" id="statusAtendimento" class="form-control" placeholder="Status" value="0">
        </div>
        <div class="col-md-9">
            <button type="submit" class="btn btn-success">Salvar</button>
            <button type="reset" class="btn btn-secondary">Limpar</button>
        </div>
    </form>

    <hr>

    <table class="table table-bordered mt-3" id="tabelaListas">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Funcionário</th>
            <th>Data</th>
            <th>Descrição</th>
            <th>Status</th>
            <th>Ações</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>

<script>
    const apiListas = 'http://localhost:8080/api/listas';

    async function carregarListas() {
        const res = await fetch(apiListas);
        const data = await res.json();
        const tbody = document.querySelector("tbody");
        tbody.innerHTML = "";
        data.forEach(l => {
            tbody.innerHTML += "" +
                "<tr> " +
                "<td>"+l.lcCod+"</td> " +
                "<td>"+l.funcionarioCpf+"</td> "+
                "<td>"+l.dataCriacao+"</td>" +
                "<td>"+l.descricao+"</td> " +
            "<td>"+l.statusAtendimento+"</td> " +
            "<td>" +
    "<button class='btn btn-warning btn-sm' onclick='editar("+JSON.stringify(l)+")'>Editar</button>" +
    "<button class='btn btn-danger btn-sm' onclick='excluir("+l.lcCod+")'>Excluir</button> " +
    "</td> " +
    "</tr>";
        });
    }

    function editar(l) {
        document.getElementById("lcCod").value = l.lcCod;
        document.getElementById("funcionarioCpf").value = l.funcionarioCpf;
        document.getElementById("dataCriacao").value = l.dataCriacao;
        document.getElementById("descricao").value = l.descricao;
        document.getElementById("statusAtendimento").value = l.statusAtendimento;
    }

    async function excluir(id) {
        if (!confirm("Deseja excluir esta lista?")) return;
        await fetch(apiListas+"/"+id, { method: "DELETE" });
        carregarListas();
    }

    document.getElementById("formLista").addEventListener("submit", async (e) => {
        e.preventDefault();
        const id = document.getElementById("lcCod").value;
        const lista = {
            lcCod: id ? parseInt(id) : null,
            funcionarioCpf: document.getElementById("funcionarioCpf").value,
            dataCriacao: document.getElementById("dataCriacao").value,
            descricao: document.getElementById("descricao").value,
            statusAtendimento: parseInt(document.getElementById("statusAtendimento").value)
        };

        const method = id ? "PUT" : "POST";
        const url = id ? apiListas+"/"+id : apiListas;
        await fetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(lista)
        });
        e.target.reset();
        carregarListas();
    });

    carregarListas();
</script>
</body>
</html>

