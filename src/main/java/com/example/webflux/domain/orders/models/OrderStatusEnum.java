package com.example.webflux.domain.orders.models;

public enum OrderStatusEnum {
    PROGRESS, APPROVED, ISSUED, CONFIRMED, PARTIALLY, RECEIVED, INVOICED, CLOSED, FAILED, CANCELLED
}

/**
 * DIFERENTES ESTADOS DE ORDEN DE COMPRA
 * 
 * - PROGRESS: La orden de compra en progreso (verificaciones de stock , precio
 * etc etc)
 * 
 * - APPROVED: La orden de compra aprobada, cuando se ha cumplido todos los
 * requerimientos para la compra
 * 
 * - ISSUE: La orden de compra ha sido remitida al vendedor y se espera
 * confirmacion del mismo
 * 
 * - CONFIRMED: El proveedor ha recibido la orden y aceptado los términos
 * (precio, plazos, cantidad).
 * 
 * - PARTIALLY: Se ha recibido una parte de los productos solicitados.
 * 
 * - RECEIVED: Todos los bienes o servicios han sido entregados y aceptados
 * satisfactoriamente por el comprador.
 * 
 * - INVOICED: El proveedor ha emitido la factura correspondiente y coincide con
 * la orden.
 * 
 * - CLOSED: El proceso ha finalizado, el producto fue recibido y la factura
 * pagada. No quedan cantidades pendientes.
 * 
 * - CANCELLED: La orden se ha anulado antes de su cumplimiento final.
 * 
 * - FAILED: Error en el enrutamiento o envío electrónico de la orden al
 * proveedor.
 */
