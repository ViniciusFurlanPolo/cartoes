package com.cartoes.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cartoes.api.dtos.TransacaoDto;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.services.TrasacaoService;
import com.cartoes.api.utils.ConsistenciaException;
import com.cartoes.api.utils.ConversaoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransacaoControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TrasacaoService transacaoService;
	
	private Transacao criarTransacaoTest(){
		
		Transacao transacao = new Transacao();
		
		transacao.Setid(1);
		transacao.SetCnpj("76926099000119");
		transacao.SetJuros(2.5);
		transacao.Setqdtparcelas(12);
		transacao.SetValor(150.0);
		transacao.SetCartao(new Cartao());
		transacao.getCartao().setNumero("4226519646077031");
		
		return transacao;
		
	}
	
	@Test
	@WithMockUser
	public void testBuscarNumeroSucesso() throws Exception{
		Transacao transacao = criarTransacaoTest();
		List<Transacao> lstTransacao = new ArrayList<>();
		lstTransacao.add(transacao);
		
		BDDMockito.given(transacaoService.buscarPorNumeroCartao(Mockito.anyString()))
		.willReturn(Optional.of(lstTransacao));
		
		mvc.perform(MockMvcRequestBuilders.get("/api/transacao/cartao/4226519646077031")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dados[0].id").value(transacao.Getid()))
				.andExpect(jsonPath("$.dados[0].cnpj").value(transacao.GetCnpj()))
				.andExpect(jsonPath("$.dados[0].juros").value(transacao.GetJuros()))
				.andExpect(jsonPath("$.dados[0].qtdParcelas").value(transacao.GetqdtParcelas()))
				.andExpect(jsonPath("$.dados[0].valor").value(transacao.GetValor()))
				.andExpect(jsonPath("$.dados[0].numeroCartao").value(transacao.getCartao().getNumero()))
				.andExpect(jsonPath("$.erros").isEmpty());

		
	}
	
	@Test
	@WithMockUser
	public void testBuscarNumeroInconsistencia() throws Exception{
		BDDMockito.given(transacaoService.buscarPorNumeroCartao((Mockito.anyString())))
		.willThrow(new ConsistenciaException("Teste inconsistência"));

	mvc.perform(MockMvcRequestBuilders.get("/api/transacao/cartao/4226519646077031")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.erros").value("Teste inconsistência"));
		
	}
	
	@Test
	@WithMockUser
	public void testSalvarSucesso() throws Exception{
		Transacao transacao = criarTransacaoTest();
		TransacaoDto objEntrada = ConversaoUtils.Converter(transacao);

		String json = new ObjectMapper().writeValueAsString(objEntrada);
		
		BDDMockito.given(transacaoService.salvar(Mockito.any(Transacao.class)))
			.willReturn(transacao);
		
		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dados.id").value(transacao.Getid()))
				.andExpect(jsonPath("$.dados.cnpj").value(transacao.GetCnpj()))
				.andExpect(jsonPath("$.dados.juros").value(transacao.GetJuros()))
				.andExpect(jsonPath("$.dados.qdtParcelas").value(transacao.GetqdtParcelas()))
				.andExpect(jsonPath("$.dados.valor").value(transacao.GetValor()))
				.andExpect(jsonPath("$.dados.numeroCartao").value(transacao.getCartao().getNumero()))
				.andExpect(jsonPath("$.erros").isEmpty());
		
	}
	
	@Test
	@WithMockUser
	public void testSalvarIncosistencia() throws Exception{
		Transacao transacao = criarTransacaoTest();
		TransacaoDto objEntrada = ConversaoUtils.Converter(transacao);

		String json = new ObjectMapper().writeValueAsString(objEntrada);
		
		BDDMockito.given(transacaoService.salvar(Mockito.any(Transacao.class)))
			.willThrow(new ConsistenciaException("Teste inconsistência"));
		
		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.erros").value("Teste inconsistência."));
	}
	
	@Test
	@WithMockUser
	public void testSalvarCNPJemBranco() throws Exception{
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setJuros("2.5");
		objEntrada.setQtdParcalas("12");
		objEntrada.setValor("150.0");
		objEntrada.setCartaoNumero("4226519646077031");

		

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("CNPJ não pode ser vazio."));
		
	}
	
	@Test
	@WithMockUser
	public void testSalvarCNPJInvalido() throws Exception{
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("123456");
		objEntrada.setJuros("2.5");
		objEntrada.setQtdParcalas("12");
		objEntrada.setValor("150.0");
		objEntrada.setCartaoNumero("4226519646077031");

		

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("CNPJ Invalido."));
		
	}
	
	@Test
	@WithMockUser
	public void testSalvarJurosEmBranco() throws Exception{
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("76926099000119");
		objEntrada.setQtdParcalas("12");
		objEntrada.setValor("150.0");
		objEntrada.setCartaoNumero("4226519646077031");

		

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Juros não pode ser vazio."));
		
	}
	@Test
	@WithMockUser
	public void testSalvarJurosInvalido() throws Exception{
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("76926099000119");
		objEntrada.setQtdParcalas("12");
		objEntrada.setJuros("15447");
		objEntrada.setValor("150.0");
		objEntrada.setCartaoNumero("4226519646077031");

		

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Juros Invalidos."));
		
	}
	@Test
	@WithMockUser
	public void testSalvarQtdParcelasEmBranco() throws Exception{
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("76926099000119");
		objEntrada.setJuros("34");
		objEntrada.setValor("150.0");
		objEntrada.setCartaoNumero("4226519646077031");

		

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Quantidade de Parcelas não pode ser vazio."));
		
	}
	
	@Test
	@WithMockUser
	public void testSalvarQtdParcelasInvalido() throws Exception{
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("76926099000119");
		objEntrada.setJuros("34");
		objEntrada.setQtdParcalas("157");
		objEntrada.setValor("150.0");
		objEntrada.setCartaoNumero("4226519646077031");

		

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Quantidade de Parcelas Invalido."));
		
	}
	
	@Test
	@WithMockUser
	public void testSalvarCartaoEmBranco() throws Exception{
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("76926099000119");
		objEntrada.setJuros("34");
		objEntrada.setValor("150.0");
		objEntrada.setQtdParcalas("12");

		

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Numero do Cartão não pode ser vazio."));
		
	}
	
	@Test
	@WithMockUser
	public void testSalvarCartaoInsuficiente() throws Exception{
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("76926099000119");
		objEntrada.setJuros("34");
		objEntrada.setValor("150.0");
		objEntrada.setQtdParcalas("12");
		objEntrada.setCartaoNumero("4226");

		

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Numero do Cartão com digitos insuficientes."));
		
	}
	@Test
	@WithMockUser
	public void testSalvarCartaoExcedentes() throws Exception{
		TransacaoDto objEntrada = new TransacaoDto();
		
		objEntrada.setCnpj("76926099000119");
		objEntrada.setJuros("34");
		objEntrada.setValor("150.0");
		objEntrada.setQtdParcalas("12");
		objEntrada.setCartaoNumero("42265196460770313156131");

		

		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.erros").value("Numero do Cartão com digitos excedentes."));
		
	}
	
	
	
	
}
