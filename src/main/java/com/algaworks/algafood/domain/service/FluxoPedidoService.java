package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Pedido;

@Service
public class FluxoPedidoService {

	@Autowired
	private EmissaoPedidoService cadastroPedido;
	
	@Transactional
	public void confirmar(String codigoPedido) {
		Pedido pedido = cadastroPedido.buscarOuFalhar(codigoPedido);
		
		pedido.confirmar();
	}
	
	@Transactional
	public void cancelar(String codigoPedido) {
		Pedido pedido = cadastroPedido.buscarOuFalhar(codigoPedido);
		
		pedido.cancelar();
	}
	
	@Transactional
	public void confirmarEntrega(String codigoPedido) {
		Pedido pedido = cadastroPedido.buscarOuFalhar(codigoPedido);
		
		pedido.confirmarEntrega();
	}
}
