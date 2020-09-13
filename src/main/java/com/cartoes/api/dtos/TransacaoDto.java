package com.cartoes.api.dtos;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

public class TransacaoDto {
	
	@NotEmpty(message = "O id não pode estar vazio")
	private String id;
	
	@NotEmpty(message = "O CNPJ não pode estar vazio")
	@CNPJ(message = "CNPJ invalido")
	@Length(min = 14, max = 14, message = "CNPJ deve ter exatamente 14 caracteres")
	private String cnpj;
	
	@NotEmpty(message = "O valor não pode estar vazio")
	@Length(min = 1, max = 10, message = "O valor deve ter até 10 digitos")
	private String valor;
	
	@NotEmpty(message = "O juros não pode estar vazio")
	@Length(min = 1, max = 4, message = "Juros deve ter até 4 digitos")
	private String juros;
	
	@NotEmpty(message = "A Quantidade de Parcelas não pode estar sozinho")
	@Length(min = 1, max = 2, message = "Quantidade de parcelas deve ter até 2 digitos")
	private String qtdParcelas;
	
	@NotEmpty(message = "O numero do cartão não pode estar vazio")
	private String cartaoNumero;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getJuros() {
		return juros;
	}

	public void setJuros(String juros) {
		this.juros = juros;
	}

	public String getQtdParcelas() {
		return qtdParcelas;
	}

	public void setQtdParcalas(String qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
	}

	public String getCartaoNumero() {
		return cartaoNumero;
	}

	public void setCartaoNumero(String cartaoNumero) {
		this.cartaoNumero = cartaoNumero;
	}

	@Override
	public String toString() {
		
		return "Transacao[id =" + id + ";"
				+ "cnpj =" + cnpj + ";"
				+ "valor =" + valor + ";"
				+ "juros =" + juros + ";"
				+ "qtdParcelas =" + qtdParcelas + ";"
				+ "cartaoId =" + cartaoNumero + "]";
				
	}
}
