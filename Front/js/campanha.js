// Helpers locais
const API = {
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
const fmtDate = (d)=> d ? String(d).substring(0,10) : '';
const fmtBRL  = (v)=> (v==null||isNaN(v)) ? '-' :
  new Intl.NumberFormat('pt-BR',{style:'currency',currency:'BRL'}).format(Number(v));

function notifOk(msg){ toast(msg, 'success'); }
function notifErr(msg){ toast(msg, 'error'); }
function toast(mensagem, tipo='success'){
  // simples: usa alert estilizado do bootstrap via classes
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

// Data no topo
document.getElementById('current-date').textContent = new Date().toLocaleDateString('pt-BR');

// Refs
const $ = (s)=> document.querySelector(s);
const tbody     = $('#cmp-table tbody');
const formCard  = $('#cmp-form-card');
const detCard   = $('#cmp-detalhe-card');
const toggle    = (el, show)=> el.classList.toggle('d-none', !show);

// Lista
async function loadCampanhas(){
  try{
    tbody.innerHTML = '<tr><td colspan="6">Carregando...</td></tr>';
    const rows = await API.get('/api/campanhas');
    tbody.innerHTML = rows.map(r => `
      <tr>
        <td>${r.idCampanha}</td>
        <td>${r.campanhaDescr||''}</td>
        <td>${fmtDate(r.campanhaDtIni)}</td>
        <td>${fmtDate(r.campanhaDtFim)}</td>
        <td>${fmtBRL(r.campanhaTotalArrecadado)}</td>
        <td class="d-flex gap-2">
          <button class="btn btn-sm btn-outline-secondary" data-action="open" data-id="${r.idCampanha}">
            <i class="bi bi-box-arrow-up-right"></i> Abrir
          </button>
          <button class="btn btn-sm btn-outline-secondary" data-action="edit" data-id='${encodeURIComponent(JSON.stringify(r))}'>
            <i class="bi bi-pencil-square"></i> Editar
          </button>
          <button class="btn btn-sm btn-danger" data-action="del" data-id="${r.idCampanha}">
            <i class="bi bi-trash"></i> Excluir
          </button>
        </td>
      </tr>`).join('');
  }catch(e){
    tbody.innerHTML = '';
    notifErr('Erro ao carregar campanhas: ' + e.message);
  }
}

// Form novo/editar
function openForm(c){
  $('#cmp-form-title').textContent = c ? `Editar Campanha #${c.idCampanha}` : 'Nova Campanha';
  $('#cmp-id').value    = c?.idCampanha || '';
  $('#cmp-descr').value = c?.campanhaDescr || '';
  $('#cmp-ini').value   = fmtDate(c?.campanhaDtIni) || new Date().toISOString().substring(0,10);
  $('#cmp-fim').value   = fmtDate(c?.campanhaDtFim) || '';
  $('#cmp-obs').value   = c?.observacao || '';
  toggle(detCard,false); toggle(formCard,true);
}

// Submit
document.getElementById('cmp-form').addEventListener('submit', async (e)=>{
  e.preventDefault();
  const id = $('#cmp-id').value;
  const payload = {
    idCampanha: id? Number(id): undefined,
    campanhaDescr: $('#cmp-descr').value.trim(),
    campanhaDtIni: $('#cmp-ini').value || null,
    campanhaDtFim: $('#cmp-fim').value || null,
    observacao: $('#cmp-obs').value || null,
  };
  try{
    if(id) await API.send(`/api/campanhas/${id}`, 'PUT', payload);
    else   await API.send('/api/campanhas', 'POST', payload);
    notifOk('Campanha salva.');
    toggle(formCard,false);
    await loadCampanhas();
  }catch(e){ notifErr('Erro ao salvar: ' + e.message); }
});

// Cancelar
document.getElementById('cmp-cancelar').addEventListener('click', ()=> toggle(formCard,false));

// Delegação da tabela
tbody.addEventListener('click', async (ev)=>{
  const btn = ev.target.closest('button'); if(!btn) return;
  const act = btn.dataset.action;
  if(act==='open'){
    const id = btn.dataset.id;
    const c  = await API.get(`/api/campanhas/${id}`);
    openDetalhe(c);
  } else if(act==='edit'){
    const c = JSON.parse(decodeURIComponent(btn.dataset.id));
    openForm(c);
  } else if(act==='del'){
    const id = btn.dataset.id;
    if(!confirm('Excluir campanha ' + id + '?')) return;
    try{
      await API.send(`/api/campanhas/${id}`, 'DELETE');
      notifOk('Excluída.');
      loadCampanhas();
    }catch(e){ notifErr('Erro: ' + e.message); }
  }
});

// Detalhe
function buildStats(c){
  const stats = document.getElementById('cmp-det-stats'); stats.innerHTML='';
  const blocks = [
    ['Início', fmtDate(c.campanhaDtIni)],
    ['Fim', fmtDate(c.campanhaDtFim)],
    ['Total', fmtBRL(c.campanhaTotalArrecadado)],
    ['Observação', c.observacao || '—'],
  ];
  blocks.forEach(([k,v])=>{
    const col = document.createElement('div');
    col.className='col-md-3';
    col.innerHTML = `
      <div class="p-3 border rounded">
        <div class="text-muted small">${k}</div>
        <div class="fw-semibold">${v}</div>
      </div>`;
    stats.appendChild(col);
  });
}

function openDetalhe(c){
  detCard.dataset.id = c.idCampanha;
  document.getElementById('cmp-det-title').textContent = `Campanha #${c.idCampanha} — ${c.campanhaDescr||''}`;
  document.getElementById('cmp-finalizar-total').value = c.campanhaTotalArrecadado || 0;
  buildStats(c);
  toggle(formCard,false); toggle(detCard,true);
  loadResponsaveis(c.idCampanha);
}

document.getElementById('cmp-detalhe-fechar').addEventListener('click', ()=> { toggle(detCard,false); loadCampanhas(); });

// Finalizar
document.getElementById('cmp-finalizar-open').addEventListener('click', ()=> document.getElementById('cmp-finalizar-total').focus());
document.getElementById('cmp-finalizar-save').addEventListener('click', async ()=>{
  const id    = detCard.dataset.id; if(!id) return;
  const total = Number(document.getElementById('cmp-finalizar-total').value || 0);
  try{
    const saved = await API.send(`/api/campanhas/${id}/finalizar`, 'PUT', { campanhaTotalArrecadado: total });
    notifOk('Resultado lançado.');
    openDetalhe(saved);
  }catch(e){ notifErr('Erro ao finalizar: ' + e.message); }
});

// Responsáveis
const respTbody = document.querySelector('#cmp-resp-table tbody');
async function loadResponsaveis(idCamp){
  respTbody.innerHTML = '<tr><td colspan="3">Carregando...</td></tr>';
  try{
    const rows = await API.get(`/api/campanhas/${idCamp}/responsaveis`);
    respTbody.innerHTML = rows.map(r=>{
      const cpf = r?.id?.voluntarioVolCpf || '';
      return `<tr>
        <td>${cpf}</td>
        <td>${r.cargoCampanha||''}</td>
        <td><button class="btn btn-sm btn-danger" data-action="resp-del" data-cpf="${cpf}">
            <i class="bi bi-x"></i> Remover
        </button></td>
      </tr>`;
    }).join('');
  }catch(e){
    respTbody.innerHTML = '';
    notifErr('Erro ao carregar responsáveis: ' + e.message);
  }
}

document.getElementById('cmp-resp-add').addEventListener('click', async ()=>{
  const id   = detCard.dataset.id; if(!id) return;
  const cpf  = document.getElementById('cmp-resp-cpf').value.trim(); if(!cpf) return notifErr('Informe o CPF.');
  const cargo= document.getElementById('cmp-resp-cargo').value.trim();
  try{
    await API.send(`/api/campanhas/${id}/responsaveis`, 'POST', {
      id:{ campanhaIdCampanha:Number(id), voluntarioVolCpf: cpf },
      cargoCampanha: cargo || null
    });
    document.getElementById('cmp-resp-cpf').value=''; document.getElementById('cmp-resp-cargo').value='';
    notifOk('Vínculo criado.');
    loadResponsaveis(id);
  }catch(e){ notifErr('Erro ao vincular: ' + e.message); }
});

respTbody.addEventListener('click', async (ev)=>{
  const btn = ev.target.closest('button'); if(!btn) return;
  if(btn.dataset.action!=='resp-del') return;
  const id  = detCard.dataset.id;
  const cpf = btn.dataset.cpf;
  if(!confirm('Remover vínculo do CPF ' + cpf + '?')) return;
  try{
    await API.send(`/api/campanhas/${id}/responsaveis/${cpf}`, 'DELETE');
    notifOk('Vínculo removido.');
    loadResponsaveis(id);
  }catch(e){ notifErr('Erro: ' + e.message); }
});

// Botões topo
document.getElementById('cmp-btn-nova').addEventListener('click', ()=> openForm());

// Boot
loadCampanhas();
