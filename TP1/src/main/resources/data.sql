-- Crear la secuencia solo si no existe
CREATE SEQUENCE IF NOT EXISTS usuarios_id_seq AS INTEGER;

-- Establecer el propietario de la secuencia si no está configurado
ALTER SEQUENCE IF EXISTS usuarios_id_seq OWNER TO postgres;

-- Crear la tabla solo si no existe
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT DEFAULT nextval('usuarios_id_seq'::regclass) NOT NULL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Establecer la propiedad de la secuencia si la tabla se ha creado
ALTER SEQUENCE IF EXISTS usuarios_id_seq OWNED BY usuarios.id;

-- Insertar el usuario 'admin' solo si no existe
INSERT INTO usuarios (username, password, role)
SELECT 'admin', '$2a$12$zwwtkMBwamfA9aOKuuhsdOVv9MYCsZiDrMUoys1M/66n816d9K7zu', 'ROLE_ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE username = 'admin');
