document.addEventListener('DOMContentLoaded', async () => {
  const form = document.getElementById('paramForm');
  const msg = document.getElementById('msg');
  const errorBox = document.getElementById('error');
  const submitBtn = document.getElementById('submitBtn');

  // Campos (ids batendo com o HTML em camelCase)
  const f = {
    razao_social:   document.getElementById('razaoSocial'),
    nome_fantasia:  document.getElementById('nomeFantasia'),
    telefone:       document.getElementById('telefone'),
    email:          document.getElementById('email'),
    site:           document.getElementById('site'),
    rua:            document.getElementById('rua'),
    bairro:         document.getElementById('bairro'),
    cidade:         document.getElementById('cidade'),
    uf:             document.getElementById('uf'),
    cep:            document.getElementById('cep'),
    logotipo_small: document.getElementById('logotipoSmall'),
    logotipo_big:   document.getElementById('logotipoBig'),
    prevSmall:      document.getElementById('logoPreviewSmall'),
    prevBig:        document.getElementById('logoPreviewBig'),
  };

  // ===== ENDPOINTS =====
  const API_BASE = 'http://localhost:8080';
  const GET_ENDPOINT = `${API_BASE}/api/parametrizacao`; // GET ?email=
  const UPSERT_ENDPOINT = `${API_BASE}/api/parametrizacao`; // POST multipart (upsert)

  // ===== URL params =====
  const urlParams = new URLSearchParams(window.location.search);
  const emailEmpresa = urlParams.get('emailEmpresa');
  const nivel = urlParams.get('nivel'); // '1' pode editar
  if (emailEmpresa) f.email.value = emailEmpresa;

  // ===== Máscaras e UX =====
  if (window.IMask) {
    IMask(f.telefone, { mask: '(00) 00000-0000' });
    IMask(f.cep, { mask: '00000-000' });
  }
  f.uf.addEventListener('input', () => { f.uf.value = f.uf.value.toUpperCase(); });

  // ===== Preview de imagens =====
  function previewFile(input, imgEl) {
    const file = input?.files?.[0];
    if (!file) { imgEl.removeAttribute('src'); return; }
    const fr = new FileReader();
    fr.onload = e => imgEl.src = e.target.result;
    fr.readAsDataURL(file);
  }
  f.logotipo_small.addEventListener('change', () => previewFile(f.logotipo_small, f.prevSmall));
  f.logotipo_big.addEventListener('change',   () => previewFile(f.logotipo_big,   f.prevBig));

  // ===== Buscar empresa existente =====
  let empresaExistente = null;
  async function loadEmpresa() {
    if (!f.email.value) return;
    try {
      const res = await fetch(`${GET_ENDPOINT}?email=${encodeURIComponent(f.email.value)}`);
      if (res.ok) {
        empresaExistente = await res.json();

        f.razaoSocial.value  = empresaExistente.razaoSocial  ?? '';
        f.nomeFantasia.value = empresaExistente.nomeFantasia ?? '';
        f.telefone.value      = empresaExistente.telefone     ?? '';
        f.email.value         = empresaExistente.email        ?? f.email.value;
        f.site.value          = empresaExistente.site         ?? '';
        f.rua.value           = empresaExistente.rua          ?? '';
        f.bairro.value        = empresaExistente.bairro       ?? '';
        f.cidade.value        = empresaExistente.cidade       ?? '';
        f.uf.value            = (empresaExistente.uf || '').toUpperCase();
        f.cep.value           = empresaExistente.cep          ?? '';

        // Se o back expõe URLs dos logos
        if (empresaExistente.logotipo_small_url) f.prevSmall.src = empresaExistente.logotipo_small_url;
        if (empresaExistente.logotipo_big_url)   f.prevBig.src   = empresaExistente.logotipo_big_url;

        // Se já existe, o big deixa de ser estritamente obrigatório em runtime
        f.logotipo_big.removeAttribute('required');
      }
    } catch (e) {
      console.warn('Falha ao buscar empresa:', e);
    }
  }
  await loadEmpresa();

  // ============================================================
  // ===== CONTROLE DE PERMISSÃO (DESLIGADO POR ENQUANTO) =======
  // *** IMPORTANTE: a permissão REAL deve ser validada no BACKEND. ***
  // const podeEditar = (nivel === '1');
  // if (!podeEditar) {
  //   Array.from(form.elements).forEach(el => el.disabled = true);
  //   if (submitBtn) submitBtn.style.display = 'none';
  //   const aviso = document.createElement('div');
  //   aviso.className = 'alert alert-info mt-3';
  //   aviso.innerHTML = '<i class="bi bi-eye"></i> Acesso somente leitura.';
  //   form.prepend(aviso);
  // }
  // ============================================================

  // ===== Helpers de validação (data-validation) =====
  const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const regexPhone = /^\(?\d{2}\)?\s?\d{4,5}-?\d{4}$/;

  function parseMaxSize(v) {
    const m = /^(\d+(?:\.\d+)?)(KB|MB|B)$/i.exec(v || '');
    if (!m) return null;
    const num = parseFloat(m[1]);
    const unit = m[2].toUpperCase();
    if (unit === 'B')  return Math.round(num);
    if (unit === 'KB') return Math.round(num * 1024);
    if (unit === 'MB') return Math.round(num * 1024 * 1024);
    return null;
  }
  function setFieldError(input, message) {
    input.classList.add('error');
    const errEl = document.querySelector(`.error-text[data-for="${input.id}"]`);
    if (errEl) errEl.textContent = message || '';
  }
  function clearFieldError(input) {
    input.classList.remove('error');
    const errEl = document.querySelector(`.error-text[data-for="${input.id}"]`);
    if (errEl) errEl.textContent = '';
  }

  function validateField(input) {
    clearFieldError(input);
    const rulesStr = input.getAttribute('data-validation');
    if (!rulesStr) return true;

    const rules = rulesStr.split('|');
    const value = (input.type === 'file') ? (input.files[0] || null) : (input.value || '').trim();
    const customMsg = input.getAttribute('data-msg') || null;

    for (const rule of rules) {
      const [name, paramRaw] = rule.split(':');
      const param = paramRaw?.trim();

      if (name === 'required') {
        if (input.type === 'file') {
          const isRuntimeRequired = input.hasAttribute('required') && !empresaExistente;
          if (isRuntimeRequired && !value) { setFieldError(input, customMsg || 'Campo obrigatório.'); return false; }
        } else if (!value) { setFieldError(input, customMsg || 'Campo obrigatório.'); return false; }
      }
      if (name === 'min' && value && value.length < Number(param)) { setFieldError(input, customMsg || `Mínimo de ${param} caracteres.`); return false; }
      if (name === 'max' && value && value.length > Number(param)) { setFieldError(input, customMsg || `Máximo de ${param} caracteres.`); return false; }
      if (name === 'email' && value && !regexEmail.test(value))   { setFieldError(input, customMsg || 'E-mail inválido.'); return false; }
      if (name === 'phone' && value && !regexPhone.test(value))   { setFieldError(input, customMsg || 'Telefone inválido.'); return false; }
      if (name === 'image' && value) {
        if (!(value instanceof File) || !value.type.startsWith('image/')) { setFieldError(input, customMsg || 'Selecione um arquivo de imagem.'); return false; }
      }
      if (name === 'maxsize' && value && value instanceof File) {
        const maxBytes = parseMaxSize(param);
        if (maxBytes && value.size > maxBytes) { setFieldError(input, customMsg || `Arquivo excede ${param}.`); return false; }
      }
    }
    return true;
  }

  function validateForm() {
    errorBox.textContent = '';
    let firstInvalid = null;
    const inputs = Array.from(form.querySelectorAll('[data-validation]'));
    let ok = true;
    for (const el of inputs) {
      const valid = validateField(el);
      if (!valid && !firstInvalid) firstInvalid = el;
      ok = ok && valid;
    }
    if (!ok && firstInvalid) firstInvalid.focus();
    return ok;
  }

  // valida on-blur/input para UX melhor
  form.querySelectorAll('[data-validation]').forEach(el => {
    el.addEventListener('blur', () => validateField(el));
    el.addEventListener('input', () => { if (el.classList.contains('error')) validateField(el); });
    if (el.type === 'file') el.addEventListener('change', () => validateField(el));
  });

  // ===== Helper simples: monta e envia o FormData (camelCase!) =====
  async function enviarParametrizacao() {
    const fd = new FormData();

    // Campos de texto — agora com as CHAVES em camelCase (iguais à entidade)
    const camposTexto = {
      razaoSocial:  f.razao_social.value.trim(),
      nomeFantasia: f.nome_fantasia.value.trim(),
      telefone:     f.telefone.value.trim(),
      site:         f.site.value.trim(),
      email:        f.email.value.trim(),
      rua:          f.rua.value.trim(),
      bairro:       f.bairro.value.trim(),
      cidade:       f.cidade.value.trim(),
      uf:           f.uf.value.trim().toUpperCase(),
      cep:          f.cep.value.trim(),
    };
    Object.entries(camposTexto).forEach(([k, v]) => fd.append(k, v));

    // Arquivos — camelCase também
    if (f.logotipo_small.files[0]) fd.append('logotipoSmall', f.logotipo_small.files[0]);
    if (f.logotipo_big.files[0])   fd.append('logotipoBig',   f.logotipo_big.files[0]);

    const resp = await fetch(UPSERT_ENDPOINT, { method: 'POST', body: fd });
    if (!resp.ok) throw new Error(await resp.text());
    return resp.text();
  }

  // ===== Submit (bem simples) =====
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    msg.textContent = '';
    errorBox.textContent = '';

    // if (!podeEditar) { errorBox.textContent = 'Você não tem permissão para editar.'; return; }

    if (!validateForm()) {
      errorBox.textContent = 'Corrija os campos destacados.';
      return;
    }

    try {
      await enviarParametrizacao();
      msg.textContent = 'Empresa cadastrada/atualizada com sucesso!';
      setTimeout(() => {
        window.location.href = `empresa.html?funcEmail=${encodeURIComponent(f.email.value.trim())}&nivel=${nivel ?? ''}`;
      }, 1200);
    } catch (err) {
      errorBox.textContent = err.message || 'Erro ao salvar dados.';
    }
  });
});
