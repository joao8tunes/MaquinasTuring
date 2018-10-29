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

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JLabel;


public class Simbolo extends JLabel implements MouseListener
{
	
	
	private static final long serialVersionUID = 1L;
	private Transicao transicao;
	private ArrayList<String> leitura;
	private ArrayList<String> escrita;
	private ArrayList<String> movimento;
	
	
	public Simbolo(String simbolo, double x, double y, double xPonto, double yPonto, Transicao transicao, int numFitas, int indice)
	{
		String simbolos[] = simbolo.split("-");
		
		leitura = new ArrayList<String>();
		escrita = new ArrayList<String>();
		movimento = new ArrayList<String>();
		
		for (String s : simbolos) {
			String tokens[] = s.split(";");
			
			leitura.add(tokens[0]);
			escrita.add(tokens[1]);
			movimento.add(tokens[2]);
		}
		
		this.transicao = transicao;
		setText(simbolo.replaceAll("-", " | ")); //Definindo o símbo/token.
		setToolTipText((indice + 1) + "º símbolo");
		setBounds((int) (x + xPonto), (int) (-y + yPonto), simbolo.length() * 20, 20); //Definindo a posição em relação ao arco de transição e seus símbolos.		
		setFont(new Font("Monospaced", Font.BOLD, 12));
		setVisible(true);
		addMouseListener(this);
	}
	
	
	public void rotacionar(double x2, double y2, double seno, double cosseno, int posicao, boolean ciclo)
	{
		double x = 0, y = posicao * 20 + transicao.getEspacoCurva();
		
		setToolTipText((posicao + 1) + "º símbolo");
		
		if (ciclo) {
			setLocation(new Point((int) (x2 - getWidth() / 6 - getHeight() / 2 - 3), (int) (y2 - y - 50))); //Atualizando as coordenadas do símbolo.
			return;
		}
		else y2 += 10;
		
		setLocation(new Point((int) (x * cosseno - y * seno + x2 - getWidth() / 6 - getHeight() - 2 + Math.PI * (700 / getWidth())), (int) (x * seno + y * cosseno + y2 - getHeight()))); //Atualizando as coordenadas do símbolo.
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		switch (transicao.getEstadoInicial().getPainelAnimacao().getInfoFuncionalidade().getFuncao()) {
			case InfoFuncionalidade.REMOCAO: {
				transicao.removerSimbolo(this);
			}
			break;
			default: {
				if (e.getClickCount() == 2) { //Edição dos símbolos da transição. 
					setText(getText().replaceAll(" | ", "-").replaceAll("-", "").replaceAll("\\|", "-"));
					transicao.editarSimbolo(this);
					
					String simbolos[] = getText().split("-");
					int i = 0;
					
					for (String s : simbolos) {
						String tokens[] = s.split(";");
						
						leitura.set(i, tokens[0]);
						escrita.set(i, tokens[1]);
						movimento.set(i, tokens[2]);
						i++;
					}
					
					setText(getText().replaceAll("-", " | "));
				}
			}
		}
	}
	
	
	@Override
	public void mousePressed(MouseEvent e)
	{
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
		setCursor(new Cursor(transicao.getEstadoInicial().getPainelAnimacao().getInfoFuncionalidade().getPonteiroCursor()));
	}
	
	
	@Override
	public void mouseExited(MouseEvent e)
	{
	}
	
	
	public void setSimbolo(String s)
	{
		setText(s);
	}
	
	
	public String getLeitura(int i)
	{
		return leitura.get(i);
	}
	
	
	public String getEscrita(int i)
	{
		return escrita.get(i);
	}
	
	
	public String getMovimento(int i)
	{
		return movimento.get(i);
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
	
}
