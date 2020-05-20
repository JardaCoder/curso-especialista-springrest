package com.algaworks.algafood.domain.repository;

import java.util.List;

import com.algaworks.algafood.domain.model.Cidade;

public interface CidadeRepository {
	
	List<Cidade> listar();
	Cidade buscarPorId(Long id);
	Cidade salvarOuAdicionar (Cidade cidade);
	void remover (Cidade cidade);

}