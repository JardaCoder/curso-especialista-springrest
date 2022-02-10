package com.algaworks.algafood.api.model.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarSenhaInputDto {
	
	@NotBlank
	private String senhaAtual;
	
	@NotBlank
	private String novaSenha;

}
