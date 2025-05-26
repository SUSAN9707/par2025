package com.trabajo_practico.gestion_comercial.service.impl;

import com.trabajo_practico.gestion_comercial.dto.MovimientoMensualDTO;
import com.trabajo_practico.gestion_comercial.model.Venta;
import com.trabajo_practico.gestion_comercial.model.Compra;
import com.trabajo_practico.gestion_comercial.repository.VentaRepository;
import com.trabajo_practico.gestion_comercial.repository.CompraRepository;
import com.trabajo_practico.gestion_comercial.service.ReporteService;
import com.trabajo_practico.gestion_comercial.service.ReporteServiceG;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteServiceG {

    private final VentaRepository ventaRepository;
    private final CompraRepository compraRepository;

    public ReporteServiceImpl(VentaRepository ventaRepository, CompraRepository compraRepository) {
        this.ventaRepository = ventaRepository;
        this.compraRepository = compraRepository;
    }

    @Override
    public MovimientoMensualDTO obtenerMovimientoMensual(Integer mes, Integer anio) {
        LocalDate hoy = LocalDate.now();
        int mesConsulta = (mes != null) ? mes : hoy.getMonthValue();
        int anioConsulta = (anio != null) ? anio : hoy.getYear();

        List<Venta> ventas = ventaRepository.findByMesYAnio(mesConsulta, anioConsulta);
        List<Compra> compras = compraRepository.findByMesYAnio(mesConsulta, anioConsulta);

        long cantidadVentas = ventas.size();
        double totalVentas = ventas.stream().mapToDouble(Venta::getMontoTotal).sum();

        long cantidadCompras = compras.size();
        double totalCompras = compras.stream().mapToDouble(Compra::getMontoTotal).sum();

        return new MovimientoMensualDTO(
                mesConsulta,
                cantidadVentas,
                totalVentas,
                cantidadCompras,
                totalCompras
        );
    }
}
