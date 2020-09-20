package com.cartoes.api.ingracao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.DataBinder;

import com.cartoes.api.controllers.TransacaoController;
import com.cartoes.api.dtos.TransacaoDto;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Cliente;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.repositories.ClienteRepository;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.response.Response;
import com.cartoes.api.utils.ConversaoUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransacaoIntegracaoTest {
	
	@Autowired
	private TransacaoController transacaoController;
	
	@Autowired
	private TransacaoRepository transacaoRepository;
			
	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	private Transacao transacaoTeste;
	private Cliente clienteTeste;
	private Cartao cartaoTeste;
	
	private void criarClienteTeste() throws ParseException{
		clienteTeste = new Cliente();
		
		clienteTeste.setCpf("27586047014");
		clienteTeste.setNome("Fuleano De Tales");
		clienteTeste.setUf("BA");
	}
	
	private void criarCartaoTeste() throws ParseException{
		
		Calendar c = Calendar.getInstance();
		int dia = c.get(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, dia + 1);
		
		cartaoTeste = new Cartao();
		
		cartaoTeste.setBloqueado(false);
		cartaoTeste.setCliente(clienteTeste);
		cartaoTeste.setDataValidade(c.getTime());
		cartaoTeste.setNumero("1111222233334444");
	}
	
	
	private void criarTransacaoTeste() throws ParseException{
		transacaoTeste = new Transacao();
		
		transacaoTeste.SetCnpj("87307381000134");		
		transacaoTeste.SetJuros(0.2);
		transacaoTeste.SetCartao(cartaoTeste);
		transacaoTeste.Setqdtparcelas(1);
		transacaoTeste.SetValor(15.0);
		transacaoTeste.prePersist();
		
	}
	
	
	
	@Before
	public void setUp() throws Exception{
		criarClienteTeste();
		criarCartaoTeste();
		criarTransacaoTeste();
		
		clienteRepository.save(clienteTeste);
		cartaoRepository.save(cartaoTeste);
		transacaoRepository.save(transacaoTeste);
	}
	
	@After
	public void tearDown() throws Exception {
		
		transacaoRepository.deleteAll();
		cartaoRepository.deleteAll();
		clienteRepository.deleteAll();
		
	}
	
	@Test
	@WithMockUser
	public void buscarPorNumeroTest() throws Exception{
		
		ResponseEntity<Response<List<TransacaoDto>>> resultadoDto = transacaoController.buscarPorNumeroCartao("1111222233334444");
		
		TransacaoDto trasacaoTesteDto = ConversaoUtils.Converter(transacaoTeste);
		
		assertEquals(resultadoDto.getBody().getDados().get(0).toString(), trasacaoTesteDto.toString());
		
	}
	
	@Test
	@WithMockUser
	public void buscarPorNumeroInconsistenciaTest() throws Exception{
		
		ResponseEntity<Response<List<TransacaoDto>>> resultadoDto = transacaoController.buscarPorNumeroCartao("111122223333444");			
		
		assertEquals(resultadoDto.getStatusCodeValue(), 400);
		
	}
	
	@Test
	@WithMockUser
	public void salvarTransacaoTest() throws Exception{
		
		TransacaoDto transacaoDto = new TransacaoDto();
		transacaoDto.setCnpj("55570957000165");
		transacaoDto.setJuros("1.3");
		transacaoDto.setQtdParcalas("12");
		transacaoDto.setValor("150.00");
		transacaoDto.setCartaoNumero("1111222233334444");
		
		DataBinder teste = new DataBinder(transacaoDto);
	
		ResponseEntity<Response<TransacaoDto>> resultadoDto = transacaoController.salvar(transacaoDto, teste.getBindingResult());
		
		
	}
	
	@Test
	@WithMockUser
	public void salvarTransacaoInconsistenciaTest() throws Exception{
		
		TransacaoDto transacaoDto = new TransacaoDto();
		transacaoDto.setCnpj("55570957000165");
		transacaoDto.setJuros("1");
		transacaoDto.setQtdParcalas("1");
		transacaoDto.setValor("100");
		transacaoDto.setCartaoNumero("111122223333444");
		
		DataBinder teste = new DataBinder(transacaoDto);
	
		ResponseEntity<Response<TransacaoDto>> resultadoDto = transacaoController.salvar(transacaoDto, teste.getBindingResult());
		
		
	}
}