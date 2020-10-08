package com.basis.srs.web.rest;


import com.basis.srs.builder.EquipamentoBuilder;
import com.basis.srs.dominio.Equipamento;
import com.basis.srs.util.IntTestComum;
import com.basis.srs.util.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Transactional
public class EquipamentoRecursoIT extends IntTestComum {

    @Autowired
    private EquipamentoBuilder equipamentoBuilder;

    @BeforeEach
    public void limparBanco(){
        equipamentoBuilder.limparBanco();
    }

    @Test
    public void salvar() throws Exception {
        //salva sem persistir no banco
        Equipamento equipamento = equipamentoBuilder.construirEntidade();
        getMockMvc().perform(post("/api/equipamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(
                    equipamentoBuilder.construirToDto(equipamento))))
            .andExpect(status().isCreated());

    }

    @Test
    public void buscar() throws Exception{
        Equipamento equipamento = equipamentoBuilder.construir();
        getMockMvc().perform(get("/api/equipamentos/" + equipamento.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equipamento.getId()));
    }

    @Test
    public void listar() throws Exception {
        equipamentoBuilder.construir();
        getMockMvc().perform(get("/api/equipamentos"))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasSize(1)));
    }

    @Test
    public void editar() throws Exception {
        Equipamento equipamento = equipamentoBuilder.construir();
        getMockMvc().perform(put("/api/equipamentos").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(equipamentoBuilder.construirToDto(equipamento))))
                .andExpect((status().isOk()));
        ;
    }

    @Test
    public void deletar() throws Exception {
        Equipamento equipamento = equipamentoBuilder.construir();
        getMockMvc().perform(delete("/api/equipamentos/" + equipamento.getId()))
                .andExpect(status().isOk());
    }
}