package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.JardaLinks;
import com.algaworks.algafood.api.v1.assembler.PermissaoDtoAssembler;
import com.algaworks.algafood.api.v1.controller.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.algaworks.algafood.api.v1.model.PermissaoDto;
import com.algaworks.algafood.core.security.SecurityUtils;
import com.algaworks.algafood.core.security.annotations.CheckSecurity;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.PermissaoRepository;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping(value = "/v1/grupos/{grupoId}/permissoes",  produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {
	
	
	@Autowired
	PermissaoRepository permissaoRepository;
	@Autowired
	CadastroGrupoService cadastroGrupo;
	@Autowired
	private PermissaoDtoAssembler permissaoDtoAssembler;
	@Autowired
	private JardaLinks jardaLinks;
	@Autowired
	private SecurityUtils securityUtils;

	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@Override
	@GetMapping
	public CollectionModel<PermissaoDto> listar(@PathVariable Long grupoId){
	    Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
	    
	    CollectionModel<PermissaoDto> permissoesModel 
	        = permissaoDtoAssembler.toCollectionModel(grupo.getPermissoes())
	            .removeLinks();
	    
	    permissoesModel.add(jardaLinks.linkToGrupoPermissoes(grupoId));
	    
	    if(securityUtils.podeEditarUsuariosGruposPermissoes()) {
	    	permissoesModel.add(jardaLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));
	    	
	    	permissoesModel.getContent().forEach(permissaoModel -> {
	    		permissaoModel.add(jardaLinks.linkToGrupoPermissaoDesassociacao(
	    				grupoId, permissaoModel.getId(), "desassociar"));
	    	});
	    }
	    
	    return permissoesModel;
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@DeleteMapping("/{permissaoId}")
	public ResponseEntity<Void> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId){
		cadastroGrupo.desassociarPermissao(grupoId, permissaoId);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@PutMapping("/{permissaoId}")
	public ResponseEntity<Void> associar(@PathVariable Long grupoId, @PathVariable Long permissaoId){
		cadastroGrupo.associarPermissao(grupoId, permissaoId);
		
		return ResponseEntity.noContent().build();
	}
	
}
