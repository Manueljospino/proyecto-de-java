const BASE = 'http://localhost:8080';

// ── Auth ──────────────────────────────────────────────
async function login(numberId, password) {
  const res = await fetch(`${BASE}/api/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ numberId, password })
  });
  if (!res.ok) throw new Error(await res.text());
  const token = await res.text();
  localStorage.setItem('jwt', token);
  return token;
}

function logout() {
  localStorage.removeItem('jwt');
  location.href = 'index.html';
}

function getToken() {
  return localStorage.getItem('jwt');
}

function getPayload() {
  const token = getToken();
  if (!token) return null;
  return JSON.parse(atob(token.split('.')[1]));
}

function getRole() {
  const p = getPayload();
  return p ? p.role : null;
}

function getName() {
  const p = getPayload();
  return p ? p.name : '';
}

function requireRole(role) {
  const actual = getRole();
  if (!actual || actual !== role) {
    alert('No tienes permiso para acceder a esta página.');
    location.href = 'index.html';
  }
}

// ── Fetch autenticado ─────────────────────────────────
async function api(path, options = {}) {
  const token = getToken();
  const res = await fetch(BASE + path, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : '',
      ...(options.headers || {})
    }
  });
  if (!res.ok) throw new Error(await res.text());
  const text = await res.text();
  try { return JSON.parse(text); } catch { return text; }
}

// ── Asistencia ────────────────────────────────────────
function getActiveSessions()               { return api('/api/assistance/active-sessions'); }
function registerAssistance(teacherNumberId) { return api('/api/assistance/register', { method: 'POST', body: JSON.stringify({ teacherNumberId }) }); }
function getMyAssistance()                 { return api('/api/assistance/mine'); }
function openSession(subject)              { return api(`/api/assistance/open?subject=${encodeURIComponent(subject)}`, { method: 'POST' }); }
function closeSession(sessionId)           { return api(`/api/assistance/close/${sessionId}`, { method: 'PUT' }); }
function getSessionAssistance(sessionId)   { return api(`/api/assistance/session/${sessionId}`); }

// ── Inscripciones ─────────────────────────────────────
function getMyInscriptions()               { return api('/api/inscription/mine'); }
function inscribeStudent(studentNumberId, teacherNumberId) {
  return api('/api/inscription/inscribe', { method: 'POST', body: JSON.stringify({ studentNumberId, teacherNumberId }) });
}

// ── Admin / Auth ──────────────────────────────────────
function registerUser(data)                { return api('/api/auth/register', { method: 'POST', body: JSON.stringify(data) }); }
function resetPassword(numberId, newPassword) {
  return api('/api/auth/reset-password', { method: 'PUT', body: JSON.stringify({ numberId, newPassword }) });
}
function changePassword(currentPassword, newPassword) {
  return api('/api/auth/change-password', { method: 'PUT', body: JSON.stringify({ currentPassword, newPassword }) });
}

// ── UI helpers ────────────────────────────────────────
function showAlert(id, message, type = 'success') {
  const el = document.getElementById(id);
  if (!el) return;
  el.textContent = message;
  el.className = `alert alert-${type} show`;
  setTimeout(() => el.classList.remove('show'), 4000);
}

function setNavUser() {
  const el = document.getElementById('nav-user-name');
  if (el) el.textContent = getName();
}