package com.algaworks.algafood.infrastructure.service.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.enums.StatusPedido;
import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.dtos.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
	
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		CriteriaQuery<VendaDiaria> query = builder.createQuery(VendaDiaria.class);
		Root<Pedido> root = query.from(Pedido.class);
		
		var functionConvertTzDataCriacao = builder.function(
				"convert_tz", Date.class,  root.get("dataCriacao"), builder.literal("+00:00"), builder.literal(timeOffset));
		
		var functionDateDataCriacao = builder.function(
				"date", Date.class, functionConvertTzDataCriacao);
		
		
		var selection = builder.construct(VendaDiaria.class,
					functionDateDataCriacao,
					builder.count(root.get("id")),
					builder.sum(root.get("valorTotal"))
				);
		
		
		
		var predicates = new ArrayList<Predicate>();
		

		if (filtro.getRestauranteId() != null) {
			predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
		}

		if (filtro.getDataCriacaoInicio() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
		}
		
		if (filtro.getDataCriacaoFim() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
		}
		
		predicates.add(root.get("status").in(StatusPedido.ENTREGUE, StatusPedido.CONFIRMADO));

		query.select(selection);
		
		query.where(predicates.toArray(new Predicate[0]));
		
		query.groupBy(functionDateDataCriacao);
		
		
		return manager.createQuery(query).getResultList();
	}

}
