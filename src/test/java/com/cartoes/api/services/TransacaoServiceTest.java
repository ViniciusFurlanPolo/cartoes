package com.cartoes.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
	private Date dataTest;

	@Before
	public void setUp() throws Exception {
		
		testTransacao = new Transacao();

		testTransacao.SetCartao(new Cartao());
		
		testCartao = new Cartao();
		
		dataTest = new Date();
		
		dataTest.setTime(1000);
		
		testCartao.setDataValidade(dataTest);
	}

	@Test
	public void testBuscarPorNumeroCartaoExistente() throws ConsistenciaException {

		Optional<List<Transacao>> lstTransacao = transacaoService.buscarPorNumeroCartao("9993063207738947");

		BDDMockito.given(transacaoRepository.findByNumeroCartao(Mockito.anyString()))
		.willReturn(lstTransacao);

		assertTrue(lstTransacao.isPresent());

	}

	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorNumeroCartaoNaoExistente() throws ConsistenciaException {

		BDDMockito.given(transacaoRepository.findByNumeroCartao(Mockito.anyString()))
		.willReturn(null);

		transacaoService.buscarPorNumeroCartao("9993063207769834");

	}

	@Test
	public void testSalvarComSucesso() throws ConsistenciaException {

		BDDMockito.given(transacaoRepository.save(Mockito.any(Transacao.class)))
		.willReturn(new Transacao());

		Transacao resultado = transacaoService.salvar(new Transacao());

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
		
		dataTest = new Date();
		testTransacao.getCartao().setDataValidade(dataTest);
		
		BDDMockito.given(cartaoRepository.findByNumero(Mockito.any()))
		.willReturn(Optional.of(testCartao));
		
		transacaoRepository.save(testTransacao);
		
		
	}

}
