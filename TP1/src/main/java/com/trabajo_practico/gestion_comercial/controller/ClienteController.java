package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.model.Cliente;
import com.trabajo_practico.gestion_comercial.service.ClienteService;
import com.trabajo_practico.gestion_comercial.dto.ApiResponse;
import com.trabajo_practico.gestion_comercial.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/${api.version}/clientes")
public class ClienteController {

    @Autowired
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Cliente>> crearCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.crearCliente(cliente);
        return ResponseEntity.ok(new ApiResponse<>("Cliente creado exitosamente", HttpStatus.OK.value(), nuevoCliente));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Cliente>>> obtenerTodos() {
        List<Cliente> clientes = clienteService.obtenerTodos();
        return ResponseEntity.ok(new ApiResponse<>("Clientes obtenidos correctamente", HttpStatus.OK.value(), clientes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> obtenerPorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.obtenerPorId(id);
        return cliente
                .map(c -> ResponseEntity.ok(new ApiResponse<>("Cliente encontrado", HttpStatus.OK.value(), c)))
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con ID " + id + " no encontrado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> actualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteActualizado) {
        Cliente cliente = clienteService.actualizarCliente(id, clienteActualizado);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente con ID " + id + " no encontrado");
        }
        return ResponseEntity.ok(new ApiResponse<>("Cliente actualizado correctamente", HttpStatus.OK.value(), cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.ok(new ApiResponse<>("Cliente eliminado correctamente", HttpStatus.OK.value(), null));
    }
}
