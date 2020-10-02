package com.basis.srs.repositorio;

import com.basis.srs.dominio.TipoEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Tipo_equipamentoRepositorio extends JpaRepository<TipoEquipamento,Integer> {
}
