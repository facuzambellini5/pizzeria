-- =============================================================
-- Flyway Migration: V1__rename_tablas_y_columnas.sql
-- Descripción: Alinea nombres de tablas y columnas de la BD
--              con los @Table / @Column de las clases Java.
-- Motor: MySQL 8.0+ (usa la sintaxis RENAME COLUMN)
-- =============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- -------------------------------------------------------------
-- 1. user  →  usuario
--    username   → nombre_usuario
--    contraseña → contrasena
--    role       → rol
-- -------------------------------------------------------------
RENAME TABLE `user` TO `usuario`;

ALTER TABLE `usuario`
    RENAME COLUMN `username`    TO `nombre_usuario`,
    RENAME COLUMN `contraseña`  TO `contrasena`,
    RENAME COLUMN `role`        TO `rol`;

-- -------------------------------------------------------------
-- 2. invoice  →  factura
--    id_invoice → numero
--    issued_at  → emision
--    (order_id permanece igual: @JoinColumn name="order_id")
-- -------------------------------------------------------------
RENAME TABLE `invoice` TO `factura`;

ALTER TABLE `factura`
    RENAME COLUMN `id_invoice` TO `numero`,
    RENAME COLUMN `issued_at`  TO `emision`;

-- -------------------------------------------------------------
-- 3. pedido  →  pedido  (mismo nombre de tabla)
--    id             → numero
--    client_name    → nombre_cliente
--    order_date     → fecha_hora
--    delivered_at   → hora_entrega
--    time_estimated → tiempo_estimado
--    status         → estado
-- -------------------------------------------------------------
ALTER TABLE `pedido`
    RENAME COLUMN `id`             TO `numero`,
    RENAME COLUMN `client_name`    TO `nombre_cliente`,
    RENAME COLUMN `order_date`     TO `fecha_hora`,
    RENAME COLUMN `delivered_at`   TO `hora_entrega`,
    RENAME COLUMN `time_estimated` TO `tiempo_estimado`,
    RENAME COLUMN `status`         TO `estado`;

-- -------------------------------------------------------------
-- 4. pizza  →  pizza  (mismo nombre de tabla)
--    name         → nombre
--    cooking_type → tipo_coccion
--    price        → precio
--    size         → tamano
--    (id, active, description sin @Column explícito → sin cambio)
-- -------------------------------------------------------------
ALTER TABLE `pizza`
    RENAME COLUMN `name`         TO `nombre`,
    RENAME COLUMN `cooking_type` TO `tipo_coccion`,
    RENAME COLUMN `price`        TO `precio`,
    RENAME COLUMN `size`         TO `tamano`;

-- -------------------------------------------------------------
-- 5. pizza_pedido  →  pizza_pedido  (mismo nombre de tabla)
--    amount     → cantidad
--    unit_price → precio_unit
--    (id, pizza_id, order_id sin cambio)
-- -------------------------------------------------------------
ALTER TABLE `pizza_pedido`
    RENAME COLUMN `amount`     TO `cantidad`,
    RENAME COLUMN `unit_price` TO `precio_unit`;

SET FOREIGN_KEY_CHECKS = 1;