import { EquipamentoOpcional } from './../reservas-equipamento';
import { ClientesService } from './../../clientes/clientes.service';
import { EquipamentosService } from './../../equipamentos/equipamentos.service';
import { SalasService } from './../../salas/salas.service';
import { ReservasComponent } from './../reservas.component';
import { ReservasService } from './../reservas.service';
import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Reserva } from '../reserva';
import { Sala } from 'src/app/salas/sala';
import { Cliente } from 'src/app/clientes/models/cliente.interface';
import { Equipamento } from 'src/app/equipamentos/models/equipamento';
import { ValueConverter } from '@angular/compiler/src/render3/view/template';
import { debounce } from '@fullcalendar/core';

@Component({
  selector: 'app-reservas-form',
  templateUrl: './reservas-form.component.html',
  styleUrls: ['./reservas-form.component.css']
})
export class ReservasFormComponent implements OnInit {

  @Output() criarReservaEvento = new EventEmitter();

  formulario: FormGroup;
  formulario2: FormGroup;

  controlDrop = new FormControl();

  reserva: Reserva;

  equipamentos: FormArray;
  equipamentoOpcionais = [];
  equipamentosOpcionaisNew: EquipamentoOpcional[] = []; 

  salas = [];
  salaSelecionada: Sala;

  clientes: any[];
  clienteSelecionado: Cliente;

  mostrarLista: boolean = false;

  @Input() set reservaId(id: number){
    if(id)  {
      this.buscarReservaPorId(id);
      console.log("ID " + id);
      this.mostrarLista = true;
      console.log(this.mostrarLista);
    }
  }

  constructor(private reservasService: ReservasService, private formBuilder: FormBuilder
    , private salasService: SalasService, private equipamentosService: EquipamentosService
    , private clientesService: ClientesService) { }

  ngOnInit(): void {

    this.initForm();

    this.listarSalas();
    this.listarClientes();

    this.equipamentosOpcionaisNew.push(new EquipamentoOpcional());
  }

  private listarClientes() {
    this.clientesService.listar().subscribe((data) => {
      this.clientes = data.map(e => { return { label: e.cpf, value: e.id } });
      console.log(this.clientes);
    }, err => {
      console.log(err);
    });
  }

  private listarSalas() {
    this.salasService.listarSalas().subscribe((data) => {
      this.salas = data.map(e => { return { label: e.descricao, value: e.id } });
      console.log(this.salas);
    }, err => {
      console.log(err);
    });
  }

  atualizarForm() {
    var equipamentos = this.equipamentosOpcionaisNew.map(e => {return { idReserva: null, idEquipamento: e.idEquipamento, quantidade: e.quantidade }});

    this.formulario.get('idCliente').setValue(this.reserva.idCliente);
    this.formulario.get('idSala').setValue(this.reserva.idSala);
    this.formulario.get('dataInicio').setValue(this.reserva.dataInicio);
    this.formulario.get('dataFim').setValue(this.reserva.dataFim);
    this.formulario.get('total').setValue(this.reserva.total);
    // this.formulario.get('equipamentos').patchValue(equipamentos);

    console.log("AAAAAAAAA", this.reserva);
    console.log("BBBBBBBBB", this.formulario.value);

  }

  private initForm() {
    this.formulario = this.formBuilder.group({
      idSala: new FormControl(),
      idCliente: new FormControl(),
      dataInicio: new FormControl(),
      dataFim: new FormControl(),
      total: new FormControl()
    })
  }

  private buscarReservaPorId(id: number){
    this.reservasService.getByid(id).subscribe((data)=>{
      this.reserva = data;
      this.atualizarForm();
      console.log(data);
      this.equipamentosOpcionaisNew = this.reserva.equipamentos;
      console.log(this.equipamentosOpcionaisNew);
    }, err =>{
      console.log(err);
    });

  }

  addEquipamento(){
    this.equipamentosOpcionaisNew.push(new EquipamentoOpcional());
  }

  removerEquipamento(index: number){
    this.equipamentosOpcionaisNew.splice(index, 1);
  }

  clear(){
    this.equipamentos.clear();
  }

  criarEquipamento(){
    return this.formBuilder.group({
      idReserva: null,
      idEquipamento: new FormControl(),
      quantidade: new FormControl()
    })
  }

  onSubmit(){
    if(this.formulario.valid){
      if(this.reserva.id){
        console.log('sala editada' , this.reserva);

        this.reservasService.atualizarReserva(this.construirObjetoReserva()).subscribe(
          success => {
            console.log('PUT' + this.reserva)},
          error => console.error(error),
          () => {console.log('request completo')
         debounce(this.criarReservaEvento.emit(), 2)
        });
     
      }
      else{
       console.log('nova sala', this.construirObjetoReserva());
       
        this.reservasService.salvar(this.construirObjetoReserva()).subscribe(
          success => {
            console.log('POST')},
          error => console.error(error),
          () => {console.log('request completo')
         debounce(this.criarReservaEvento.emit(), 2)
        });
     
      }
      this.formulario.reset();
      //window.location.reload();
    }
  }

  listarOpcionais() {

    this.salasService.listarEquipamentoOpcionais(this.formulario.value.idSala).subscribe((dado) => {
      this.equipamentoOpcionais = dado.map(e => { return { label: e.nome, value: e.id } });
      console.log(this.equipamentoOpcionais, "equipamentos");

    }, err => {
      console.log('erro', err);
    });
  }

  valorTotal() {
    this.reserva = {
      ...this.formulario.value,
    }
    var lista = this.formulario.value.equipamentos;
    if(lista != null){
      this.reserva.equipamentos = []
      lista.forEach(element => {
        this.reserva.equipamentos.push(element.value);
      });
    }
    console.log(this.formulario.value);
    
    this.getTotal(this.reserva);
  }
  getTotal(reserva: Reserva) {
    console.log(reserva);
    this.reservasService.getTotal(reserva).subscribe((dado) => {
      this.formulario.patchValue({ total: dado.total });
    });
  }


  construirObjetoReserva(): Reserva {
    const reserva = new Reserva();
    reserva.id = this.reserva.id? this.reserva.id : null;
    reserva.idSala = this.formulario.get('capacidade').value;
    reserva.idCliente = this.formulario.get('descricao').value;
    reserva.dataInicio = this.formulario.get('idTiporeserva').value;
    reserva.dataFim = this.formulario.get('precoDiario').value;
    reserva.total = this.formulario.get('precoDiario').value;

    reserva.equipamentos = this.equipamentosOpcionaisNew.map(item=>{
      console.log('item lista', item);
      
      if(item.idEquipamento && item.quantidade){
        return item;
      }
      });
    return reserva;
  }
  
  alterarIdEquipamento(idEquipamento: number, index: number){
    this.equipamentosOpcionaisNew[index].idReserva = null;
    this.equipamentosOpcionaisNew[index].idEquipamento = idEquipamento;
  }

  alterarQuantidade(quantidade: number, index: number){
    console.log(quantidade, index);
    this.equipamentosOpcionaisNew[index].quantidade = Number(quantidade);
  }

  getNomeEquipamento(idEquipamento: number): string {
    let item = this.equipamentoOpcionais.find(i => i.value === idEquipamento);
    return item ? item.label : '';
  }

}