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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import automato.InfoFuncionalidade;


public class JanelaLimitePilha extends JDialog
{
	
	
	private static final long serialVersionUID = 1L;
	private int valorAnterior;
	private JSpinner spinner;
	
	
	public JanelaLimitePilha(final InfoFuncionalidade infoFuncionalidade)
	{
		addWindowListener(new WindowListener()
		{
			
			
			@Override
			public void windowOpened(WindowEvent e)
			{
			}
			
			
			@Override
			public void windowIconified(WindowEvent e)
			{
			}
			
			
			@Override
			public void windowDeiconified(WindowEvent e)
			{
			}
			
			
			@Override
			public void windowDeactivated(WindowEvent e)
			{
			}
			
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				spinner.setValue(valorAnterior);
			}
			
			
			@Override
			public void windowClosed(WindowEvent e)
			{
			}
			
			
			@Override
			public void windowActivated(WindowEvent e)
			{
			}
		});
		
		setModal(true);
		setTitle("Limite de Pilha");
		setResizable(false);
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setBounds(100, 100, 211, 187);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addComponent(panel, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(panel, GroupLayout.PREFERRED_SIZE, 161, Short.MAX_VALUE));
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setFont(new Font("Monospaced", Font.PLAIN, 11));
		editorPane.setEditable(false);
		editorPane.setContentType("text/html");
		editorPane.setCaretPosition(0);
		editorPane.setText("<div style=\\\"font-family: monospace; font-size: 12;\\\">\r\n<p>\r\nQuantidade máxima de iterações recursivas para um mesmo estado.\r\n</p>\r\n</div>");
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(500, 500, 2147483647, 1));
		spinner.setFont(new Font("Monospaced", Font.PLAIN, 11));
		
		JButton button = new JButton("OK");
		button.addActionListener(new ActionListener()
		{
			
			
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
				infoFuncionalidade.setLoopMaximo((int) spinner.getValue());
			}
		});
		button.setFont(new Font("Monospaced", Font.BOLD, 11));
		button.setActionCommand("OK");
		
		JLabel label = new JLabel("Máx. 2147483647");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Monospaced", Font.BOLD, 8));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addGap(12).addComponent(editorPane, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)).addGroup(gl_panel.createSequentialGroup().addGap(47).addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGroup(gl_panel.createSequentialGroup().addGap(47).addComponent(label, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)).addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup().addContainerGap(145, Short.MAX_VALUE).addComponent(button).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addGap(6).addComponent(editorPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(6).addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(6).addComponent(label).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(button).addGap(10)));
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
		setVisible(false);
	}
	
	
	public void exibir()
	{
		revalidate();
		repaint();
		valorAnterior = (int) spinner.getValue();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
