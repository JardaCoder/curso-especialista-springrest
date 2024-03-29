package com.algaworks.algafood.api.v2.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("CidadeDto")
@Relation(collectionRelation = "cidades")
@Getter
@Setter
public class CidadeDtoV2 extends RepresentationModel<CidadeDtoV2>{

	//@ApiModelProperty(value = "ID da cidade", example = "1")
	@ApiModelProperty(example = "1")
	private Long idCidade;
	
	@ApiModelProperty( example = "Brusque")
	private String nomeCidade;
	
	private Long idEstado;
	
	private String nomeEstado;
	
	

}
