package com.cartoes.api.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.services.TrasacaoService;
import com.cartoes.api.utils.ConsistenciaException;

@RestController
@RequestMapping("/api/transacao")
@CrossOrigin(origins = "*")
public class TransacaoController {
	private static final Logger log = LoggerFactory.getLogger(TransacaoController.class);
	@Autowired
	private TrasacaoService trasacaoService;
	
	@GetMapping(value = "/cartao/{cartaoId}")
	public ResponseEntity<List<Transacao>> buscarPorCartaoId(@PathVariable("cartaoId") int cartaoId) {
		try {
			log.info("Controller: buscando transações do cartão de ID: {}", cartaoId);
			Optional<List<Transacao>> listaTransacao = trasacaoService.buscarPorCartaoId(cartaoId);
			return ResponseEntity.ok(listaTransacao.get());
		} catch (ConsistenciaException e) {
			log.info("Controller: Inconsistência de dados: {}", e.getMessage());
			return ResponseEntity.badRequest().body(new ArrayList<Transacao>());
		} catch (Exception e) {
			log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
			return ResponseEntity.status(500).body(new ArrayList<Transacao>());
		}
	}
	
	@PostMapping
	public ResponseEntity<Transacao> salvar(@RequestBody Transacao transacao){
		try {
			log.info("Controller: salvando a transação: {}", transacao.toString());

			return ResponseEntity.ok(this.trasacaoService.salvar(transacao));
		} catch (ConsistenciaException e) {
			log.info("Controller: Inconsistência de dados: {}", e.getMessage());
			return ResponseEntity.badRequest().body(new Transacao());
		} catch (Exception e) {
			log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
			return ResponseEntity.status(500).body(new Transacao());
		}
	}
	
	@DeleteMapping(value = "excluir/{id}")
	public ResponseEntity<String> excluirPorId(@PathVariable("id") int id) {

		try {
			log.info("Controller: excluíndo transação de ID: {}", id);
			trasacaoService.excluirPorId(id);
			return ResponseEntity.ok("Transação de id: " + id + " excluído com sucesso");
		} catch (ConsistenciaException e) {
			log.info("Controller: Inconsistência de dados: {}", e.getMessage());
			return ResponseEntity.badRequest().body(e.getMensagem());
		} catch (Exception e) {
			log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
			return ResponseEntity.status(500).body(e.getMessage());
		}

	}

}