package com.itq.VentasColeccionables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import java.util.List;

import producto.com.*;

@Endpoint
public class CollecionablesEndpoint {
    private static final String NAMESPACE_URI = "http://com.Producto";

    private ProductoRepository productoRepository;

    @Autowired
    public CollecionablesEndpoint(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductoRequest")
    @ResponsePayload
    public GetProductoResponse VentaCliente(@Validated @RequestPayload GetProductoRequest request) {
        GetProductoResponse response = new GetProductoResponse();
        try {
            response.setProducto(productoRepository.findProducto(request.getId()));
            LogToFile.getLogger()
                    .info("Se obtuvo informacion del siguiente producto: " + request.getId());

        } catch (Exception e) {
            // Manejar la excepción
            LogToFile.getLogger().warning("Error al obtener el producto con ID: " + request.getId()
                    + ". Ocurrio la siguiente excepcion: " + e.getMessage());

            response.setMessage("No se pudo obtener el producto.");
            response.setProducto(null);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createProductoRequest")
    @ResponsePayload
    public CreateProductoResponse crearProducto(@Validated @RequestPayload CreateProductoRequest request) {
        CreateProductoResponse response = new CreateProductoResponse();

        try {
            CreateProductoRequest.Producto productoRequest = request.getProducto();

            // Convertir el objeto de CreateProductoRequest.Producto a Producto
            Producto producto = new Producto();
            producto.setId(productoRequest.getId());
            producto.setName(productoRequest.getName());
            producto.setDescription(productoRequest.getDescription());
            producto.setPrecio(productoRequest.getPrecio());
            producto.setNumeroserie(productoRequest.getNumeroserie());
            producto.setInventarios(productoRequest.getInventarios());
            producto.setDescuentos(productoRequest.getDescuentos());

            // Verificar el producto antes de agregarlo
            productoRepository.verificarProducto(producto);

            // Agregar el producto al repositorio
            productoRepository.addProducto(producto);

            response.setSuccess(true);
            LogToFile.getLogger().info("Objeto agregado de manera correcta " + producto.getId());
            response.setMessage("Producto creado exitosamente");

        } catch (Exception e) {
            // Manejar la excepción
            response.setSuccess(false);
            LogToFile.getLogger()
                    .warning("Error al crear el producto. Ocurrio la siguiente excepcion: " + e.getMessage());
            response.setMessage("Error al crear el producto, " + e.getMessage());
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllProductosRequest")
    @ResponsePayload
    public GetAllProductosResponse obtenerTodosLosProductos(@RequestPayload GetAllProductosRequest request) {
        GetAllProductosResponse response = new GetAllProductosResponse();
        try {
            List<Producto> productos = productoRepository.getAllProductos();
            response.getProductos().addAll(productos);
            LogToFile.getLogger()
                .info("Se obtuvo informacion de todos los productos.");
        } catch (Exception e) {
            // Manejar la excepción
            LogToFile.getLogger()
                .warning("Error al obtener todos los productos: " + e.getMessage());
            response.setMessage("No se pudo obtener la lista de productos.");
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllProductosRequest")
    @ResponsePayload
    public GetAllProductosResponse obtenerTodosLosProductos() {
        GetAllProductosResponse response = new GetAllProductosResponse();
        try {
            List<Producto> productos = productoRepository.getAllProductos();
            response.getProductos().addAll(productos);
            LogToFile.getLogger()
                .info("Se obtuvo información de todos los productos.");
        } catch (Exception e) {
            // Manejar la excepción
            LogToFile.getLogger()
                .warning("Error al obtener todos los productos: " + e.getMessage());
            response.setMessage("No se pudo obtener la lista de productos.");
        }
        return response;
    }
    

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteProductoRequest")
    @ResponsePayload
    public DeleteProductoResponse eliminarProducto(@Validated @RequestPayload DeleteProductoRequest request) {
        DeleteProductoResponse response = new DeleteProductoResponse();
        int id = request.getId();

        try {
            productoRepository.deleteProducto(id);
            response.setSuccess(true); // Indicar que la operación fue exitosa
            LogToFile.getLogger().info("Se ha eliminado exitosamente el producto con ID: " + id);
            response.setMessage("Producto borrado exitosamente");
        } catch (Exception e) {
            response.setSuccess(false); // Indicar que la operación falló
            LogToFile.getLogger()
                    .warning("No se pudo eliminar el producto con ID: " + id + ". Ocurrio la siguiente excepcion: "
                            + e.getMessage());
            response.setMessage("Error al eliminar el producto");
        }

        return response;
    }
}
