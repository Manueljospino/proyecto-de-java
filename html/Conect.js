// 1. Login → guardar token
async function login(numberId, password) {
  const res = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ numberId, password })
  });
  const token = await res.text(); // tu backend devuelve el token como texto plano
  localStorage.setItem('jwt', token);
}

// 2. Todas las demás llamadas llevan el token en el header
async function fetchConAuth(url, options = {}) {
  const token = localStorage.getItem('jwt');
  return fetch('http://localhost:8080' + url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token,
      ...options.headers
    }
  });
}

// 3. Ejemplo: estudiante registra asistencia
async function registrarAsistencia(teacherNumberId) {
  const res = await fetchConAuth('/api/assistance/register', {
    method: 'POST',
    body: JSON.stringify({ teacherNumberId })
  });
  const msg = await res.text();
  alert(msg);
}

function getRolFromToken() {
  const token = localStorage.getItem('jwt');
  const payload = JSON.parse(atob(token.split('.')[1]));
  return payload.roles; // o como lo hayas nombrado en JwtUtil
}

async function login(numberId, password) {
    try {
        const res = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ numberId, password })
        });

        if (!res.ok) throw new Error(await res.text());

        const token = await res.text();
        localStorage.setItem('jwt', token);
    } catch (e) {
        alert('Error al iniciar sesión: ' + e.message);
    }
}