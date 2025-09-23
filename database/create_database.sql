-- Script SQL para crear la base de datos del Sistema de Cines Magenta
-- Fecha: 21 de Septiembre, 2025
-- Autor: Alex Fernández Varas

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS Cine_DB;

-- Usar la base de datos
USE Cine_DB;

-- Crear la tabla Cartelera para almacenar información de películas
CREATE TABLE IF NOT EXISTS Cartelera (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    director VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    duration INT NOT NULL COMMENT 'Duración en minutos',
    genre ENUM(
        'Acción', 
        'Drama', 
        'Comedia', 
        'Terror', 
        'Romance', 
        'Ciencia Ficción', 
        'Thriller', 
        'Aventura', 
        'Animación', 
        'Documental'
    ) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
