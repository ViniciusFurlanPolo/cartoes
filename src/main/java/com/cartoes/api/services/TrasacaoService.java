package com.cartoes.api.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import org.springframework.stereotype.Service;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;

@Service
public class TrasacaoService {
	private static final Logger log = LoggerFactory.getLogger(TrasacaoService.class);
	@Autowired
	private CartaoRepository cartaoRepository;
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	public Optional<Transacao> buscarPorId(int id) throws ConsistenciaException {
		log.info("Service: buscando as transações de id: {}", id);
		Optional<Transacao> transacao = transacaoRepository.findById(id);
		if (!transacao.isPresent()) {
			log.info("Service: Nenhuma transação com id: {} foi encontrado", id);
			throw new ConsistenciaException("Nenhuma transação com id: {} foi encontrado", id);
		}
		return transacao;
	}
	
	public Optional<List<Transacao>> buscarPorNumeroCartao(String numeroCartao) throws ConsistenciaException {
		log.info("Service: buscando as transações do cartão de id: {}", numeroCartao);
		Optional<List<Transacao>> transacoes = transacaoRepository.findByNumeroCartao(numeroCartao);
		
		if (!transacoes.isPresent() || transacoes.get().size() < 1) {
			
			log.info("Service: Nenhuma transação foi encontrada para o cartão de id: {}", numeroCartao);
			
			throw new ConsistenciaException("Nenhuma transação encontrada para o cartão de id: {}", numeroCartao);
			
		}
		return transacoes;
	}
	
	public Transacao salvar(Transacao transacao) throws ConsistenciaException {
		
		log.info("Service: salvando a transação: {}", transacao);
		
		Optional<Cartao> cartao = cartaoRepository.findByNumero(transacao.getCartao().getNumero());
		
		if (!cartao.isPresent()) {
			
			log.info("Service: Nenhum cartão com id: {} foi encontrado", transacao.getCartao().getId());
			
			throw new ConsistenciaException("Nenhum cartão com id: {} foi encontrado", transacao.getCartao().getId());
			
		}
		
		if (transacao.Getid() > 0){
			log.info("Sevice: Transações não podem ser alteradas, apenas incluidas.");
			throw new ConsistenciaException("Transações não podem ser alteradas, apenas incluidas");
		}
		if (cartao.get().getBloqueado()) {
			log.info(
					"Service: Não é possivel adicionar transações para esse cartão, pois o mesmo se encontra bloqueado");
			throw new ConsistenciaException(
					"Não é possivel adicionar transações para esse cartão, pois o mesmo se encontra bloqueado");
		}
		if (cartao.get().getDataValidade().before(new Date())) {
			log.info(
					"Service: Não é possivel adicionar transações ao cartão, pois ele se encontra vencido.");
			throw new ConsistenciaException(
					"Service: Não é possivel adicionar transações ao cartão, pois ele se encontra vencido.");
		}
		transacao.SetCartao(cartao.get());
		
		return transacaoRepository.save(transacao);
	}

}
