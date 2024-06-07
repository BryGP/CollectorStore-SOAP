package com.itq.VentasColeccionables;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import producto.com.Producto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class ProductoRepository {
    private static final Map<Integer, Producto> productos = new HashMap<>();
    private static final Logger logger = Logger.getLogger(ProductoRepository.class.getName());

    @PostConstruct
    public void initData() {
        // Inicializamos los productos
        Producto producto1 = new Producto();
        producto1.setId(1);
        producto1.setName("Camiseta");
        producto1.setDescription("Una camiseta de algodón de alta calidad");
        producto1.setPrecio(20.0);
        producto1.setNumeroserie(12345);
        producto1.setInventarios(10);
        producto1.setDescuentos(0);

        // Agregamos el primer producto
        addProducto(producto1);
    }

    public Producto findProducto(int id) {
        Assert.isTrue(id > 0, "El ID del producto debe ser mayor que cero");
        Assert.isTrue(productos.containsKey(id), "El ID del producto no existe, verifica la información");

        return productos.get(id);
    }

    public List<Producto> getAllProductos() {
        return new ArrayList<>(productos.values());
    }

    public void addProducto(@Validated Producto producto) {
        Assert.notNull(producto, "El producto no puede ser nulo");
        Assert.isTrue(producto.getId() > 0, "El ID del producto debe ser mayor que cero");
        Assert.isTrue(!productos.containsKey(producto.getId()), "El ID del producto ya está en uso");

        productos.put(producto.getId(), producto);
        logger.info("Producto agregado: " + producto.getId());
    }

    public void verificarProducto(@Validated Producto producto) {
        // Iterar sobre los atributos del producto
        for (Field field : producto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(producto);
                // Verificar si el valor comienza con "?"
                if (value instanceof String && ((String) value).startsWith("?")) {
                    throw new IllegalArgumentException("El campo " + field.getName() + " del producto no puede comenzar con '?'");
                }
            } catch (IllegalAccessException e) {
                // Manejar la excepción si se produce un error al acceder al campo
                e.printStackTrace();
            }
        }
    
        // Verificar cada atributo del producto
        if (producto.getId() <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser mayor que cero");
        }
        if (producto.getName() == null || producto.getName().isEmpty() || producto.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del producto no puede estar vacío o ser nulo");
        }
        if (producto.getDescription() == null || producto.getDescription().isEmpty() || producto.getDescription().isBlank()) {
            throw new IllegalArgumentException(
                    "La descripción del producto no puede estar vacía o ser nula");
        }
        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor que cero");
        }
        if (producto.getNumeroserie() <= 0) {
            throw new IllegalArgumentException(
                    "El número de serie del producto no puede estar vacío o ser nulo");
        }
        if (producto.getInventarios() < 0) {
            throw new IllegalArgumentException("El inventario del producto no puede ser negativo");
        }
        if (producto.getDescuentos() < 0) {
            throw new IllegalArgumentException("Los descuentos del producto no pueden ser negativos");
        }
    }
    

    public Producto modificarProducto(Producto productoModificado) {
        Assert.notNull(productoModificado, "El producto modificado no puede ser nulo");
        Assert.isTrue(productoModificado.getId() > 0, "El ID del producto debe ser mayor que cero");
        Assert.isTrue(productos.containsKey(productoModificado.getId()), "El ID del producto no existe");
    
        // Obtener el ID del producto modificado
        int id = productoModificado.getId();
    
        // Actualizar el producto en el mapa de productos
        productos.put(id, productoModificado);
    
        logger.info("Producto modificado: " + id);
    
        return productoModificado;
    }

    public void deleteProducto(int id) {
        Assert.isTrue(id > 0, "El ID del producto debe ser mayor que cero");
        productos.remove(id);
        logger.info("Producto eliminado: " + id);
    }
}
