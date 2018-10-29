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


public class JanelaAutomatoIncorreto extends JDialog
{
	
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	
	public JanelaAutomatoIncorreto()
	{
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setModal(true);
		setTitle("Autômato Incorreto");
		setBounds(100, 100, 372, 306);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_pc.gif")));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap(140, Short.MAX_VALUE).addComponent(label, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE).addGap(126)).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addComponent(label).addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		
		JEditorPane txtrPorRazesDe = new JEditorPane();
		txtrPorRazesDe.setFont(new Font("Monospaced", Font.PLAIN, 11));
		txtrPorRazesDe.setContentType("text/html");
		txtrPorRazesDe.setEditable(false);
		txtrPorRazesDe.setText("<div style=\\\"font-family: monospace; font-size: 12;\\\">\r\n<p>\r\nAutômato mal formado! Para simular uma Máquina de Turing, os seguintes requisitos são necessários:<br>\r\n- <b>Conexidade:</b> todos os estados devem estar conectados, formando um único conjunto interligado (grafo conexo);<br>\r\n- <b>Estado inicial:</b> deve haver 1 estado inicial, que deve ser único;<br>\r\n- <b>Estado final:</b> deve haver pelo menos 1 estado final, que não pode possuir transições de saída;\r\n</p>\r\n</div>");
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
