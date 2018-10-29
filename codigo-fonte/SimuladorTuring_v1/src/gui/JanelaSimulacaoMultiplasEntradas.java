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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import automato.PainelAnimacao;
import auxiliar_gui.StatusBorda;


public class JanelaSimulacaoMultiplasEntradas extends JDialog
{
	
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable entradas;
	private JScrollPane scrollPane;
	private JButton btnOk;
	private PainelAnimacao painelAnimacao;
	private JButton btnLimpar;
	private JButton btnReiniciar;
	
	
	public JanelaSimulacaoMultiplasEntradas()
	{
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setTitle("Simulação de Múltiplas Entradas");
		inicializaInterfaceGrafica();
		inicializaEventos();
	}
	
	
	public void setPainelAnimacao(PainelAnimacao painelAnimacao)
	{
		this.painelAnimacao = painelAnimacao;
	}
	
	
	private void inicializaInterfaceGrafica()
	{
		setModal(true);
		setVisible(false);
		setBounds(100, 100, 393, 327);
		setMinimumSize(new Dimension(393, 327));
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		scrollPane = new JScrollPane();
		
		entradas = new JTable();
		entradas.setFont(new Font("Monospaced", Font.PLAIN, 11));
		entradas.setModel(new DefaultTableModel(new Object[][] { { null }, }, new String[] { "Entrada" })
		{
			
			
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { String.class };
			
			
			public Class<?> getColumnClass(int columnIndex)
			{
				return columnTypes[columnIndex];
			}
		});
		entradas.getColumnModel().getColumn(0).setPreferredWidth(127);
		entradas.getTableHeader().setReorderingAllowed(false);
		entradas.setCellSelectionEnabled(true); //selecionar a célula e não toda a linha
		entradas.getTableHeader().setReorderingAllowed(false);
		entradas.putClientProperty("terminateEdit", Boolean.TRUE);
		entradas.setDefaultRenderer(Object.class, new StatusBorda());
		scrollPane.setViewportView(entradas);
		
		btnOk = new JButton("Simular");
		btnOk.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		btnLimpar = new JButton("Limpar");
		btnLimpar.addActionListener(new ActionListener()
		{
			
			
			public void actionPerformed(ActionEvent e)
			{
				((StatusBorda) entradas.getColumnModel().getColumn(0).getCellRenderer()).resetStatus();
				entradas.updateUI();
				entradas.revalidate();
				entradas.repaint();
			}
		});
		btnLimpar.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		btnReiniciar = new JButton("Reiniciar");
		btnReiniciar.addActionListener(new ActionListener()
		{
			
			
			public void actionPerformed(ActionEvent e)
			{
				DefaultTableModel modeloTabela = (DefaultTableModel) entradas.getModel();
				
				modeloTabela.setRowCount(0);
				modeloTabela.addRow(new Object[] { null }); //adiciona a nova linha
				((StatusBorda) entradas.getColumnModel().getColumn(0).getCellRenderer()).resetStatus();
			}
		});
		btnReiniciar.setFont(new Font("Monospaced", Font.BOLD, 11));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE).addGroup(gl_contentPane.createSequentialGroup().addGap(107).addComponent(btnReiniciar).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnLimpar).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane.createSequentialGroup().addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(btnOk).addComponent(btnLimpar, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE).addComponent(btnReiniciar, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))));
		contentPane.setLayout(gl_contentPane);
		
		entradas.getColumnModel().getColumn(0).setCellRenderer(new StatusBorda());
	}
	
	
	private void inicializaEventos()
	{
		entradas.addKeyListener(new KeyAdapter()
		{
			
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER) { //a tecla pressionada foi enter
					if (entradas.getSelectedRow() == entradas.getModel().getRowCount() - 1) { //se for a última linha da tabela
						adicionaLinhaTabela();
					}
				}
			}
		});
		
		btnOk.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				verificaEntradas();
				entradas.updateUI();
				entradas.revalidate();
				entradas.repaint();
			}
		});
	}
	
	
	private void adicionaLinhaTabela()
	{
		DefaultTableModel modeloTabela = (DefaultTableModel) entradas.getModel();
		modeloTabela.addRow(new Object[] { null, null }); //adiciona a nova linha
	}
	
	
	public void verificaEntradas()
	{
		int numeroEntradas = entradas.getRowCount();
		
		((StatusBorda) entradas.getColumnModel().getColumn(0).getCellRenderer()).resetStatus();
		
		for (int i = 0; i < numeroEntradas; ++i) {
			String entrada = (String) entradas.getValueAt(i, 0);
			
			if (entrada == null || entrada.equals("")) entradas.setValueAt(String.valueOf("□"), i, 0);
			else entradas.setValueAt(entradas.getValueAt(i, 0).toString().replace(" ", "□"), i, 0);
			
			entrada = (String) entradas.getValueAt(i, 0);
			
			switch (painelAnimacao.testarEntradaME(entrada)) {
				case 0: {
					((StatusBorda) entradas.getColumnModel().getColumn(0).getCellRenderer()).addStatus(i, 0, StatusBorda.STATUS_ACEITA);
				}
				break;
				case 1: {
					((StatusBorda) entradas.getColumnModel().getColumn(0).getCellRenderer()).addStatus(i, 0, StatusBorda.STATUS_REJEITA);
				}
				break;
				default: {
					((StatusBorda) entradas.getColumnModel().getColumn(0).getCellRenderer()).addStatus(i, 0, StatusBorda.STATUS_ATENCAO);
				}
			}
		}
	}
	
	
	public void exibir()
	{
		DefaultTableModel modeloTabela = (DefaultTableModel) entradas.getModel();
		
		if (modeloTabela.getRowCount() == 0) modeloTabela.addRow(new Object[] { null }); //adiciona a nova linha
		revalidate();
		repaint();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
}
