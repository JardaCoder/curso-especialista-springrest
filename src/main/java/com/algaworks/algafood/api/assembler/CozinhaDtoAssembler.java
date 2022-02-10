package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.CozinhaDto;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaDtoAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public CozinhaDto cozinhaToCozinhaDto(Cozinha cozinha) { 
		return modelMapper.map(cozinha, CozinhaDto.class);
	}
	
	public List<CozinhaDto> cozinhasToListCozinhaDto(List<Cozinha> cozinhas) {
		return cozinhas.stream().map(this::cozinhaToCozinhaDto).collect(Collectors.toList());
	}
	
}