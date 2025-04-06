package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.dto.ClienteDTO;
import com.trabajo_practico.gestion_comercial.dto.CreateUpdateClienteDTO;
import com.trabajo_practico.gestion_comercial.dto.CreateUpdateReporteDTO;
import com.trabajo_practico.gestion_comercial.model.Cliente;
import com.trabajo_practico.gestion_comercial.service.ClienteService;
import com.trabajo_practico.gestion_comercial.dto.ApiResponse;
import com.trabajo_practico.gestion_comercial.exception.ResourceNotFoundException;
import org.aspectj.weaver.ast.Var;
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
    public ResponseEntity<ApiResponse<ClienteDTO>> crearCliente(@RequestBody CreateUpdateClienteDTO cliente) {
        var nuevoCliente = clienteService.crearCliente(cliente);
        return ResponseEntity.ok(new ApiResponse<>("Cliente creado exitosamente", HttpStatus.OK.value(), nuevoCliente));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteDTO>>> obtenerTodos() {
        var clientes = clienteService.obtenerTodos();
        return ResponseEntity.ok(new ApiResponse<>("Clientes obtenidos correctamente", HttpStatus.OK.value(), clientes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> obtenerPorId(@PathVariable Long id) {
        Optional<ClienteDTO> cliente = clienteService.obtenerPorId(id);
        return cliente
                .map(c -> ResponseEntity.ok(new ApiResponse<>("Cliente encontrado", HttpStatus.OK.value(), c)))
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con ID " + id + " no encontrado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> actualizarCliente(@PathVariable Long id, @RequestBody CreateUpdateClienteDTO clienteActualizado) {
        var cliente = clienteService.actualizarCliente(id, clienteActualizado);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente con ID " + id + " no encontrado");
        }
        return ResponseEntity.ok(new ApiResponse<>("Cliente actualizado correctamente", HttpStatus.OK.value(), cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCliente(@PathVariable Long id) {
        boolean eliminado = clienteService.eliminarCliente(id);
        if (!eliminado) {
            throw new ResourceNotFoundException("Cliente con ID " + id + " no encontrado");
        }
        return ResponseEntity.ok(new ApiResponse<>("Cliente eliminado correctamente", HttpStatus.OK.value(), null));
    }
}
