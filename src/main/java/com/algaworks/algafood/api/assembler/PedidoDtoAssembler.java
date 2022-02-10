package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.PedidoDto;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoDtoAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public PedidoDto pedidoToPedidoDto(Pedido pedido) {
		return modelMapper.map(pedido, PedidoDto.class);
	}
	
	public List<PedidoDto> pedidosToListPedidoDto(List<Pedido> pedidos) {
		return pedidos.stream().map(this::pedidoToPedidoDto).collect(Collectors.toList());
	}
	

}
