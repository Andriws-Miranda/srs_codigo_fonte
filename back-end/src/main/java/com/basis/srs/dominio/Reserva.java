package com.basis.srs.dominio;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reserva")
public class Reserva implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_reserva")
    @SequenceGenerator(name="sq_reserva", allocationSize = 1, sequenceName = "sq_reserva")
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sala")
    private Sala sala;

    @Column(name = "total")
    private Double total;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name= "data_fim")
    private LocalDateTime dataFim;

}
