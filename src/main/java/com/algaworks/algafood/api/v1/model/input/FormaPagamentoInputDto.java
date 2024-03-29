package com.algaworks.algafood.api.v1.model.input;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormaPagamentoInputDto {
	
	
	@ApiModelProperty(example = "Cartão de crédito", required = true)
	@NotBlank
	private String descricao;

}
