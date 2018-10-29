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

import gui.JanelaPrincipal;
import java.awt.Color;
import java.io.File;
import javax.swing.JScrollPane;
import automato.InfoFuncionalidade;
import automato.PainelAnimacao;


public class AbaAutomato
{
	
	
	private JScrollPane scrollAnimacao;
	private PainelAnimacao painelAnimacao;
	private File arquivo;
	private int numFitas;
	private boolean maquinaModificada;
	
	
	public AbaAutomato(InfoFuncionalidade infoFuncionalidade, int numFitas, JanelaPrincipal janelaPrincipal)
	{
		maquinaModificada = true;
		scrollAnimacao = new JScrollPane();
		painelAnimacao = new PainelAnimacao(infoFuncionalidade, numFitas, janelaPrincipal);
		scrollAnimacao.add(painelAnimacao);
		painelAnimacao.setBackground(Color.WHITE);
		scrollAnimacao.setViewportView(painelAnimacao);
	}
	
	
	//Abertura de arquivo:
	public AbaAutomato(InfoFuncionalidade infoFuncionalidade, int numFitas, JanelaPrincipal janelaPrincipal, PainelAnimacao painelAnimacao)
	{
		maquinaModificada = true;
		scrollAnimacao = new JScrollPane();
		this.painelAnimacao = painelAnimacao;
		scrollAnimacao.add(painelAnimacao);
		painelAnimacao.setBackground(Color.WHITE);
		scrollAnimacao.setViewportView(painelAnimacao);
	}
	
	
	public JScrollPane getScrollAnimacao()
	{
		return scrollAnimacao;
	}
	
	
	public PainelAnimacao getPainelAnimacao()
	{
		return painelAnimacao;
	}
	
	
	public File getArquivo()
	{
		return arquivo;
	}
	
	
	public void setArquivo(File arquivo)
	{
		this.arquivo = arquivo;
	}
	
	
	public void setNumFitas(int numFitas)
	{
		this.numFitas = numFitas;
	}
	
	
	public int getNumFitas()
	{
		return numFitas;
	}
	
	
	public boolean getMaquinaModificada()
	{
		return maquinaModificada;
	}
	
	
	public void setMaquinaModificada(boolean maquinaModificada)
	{
		this.maquinaModificada = maquinaModificada;
	}
	
}
