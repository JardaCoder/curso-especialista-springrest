package com.algaworks.algafood.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.CozinhaController;
import com.algaworks.algafood.api.model.CozinhaDto;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaDtoAssembler extends RepresentationModelAssemblerSupport<Cozinha, CozinhaDto> {
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public CozinhaDtoAssembler() {
		super(CozinhaController.class, CozinhaDto.class);
	}
	
	public CozinhaDto toModel(Cozinha cozinha) { 
		
		CozinhaDto cozinhaDto = createModelWithId(cozinha.getId(), cozinha);
		
		modelMapper.map(cozinha, cozinhaDto);
		
		cozinhaDto.add(linkTo(CozinhaController.class).withRel("cozinhas"));
		
		return cozinhaDto;
	}
	
	public List<CozinhaDto> cozinhasToListCozinhaDto(List<Cozinha> cozinhas) {
		return cozinhas.stream().map(this::toModel).collect(Collectors.toList());
	}
	
}
