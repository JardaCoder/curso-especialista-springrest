package com.algaworks.algafood.infrastructure.service.email;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.algaworks.algafood.domain.service.EnvioEmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnvioEmailFakeService implements EnvioEmailService{

	@Autowired
	private ProcessadorEmailTemplate processadorEmailTemplate;

	@Override
	public void enviar(Mensagem mensagem) {
		
		try {
			
			String corpo = processadorEmailTemplate.processarTemplate(mensagem);
			
			log.info(String.format("Email FAKE enviado para: %s \n\n -----------CORPO---------- \n\n %s",
					mensagem.getDestinatarios().stream()
					.map(String::toString)
					.collect(Collectors.joining(", ")), corpo));
			
		} catch (Exception e) {
			throw new EmailException("Não foi possivel enviar o email", e);
		}
		
	}
}
