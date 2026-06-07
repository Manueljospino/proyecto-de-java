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
  if (!confirm('¿Estás seguro de que quieres cerrar sesión?')) return;
  localStorage.removeItem('jwt');
  location.href = '../index.html';
}

function getToken() {
  return localStorage.getItem('jwt');
}

function getPayload() {
  const token = getToken();
  if (!token) return null;
  try {
    const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    const payload = JSON.parse(decodeURIComponent(atob(base64).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join('')));
    if (payload.exp && Date.now() / 1000 > payload.exp) {
      localStorage.removeItem('jwt');
      return null;
    }
    return payload;
  } catch {
    localStorage.removeItem('jwt');
    return null;
  }
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
    location.href = '../index.html';
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
// Estudiante: sesiones activas de sus materias
function getActiveSessions()                  { return api('/api/assistance/active-sessions'); }
// Estudiante: registrar asistencia en una sesión
function registerAssistance(sessionId)        { return api(`/api/assistance/register?sessionId=${sessionId}`, { method: 'POST' }); }
// Estudiante: historial de asistencia
function getMyAssistance()                    { return api('/api/assistance/my-history'); }
// Docente: abrir clase de una materia asignada
function openSession(subjectId)               { return api(`/api/assistance/open?subjectId=${subjectId}`, { method: 'POST' }); }
// Docente: cerrar clase
function closeSession(sessionId)              { return api(`/api/assistance/close/${sessionId}`, { method: 'PUT' }); }
// Docente: asistidos de una sesión
function getSessionAssistance(sessionId)      { return api(`/api/assistance/session/${sessionId}`); }
// Docente: sus sesiones históricas
function getMySessions()                      { return api('/api/assistance/my-sessions'); }
// Docente: materias asignadas
function getMyTeacherSubjects()               { return api('/api/assistance/my-subjects'); }

// ── Inscripciones ─────────────────────────────────────
// Estudiante: sus materias inscritas
function getMyInscriptions()                  { return api('/api/inscription/mine'); }
// Admin: inscribir estudiante a materia
function inscribeStudent(studentNumberId, subjectId) {
  return api('/api/inscription/inscribe', {
    method: 'POST',
    body: JSON.stringify({ studentNumberId, subjectId: Number(subjectId) })
  });
}

// ── Asignación docente-materia ────────────────────────
// Admin: asignar materia a docente
function assignSubjectToTeacher(teacherNumberId, subjectId) {
  return api('/api/teacher-subjects/assign', {
    method: 'POST',
    body: JSON.stringify({ teacherNumberId, subjectId: Number(subjectId) })
  });
}
// Admin: desasignar materia de docente
function unassignSubjectFromTeacher(teacherNumberId, subjectId) {
  return api('/api/teacher-subjects/unassign', {
    method: 'DELETE',
    body: JSON.stringify({ teacherNumberId, subjectId: Number(subjectId) })
  });
}
// Admin: materias de un docente
function getTeacherSubjects(teacherNumberId)  { return api(`/api/teacher-subjects/by-teacher/${teacherNumberId}`); }

// ── Admin / Auth ──────────────────────────────────────
function registerUser(data)                   { return api('/api/auth/register', { method: 'POST', body: JSON.stringify(data) }); }
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
// Docente: historial de sesiones con filtro de fechas
function getSessionHistory(desde, hasta) {
  let url = '/api/assistance/session-history';
  if (desde && hasta) url += `?desde=${desde}&hasta=${hasta}`;
  return api(url);
}
// Docente: detalle de asistidos de una sesión
function getSessionDetail(sessionId) { return api(`/api/assistance/session-detail/${sessionId}`); }