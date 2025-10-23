// Helpers locais
const APIv = {
  async get(url){ const r = await fetch(url); if(!r.ok) throw new Error(await r.text()); return r.json(); },
  async send(url, method, data){
    const r = await fetch(url, {
      method,
      headers: { 'Content-Type':'application/json' },
      body: data ? JSON.stringify(data) : undefined
    });
    if(!r.ok) throw new Error(await r.text());
    return r.status === 204 ? null : r.json();
  }
};
function notifOkV(msg){ toastV(msg, 'success'); }
function notifErrV(msg){ toastV(msg, 'error'); }
function toastV(mensagem, tipo='success'){
  const wrap = document.getElementById('notificacao-container') || (()=> {
    const c = document.createElement('div');
    c.id = 'notificacao-container';
    c.style.position='fixed'; c.style.top='20px'; c.style.right='20px'; c.style.zIndex=9999;
    document.body.appendChild(c); return c;
  })();
  const el = document.createElement('div');
  el.className = `alert ${tipo==='success'?'alert-success':'alert-danger'} shadow-sm`;
  el.innerHTML = `<strong>${tipo==='success'?'Sucesso':'Erro'}:</strong> ${mensagem}`;
  wrap.appendChild(el);
  setTimeout(()=> el.remove(), 3000);
}

// Data topo
document.getElementById('current-date').textContent = new Date().toLocaleDateString('pt-BR');

// Refs
const $$ = (s)=> document.querySelector(s);
const tbody    = $$('#vol-table tbody');
const formCard = document.getElementById('vol-form-card');
const toggle   = (el, show)=> el.classList.toggle('d-none', !show);

// Lista
async function loadVoluntarios(){
  try{
    tbody.innerHTML = '<tr><td colspan="6">Carregando...</td></tr>';
    const rows = await APIv.get('/api/voluntarios');
    tbody.innerHTML = rows.map(r => `
      <tr>
        <td>${r.volCpf}</td>
        <td>${r.volNome||''}</td>
        <td>${r.volTelefone||''}</td>
        <td>${r.cidade||''}</td>
        <td>${r.email||''}</td>
        <td class="d-flex gap-2">
          <button class="btn btn-sm btn-outline-secondary" data-action="edit" data-id='${encodeURIComponent(JSON.stringify(r))}'>
            <i class="bi bi-pencil-square"></i> Editar
          </button>
          <button class="btn btn-sm btn-danger" data-action="del" data-cpf="${r.volCpf}">
            <i class="bi bi-trash"></i> Excluir
          </button>
        </td>
      </tr>`).join('');
  }catch(e){
    tbody.innerHTML = '';
    notifErrV('Erro ao carregar voluntários: ' + e.message);
  }
}

// Abrir form
function openVolForm(v){
  document.getElementById('vol-form-title').textContent = v ? `Editar Voluntário ${v.volCpf}` : 'Novo Voluntário';
  document.getElementById('vol-cpf').value    = v?.volCpf || '';
  document.getElementById('vol-cpf').disabled = !!v?.volCpf;
  document.getElementById('vol-nome').value   = v?.volNome || '';
  document.getElementById('vol-tel').value    = v?.volTelefone || '';
  document.getElementById('vol-cidade').value = v?.cidade || '';
  document.getElementById('vol-uf').value     = v?.uf || '';
  document.getElementById('vol-cep').value    = v?.cep || '';
  document.getElementById('vol-email').value  = v?.email || '';
  document.getElementById('vol-user').value   = v?.userName || '';
  document.getElementById('vol-form').dataset.cpf = v?.volCpf || '';
  toggle(formCard, true);
}

// Submit
document.getElementById('vol-form').addEventListener('submit', async (e)=>{
  e.preventDefault();
  const currentCpf = document.getElementById('vol-form').dataset.cpf;
  const payload = {
    volCpf:  document.getElementById('vol-cpf').value.trim(),
    volNome: document.getElementById('vol-nome').value.trim(),
    volTelefone: document.getElementById('vol-tel').value.trim(),
    cidade:  document.getElementById('vol-cidade').value.trim(),
    uf:      document.getElementById('vol-uf').value.trim(),
    cep:     document.getElementById('vol-cep').value.trim(),
    email:   document.getElementById('vol-email').value.trim(),
    userName:document.getElementById('vol-user').value.trim(),
  };
  try{
    if(currentCpf) await APIv.send(`/api/voluntarios/${currentCpf}`, 'PUT', payload);
    else           await APIv.send('/api/voluntarios', 'POST', payload);
    notifOkV('Voluntário salvo.');
    toggle(formCard,false);
    loadVoluntarios();
  }catch(e){ notifErrV('Erro ao salvar voluntário: ' + e.message); }
});

// Cancelar
document.getElementById('vol-cancelar').addEventListener('click', ()=> toggle(formCard,false));

// Delegação da tabela
tbody.addEventListener('click', async (ev)=>{
  const btn = ev.target.closest('button'); if(!btn) return;
  const act = btn.dataset.action;
  if(act==='edit'){
    const v = JSON.parse(decodeURIComponent(btn.dataset.id));
    openVolForm(v);
  } else if(act==='del'){
    const cpf = btn.dataset.cpf;
    if(!confirm('Excluir voluntário ' + cpf + '?')) return;
    try{
      await APIv.send(`/api/voluntarios/${cpf}`, 'DELETE');
      notifOkV('Excluído.');
      loadVoluntarios();
    }catch(e){ notifErrV('Erro: ' + e.message); }
  }
});

// Topo
document.getElementById('vol-btn-novo').addEventListener('click', ()=> openVolForm());

// Boot
loadVoluntarios();
