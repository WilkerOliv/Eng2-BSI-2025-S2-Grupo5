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
    <title>Registrar Necessidades - SALF</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-4">
    <h2>Registrar Necessidades de Pessoas Carentes</h2>

    <form id="formNecessidade" class="row g-3">
        <div class="col-md-3">
            <input type="text" id="pessoaCpf" class="form-control" placeholder="CPF da Pessoa" required>
        </div>
        <div class="col-md-3">
            <input type="number" id="produtoId" class="form-control" placeholder="ID do Produto" required>
        </div>
        <div class="col-md-2">
            <input type="number" id="quantidade" class="form-control" placeholder="Qtd" required>
        </div>
        <div class="col-md-2">
            <input type="date" id="data" class="form-control" required>
        </div>
        <div class="col-md-10">
            <input type="text" id="observacao" class="form-control" placeholder="Observação (opcional)">
        </div>
        <div class="col-md-2">
            <button type="submit" class="btn btn-success w-100">Salvar</button>
        </div>
    </form>

    <hr>

    <table class="table table-bordered mt-3" id="tabelaNecessidades">
        <thead class="table-dark">
        <tr>
            <th>CPF Pessoa</th>
            <th>Produto ID</th>
            <th>Data</th>
            <th>Quantidade</th>
            <th>Observação</th>
            <th>Ações</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>

<script>
    const apiNecessidades = 'http://localhost:8080/api/necessidades/produtos';

    async function carregarNecessidades() {
        const res = await fetch(apiNecessidades);
        const data = await res.json();
        const tbody = document.querySelector("#tabelaNecessidades tbody");
        tbody.innerHTML = "";
        data.forEach(n => {
            tbody.innerHTML += "" +
                "<tr>"+
        "<td>"+n.pessoaCpf+"</td>"+
        "<td>"+n.produtoId+"</td>"+
        "<td>"+n.data+"</td>"+
        "<td>"+n.quantidade+"</td>"+
        "<td>"+n.observacao || ''+"</td>"+
        "<td>"+
          "<button class='btn btn-danger btn-sm' onclick='excluir("+n.pessoaCpf+", "+n.produtoId+")'>Excluir</button>"+
        "</td>"+
      "</tr>";
        });
    }

    async function excluir(cpf, produtoId) {
        if (!confirm("Deseja excluir esta necessidade?")) return;
        await fetch(apiNecessidades, {
            method: "DELETE",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ pessoaCpf: cpf, produtoId })
        });
        carregarNecessidades();
    }

    document.getElementById("formNecessidade").addEventListener("submit", async (e) => {
        e.preventDefault();
        const necessidade = {
            pessoaCpf: document.getElementById("pessoaCpf").value,
            produtoId: parseInt(document.getElementById("produtoId").value),
            data: document.getElementById("data").value,
            quantidade: parseInt(document.getElementById("quantidade").value),
            observacao: document.getElementById("observacao").value
        };
        await fetch(apiNecessidades, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(necessidade)
        });
        e.target.reset();
        carregarNecessidades();
    });

    carregarNecessidades();
</script>
</body>
</html>

