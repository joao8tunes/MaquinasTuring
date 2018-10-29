/*
 * Simulador de Máquinas de Turing - v1
 * 
 * Data: 11/10/2014
 * Programadores: Carol Chaves (RA 1304984), João Antunes (RA 1305093)
 * 
 * Implementado com o Eclipse Kepler, utilizando WindowBuilder Pro GUI Designer. 
 * Abra as classes do pacote 'gui' com este programa.
 */

package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import auxiliar_gui.RestringeCaracteres;


public class JanelaSimboloTransicao extends JDialog
{
	
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private String simbolo = "";
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JanelaSimboloTransicao(final int numFitas)
	{
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setModal(true);
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setTitle("Transição(ões)");
		setBounds(100, 100, 313, 179);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(panel, GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(panel, GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE));
		
		final JButton btnOk = new JButton("OK");
		btnOk.setFont(new Font("Monospaced", Font.BOLD, 11));
		btnOk.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				simbolo = "";
				
				for (int i = 0; i < table.getRowCount(); i++) {
					if (table.getValueAt(i, 1).toString().equals("")) table.setValueAt(String.valueOf("□"), i, 1);
					if (table.getValueAt(i, 2).toString().equals("")) table.setValueAt(String.valueOf("□"), i, 2);
					
					simbolo += table.getValueAt(i, 1).toString().trim() + ";" + table.getValueAt(i, 2).toString().trim() + ";" + table.getValueAt(i, 3).toString().trim();
					
					if (i + 1 < table.getRowCount()) simbolo += "-";
				}
				
				setVisible(false);
				setLocationRelativeTo(null);
				
				for (int i = 0; i < table.getRowCount(); i++) {
					table.setValueAt(String.valueOf("□"), i, 1);
					table.setValueAt(String.valueOf("□"), i, 2);
					table.setValueAt("D", i, 3);
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		
		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] { { "", null, null, "" }, }, new String[] { "N\u00BA Fita", "Leitura", "Escrita", "Movimento" })
		{
			
			
			private static final long serialVersionUID = 1L;
			Class[] columnTypes = new Class[] { String.class, String.class, String.class, Object.class };
			
			
			public Class getColumnClass(int columnIndex)
			{
				return columnTypes[columnIndex];
			}
			
			
			boolean[] columnEditables = new boolean[] { false, true, true, true };
			
			
			public boolean isCellEditable(int row, int column)
			{
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(3).setResizable(false);
		table.putClientProperty("terminateEdit", Boolean.TRUE);
		table.getTableHeader().setReorderingAllowed(false);
		
		DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
		centralizado.setHorizontalAlignment(SwingConstants.CENTER);
		
		for (int i = 1; i < numFitas; i++) {
			((DefaultTableModel) table.getModel()).addRow(new Object[] { i, "□", "□", "D" });
		}
		
		table.setFont(new Font("Monospaced", Font.PLAIN, 11));
		scrollPane.setViewportView(table);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel.createSequentialGroup().addContainerGap(246, Short.MAX_VALUE).addComponent(btnOk)).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup().addGap(4).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnOk)));
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
		addWindowListener(new WindowAdapter()
		{
			
			
			public void windowClosing(WindowEvent evt)
			{
				simbolo = ""; //Cancelou a edição/criação.
				
				for (int i = 0; i < numFitas; i++) {
					table.setValueAt(String.valueOf(i + 1), i, 0);
					table.setValueAt(String.valueOf("□"), i, 1);
					table.setValueAt(String.valueOf("□"), i, 2);
					table.setValueAt("D", i, 3);
				}
				
				setVisible(false);
			}
		});
		
		//Combo de opções de movimento:
		String[] movimentos = new String[] { "D", "E", "P" };
		table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JComboBox(movimentos)));
		
		//Selecionar símbolo ao clicar (duas vezes) sobre a célula e limitar string:
		JTextField field = new JTextField(new RestringeCaracteres("0123456789abcdefghijklmnopkrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWYXZ□ ", 1), "", 10);
		
		field.addFocusListener(new FocusAdapter()
		{
			
			
			@Override
			public void focusGained(FocusEvent e)
			{
				Object source = e.getSource();
				
				if (source instanceof JTextComponent) {
					((JTextComponent) source).selectAll();
					
					if (((JTextComponent) source).getText().equals("□") || ((JTextComponent) source).getText().equals(" ") || ((JTextComponent) source).getText().length() == 0) ((JTextComponent) source).setText("");
				}
			}
			
			
			public void focusLost(FocusEvent e)
			{
				Object source = e.getSource();
				
				if (((JTextComponent) source).getText().length() == 0 || ((JTextComponent) source).getText().equals(" ")) {
					((JTextComponent) source).setText("□");
				}
			}
		});
		
		field.addKeyListener(new KeyListener()
		{
			
			
			@Override
			public void keyTyped(KeyEvent arg0)
			{
			}
			
			
			@Override
			public void keyReleased(KeyEvent arg0)
			{
			}
			
			
			@Override
			public void keyPressed(KeyEvent arg0)
			{
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					btnOk.doClick();
				}
			}
		});
		
		table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(field));
		table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(field));
	}
	
	
	public void exibir(String s)
	{
		String conjuntos[] = s.split("-");
		
		for (int i = 0; i < conjuntos.length; i++) {
			String simbolos[] = conjuntos[i].split(";");
			
			//Se estiver editando uma transição, resgata seus valores; senão, apenas zera (tem q passar " ; ; " em 's'):
			table.setValueAt(i + 1, i, 0);
			table.setValueAt(simbolos[0].trim(), i, 1);
			table.setValueAt(simbolos[1].trim(), i, 2);
			table.setValueAt(simbolos[2].trim(), i, 3);
		}
		
		revalidate();
		repaint();
		setVisible(true);
	}
	
	
	public String getSimbolo()
	{
		//Leitura ; Escrita ; Movimento:
		return simbolo;
	}
	
}
