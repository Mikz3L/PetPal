# PetPal

## Descripción

PetPal es una aplicación diseñada para gestionar usuarios utilizando una base de datos local en SQLite. Este proyecto implementa operaciones básicas de creación, lectura, actualización y eliminación (CRUD) de usuarios.

## Base de Datos

La aplicación utiliza una base de datos SQLite llamada `PetPal.db`, que contiene una tabla principal para almacenar la información de los usuarios. La tabla se llama `users` y tiene las siguientes columnas:

- `id`: Identificador único de cada usuario, configurado como clave primaria.
- `name`: Nombre del usuario (opcional, para futuras expansiones).
- `email`: Dirección de correo electrónico del usuario, configurado como único para evitar duplicados.
- `password`: Contraseña del usuario.

## Funcionalidades CRUD

1. **Crear Usuario**: Registra un nuevo usuario en la base de datos, almacenando su correo electrónico y contraseña. Este proceso verifica que el correo no esté ya registrado.

2. **Leer Usuarios**: Obtiene y muestra la lista de todos los usuarios registrados en la base de datos.

3. **Actualizar Usuario**: Permite actualizar el correo electrónico de un usuario específico, identificado por su `id`.

4. **Eliminar Usuario**: Borra un usuario de la base de datos utilizando su `id`.

## Cómo Ejecutar la Aplicación

1. Clona este repositorio y ábrelo en Android Studio.
2. Compila y ejecuta la aplicación en un dispositivo o emulador Android.
3. Utiliza la interfaz de usuario para registrar, ver, actualizar y eliminar usuarios en la base de datos.

## Requisitos

- Android Studio
- Dispositivo o emulador Android con versión mínima SDK compatible

## Notas

Este proyecto está en fase de desarrollo, y futuras versiones pueden incluir más funcionalidades relacionadas con la gestión de mascotas.
