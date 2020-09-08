package com.cartoes.api.utils;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
 
import com.cartoes.api.dtos.CartaoDto;
import com.cartoes.api.dtos.ClienteDto;
import com.cartoes.api.dtos.TransacaoDto;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Cliente;
import com.cartoes.api.entities.Transacao;

 
public class ConversaoUtils {
 
   	public static Cartao Converter(CartaoDto cartaoDto) throws ParseException {
 
         	Cartao cartao = new Cartao();
 
         	if (cartaoDto.getId() != null && cartaoDto.getId() != "")
                	cartao.setId(Integer.parseInt(cartaoDto.getId()));
 
         	cartao.setNumero(cartaoDto.getNumero());
         	cartao.setDataValidade(new SimpleDateFormat("dd/MM/yyyy").parse(cartaoDto.getDataValidade()));
         	cartao.setBloqueado(Boolean.parseBoolean(cartaoDto.getBloqueado()));
 
         	Cliente cliente = new Cliente();
         	cliente.setId(Integer.parseInt(cartaoDto.getClienteId()));
 
         	cartao.setCliente(cliente);
 
         	return cartao;
 
   	}
   	
   	public static CartaoDto Converter(Cartao cartao) {
 
         	CartaoDto cartaoDto = new CartaoDto();
         	
         	cartaoDto.setId(String.valueOf(cartao.getId()));
         	cartaoDto.setNumero(cartao.getNumero());
         	cartaoDto.setDataValidade(cartao.getDataValidade().toString());
         	cartaoDto.setBloqueado(String.valueOf(cartao.getBloqueado()));
         	cartaoDto.setClienteId(String.valueOf(cartao.getCliente().getId()));
 
         	return cartaoDto;
 
   	}
   	
   	public static List<CartaoDto> ConverterLista(List<Cartao> lista){
         	
         	List<CartaoDto> lst = new ArrayList<CartaoDto>(lista.size());
         	
         	for (Cartao cartao : lista) {
                	lst.add(Converter(cartao));
         	}
         	
         	return lst;
         	
   	}
 
   	public static Cliente Converter(ClienteDto clienteDto) {
 
         	Cliente cliente = new Cliente();
 
         	if (clienteDto.getId() != null && clienteDto.getId() != "")
                	cliente.setId(Integer.parseInt(clienteDto.getId()));
 
         	cliente.setNome(clienteDto.getNome());
         	cliente.setCpf(clienteDto.getCpf());
         	cliente.setUf(clienteDto.getUf());
 
         	return cliente;
 
   	}
   	
   	public static ClienteDto Converter(Cliente cliente) {
 
         	ClienteDto clienteDto = new ClienteDto();
 
         	clienteDto.setId(String.valueOf(cliente.getId()));
         	clienteDto.setNome(cliente.getNome());
         	clienteDto.setCpf(cliente.getCpf());
         	clienteDto.setUf(cliente.getUf());
 
         	return clienteDto;
 
   	}
   	
   	public static Transacao Converter(TransacaoDto transacaoDto) throws ParseException{
   		Transacao transacao = new Transacao();
   		
   		if (transacaoDto.getId() != null && transacaoDto.getId() != "")
        	transacao.Setid(Integer.parseInt(transacaoDto.getId()));
   		transacao.SetCnpj(transacaoDto.getCnpj());
   		transacao.SetValor(Double.parseDouble(transacaoDto.getValor()));
   		transacao.SetJuros(Double.parseDouble(transacaoDto.getJuros()));
   		transacao.Setqdtparcelas(Integer.parseInt(transacaoDto.getQtdParcelas()));
   		
   		Cartao cartao = new Cartao();
   		cartao.setId(Integer.parseInt(transacaoDto.getId()));
   		
   		transacao.SetCartao(cartao);
   		
   		return transacao;
   	}
   	
   	public static TransacaoDto Converter(Transacao transacao) {
   		TransacaoDto transacaoDto = new TransacaoDto();
   		
   		transacaoDto.setId(String.valueOf(transacao.Getid()));
   		transacaoDto.setCnpj(transacao.GetCnpj());
   		transacaoDto.setValor(String.valueOf(transacao.GetValor()));
   		transacaoDto.setJuros(String.valueOf(transacao.GetJuros()));
   		transacaoDto.setQtdParcalas(String.valueOf(transacao.GetqdtParcelas()));
   		transacaoDto.setCartaoId(String.valueOf(transacao.getCartao().getId()));
   		
   		return transacaoDto;
   	}
   	

}
