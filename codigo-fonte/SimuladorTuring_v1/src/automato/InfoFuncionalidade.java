/*
 * Simulador de Máquinas de Turing - v1
 * 
 * Data: 11/10/2014
 * Programadores: Carol Chaves (RA 1304984), João Antunes (RA 1305093)
 * 
 * Implementado com o Eclipse Kepler, utilizando WindowBuilder Pro GUI Designer. 
 * Abra as classes do pacote 'gui' com este programa.
 */

package automato;

import gui.JanelaExcessoEstado;
import java.awt.Cursor;


public class InfoFuncionalidade
{
	
	
	public static final char ADICAO = 0; //Adicionar novo estado.
	public static final char MOVIMENTACAO = 1; //Movimentar estados.
	public static final char TRANSICAO = 2; //Criar transição entre estados.
	public static final char REMOCAO = 3; //Remover estados, transições ou símbolos.
	private int ponteiroCursor = Cursor.DEFAULT_CURSOR; //Ponteiro do mouse relativo à função a ser realizada (criar, mover, conectar ou remover estados).
	private char funcao = ADICAO; //Por padrão, habilita a funcionalidade de criar estado.
	private JanelaExcessoEstado janelaExcessoEstado = new JanelaExcessoEstado();
	private int loopMaximo = 500; //Quantidade máxima de vezes que um loop de processamento pode envolver o mesmo estado.
	
	
	public void setFuncao(char funcao)
	{
		switch (this.funcao = funcao) {
			case MOVIMENTACAO:
				ponteiroCursor = Cursor.HAND_CURSOR;
			break;
			case REMOCAO:
				ponteiroCursor = Cursor.CROSSHAIR_CURSOR;
			break;
			default: //FUNCAO_CRIAR e FUNCAO_CONECTAR
				ponteiroCursor = Cursor.DEFAULT_CURSOR;
		}
	}
	
	
	/**************************************************************************/
	/*** MÉTODOS FUNCIONAIS ***************************************************/
	/**************************************************************************/
	
	public char getFuncao()
	{
		return funcao;
	}
	
	
	public int getPonteiroCursor()
	{
		return ponteiroCursor;
	}
	
	
	public void exibirExcesso()
	{
		janelaExcessoEstado.setVisible(true);
	}
	
	
	public void setLoopMaximo(int loopMaximo)
	{
		this.loopMaximo = loopMaximo;
	}
	
	
	public int getLoopMaximo()
	{
		return loopMaximo;
	}
	
}
