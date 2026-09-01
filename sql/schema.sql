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
    estado          VARCHAR(20)   NOT NULL DEFAULT 'pendiente'
    CHECK (estado IN ('pendiente', 'confirmado', 'cancelado', 'atendido')),
    creado_en       TIMESTAMP     NOT NULL DEFAULT now(),
    CONSTRAINT uq_turno_fecha_hora UNIQUE (fecha, hora)
);

ALTER TABLE turnos ADD COLUMN IF NOT EXISTS estado VARCHAR(20) NOT NULL DEFAULT 'pendiente';

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
    imagen_url      VARCHAR(300),
    orden           INTEGER       NOT NULL DEFAULT 0
);

ALTER TABLE servicios ADD COLUMN IF NOT EXISTS imagen_url VARCHAR(300);

CREATE TABLE IF NOT EXISTS articulos_blog (
    id              SERIAL PRIMARY KEY,
    slug            VARCHAR(170)  NOT NULL UNIQUE,
    etiqueta        VARCHAR(30)   NOT NULL,
    titulo          VARCHAR(150)  NOT NULL,
    resumen         TEXT          NOT NULL,
    contenido       TEXT          NOT NULL DEFAULT '',
    imagen_url      VARCHAR(300),
    publicado_en    DATE          NOT NULL DEFAULT CURRENT_DATE
);

ALTER TABLE articulos_blog ADD COLUMN IF NOT EXISTS contenido TEXT NOT NULL DEFAULT '';
ALTER TABLE articulos_blog ADD COLUMN IF NOT EXISTS imagen_url VARCHAR(300);

CREATE INDEX IF NOT EXISTS idx_turnos_fecha ON turnos (fecha);
CREATE INDEX IF NOT EXISTS idx_turnos_mascota ON turnos (mascota_id);
CREATE INDEX IF NOT EXISTS idx_contacto_creado_en ON consultas_contacto (creado_en);

INSERT INTO servicios (codigo, titulo, descripcion, detalle, imagen_url, orden)
VALUES ('consulta', 'Consultas clínicas',
        'Control general de salud, diagnóstico y seguimiento de tratamientos para perros, gatos, conejos, hurones y cobayos.',
        'Duración aprox. 30 min · Con o sin turno previo.', '/resources/img/servicios/consulta.svg', 1),
       ('vacunacion', 'Vacunación',
        'Calendario completo de vacunas obligatorias y refuerzos según especie, edad y estilo de vida del animal.',
        'Incluye carnet sanitario actualizado.', '/resources/img/servicios/vacunacion.svg', 2),
       ('cirugia', 'Cirugías',
        'Castraciones, cirugías de partes blandas y procedimientos programados con seguimiento post-operatorio.',
        'Requiere estudios prequirúrgicos previos.', '/resources/img/servicios/cirugia.svg', 3),
       ('estudios', 'Estudios y diagnóstico',
        'Análisis de sangre, radiografías y ecografías para un diagnóstico preciso.',
        'Resultados en 24 a 48 h según el estudio.', '/resources/img/servicios/estudios.svg', 4),
       ('odontologia', 'Odontología',
        'Limpieza dental, control de sarro y extracciones para prevenir enfermedades bucales.',
        'Recomendado una vez al año.', '/resources/img/servicios/odontologia.svg', 5),
       ('peluqueria', 'Peluquería y baño',
        'Baño, corte de pelo y cuidado estético para que tu mascota esté siempre cómoda.', 'Turnos de martes a sábado.',
        '/resources/img/servicios/peluqueria.svg', 6) ON CONFLICT (codigo) DO NOTHING;

INSERT INTO articulos_blog (slug, etiqueta, titulo, resumen, contenido, imagen_url, publicado_en)
VALUES ('calendario-vacunacion-cachorros', 'Perros', 'Calendario de vacunación para cachorros',
        'Qué vacunas necesita tu cachorro en los primeros seis meses y por qué no conviene atrasarlas.',
        E 'Los primeros seis meses de vida son la etapa más importante para construir las defensas de un cachorro. Durante ese período, la protección que heredó de la madre se va perdiendo de a poco, y ahí es cuando el plan de vacunación tiene que tomar la posta.\n\nEn general, el esquema arranca entre las 6 y 8 semanas de vida con una vacuna múltiple (que cubre moquillo, parvovirus, hepatitis y parainfluenza, entre otras) y se repite cada 3 a 4 semanas hasta completar tres dosis. La vacuna antirrábica suele aplicarse a partir de los 3 meses.\n\nAtrasar una dosis no es un detalle menor: cada refuerzo está pensado para "pisarle el pie" a la ventana de tiempo en la que bajan los anticuerpos maternos y todavía no está completa la inmunidad propia del cachorro. Ese hueco es, justamente, el momento de mayor riesgo frente a enfermedades como el parvovirus, que puede ser mortal.\n\nHasta completar el esquema, lo recomendable es evitar espacios con mucha circulación de otros animales no vacunados (plazas, veterinarias de paso, paseos largos) y priorizar el contacto en casa o con mascotas que ya estén con las vacunas al día.\n\nEn Huellas llevamos un carnet sanitario digital para cada mascota, así nunca perdés de vista cuándo toca el próximo refuerzo.',
        '/resources/img/blog/cachorro-vacunacion.svg', CURRENT_DATE - INTERVAL '30 days'),

       ('senales-dolor-gatos', 'Gatos', 'Señales de que tu gato tiene dolor',
        'Los gatos esconden bien el malestar. Estas son las señales sutiles que conviene vigilar.',
        E 'Los gatos son animales de instinto de supervivencia: en la naturaleza, mostrar signos de debilidad los vuelve un blanco fácil, y esa herencia hace que sigan ocultando el dolor incluso viviendo cómodos en un sillón. Por eso, muchas veces el dueño nota que "algo no está bien" recién cuando el malestar ya lleva un tiempo.\n\nAlgunas señales sutiles a las que conviene prestarle atención son los cambios en la postura (encorvarse o quedarse muy quieto en una posición), dejar de usar el arenero o hacerlo fuera de su lugar habitual, disminuir el acicalamiento (un gato con dolor suele dejar de lavarse), esconderse más de lo normal, o cambios bruscos en el apetito.\n\nTambién hay señales más específicas en la cara: los ojos entrecerrados, las orejas hacia atrás y los bigotes tensos y hacia adelante son parte de lo que se conoce como la "escala de dolor facial felino", una herramienta que usamos en la clínica para estimar el nivel de molestia.\n\nSi notás dos o más de estas señales sostenidas por más de un día, lo mejor es traerlo a control. Cuanto antes se detecta la causa, más simple suele ser el tratamiento.',
        '/resources/img/blog/gato-senales-dolor.svg', CURRENT_DATE - INTERVAL '20 days'),

       ('alimentacion-conejos', 'Conejos', 'Alimentación correcta para conejos',
        'Cuánto heno, verdura y balanceado necesita un conejo sano, según su edad.',
        E 'La base de la dieta de un conejo adulto sano tiene que ser heno, y no es una exageración: debería representar cerca del 80% de lo que come en el día. El heno (de pasto, tipo alfalfa para cachorros o gramíneas para adultos) aporta la fibra necesaria para que el intestino funcione bien y, de paso, ayuda a desgastar los dientes, que en los conejos crecen durante toda la vida.\n\nEl otro 15% aproximadamente debería ser verdura fresca: hojas verdes como acelga, radicheta o achicoria, introducidas de a una por vez para detectar si alguna le cae mal. Hay que evitar la lechuga tipo iceberg, que aporta poco nutriente, y todo vegetal de la familia de la cebolla o el ajo.\n\nEl balanceado (pellets) debería ser la porción más chica de la dieta, apenas una o dos cucharadas por kilo de peso al día en un adulto. Es común el error de darle balanceado como plato principal, lo que puede derivar en sobrepeso y problemas dentales a largo plazo.\n\nLos cachorros y las hembras en gestación o lactancia tienen requerimientos distintos (más calorías y calcio), así que conviene siempre confirmar la dieta puntual en la consulta según la etapa de vida del conejo.',
        '/resources/img/blog/conejo-alimentacion.svg', CURRENT_DATE - INTERVAL '15 days'),

       ('cuidados-basicos-hurones', 'Hurones', 'Cuidados básicos para hurones en casa',
        'Temperatura, jaula y rutina de juego: lo esencial para un hurón sano.',
        E 'Los hurones son animales muy sensibles al calor: su rango de confort térmico ronda entre los 15° y 21°C, y por encima de los 27°C corren riesgo real de golpe de calor. En los meses de verano, esto significa asegurar un ambiente fresco y ventilado, con sombra y agua fresca disponible todo el tiempo.\n\nLa jaula debería ser lo más grande posible, con varios niveles, una zona de descanso oscura y separada del área donde come y hace sus necesidades (los hurones suelen aprender rápido a usar una esquina como bañera). Eso sí: la jaula es solo la base, porque necesitan salir a explorar y jugar fuera de ella todos los días, en un espacio a prueba de hurones (sin huecos pequeños, cables sueltos o materiales de espuma que puedan tragar).\n\nSon animales muy sociales y juguetones, así que la rutina diaria de juego no es un lujo sino una necesidad para su bienestar. Un hurón aburrido tiende a desarrollar conductas destructivas o de estrés.\n\nEn cuanto a la alimentación, necesitan una dieta alta en proteína animal y baja en fibra (son carnívoros estrictos), por lo que conviene usar balanceados formulados específicamente para hurones y no para gatos.',
        '/resources/img/blog/huron-cuidados.svg', CURRENT_DATE - INTERVAL '10 days'),

       ('vitamina-c-cobayos', 'Cobayos', 'Vitamina C en cobayos: por qué es clave',
        'A diferencia de otros roedores, los cobayos no producen su propia vitamina C.',
        E 'A diferencia de la mayoría de los mamíferos, los cobayos no tienen la enzima necesaria para sintetizar su propia vitamina C, igual que los humanos. Esto significa que dependen exclusivamente de la dieta para cubrir ese requerimiento, y una carencia sostenida puede derivar en escorbuto: encías inflamadas, dolor articular, pelo sin brillo y, en casos avanzados, complicaciones más serias.\n\nUn cobayo adulto necesita en promedio entre 10 y 30 mg de vitamina C por día, cantidad que aumenta en épocas de crecimiento, gestación o enfermedad. La mejor fuente son las verduras frescas: morrón rojo, brócoli, perejil y hojas verdes son buenas opciones para incorporar a diario.\n\nUn punto importante es que la vitamina C se degrada con el tiempo y el calor, así que los pellets balanceados "enriquecidos" pierden buena parte de su aporte a las pocas semanas de fabricados. Por eso no conviene depender solo del balanceado: la verdura fresca diaria sigue siendo indispensable.\n\nSi notás que tu cobayo cojea, deja de comer con ganas o tiene las encías con un aspecto raro, es buena idea hacer una consulta: son señales que muchas veces se relacionan con un déficit de esta vitamina.',
        '/resources/img/blog/cobayo-vitamina-c.svg', CURRENT_DATE - INTERVAL '5 days') ON CONFLICT (slug) DO NOTHING;
