package com.algaworks.algafood.api.v1.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "pedidos")
@Getter
@Setter
public class PedidoDto extends RepresentationModel<PedidoDto> {

	@ApiModelProperty(example = "f9981ca4-5a5e-4da3-af04-933861df3e55", required = true)
	private String codigo;
	
	@ApiModelProperty(example = "200.00")
	private BigDecimal subtotal;
	
	@ApiModelProperty(example = "10.00")
	private BigDecimal taxaFrete;
	
	@ApiModelProperty(example = "210.00")
	private BigDecimal valorTotal;
	
	@ApiModelProperty(example = "CONFIRMADO")
	private String status;
	
	@ApiModelProperty(example = "2022-02-11T14:13:25Z")
	private OffsetDateTime dataCriacao;
	
	@ApiModelProperty(example = "2022-02-11T14:13:25Z")
	private OffsetDateTime dataConfirmacao;
	
	@ApiModelProperty(example = "2022-02-11T14:13:25Z")
	private OffsetDateTime dataEntrega;
	
	@ApiModelProperty(example = "2022-02-11T14:13:25Z")
	private OffsetDateTime dataCancelamento;
	
	private RestauranteApenasNomeDto restaurante;
	
	private UsuarioDto cliente;
	
	private FormaPagamentoDto formaPagamento;
	
	private EnderecoDto enderecoEntrega;
	
	private List<ItemPedidoDto> itens;
	
}
