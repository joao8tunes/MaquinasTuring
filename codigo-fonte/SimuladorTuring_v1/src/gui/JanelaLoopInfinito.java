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
import automato.InfoFuncionalidade;


public class JanelaLoopInfinito extends JDialog
{
	
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private InfoFuncionalidade infoFuncionalidade;
	private JEditorPane txtrPorRazesDe;
	
	
	public JanelaLoopInfinito()
	{
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setResizable(false);
		setModal(true);
		setTitle("Estouro de Pilha");
		setBounds(100, 100, 261, 193);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_pc.gif")));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap(82, Short.MAX_VALUE).addComponent(label, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE).addGap(73)).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addComponent(label).addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)));
		
		txtrPorRazesDe = new JEditorPane();
		txtrPorRazesDe.setFont(new Font("Monospaced", Font.PLAIN, 11));
		txtrPorRazesDe.setContentType("text/html");
		txtrPorRazesDe.setEditable(false);
		txtrPorRazesDe.setCaretPosition(0);
		scrollPane.setViewportView(txtrPorRazesDe);
		contentPanel.setLayout(gl_contentPanel);
		setLocationRelativeTo(null);
		setVisible(false);
	}
	
	
	public void exibir()
	{
		txtrPorRazesDe.setText("<div style=\\\"font-family: monospace; font-size: 12;\\\">\r\n<p>\r\nEstouro de pilha! O limite atual de chamadas recursivas para um mesmo estado é de " + infoFuncionalidade.getLoopMaximo() + ".\r\n</p>\r\n</div>");
		revalidate();
		repaint();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	public void setInfoFuncionalidade(InfoFuncionalidade infoFuncionalidade)
	{
		this.infoFuncionalidade = infoFuncionalidade;
	}
	
}
