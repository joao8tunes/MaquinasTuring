/*
 * Simulador de Máquinas de Turing - v1
 * 
 * Data: 11/10/2014
 * Programadores: Carol Chaves (RA 1304984), João Antunes (RA 1305093)
 * 
 * Implementado com o Eclipse Kepler, utilizando WindowBuilder Pro GUI Designer. 
 * Abra as classes do pacote 'gui' com este programa.
 */

package auxiliar_automato;

import java.util.ArrayList;
import automato.Estado;
import automato.Transicao;


public class Passo
{
	
	
	private Estado estado;
	private ArrayList<String> fitas;
	private ArrayList<char[]> caracteresFita;
	private ArrayList<Integer> posicaoFita;
	private boolean falha;
	private Passo backtrack; //Só os passos que possuem erros possuem esse "passo especial".
	private boolean visitado;
	
	//Log do processamento:
	private ArrayList<String> leitura;
	private ArrayList<String> escrita;
	private ArrayList<String> movimento;
	
	//Log de rejeição:
	private Transicao transicao;
	private int idxSimbolo;
	private int numFita;
	
	
	public Passo(Estado estado, ArrayList<String> fitas, ArrayList<char[]> caracteresFita, ArrayList<Integer> posicaoFita, boolean falha, ArrayList<String> leitura, ArrayList<String> escrita, ArrayList<String> movimento)
	{
		backtrack = null;
		visitado = false;
		this.estado = estado;
		this.fitas = fitas;
		this.caracteresFita = caracteresFita;
		this.posicaoFita = posicaoFita;
		this.falha = falha;
		this.leitura = leitura;
		this.escrita = escrita;
		this.movimento = movimento;
	}
	
	
	public Estado getEstado()
	{
		return estado;
	}
	
	
	public ArrayList<String> getFitas()
	{
		return fitas;
	}
	
	
	public ArrayList<char[]> getCaracteresFitas()
	{
		return caracteresFita;
	}
	
	
	public ArrayList<Integer> getPosicaoFita()
	{
		return posicaoFita;
	}
	
	
	public boolean possuiFalha()
	{
		return falha;
	}
	
	
	public ArrayList<String> getLeitura()
	{
		return leitura;
	}
	
	
	public ArrayList<String> getEscrita()
	{
		return escrita;
	}
	
	
	public ArrayList<String> getMovimento()
	{
		return movimento;
	}
	
	
	public void setEstado(Estado e)
	{
		this.estado = e;
	}
	
	
	public void setFitas(ArrayList<String> f)
	{
		this.fitas = f;
	}
	
	
	public void setCaracteresFitas(ArrayList<char[]> c)
	{
		this.caracteresFita = c;
	}
	
	
	public void setPosicoesFitas(ArrayList<Integer> p)
	{
		this.posicaoFita = p;
	}
	
	
	public void setIdxSimbolo(int idxSimbolo)
	{
		this.idxSimbolo = idxSimbolo;
	}
	
	
	public void setTransicao(Transicao transicao)
	{
		this.transicao = transicao;
	}
	
	
	public void setNumFita(int numFita)
	{
		this.numFita = numFita;
	}
	
	
	public int getIdxSimbolo()
	{
		return idxSimbolo;
	}
	
	
	public Transicao getTransicao()
	{
		return transicao;
	}
	
	
	public int getNumFita()
	{
		return numFita;
	}
	
	
	public void setBacktrack(Passo p)
	{
		backtrack = p;
	}
	
	
	public Passo getBacktrack()
	{
		return backtrack;
	}
	
	
	public void setVisitado(boolean visitado)
	{
		this.visitado = visitado;
	}
	
	
	public boolean getVisitado()
	{
		return visitado;
	}
	
}
