const API_BASE = "http://localhost:8080";

async function apiGet(url) {
  const res = await fetch(API_BASE + url);
  if (!res.ok) throw new Error(`Erro ${res.status}: ${await res.text()}`);
  return res.json();
}

async function apiPost(url, body) {
  const res = await fetch(API_BASE + url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  });
  if (!res.ok) throw new Error(`Erro ${res.status}: ${await res.text()}`);
  return res.json();
}

async function apiPut(url, body) {
  const res = await fetch(API_BASE + url, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  });
  if (!res.ok) throw new Error(`Erro ${res.status}: ${await res.text()}`);
  return res.json();
}

async function apiDelete(url) {
  const res = await fetch(API_BASE + url, { method: "DELETE" });
  if (!res.ok && res.status !== 204)
    throw new Error(`Erro ${res.status}: ${await res.text()}`);
  return true;
}
