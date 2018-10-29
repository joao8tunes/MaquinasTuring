/*
 * Simulador de Máquinas de Turing - v1
 * 
 * Data: 11/10/2014
 * Programadores: Carol Chaves (RA 1304984), João Antunes (RA 1305093)
 * 
 * Implementado com o Eclipse Kepler, utilizando WindowBuilder Pro GUI Designer. 
 * Abra as classes do pacote 'gui' com este programa.
 */

package auxiliar_gui;

public class Posicao
{
	
	
	private int linha;
	private int coluna;
	private int posicao;
	
	
	public Posicao(int linha, int coluna, int posicao)
	{
		this.linha = linha;
		this.coluna = coluna;
		this.posicao = posicao;
	}
	
	
	public int getLinha()
	{
		return linha;
	}
	
	
	public int getColuna()
	{
		return coluna;
	}
	
	
	public int getPosicao()
	{
		return posicao;
	}
	
	
	public void setPosicao(int p)
	{
		this.posicao = p;
	}
	
}
