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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;


public class JanelaCreditos extends JDialog
{
	
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	
	public JanelaCreditos()
	{
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setResizable(false);
		setTitle("Créditos");
		setModal(true);
		setBounds(100, 100, 290, 162);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(panel, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(panel, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE));
		
		JLabel lblCarolinaChaves = new JLabel("Carolina Dias Chaves");
		lblCarolinaChaves.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		JLabel lblJooVtorAntunes = new JLabel("João Vítor Antunes Ribeiro");
		lblJooVtorAntunes.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		JLabel label = new JLabel("1304984");
		label.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		JLabel label_1 = new JLabel("1305093");
		label_1.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		JLabel label_2 = new JLabel("");
		label_2.setToolTipText("Tá estressado?! Toma um café e vai relaxar...");
		label_2.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_programador.gif")));
		label_2.setFont(new Font("Monospaced", Font.BOLD, 12));
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel.createSequentialGroup().addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblCarolinaChaves).addComponent(lblJooVtorAntunes)).addPreferredGap(ComponentPlacement.RELATED, 39, Short.MAX_VALUE).addGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addComponent(label).addComponent(label_1, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE))).addGroup(gl_panel.createSequentialGroup().addContainerGap(99, Short.MAX_VALUE).addComponent(label_2, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE).addGap(80)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel.createSequentialGroup().addComponent(label_2, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE).addGap(11).addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblCarolinaChaves).addComponent(label, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED, 7, Short.MAX_VALUE).addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblJooVtorAntunes, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE).addComponent(label_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))));
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
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
