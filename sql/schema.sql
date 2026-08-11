CREATE TABLE IF NOT EXISTS mascotas (
    id              SERIAL PRIMARY KEY,
    nombre          VARCHAR(80)   NOT NULL,
    especie         VARCHAR(30)   NOT NULL,
    dueno_nombre    VARCHAR(120)  NOT NULL,
    dueno_contacto  VARCHAR(120)  NOT NULL,
    creado_en       TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS turnos (
    id              SERIAL PRIMARY KEY,
    mascota_id      INTEGER       NOT NULL REFERENCES mascotas (id) ON DELETE CASCADE,
    servicio        VARCHAR(40)   NOT NULL,
    fecha           DATE          NOT NULL,
    hora            TIME          NOT NULL,
    notas           TEXT,
    creado_en       TIMESTAMP     NOT NULL DEFAULT now(),
    CONSTRAINT uq_turno_fecha_hora UNIQUE (fecha, hora)
);

CREATE TABLE IF NOT EXISTS consultas_contacto (
    id              SERIAL PRIMARY KEY,
    nombre          VARCHAR(120)  NOT NULL,
    email           VARCHAR(150)  NOT NULL,
    asunto          VARCHAR(150),
    mensaje         TEXT          NOT NULL,
    creado_en       TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS suscriptores_newsletter (
    id              SERIAL PRIMARY KEY,
    email           VARCHAR(150)  NOT NULL UNIQUE,
    creado_en       TIMESTAMP     NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS servicios (
    id              SERIAL PRIMARY KEY,
    codigo          VARCHAR(40)   NOT NULL UNIQUE,
    titulo          VARCHAR(100)  NOT NULL,
    descripcion     TEXT          NOT NULL,
    detalle         VARCHAR(200),
    orden           INTEGER       NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS articulos_blog (
    id              SERIAL PRIMARY KEY,
    etiqueta        VARCHAR(30)   NOT NULL,
    titulo          VARCHAR(150)  NOT NULL,
    resumen         TEXT          NOT NULL,
    publicado_en    DATE          NOT NULL DEFAULT CURRENT_DATE
);

CREATE INDEX IF NOT EXISTS idx_turnos_fecha ON turnos (fecha);
CREATE INDEX IF NOT EXISTS idx_turnos_mascota ON turnos (mascota_id);
CREATE INDEX IF NOT EXISTS idx_contacto_creado_en ON consultas_contacto (creado_en);

INSERT INTO servicios (codigo, titulo, descripcion, detalle, orden) VALUES
('consulta',    'Consultas clínicas',   'Control general de salud, diagnóstico y seguimiento de tratamientos para perros, gatos, conejos, hurones y cobayos.', 'Duración aprox. 30 min · Con o sin turno previo.', 1),
('vacunacion',  'Vacunación',           'Calendario completo de vacunas obligatorias y refuerzos según especie, edad y estilo de vida del animal.', 'Incluye carnet sanitario actualizado.', 2),
('cirugia',     'Cirugías',             'Castraciones, cirugías de partes blandas y procedimientos programados con seguimiento post-operatorio.', 'Requiere estudios prequirúrgicos previos.', 3),
('estudios',    'Estudios y diagnóstico','Análisis de sangre, radiografías y ecografías para un diagnóstico preciso.', 'Resultados en 24 a 48 h según el estudio.', 4),
('odontologia', 'Odontología',          'Limpieza dental, control de sarro y extracciones para prevenir enfermedades bucales.', 'Recomendado una vez al año.', 5),
('peluqueria',  'Peluquería y baño',    'Baño, corte de pelo y cuidado estético para que tu mascota esté siempre cómoda.', 'Turnos de martes a sábado.', 6)
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO articulos_blog (etiqueta, titulo, resumen, publicado_en) VALUES
('Perros',  'Calendario de vacunación para cachorros', 'Qué vacunas necesita tu cachorro en los primeros seis meses y por qué no conviene atrasarlas.', CURRENT_DATE - INTERVAL '30 days'),
('Gatos',   'Señales de que tu gato tiene dolor', 'Los gatos esconden bien el malestar. Estas son las señales sutiles que conviene vigilar.', CURRENT_DATE - INTERVAL '20 days'),
('Conejos', 'Alimentación correcta para conejos', 'Cuánto heno, verdura y balanceado necesita un conejo sano, según su edad.', CURRENT_DATE - INTERVAL '15 days'),
('Hurones', 'Cuidados básicos para hurones en casa', 'Temperatura, jaula y rutina de juego: lo esencial para un hurón sano.', CURRENT_DATE - INTERVAL '10 days'),
('Cobayos', 'Vitamina C en cobayos: por qué es clave', 'A diferencia de otros roedores, los cobayos no producen su propia vitamina C.', CURRENT_DATE - INTERVAL '5 days');
