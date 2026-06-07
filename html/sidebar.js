// sidebar.js — Barra lateral dinámica según rol

const SIDEBAR_CONFIG = {
  ADMIN: {
    sections: [
      {
        label: 'Principal',
        items: [
          { icon: 'ti-layout-dashboard', label: 'Panel de Admin', href: 'Admin.html', page: 'admin' },
          { icon: 'ti-users', label: 'Ver todos los usuarios', href: 'Users.html', page: 'users' },
        ]
      },
      {
        label: 'Usuarios',
        items: [
          { icon: 'ti-user-plus', label: 'Registrar usuario', action: () => goToPageAndScroll('Admin.html', 'r-name') },
          { icon: 'ti-link', label: 'Inscribir estudiante', action: () => goToPageAndScroll('Admin.html', 'i-student') },
          { icon: 'ti-key', label: 'Resetear contraseña', action: () => goToPageAndScroll('Admin.html', 'rp-id') },
          { icon: 'ti-trash', label: 'Eliminar usuario', action: () => goToPageAndScroll('Admin.html', 'd-id') },
        ]
      },
      {
        label: 'Materias',
        items: [
          { icon: 'ti-circle-plus', label: 'Agregar materia', action: () => goToPageAndScroll('Admin.html', 'mat-name') },
          { icon: 'ti-pencil', label: 'Editar materia', action: () => goToPageAndScroll('Admin.html', 'mat-edit-select') },
          { icon: 'ti-book-off', label: 'Eliminar materia', action: () => goToPageAndScroll('Admin.html', 'lista-materias') },
          { icon: 'ti-books', label: 'Materias y docentes', action: () => goToPageAndScroll('Admin.html', 'lista-materias-docentes') },
        ]
      },
      {
        label: 'Docentes',
        items: [
          { icon: 'ti-plus', label: 'Asignar materia a docente', action: () => goToPageAndScroll('Admin.html', 'assign-teacher') },
          { icon: 'ti-search', label: 'Ver materias de docente', action: () => goToPageAndScroll('Admin.html', 'ver-teacher-id') },
        ]
      },
    ]
  },
  DOCENTE: {
    sections: [
      {
        label: 'Principal',
        items: [
          { icon: 'ti-layout-dashboard', label: 'Panel de Docente', href: 'Teacher.html', page: 'teacher' },
          { icon: 'ti-layout-list', label: 'Mis clases abiertas', href: 'Sessions.html', page: 'sessions' },
          { icon: 'ti-history', label: 'Historial de clases', href: 'record.html', page: 'record' },
        ]
      },
      {
        label: 'Clases',
        items: [
          { icon: 'ti-player-play', label: 'Abrir sesión', action: () => goToPageAndScroll('Teacher.html', 'subject-select') },
          { icon: 'ti-player-stop', label: 'Cerrar sesión', action: () => goToPageAndScroll('Teacher.html', 'sessionId') },
          { icon: 'ti-users', label: 'Lista de asistidos', action: () => scrollToId('sessionIdVer') },
        ]
      },
      {
        label: 'Mis Materias',
        items: [
          { icon: 'ti-book', label: 'Materias asignadas', action: () => scrollToId('mis-materias') },
        ]
      },
      {
        label: 'Mi cuenta',
        items: [
          { icon: 'ti-lock', label: 'Cambiar contraseña', action: () => goToPageAndScroll('Teacher.html', 'cp-current') },
        ]
      },
    ]
  },
  ESTUDIANTE: {
    sections: [
      {
        label: 'Principal',
        items: [
          { icon: 'ti-layout-dashboard', label: 'Panel de Estudiante', href: 'Student.html', page: 'student' },
        ]
      },
      {
        label: 'Asistencia',
        items: [
          { icon: 'ti-calendar', label: 'Sesiones activas', action: () => scrollToId('lista-sesiones') },
          { icon: 'ti-clipboard-list', label: 'Mi historial', action: () => scrollToId('historial-wrap') },
        ]
      },
      {
        label: 'Mis Materias',
        items: [
          { icon: 'ti-book', label: 'Materias inscritas', action: () => scrollToId('inscripciones-wrap') },
        ]
      },
      {
        label: 'Mi cuenta',
        items: [
          { icon: 'ti-lock', label: 'Cambiar contraseña', action: () => goToPageAndScroll('Student.html', 'cp-current') },
        ]
      },
    ]
  }
};

function goToPageAndScroll(page, targetId) {
  const currentPage = window.location.pathname.split('/').pop();
  if (currentPage === page) {
    scrollToId(targetId);
  } else {
    // Guardar el target en sessionStorage y redirigir
    sessionStorage.setItem('scrollTarget', targetId);
    window.location.href = page;
  }
}

function scrollToId(id) {
  const el = document.getElementById(id);
  if (el) {
    el.scrollIntoView({ behavior: 'smooth', block: 'center' });
    if (el.tagName === 'INPUT' || el.tagName === 'SELECT') {
      setTimeout(() => el.focus(), 400);
    }
    // Highlight breve
    el.style.transition = 'box-shadow 0.3s';
    el.style.boxShadow = '0 0 0 3px rgba(99,153,34,0.35)';
    setTimeout(() => { el.style.boxShadow = ''; }, 1400);
  }
  // Cerrar sidebar en móvil
  if (window.innerWidth < 769) closeSidebar();
}

function buildSidebar(role, activePage) {
  const config = SIDEBAR_CONFIG[role];
  if (!config) return;

  // Crear overlay
  const overlay = document.createElement('div');
  overlay.className = 'sidebar-overlay';
  overlay.id = 'sidebar-overlay';
  overlay.onclick = closeSidebar;
  document.body.appendChild(overlay);

  // Construir sidebar
  const sidebar = document.createElement('aside');
  sidebar.className = 'sidebar';
  sidebar.id = 'app-sidebar';

  // Header con buscador
  sidebar.innerHTML = `
    <div class="sidebar-header">
      <div class="sidebar-search-wrap">
        <i class="ti ti-search"></i>
        <input class="sidebar-search" id="sidebar-search-input" type="text" placeholder="Buscar función..." autocomplete="off" />
      </div>
    </div>
    <div class="sidebar-body" id="sidebar-body">
      <div class="sidebar-no-results" id="sidebar-no-results">
        <i class="ti ti-mood-empty" style="font-size:28px; display:block; margin-bottom:6px;"></i>
        Sin resultados
      </div>
    </div>
  `;

  const body = sidebar.querySelector('#sidebar-body');

  // Renderizar secciones
  config.sections.forEach(section => {
    const sec = document.createElement('div');
    sec.className = 'sidebar-section';
    sec.dataset.section = section.label;

    const label = document.createElement('div');
    label.className = 'sidebar-section-label';
    label.textContent = section.label;
    sec.appendChild(label);

    section.items.forEach(item => {
      const el = document.createElement(item.href ? 'a' : 'button');
      el.className = 'sidebar-item' + (item.page === activePage ? ' active' : '');
      el.dataset.label = item.label.toLowerCase();
      el.innerHTML = `<i class="ti ${item.icon}"></i><span>${item.label}</span>`;

      if (item.href && !item.action) {
        el.href = item.href;
      }
      if (item.action) {
        el.onclick = (e) => { e.preventDefault(); item.action(); };
      }

      sec.appendChild(el);
    });

    body.appendChild(sec);
  });

  document.body.appendChild(sidebar);

  // Buscador en tiempo real
  const searchInput = sidebar.querySelector('#sidebar-search-input');
  const noResults = sidebar.querySelector('#sidebar-no-results');

  searchInput.addEventListener('input', () => {
    const q = searchInput.value.toLowerCase().trim();
    let anyVisible = false;

    sidebar.querySelectorAll('.sidebar-section').forEach(sec => {
      let secHasMatch = false;
      sec.querySelectorAll('.sidebar-item').forEach(item => {
        const match = !q || item.dataset.label.includes(q);
        item.style.display = match ? '' : 'none';
        if (match) secHasMatch = true;
      });
      sec.classList.toggle('hidden', !secHasMatch);
      if (secHasMatch) anyVisible = true;
    });

    noResults.style.display = anyVisible ? 'none' : 'block';
  });

  // Añadir toggle al navbar
  const navbar = document.querySelector('.navbar');
  if (navbar) {
    const toggle = document.createElement('button');
    toggle.className = 'btn-sidebar-toggle';
    toggle.title = 'Menú';
    toggle.innerHTML = '<i class="ti ti-menu-2"></i>';
    toggle.onclick = toggleSidebar;
    navbar.insertBefore(toggle, navbar.firstChild);
  }

  // En desktop, cerrada por defecto — el usuario la abre con el botón
  // (no se abre automáticamente)
}

function openSidebar() {
  document.getElementById('app-sidebar')?.classList.add('open');
  // Overlay solo en móvil
  if (window.innerWidth < 769) {
    document.getElementById('sidebar-overlay')?.classList.add('open');
  }
  document.body.classList.add('sidebar-open');
}

function closeSidebar() {
  document.getElementById('app-sidebar')?.classList.remove('open');
  document.getElementById('sidebar-overlay')?.classList.remove('open');
  document.body.classList.remove('sidebar-open');
}

// Al cargar la página, hacer scroll al target pendiente si existe
window.addEventListener('load', () => {
  const target = sessionStorage.getItem('scrollTarget');
  if (target) {
    sessionStorage.removeItem('scrollTarget');
    setTimeout(() => scrollToId(target), 600);
  }
});

function toggleSidebar() {
  const sidebar = document.getElementById('app-sidebar');
  if (sidebar?.classList.contains('open')) {
    closeSidebar();
  } else {
    openSidebar();
  }
}