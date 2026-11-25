-- Esquema de la base de datos de El Golpe.
-- Se puede ejecutar varias veces: no borra datos existentes.
SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    monedas INT NOT NULL
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS objeto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255),
    precio INT NOT NULL,
    efecto VARCHAR(30) NOT NULL
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS entrada_inventario (
    usuarioId INT NOT NULL,
    objetoId INT NOT NULL,
    cantidad INT NOT NULL,
    PRIMARY KEY (usuarioId, objetoId),
    FOREIGN KEY (usuarioId) REFERENCES usuario (id),
    FOREIGN KEY (objetoId) REFERENCES objeto (id)
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS compra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuarioId INT NOT NULL,
    objetoId INT NOT NULL,
    cantidad INT NOT NULL,
    precioTotal INT NOT NULL,
    fecha VARCHAR(10) NOT NULL,
    FOREIGN KEY (usuarioId) REFERENCES usuario (id),
    FOREIGN KEY (objetoId) REFERENCES objeto (id)
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS museo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    dificultad INT NOT NULL,
    ancho INT NOT NULL,
    alto INT NOT NULL,
    mapa TEXT,
    recompensa INT NOT NULL,
    nombreObra VARCHAR(100)
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS vigilante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    museoId INT NOT NULL,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('GUARDIA', 'CAMARA')),
    x INT NOT NULL,
    y INT NOT NULL,
    direccion VARCHAR(10),
    ruta TEXT,
    FOREIGN KEY (museoId) REFERENCES museo (id)
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS partida (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuarioId INT NOT NULL,
    museoId INT NOT NULL,
    exito BOOLEAN NOT NULL,
    tiempoSegundos INT NOT NULL,
    monedasGanadas INT NOT NULL,
    fecha VARCHAR(10) NOT NULL,
    FOREIGN KEY (usuarioId) REFERENCES usuario (id),
    FOREIGN KEY (museoId) REFERENCES museo (id)
) DEFAULT CHARSET = utf8mb4;

-- Catalogo de la tienda
INSERT IGNORE INTO objeto (id, nombre, descripcion, precio, efecto) VALUES
    (1, 'Ganzúa', 'Abre puertas cerradas', 50, 'ABRIR_PUERTA'),
    (2, 'Bomba de humo', 'Te da una vida extra', 120, 'VIDA_EXTRA'),
    (3, 'Zapatillas silenciosas', 'Te mueves mas rapido', 200, 'MAS_VELOCIDAD');
