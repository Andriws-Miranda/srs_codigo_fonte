package com.basis.srs.servico;

import com.basis.srs.repositorio.EquipamentoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EquipamentoServico {

    private EquipamentoRepositorio equipamentoRepositorio;
}
