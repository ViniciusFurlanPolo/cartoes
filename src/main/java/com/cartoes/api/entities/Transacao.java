package com.cartoes.api.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;



@Entity
@Table(name = "transacao")
public class Transacao implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "Data_Transacao", nullable = false)
	private Date DataTransacao;
	
	@Column(name = "cnpj", nullable = false, length = 14, unique = true )
	private String cnpj;
	
	@Column(name = "Valor", nullable = false, length = 30)
	private Double valor;
	
	@Column(name = "qdt_Parcelas", nullable = false, length = 30)
	private int qdtParcelas;
	
	@Column(name = "juros", nullable = false, length = 30)
	private Double juros;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	private Cartao cartao;
	
	public int Getid() {
		return id;
	}
	public void Setid(int id) {
		this.id = id;
	}
	
	
	public String GetCnpj() {
		return cnpj;
	}
	public void SetCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	public Double GetValor() {
		return valor;
	}
	public void SetValor(Double valor) {
		this.valor = valor;
	}
	
	public int GetqdtParcelas() {
		return qdtParcelas;
	}
	public void Setqdtparcelas(int qdtParcelas) {
		this.qdtParcelas = qdtParcelas;
	}
	
	public Double GetJuros() {
		return juros;
	}
	public void SetJuros(Double juros) {
		this.juros = juros;
	}
	
	public Cartao GetCartao() {
		return cartao;
	}
	public void SetCartao(Cartao cartao) {
		this.cartao = cartao;
	}
	
	@PrePersist
	public void prePersist() {
		DataTransacao = new Date();
	}
	@Override
	public String toString() {
		return "Transacao [id=" + id + ", DataTransacao=" + DataTransacao + ", cnpj=" + cnpj + ", valor=" + valor
				+ ", qdtParcelas=" + qdtParcelas + ", juros=" + juros + ", cartao=" + cartao + "]";
	}
	
	

}
