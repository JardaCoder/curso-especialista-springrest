package com.algaworks.algafood.api.v1.model.mixin;

import java.util.ArrayList;
import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ExampleMixin {

	@JsonIgnore
	private List<Restaurante> restaurantes = new ArrayList<Restaurante>();

}
