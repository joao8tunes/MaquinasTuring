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


public class JanelaExcessoEstado extends JDialog
{
	
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	
	public JanelaExcessoEstado()
	{
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setResizable(false);
		setModal(true);
		setTitle("Limite de Estados");
		setBounds(100, 100, 324, 255);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_pc.gif")));
		
		JEditorPane txtrPorRazesDe = new JEditorPane();
		txtrPorRazesDe.setFont(new Font("Monospaced", Font.PLAIN, 11));
		txtrPorRazesDe.setContentType("text/html");
		txtrPorRazesDe.setEditable(false);
		txtrPorRazesDe.setText("<div style=\\\"font-family: monospace; font-size: 12;\\\">\r\n<p>\r\nPor razões de estética do programa, há um limite de criação de estados, que vai até o número 99999. Exclua alguns estados para criar mais alguns, de preferência estados com numerão imediatamente inferior ao limite (ex.: 99998, 99997, ...).\r\n</p>\r\n</div>");
		txtrPorRazesDe.setCaretPosition(0);
		scrollPane.setColumnHeaderView(txtrPorRazesDe);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addGap(104).addComponent(label, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE).addContainerGap(114, Short.MAX_VALUE)).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addComponent(label).addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)));
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
