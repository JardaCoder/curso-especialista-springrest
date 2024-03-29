package com.algaworks.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.v1.model.EnderecoDto;
import com.algaworks.algafood.api.v1.model.input.ItemPedidoInputDto;
import com.algaworks.algafood.api.v2.model.input.CidadeInputDtoV2;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.ItemPedido;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		
		modelMapper.createTypeMap(CidadeInputDtoV2.class, Cidade.class)
	    	.addMappings(mapper -> mapper.skip(Cidade::setId));  
		
		modelMapper.createTypeMap(Endereco.class, EnderecoDto.class)
			.<String>addMapping(endereco -> endereco.getCidade().getEstado().getNome(), 
					(dest, value) -> dest.getCidade().setEstado(value));
		
		modelMapper.createTypeMap(ItemPedidoInputDto.class, ItemPedido.class)
	    	.addMappings(mapper -> mapper.skip(ItemPedido::setId));  
		
		return modelMapper;
	}
}
