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

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;


public class JanelaMaquinaInexistente extends JDialog
{
	
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	
	public JanelaMaquinaInexistente()
	{
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setResizable(false);
		setModal(true);
		setTitle("Máquina Inexistente");
		setBounds(100, 100, 312, 214);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_pc.gif")));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap(107, Short.MAX_VALUE).addComponent(label, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE).addGap(99)).addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addComponent(label).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE).addContainerGap(63, Short.MAX_VALUE)));
		
		JEditorPane txtrPorRazesDe = new JEditorPane();
		txtrPorRazesDe.setContentType("text/html");
		txtrPorRazesDe.setEditable(false);
		txtrPorRazesDe.setText("<div style=\\\"font-family: monospace; font-size: 12;\\\">\r\n<p>\r\nMáquina inexistente! Crie um autômato de pelo menos 2 estados para formar uma Máquina de Turing e então realizar a simulação.\r\n</p>\r\n</div>");
		txtrPorRazesDe.setCaretPosition(0);
		scrollPane.setViewportView(txtrPorRazesDe);
		contentPanel.setLayout(gl_contentPanel);
		setLocationRelativeTo(null);
		setVisible(false);
	}
	
	
	public void exibir()
	{
		revalidate();
		repaint();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
}
