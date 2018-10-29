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

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;


//Ajuda daqui: http://stackoverflow.com/questions/22221587/refresh-update-highlighter-in-jtable-when-changing-search-values
public class PosicaoFita implements TableCellRenderer
{
	
	
	private ArrayList<Posicao> posicoes = new ArrayList<Posicao>();
	private final JTextField field = new JTextField();
	private final transient Highlighter.HighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
	private static final Color BACKGROUND_SELECTION_COLOR = new Color(220, 240, 255);
	
	
	public PosicaoFita()
	{
		field.setOpaque(true);
		field.setBorder(BorderFactory.createEmptyBorder());
		field.setForeground(Color.BLACK);
		field.setBackground(Color.WHITE);
		field.setEditable(false);
	}
	
	
	public void resetPosicoes()
	{
		posicoes = new ArrayList<Posicao>();
	}
	
	
	public void addPosicao(int linha, int coluna, int posicao)
	{
		posicoes.add(new Posicao(linha, coluna, posicao));
	}
	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
	{
		Highlighter highlighter = field.getHighlighter();
		String txt = Objects.toString(value, "");
		
		field.setText(txt);
		field.setBackground(isSelected ? BACKGROUND_SELECTION_COLOR : Color.WHITE);
		
		for (Posicao p : posicoes) {
			if (row == p.getLinha() && col == p.getColuna()) {
				try {
					highlighter.removeAllHighlights();
					highlighter.addHighlight(posicoes.get(row).getPosicao(), posicoes.get(row).getPosicao() + 1, highlightPainter);
				}
				catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
		
		return field;
	}
	
}
