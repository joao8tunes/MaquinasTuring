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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import auxiliar_gui.RestringeCaracteres;


public class JanelaNumeroFitas extends JDialog
{
	
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private boolean cancelado = false;
	private JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 2147483647, 1));
	private int valorAnterior;
	
	
	public JanelaNumeroFitas()
	{
		((JSpinner.NumberEditor) spinner.getEditor()).getTextField().setDocument(new RestringeCaracteres("0123456789", -1));
		
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
				cancelado = true;
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
		
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setModal(true);
		setTitle("Fitas");
		setBounds(100, 100, 141, 120);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup().addComponent(panel, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup().addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		
		JLabel lblNmeroDeFitas = new JLabel("Número de Fitas");
		lblNmeroDeFitas.setHorizontalAlignment(SwingConstants.CENTER);
		lblNmeroDeFitas.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		spinner.setFont(new Font("Monospaced", Font.PLAIN, 11));
		spinner.setModel(new SpinnerNumberModel(1, 1, 2147483647, 1));
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener()
		{
			
			
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
		btnOk.setFont(new Font("Monospaced", Font.BOLD, 10));
		
		JLabel lblMx = new JLabel("Máx. 2147483647");
		lblMx.setHorizontalAlignment(SwingConstants.CENTER);
		lblMx.setFont(new Font("Monospaced", Font.BOLD, 8));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblNmeroDeFitas, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE).addComponent(spinner, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE).addComponent(lblMx, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE).addGroup(gl_panel.createSequentialGroup().addGap(74).addComponent(btnOk)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addComponent(lblNmeroDeFitas).addGap(6).addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(4).addComponent(lblMx).addGap(6).addComponent(btnOk)));
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
		setVisible(false); //Exibe apenas com o click do BOTÃO DIREITO sobre um 'Estado'.
	}
	
	
	/**************************************************************************/
	/*** MÉTODOS FUNCIONAIS ***************************************************/
	/**************************************************************************/
	
	public void exibir()
	{
		cancelado = false;
		spinner.setValue(1);
		valorAnterior = (int) spinner.getValue();
		revalidate();
		repaint();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	public boolean cancelado()
	{
		return cancelado;
	}
	
	
	public int getNumFitas()
	{
		return (int) spinner.getValue();
	}
	
}
