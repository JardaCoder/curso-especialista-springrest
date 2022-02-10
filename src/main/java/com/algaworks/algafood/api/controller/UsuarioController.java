package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.UsuarioDtoAssembler;
import com.algaworks.algafood.api.disassembler.UsuarioInputDtoDisassembler;
import com.algaworks.algafood.api.model.UsuarioDto;
import com.algaworks.algafood.api.model.input.AlterarSenhaInputDto;
import com.algaworks.algafood.api.model.input.UsuarioInputComSenhaDto;
import com.algaworks.algafood.api.model.input.UsuarioInputDto;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {
	
	
	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	CadastroUsuarioService cadastroUsuario;
	@Autowired
	private UsuarioDtoAssembler usuarioDtoAssembler;
	@Autowired
	private UsuarioInputDtoDisassembler usuarioInputDtoDisassembler;

	
	@GetMapping
	public List<UsuarioDto> listar(){
		return usuarioDtoAssembler.usuariosToListUsuarioDto(usuarioRepository.findAll());
	}
	
	@GetMapping("/{usuarioId}")
	public UsuarioDto buscar(@PathVariable("usuarioId") Long id){
		return usuarioDtoAssembler.usuarioToUsuarioDto(cadastroUsuario.buscarOuFalhar(id));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioDto criar(@RequestBody @Valid UsuarioInputComSenhaDto usuarioInputDto){
		Usuario usuario = cadastroUsuario.salvar(usuarioInputDtoDisassembler.usuarioInputDtoToUsuario(usuarioInputDto));
		return usuarioDtoAssembler.usuarioToUsuarioDto(usuario);
	}
	
	@PutMapping("/{usuarioId}")
	public UsuarioDto editar(@PathVariable("usuarioId") Long id, @RequestBody @Valid UsuarioInputDto usuarioInputDto){
		Usuario usuarioAtual = cadastroUsuario.buscarOuFalhar(id);
		
		usuarioInputDtoDisassembler.copyToDomainObject(usuarioInputDto, usuarioAtual);
		
		usuarioAtual = cadastroUsuario.salvar(usuarioAtual);
		
		return usuarioDtoAssembler.usuarioToUsuarioDto(usuarioAtual);
	}
	
	@PutMapping("/{usuarioId}/senha")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void alterarSenha(@PathVariable("usuarioId") Long id, @RequestBody @Valid AlterarSenhaInputDto alterarSenhaInput){
		cadastroUsuario.alterarSenha(id, alterarSenhaInput.getNovaSenha(), alterarSenhaInput.getSenhaAtual());
	}
	
	
	@DeleteMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable("usuarioId") Long id){
		cadastroUsuario.excluir(id);
	}
}
