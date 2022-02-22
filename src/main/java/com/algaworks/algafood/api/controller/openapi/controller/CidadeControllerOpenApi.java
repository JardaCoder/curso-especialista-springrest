package com.algaworks.algafood.api.controller.openapi.controller;

import java.util.List;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.CidadeDto;
import com.algaworks.algafood.api.model.input.CidadeInputDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Cidades")
public interface CidadeControllerOpenApi {

	

	@ApiOperation("Lista as cidades")
	List<CidadeDto> listar();

	@ApiOperation("Busca uma cidade por ID")
	@ApiResponses({
		@ApiResponse(code = 400, message = "ID da cidade inválido", response = Problem.class),
		@ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
	})
	CidadeDto buscar(@ApiParam(value = "ID de uma cidade", example = "1", required = true) Long id);

	@ApiOperation("Cria uma cidade")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Cidade criada"),
	})
	CidadeDto criar(@ApiParam(name = "Corpo", value = "Representação de uma nova cidade", required = true) CidadeInputDto cidadeInputDtp);

	@ApiOperation("Atualiza uma cidade por ID")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Cidade Atualizada"),
		@ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
	})
	CidadeDto editar(@ApiParam(value = "ID de uma cidade", example = "1", required = true) Long id,
			@ApiParam(name = "Corpo", value = "Representação de uma cidade com os novos dados", required = true) CidadeInputDto cidadeInputDto);

	
	@ApiOperation("Exclui uma cidade por ID")
	@ApiResponses({
		@ApiResponse(code = 204, message = "Cidade excluída"),
		@ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
	})
	void remover(@ApiParam(value = "ID de uma cidade", example = "1", required = true) Long id);

}