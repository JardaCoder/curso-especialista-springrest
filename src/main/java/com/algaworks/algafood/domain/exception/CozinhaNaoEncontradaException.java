package com.algaworks.algafood.domain.exception;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	private static final String MSG_COZINHA_NAO_EXISTE = "A cozinha de código %d não existe.";

	public CozinhaNaoEncontradaException(String mensagem) {
		super(mensagem);

	}

	public CozinhaNaoEncontradaException(Long cozinhaId) {
		this(String.format(MSG_COZINHA_NAO_EXISTE, cozinhaId));

	}

}
