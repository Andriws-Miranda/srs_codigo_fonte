package com.basis.srs.web.rest;



import com.basis.srs.servico.Tipo_equipamentoServico;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.basis.srs.dominio.TipoEquipamento;
import java.util.List;

@RestController
@RequestMapping("/api/tipo-equipamentos")
@RequiredArgsConstructor
public class Tipo_equipamentoRecurso {
    private Tipo_equipamentoServico tipo_equipamentoServico;

    @GetMapping
    public List<TipoEquipamento> obter(){
        return null;
    }
    @GetMapping(path = "/{id}")
    public TipoEquipamento obterPorId(@PathVariable Integer id){
        return null;
    }
    @PostMapping
    public TipoEquipamento cadastrar(@RequestBody TipoEquipamento tipo_equipamento){
        return null;
    }
    @PutMapping
    public TipoEquipamento atualizar(@RequestBody TipoEquipamento tipo_equipamento){
        return null;
    }
    @DeleteMapping(path = "/{id}")
    public TipoEquipamento deletar(@PathVariable Integer id){
        return null;
    }
}
