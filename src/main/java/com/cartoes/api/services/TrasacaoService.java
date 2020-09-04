package com.cartoes.api.services;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;

public class TrasacaoService {
	private static final Logger log = LoggerFactory.getLogger(CartaoService.class);
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
	
	public Optional<List<Transacao>> buscarPorCartaoId(int cartaoId) throws ConsistenciaException {
		log.info("Service: buscando as transações do cartão de id: {}", cartaoId);
		Optional<List<Transacao>> transacoes = Optional.ofNullable(transacaoRepository.findByCartaoId(cartaoId));
		if (!transacoes.isPresent() || transacoes.get().size() < 1) {
			log.info("Service: Nenhuma transação foi encontrada para o cartão de id: {}", cartaoId);
			throw new ConsistenciaException("Nenhuma transação encontrada para o cartão de id: {}", cartaoId);
		}
		return transacoes;
	}
	
	public Transacao salvar(Transacao transacao) throws ConsistenciaException {
		log.info("Service: salvando a transação: {}", transacao);
		if (!cartaoRepository.findById(transacao.getCartao().getId()).isPresent()) {
			log.info("Service: Nenhum cartão com id: {} foi encontrado", transacao.getCartao().getId());
			throw new ConsistenciaException("Nenhum cartão com id: {} foi encontrado", transacao.getCartao().getId());
		}
		if (transacao.Getid() > 0)
			buscarPorId(transacao.Getid());
		try {
			return transacaoRepository.save(transacao);
		} catch (DataIntegrityViolationException e) {
			log.info("Service: Já existe uma transação de número {} cadastrado", transacao.Getid());
			throw new ConsistenciaException("Já existe uma transaçãos de número {} cadastrado", transacao.Getid());

		}
	}
	
	public void excluirPorId(int id) throws ConsistenciaException {
		log.info("Service: excluíndo a transação de id: {}", id);
		buscarPorId(id);

		transacaoRepository.deleteById(id);
	}

}
