package com.algaworks.algafood.api.v1.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.RestauranteApenasNomeDtoAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteBasicoDtoAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteDtoAssembler;
import com.algaworks.algafood.api.v1.controller.openapi.controller.RestauranteControllerOpenApi;
import com.algaworks.algafood.api.v1.disassembler.RestauranteInputDtoDisassembler;
import com.algaworks.algafood.api.v1.model.RestauranteApenasNomeDto;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoDto;
import com.algaworks.algafood.api.v1.model.RestauranteDto;
import com.algaworks.algafood.api.v1.model.input.RestauranteInputDto;
import com.algaworks.algafood.core.security.annotations.CheckSecurity;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.ValidacaoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/v1/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController implements RestauranteControllerOpenApi {

	@Autowired
	private RestauranteRepository restauranteRepository;
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	@Autowired
	private SmartValidator validator;
	@Autowired
	private RestauranteDtoAssembler restauranteDtoAssembler;
	@Autowired
	private RestauranteInputDtoDisassembler restauranteInputDtoDisassembler;
	@Autowired
	private RestauranteBasicoDtoAssembler restauranteBasicoModelAssembler;
	@Autowired
	private RestauranteApenasNomeDtoAssembler restauranteApenasNomeModelAssembler;   
	

	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	//@JsonView(RestauranteView.Resumo.class)
	public CollectionModel<RestauranteBasicoDto> listar() {
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		return restauranteBasicoModelAssembler.toCollectionModel(restaurantes);
	}
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping(params = "projecao=apenas-nome")
	//@JsonView(RestauranteView.ApenasNome.class)
	public CollectionModel<RestauranteApenasNomeDto> listarApenasNome() {

		return restauranteApenasNomeModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}

	@CheckSecurity.Restaurantes.PodeConsultar
	@Override
	@GetMapping("/{restauranteId}")
	public RestauranteDto buscar(@PathVariable("restauranteId") Long id) {
		var restaurante = cadastroRestaurante.buscarOuFalhar(id);
		
		return restauranteDtoAssembler.toModel(restaurante);
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteDto criar(@RequestBody @Valid RestauranteInputDto restauranteInputDto) {
		
		try {
			Restaurante restaurante = restauranteInputDtoDisassembler.restauranteInputDtoToRestaurante(restauranteInputDto);
			restaurante = cadastroRestaurante.salvar(restaurante);
			return restauranteDtoAssembler.toModel(restaurante);
			
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
		
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@Override
	@DeleteMapping("/{restauranteId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable("restauranteId") Long id) {
		cadastroRestaurante.excluir(id);
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@Override
	@PutMapping("/{restauranteId}")
	public RestauranteDto editar(@PathVariable("restauranteId") Long restauranteId, @Valid @RequestBody RestauranteInputDto restauranteInputDto) {
		try {	
			Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
			
			restauranteInputDtoDisassembler.copyToDomainObject(restauranteInputDto, restauranteAtual);
			
			restauranteAtual = cadastroRestaurante.salvar(restauranteAtual);
			
			return restauranteDtoAssembler.toModel(restauranteAtual);
			
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@Deprecated
	@PatchMapping("/{restauranteId}")
	public RestauranteDto editarParcialmente(@PathVariable("restauranteId") Long id,
			@RequestBody Map<String, Object> campos, HttpServletRequest request) {
		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(id);

		merge(campos, restauranteAtual, request);
		validate(restauranteAtual, "restaurante");
		
		cadastroRestaurante.editar(restauranteAtual, id);

		return restauranteDtoAssembler.toModel(restauranteAtual);
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@Override
	@PutMapping("/{restauranteId}/ativo")
	public ResponseEntity<Void> ativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.ativar(restauranteId);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@Override
	@DeleteMapping("/{restauranteId}/ativo")
	public ResponseEntity<Void> inativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.inativar(restauranteId);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@Override
	@PutMapping("/ativacoes")
	public ResponseEntity<Void> ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		cadastroRestaurante.ativar(restauranteIds);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@Override
	@DeleteMapping("/ativacoes")
	public ResponseEntity<Void> inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		cadastroRestaurante.inativar(restauranteIds);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@Override
	@PutMapping("/{restauranteId}/fechamento")
	public ResponseEntity<Void> fechar(@PathVariable Long restauranteId) {
		cadastroRestaurante.fechar(restauranteId);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@Override
	@PutMapping("/{restauranteId}/abertura")
	public ResponseEntity<Void> abrir(@PathVariable Long restauranteId) {
		cadastroRestaurante.abrir(restauranteId);
		
		return ResponseEntity.noContent().build();
	}

	private void validate(Restaurante restaurante, String objectName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
		
		validator.validate(restaurante, bindingResult);
		
		if(bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
		
	}

	private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		
		ServletServerHttpRequest serverHttpRequest =  new ServletServerHttpRequest(request);
		
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
			Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);
			System.out.println(restauranteOrigem);
			
			camposOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
				System.out.println(nomePropriedade);
				
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
			
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}
	
	///JSON View Exemplo.
	
//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {
//
//		List<Restaurante> restaurantes = restauranteRepository.findAll();
//		List<RestauranteDto> restaurantesDto = restauranteDtoAssembler.restaurantesToListRestauranteDto(restaurantes);
//
//		MappingJacksonValue restauranteWrapper = new MappingJacksonValue(restaurantesDto);
//		
//		restauranteWrapper.setSerializationView(RestauranteView.Resumo.class);
//		
//		if("apenas-nome".equals(projecao)) {
//			restauranteWrapper.setSerializationView(RestauranteView.ApenasNome.class);			
//		}else if("completo".equals(projecao)) {
//			restauranteWrapper.setSerializationView(null);			
//		}
//		
//		
//		return restauranteWrapper;
//	}
//	@GetMapping
//	public List<RestauranteDto> listar() {
//
//		List<Restaurante> restaurantes = restauranteRepository.findAll();
//
//		return restauranteDtoAssembler.restaurantesToListRestauranteDto(restaurantes);
//	}
//	
	
}



