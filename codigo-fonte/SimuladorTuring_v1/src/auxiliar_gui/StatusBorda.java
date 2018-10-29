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
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class StatusBorda extends DefaultTableCellRenderer
{
	
	
	private static final long serialVersionUID = 1L;
	public static final int STATUS_NORMAL = 0;
	public static final int STATUS_ACEITA = 1;
	public static final int STATUS_REJEITA = 2;
	public static final int STATUS_ATENCAO = 3; //Loop infinito.
	ArrayList<Status> status = new ArrayList<Status>();
	
	
	public void resetStatus()
	{
		for (Status s : status) {
			s.setStatus(STATUS_NORMAL);
		}
		
		super.repaint();
		status = new ArrayList<Status>();
	}
	
	
	public void addStatus(int linha, int coluna, int status)
	{
		this.status.add(new Status(linha, coluna, status));
	}
	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
	{
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		
		for (Status s : status) {
			if (row == s.getLinha() && col == s.getColuna()) {
				switch (s.getStatus()) {
					case StatusBorda.STATUS_ACEITA: {
						((JComponent) c).setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
					}
					break;
					case StatusBorda.STATUS_REJEITA: {
						((JComponent) c).setBorder(BorderFactory.createLineBorder(Color.RED, 2));
					}
					break;
					case StatusBorda.STATUS_ATENCAO: {
						((JComponent) c).setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
					}
					break;
					default:
						((JComponent) c).setBorder(null);
				}
			}
		}
		
		super.revalidate();
		super.repaint();
		
		return c;
	}
	
}
