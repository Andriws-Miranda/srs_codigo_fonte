package com.basis.srs.servico;

import com.basis.srs.dominio.Reserva;
import com.basis.srs.repositorio.ReservaRepositorio;
import com.basis.srs.servico.dto.ReservaDTO;
import com.basis.srs.servico.exception.RegraNegocioException;
import com.basis.srs.servico.mapper.ReservaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaServico {

    private final ReservaRepositorio reservaRepositorio;
    private final ReservaMapper reservaMapper;

    public List<ReservaDTO> listar(){
        List<ReservaDTO> reserva = reservaMapper.toDto(reservaRepositorio.findAll());
        return reserva;
    }

    public ReservaDTO buscar(Integer id){
        Reserva reserva = reservaRepositorio.findById(id)
                .orElseThrow(()-> new RegraNegocioException("Reserva não encontrada"));
        ReservaDTO reservaDTO = reservaMapper.toDto(reserva);
        return reservaDTO;
    }

    public ReservaDTO criar(ReservaDTO reserva){

        Reserva novaReserva = reservaMapper.toEntity(reserva);
        for (Reserva reservaLista:reservaRepositorio.findAll()){
            if (reservaLista.getSala() == novaReserva.getSala()
                    && (reservaLista.getDataInicio() == novaReserva.getDataInicio()
                    || reservaLista.getDataFim() == novaReserva.getDataFim()
                    || !reservaLista.getDataFim().isAfter(novaReserva.getDataInicio()))) {
                throw new RegraNegocioException("Reserva ja existe");
            }
        }
        novaReserva = reservaRepositorio.save(novaReserva);
        ReservaDTO reservaDTO = reservaMapper.toDto(novaReserva);
        return reservaDTO;
    }

    public void deletar(Integer id){
        buscar(id);
        reservaRepositorio.deleteById(id);
    }

}
