package com.cartoes.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransacaoServiceTest {

	@MockBean
	private TransacaoRepository transacaoRepository;

	@Autowired
	private TrasacaoService transacaoService;

	@MockBean
	private CartaoRepository cartaoRepository;
	
	private Transacao testTransacao;
	private Cartao testCartao;

	@Before
	public void setUp() throws Exception {
		Calendar test = Calendar.getInstance();
		
		int dataTest = test.get(Calendar.DAY_OF_MONTH);
		
		test.set(Calendar.DAY_OF_MONTH, dataTest + 1);
		
		testTransacao = new Transacao();

		testTransacao.SetCartao(new Cartao());
		
		testCartao = new Cartao();
		
		testCartao.setDataValidade(test.getTime());
	}

	@Test
	public void testBuscarPorNumeroCartaoExistente() throws ConsistenciaException {

		List<Transacao> lstTransacao = new ArrayList<>();
		lstTransacao.add(new Transacao());

		BDDMockito.given(transacaoRepository.findByNumeroCartao(Mockito.anyString()))
		.willReturn(Optional.of(lstTransacao));
		
		Optional<List<Transacao>> resultado = transacaoService.buscarPorNumeroCartao("9993063207738947");

		assertTrue(resultado.isPresent());

	}

	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorNumeroCartaoNaoExistente() throws ConsistenciaException {
		
		List<Transacao> lstTransacao = new ArrayList<>();

		BDDMockito.given(transacaoRepository.findByNumeroCartao(Mockito.anyString()))
		.willReturn(Optional.of(lstTransacao));

		transacaoService.buscarPorNumeroCartao("9993063207769834");

	}

	@Test
	public void testSalvarComSucesso() throws ConsistenciaException, ParseException {				
		
		BDDMockito.given(cartaoRepository.findByNumero(Mockito.any()))
			.willReturn(Optional.of(testCartao));
		
		BDDMockito.given(transacaoRepository.save(Mockito.any(Transacao.class)))
			.willReturn(new Transacao());
		
		Transacao resultado = transacaoService.salvar(testTransacao);
		
		assertNotNull(resultado);
	}

	@Test(expected = ConsistenciaException.class)
	public void testSalvarCartaoNaoPresente() throws ConsistenciaException {

		BDDMockito.given(cartaoRepository.findByNumero(Mockito.any()))
		.willReturn(Optional.empty());
		
		testTransacao.getCartao().setId(1);
	

		transacaoService.salvar(testTransacao);

	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarCartaoBloqueado() throws ConsistenciaException{
		
		testCartao.setBloqueado(true);
		
		BDDMockito.given(cartaoRepository.findByNumero(Mockito.any()))
		.willReturn(Optional.of(testCartao));
		
		transacaoService.salvar(testTransacao);
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarCartaoVencido() throws ConsistenciaException{
		Calendar test = Calendar.getInstance();
		
		int dataTest = test.get(Calendar.DAY_OF_MONTH);
		
		test.set(Calendar.DAY_OF_MONTH, dataTest - 1);

		
		
		testCartao.setDataValidade(test.getTime());
		
		BDDMockito.given(cartaoRepository.findByNumero(Mockito.any()))
		.willReturn(Optional.of(testCartao));
		
		transacaoRepository.save(testTransacao);
		
		
	}

}
