package com.algaworks.algafood.api.v1.controller.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.v1.model.UsuarioDto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("UsuariosDto")
@Data
public class UsuariosDtoOpenApi {
	

    private UsuariosEmbeddedDtoOpenApi _embedded;
    
    private Links _links;
    
    @ApiModel("UsuariosEmbeddedDto")
    @Data
    public class UsuariosEmbeddedDtoOpenApi {
        
        private List<UsuarioDto> usuarios;
        
    }   
}
