package com.basis.srs.dominio;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity @Getter @Setter @Table(name = "tipo_equipamento")
public class TipoEquipamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column
    private String descricao;


}
