package com.cartoes.api.dtos;

public class TransacaoDto {
	
	private String id;
	
	private String cnpj;
	
	private String valor;
	
	private String juros;
	
	private String qtdParcelas;
	
	private String cartaoId;

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

	public String getCartaoId() {
		return cartaoId;
	}

	public void setCartaoId(String cartaoId) {
		this.cartaoId = cartaoId;
	}

	@Override
	public String toString() {
		
		return "Transacao[id =" + id + ";"
				+ "cnpj =" + cnpj + ";"
				+ "valor =" + valor + ";"
				+ "juros =" + juros + ";"
				+ "qtdParcelas =" + qtdParcelas + ";"
				+ "cartaoId =" + cartaoId + "]";
				
	}
}
