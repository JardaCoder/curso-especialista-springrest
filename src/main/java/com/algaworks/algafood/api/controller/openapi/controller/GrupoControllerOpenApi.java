package com.algaworks.algafood.api.controller.openapi.controller;

import java.util.List;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.GrupoDto;
import com.algaworks.algafood.api.model.input.GrupoInputDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Grupos")
public interface GrupoControllerOpenApi {

	@ApiOperation("Lista os grupos")
	List<GrupoDto> listar();

	@ApiOperation("Busca um grupo por ID")
	@ApiResponses({
		@ApiResponse(code = 400, message = "ID do grupo inválido", response = Problem.class),
		@ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
	})
	GrupoDto buscar(@ApiParam(value = "ID de um grupo", example = "1", required = true) Long id);

	@ApiOperation("Cria um grupo")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Grupo criado"),
	})
	GrupoDto criar(@ApiParam(name = "Corpo", value = "Representação de um novo grupo", required = true) GrupoInputDto grupoInputDto);

	@ApiOperation("Atualiza um grupo por ID")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Grupo Atualizado"),
		@ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
	})
	GrupoDto editar(@ApiParam(value = "ID de um grupo", example = "1", required = true) Long id,
			@ApiParam(name = "Corpo", value = "Representação de um grupo com os novos dados", required = true) GrupoInputDto grupoInputDto);

	
	@ApiOperation("Exclui um grupo por ID")
	@ApiResponses({
		@ApiResponse(code = 204, message = "Grupo excluído"),
		@ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
	})
	void remover(@ApiParam(value = "ID de um grupo", example = "1", required = true) Long id);

}