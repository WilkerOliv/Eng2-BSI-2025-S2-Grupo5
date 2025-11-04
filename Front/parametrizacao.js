// parametrizacao.js
document.addEventListener('DOMContentLoaded', async () => {
  const dateEl = document.getElementById('current-date');
  if (dateEl) dateEl.textContent = new Date().toLocaleDateString('pt-BR');

  const form      = document.getElementById('paramForm');
  const msg       = document.getElementById('msg');
  const errorBox  = document.getElementById('error');

  const f = {
    razao_social:   document.getElementById('razao_social'),
    nome_fantasia:  document.getElementById('nome_fantasia'),
    telefone:       document.getElementById('telefone'),
    email:          document.getElementById('email'),
    site:           document.getElementById('site'),
    rua:            document.getElementById('rua'),
    bairro:         document.getElementById('bairro'),
    cidade:         document.getElementById('cidade'),
    uf:             document.getElementById('uf'),
    cep:            document.getElementById('cep'),
    logotipo_small: document.getElementById('logotipo_small'),
    logotipo_big:   document.getElementById('logotipo_big'),
    prevSmall:      document.getElementById('logoPreviewSmall'),
    prevBig:        document.getElementById('logoPreviewBig'),
  };

  // --- estado global ---
  let existeEmpresaFlag = false; // se existe alguma empresa no banco
  let empresaExistente  = null;  // objeto retornado (por e-mail ou única)

  // --- util: máscaras ---
  if (window.IMask) {
    IMask(f.telefone, { mask: '(00) 00000-0000' });
    IMask(f.cep,      { mask: '00000-000' });
  }
  f.uf.addEventListener('input', () => { f.uf.value = f.uf.value.toUpperCase(); });

  // --- util: preview de imagem ---
  function previewFile(input, imgEl) {
    const file = input?.files?.[0];
    if (!file) { imgEl?.removeAttribute('src'); return; }
    const fr = new FileReader();
    fr.onload = e => { if (imgEl) imgEl.src = e.target.result; };
    fr.readAsDataURL(file);
  }
  f.logotipo_small.addEventListener('change', () => previewFile(f.logotipo_small, f.prevSmall));
  f.logotipo_big.addEventListener('change',   () => previewFile(f.logotipo_big,   f.prevBig));

  // --- util: File -> dataURL (Base64) ---
  function toDataURL(file) {
    return new Promise((resolve) => {
      if (!file) return resolve(null);
      const fr = new FileReader();
      fr.onload = () => resolve(fr.result);
      fr.onerror = () => resolve(null);
      fr.readAsDataURL(file);
    });
  }

  // --- validação (mantida) ---
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
          // arquivo obrigatório só no primeiro cadastro global
          const isRuntimeRequired = input.hasAttribute('required') && !existeEmpresaFlag && !empresaExistente;
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
    let firstInvalid = null, ok = true;
    const inputs = Array.from(form.querySelectorAll('[data-validation]'));
    for (const el of inputs) {
      const valid = validateField(el);
      if (!valid && !firstInvalid) firstInvalid = el;
      ok = ok && valid;
    }
    if (!ok && firstInvalid) firstInvalid.focus();
    return ok;
  }

  // --- API calls explícitos (como você pediu) ---

  // Boolean: existe alguma empresa?
  async function checkExisteEmpresa() {
    try {
      const r = await fetch("http://localhost:8080/api/parametrizacao/existeEmpresa", {
        method: "GET",
        headers: { "Accept": "application/json" }
      });
      if (!r.ok) 
        return (existeEmpresaFlag = false);
      const bool = await r.json();
      return (existeEmpresaFlag = !!bool);
    } catch {
      return (existeEmpresaFlag = false);
    }
  }

  // Retorna a única empresa (novo endpoint no backend)
  async function loadEmpresaUnica({ fill = true } = {}) {
    try {
      const r = await fetch("http://localhost:8080/api/parametrizacao/unica", {
        method: "GET",
        headers: { "Accept": "application/json" }
      });
      if (!r.ok) { empresaExistente = null; return; }
      empresaExistente = await r.json();
      if (fill) preencherFormularioFromEmpresa(empresaExistente);
    } catch {
      empresaExistente = null;
    }
  }

  // Busca por e-mail (para validar no submit; fill=false no submit)
  async function loadEmpresaPorEmail(email, { fill = true } = {}) {
    const e = (email || f.email.value || '').trim();
    if (!e) { empresaExistente = null; return; }
    try {
      const qs = new URLSearchParams({ email: e }).toString();
      const r = await fetch(`http://localhost:8080/api/parametrizacao?${qs}`, {
        method: "GET",
        headers: { "Accept": "application/json" }
      });
      if (!r.ok) { empresaExistente = null; return; }
      empresaExistente = await r.json();
      if (fill) preencherFormularioFromEmpresa(empresaExistente);
    } catch {
      empresaExistente = null;
    }
  }

  function preencherFormularioFromEmpresa(emp) {
    if (!emp) return;
    f.razao_social.value  = emp.razaoSocial  ?? '';
    f.nome_fantasia.value = emp.nomeFantasia ?? '';
    f.telefone.value      = emp.telefone     ?? '';
    f.email.value         = emp.email        ?? '';
    f.site.value          = emp.site         ?? '';
    f.rua.value           = emp.rua          ?? '';
    f.bairro.value        = emp.bairro       ?? '';
    f.cidade.value        = emp.cidade       ?? '';
    f.uf.value            = (emp.uf || '').toUpperCase();
    f.cep.value           = emp.cep          ?? '';

    if (typeof emp.logotipoSmall === 'string') f.prevSmall.src = emp.logotipoSmall;
    if (typeof emp.logotipoBig   === 'string') f.prevBig.src   = emp.logotipoBig;

    // edição: logo big não obrigatório
    f.logotipo_big.removeAttribute('required');
  }

  // --- Ao abrir a tela ---
  // 1) Se já existe empresa -> carrega a ÚNICA e preenche
  // 2) Senão, se veio ?emailEmpresa=... na URL, tenta carregar por e-mail
  const urlParams = new URLSearchParams(window.location.search);
  const emailEmpresa = urlParams.get('emailEmpresa');
  await checkExisteEmpresa();
  if (existeEmpresaFlag) {
    await loadEmpresaUnica({ fill: true });
  } else if (emailEmpresa) {
    f.email.value = emailEmpresa;
    await loadEmpresaPorEmail(emailEmpresa, { fill: true });
  }

  // --- Envio (POST JSON) ---
  async function enviarParametrizacaoJSON() {
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

    const fileSmall = f.logotipo_small.files[0] || null;
    const fileBig   = f.logotipo_big.files[0]   || null;

    const [smallB64, bigB64] = await Promise.all([
      fileSmall ? toDataURL(fileSmall) : (empresaExistente?.logotipoSmall ?? null),
      fileBig   ? toDataURL(fileBig)   : (empresaExistente?.logotipoBig   ?? null),
    ]);

    const payload = { ...camposTexto, logotipoSmall: smallB64, logotipoBig: bigB64 };

    const r = await fetch("http://localhost:8080/api/parametrizacao", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    const text = await r.text();
    if (!r.ok) throw new Error(text || 'Erro ao salvar.');
    return text;
  }

  // --- Submit ---
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    msg.textContent = '';
    errorBox.textContent = '';

    // Revalida existência e verifica se o e-mail digitado corresponde à empresa
    await checkExisteEmpresa();

    if (existeEmpresaFlag) {
      // Só deixa atualizar se o e-mail do form bater com a empresa existente.
      // (Não preenche o form aqui para não apagar edições do usuário.)
      await loadEmpresaPorEmail(f.email.value, { fill: false });
      if (!empresaExistente) {
        errorBox.textContent = 'Já existe uma empresa cadastrada. Informe o e-mail correto para atualizar a existente.';
        return;
      }
    }

    if (!validateForm()) {
      errorBox.textContent = 'Corrija os campos destacados.';
      return;
    }

    try {
      const texto = await enviarParametrizacaoJSON();
      msg.textContent = texto || 'Empresa cadastrada/atualizada com sucesso!';
      // Após salvar, garante que estamos com o registro atualizado no estado/local
      await checkExisteEmpresa();
      await loadEmpresaPorEmail(f.email.value, { fill: true });
    } catch (err) {
      errorBox.textContent = err.message || 'Erro ao salvar dados.';
    }
  });
});
