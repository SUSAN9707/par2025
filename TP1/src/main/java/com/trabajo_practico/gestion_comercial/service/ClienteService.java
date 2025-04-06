package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.dto.ClienteDTO;
import com.trabajo_practico.gestion_comercial.dto.CreateUpdateClienteDTO;
import com.trabajo_practico.gestion_comercial.model.Cliente;
import com.trabajo_practico.gestion_comercial.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteDTO crearCliente(CreateUpdateClienteDTO createUpdateClienteDTO) {
        var cliente= generateCliente(createUpdateClienteDTO);
        return new ClienteDTO(clienteRepository.save(cliente));
    }

    public List<ClienteDTO> obtenerTodos() {
        return clienteRepository.findAll().stream().map(ClienteDTO::new).collect(Collectors.toList());
    }

    public Optional<ClienteDTO> obtenerPorId(Long id) {
        var cliente= clienteRepository.findById(id);
        return cliente.map(ClienteDTO::new);
    }

    public ClienteDTO actualizarCliente(Long id, CreateUpdateClienteDTO updateClienteDTO) {
        var cliente = generateCliente(updateClienteDTO);
        if (clienteRepository.existsById(id)) {
            cliente.setId(id);
            return new ClienteDTO(clienteRepository.save(cliente));
        }
        return null;
    }

    public boolean eliminarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
    private Cliente generateCliente(CreateUpdateClienteDTO updateClienteDTO) {
        var cliente = new Cliente();
        cliente.setNombre(updateClienteDTO.getNombre());
        cliente.setApellido(updateClienteDTO.getApellido());
        cliente.setRuc(updateClienteDTO.getRuc());
        cliente.setDireccion(updateClienteDTO.getDireccion());
        return cliente;
    }
}
