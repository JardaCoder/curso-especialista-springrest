package com.algaworks.algafood.api.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.ProdutoInputDto;
import com.algaworks.algafood.domain.model.Produto;

@Component
public class ProdutoInputDtoDisassembler {
	
	@Autowired
	private ModelMapper modelMapper;

	public Produto produtoInputDtoToProduto(ProdutoInputDto produtoInputDto) {
		return modelMapper.map(produtoInputDto, Produto.class);
	}
	
	public void copyToDomainObject(ProdutoInputDto produtoInputDto, Produto produto) {
		modelMapper.map(produtoInputDto, produto);
	}
}
