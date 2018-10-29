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

import automato.Estado;


public class EstadoEmProcessamento
{
	
	
	private Estado estado;
	private int chamadas; //Quantidade de vezes que esse estado é chamado em uma recursão.
	
	
	public EstadoEmProcessamento(Estado estado)
	{
		this.estado = estado;
	}
	
	
	public Estado getEstado()
	{
		return estado;
	}
	
	
	public int getChamadas()
	{
		return chamadas;
	}
	
	
	public void setChamadas(int chamadas)
	{
		this.chamadas = chamadas;
	}
	
}
