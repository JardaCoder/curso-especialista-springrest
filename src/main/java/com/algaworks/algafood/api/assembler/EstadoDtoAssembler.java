package com.algaworks.algafood.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.EstadoController;
import com.algaworks.algafood.api.model.EstadoDto;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoDtoAssembler extends RepresentationModelAssemblerSupport<Estado, EstadoDto> {
	
	public EstadoDtoAssembler() {
		super(EstadoController.class, EstadoDto.class);
	}

	@Autowired
	private ModelMapper modelMapper;
	
	public EstadoDto toModel(Estado estado) {
			
		EstadoDto estadoDto = createModelWithId(estado.getId(), estado);
		
		modelMapper.map(estado, estadoDto);
		
		estadoDto.add(linkTo(methodOn(EstadoController.class)
				.listar())
				.withRel("estados"));
	
		
		return estadoDto;
	}
	
	@Override
	public CollectionModel<EstadoDto> toCollectionModel(Iterable<? extends Estado> entities) {
		return super.toCollectionModel(entities)
				.add(linkTo(EstadoController.class).withSelfRel());
	}
}
