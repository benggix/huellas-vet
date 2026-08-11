/* Script para el menu hamburguesa, popup de bienvenida, validaciones de formularios del lado del cliente.
   Obviamente las validaciones reales (como la superposicion de horarios, persistencia, CSRF, etc) se realizan en el servidor
   (Servlet/Controller). Este script nos dara feedback inmediato
 */

document.addEventListener('DOMContentLoaded', function () {
    initYear();
    initNavToggle();
    initWelcomePopup();
    initFichaKeyboardAccess();
    initTurnosForm();
    initContactoForm();
    initNewsletterForms();
});

// Año dinamico en el footer
function initYear() {
    document.querySelectorAll('#year').forEach(function (el) {
        el.textContent = new Date().getFullYear();
    });
}

// Menu hamburguesa para dispositivos moviles
function initNavToggle() {
    var toggle = document.getElementById('navToggle');
    var nav = document.getElementById('siteNav');
    if (!toggle || !nav) return;

    toggle.addEventListener('click', function () {
        var isOpen = nav.classList.toggle('is-open');
        toggle.setAttribute('aria-expanded', isOpen ? 'true' : 'false');
        toggle.setAttribute('aria-label', isOpen ? 'Cerrar menú de navegación' : 'Abrir menú de navegación');
    });

    nav.querySelectorAll('a').forEach(function (link) {
        link.addEventListener('click', function () {
            nav.classList.remove('is-open');
            toggle.setAttribute('aria-expanded', 'false');
        });
    });
}

// El popup de bienvenida, indicando un promocion del mes. Sera solo valido una vez por sesion de navegador
function initWelcomePopup() {
    var popup = document.getElementById('welcomePopup');
    if (!popup) return;
    var closeBtn = document.getElementById('popupClose');
    var backdrop = document.getElementById('popupBackdrop');
    var STORAGE_KEY = 'huellas_promo_vista';

    var yaVista = false;
    try {
        yaVista = sessionStorage.getItem(STORAGE_KEY) === '1';
    } catch (e) {
        yaVista = false;
    }

    if (!yaVista) {
        setTimeout(function () {
            popup.hidden = false;
            var closeButton = document.getElementById('popupClose');
            if (closeButton) closeButton.focus();
        }, 900);
    }

    function cerrar() {
        popup.hidden = true;
        try {
            sessionStorage.setItem(STORAGE_KEY, '1');
        } catch (e) { /* noop */
        }
    }

    if (closeBtn) closeBtn.addEventListener('click', cerrar);
    if (backdrop) backdrop.addEventListener('click', cerrar);
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape' && !popup.hidden) cerrar();
    });

    // Si el usuario hace click en el botón "Reservar con descuento", también se lo contara como que ya fue visto, para que de esta forma no vuelva a aparecer nuevamente cuando este en la seccion de turnos
    var ctaLink = document.getElementById('popupCtaLink');
    if (ctaLink) {
        ctaLink.addEventListener('click', function () {
            try {
                sessionStorage.setItem(STORAGE_KEY, '1');
            } catch (e) { /* noop */
            }
        });
    }
}

// Para permitir acceder a la ficha con la tecla tab (ademas del mouse)
function initFichaKeyboardAccess() {
    document.querySelectorAll('.ficha').forEach(function (ficha) {
        ficha.setAttribute('tabindex', '0');
    });
}


// utilidades de validacion compartidas
function clearErrors(form) {
    form.querySelectorAll('.field.is-invalid').forEach(function (f) {
        f.classList.remove('is-invalid');
    });
}

function markInvalid(form, fieldName) {
    var input = form.querySelector('[name="' + fieldName + '"]');
    if (!input) return;
    var field = input.closest('.field');
    if (field) field.classList.add('is-invalid');
}

function requireField(form, fieldName) {
    var input = form.querySelector('[name="' + fieldName + '"]');
    if (!input || !input.value || !input.value.trim()) {
        markInvalid(form, fieldName);
        return false;
    }
    return true;
}

function isValidEmail(value) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
}

function showMsg(el, text, ok) {
    if (!el) return;
    el.textContent = text;
    el.classList.remove('is-ok', 'is-error');
    el.classList.add(ok ? 'is-ok' : 'is-error');
}


// Para la validacion de los turnos, validamos que los campos sean obligatorios, fecha, horario, la superposicion real de los horarios, etc
function initTurnosForm() {
    var form = document.getElementById('turnosForm');
    if (!form) return;
    var msg = document.getElementById('turnosMsg');

    form.addEventListener('submit', function (e) {
        clearErrors(form);
        var valid = true;

        ['nombreDueno', 'contacto', 'mascota', 'especie', 'servicio', 'fecha', 'hora'].forEach(function (name) {
            if (!requireField(form, name)) valid = false;
        });

        var fechaInput = form.querySelector('[name="fecha"]');
        if (fechaInput && fechaInput.value) {
            var hoy = new Date();
            hoy.setHours(0, 0, 0, 0);
            var fechaElegida = new Date(fechaInput.value + 'T00:00:00');
            if (fechaElegida < hoy) {
                markInvalid(form, 'fecha');
                valid = false;
            }
        }

        var horaInput = form.querySelector('[name="hora"]');
        if (horaInput && horaInput.value) {
            var partes = horaInput.value.split(':').map(Number);
            var minutos = partes[0] * 60 + partes[1];
            var apertura = 9 * 60, cierre = 19 * 60;
            if (minutos < apertura || minutos > cierre) {
                markInvalid(form, 'hora');
                valid = false;
            }
        }

        if (!valid) {
            e.preventDefault();
            showMsg(msg, 'Revisá los campos marcados: faltan datos o el horario está fuera de atención (9 a 19 h).', false);
        } else {
            // Si es valido los dats (por ahora), se deja continuar el submit real hacia el servidor (action del form), donde se hara validacion correspondiente a la superposición de los horarios
            showMsg(msg, 'Enviando solicitud de turno…', true);
        }
    });
}

// formulario de contacto
function initContactoForm() {
    var form = document.getElementById('contactoForm');
    if (!form) return;
    var msg = document.getElementById('contactoMsg');

    form.addEventListener('submit', function (e) {
        clearErrors(form);
        var valid = true;

        ['nombre', 'email', 'mensaje'].forEach(function (name) {
            if (!requireField(form, name)) valid = false;
        });

        var emailInput = form.querySelector('[name="email"]');
        if (emailInput && emailInput.value && !isValidEmail(emailInput.value)) {
            markInvalid(form, 'email');
            valid = false;
        }

        if (!valid) {
            e.preventDefault();
            showMsg(msg, 'Revisá los campos marcados antes de enviar tu consulta.', false);
        } else {
            showMsg(msg, 'Enviando tu consulta…', true);
        }
    });
}

// formulario de newsletter ubicados en el footer o en el blog.
function initNewsletterForms() {
    document.querySelectorAll('.newsletter__form, .newsletter-inline-form').forEach(function (form) {
        var msgEl = form.querySelector('.form-msg');
        form.addEventListener('submit', function (e) {
            var emailInput = form.querySelector('[name="email"]');
            if (!emailInput || !isValidEmail(emailInput.value)) {
                e.preventDefault();
                if (emailInput) emailInput.setAttribute('aria-invalid', 'true');
                showMsg(msgEl, 'Ingresá un correo electrónico válido.', false);
                return;
            }
            emailInput.removeAttribute('aria-invalid');
            showMsg(msgEl, '¡Gracias por suscribirte! Revisá tu correo.', true);
        });
    });
}