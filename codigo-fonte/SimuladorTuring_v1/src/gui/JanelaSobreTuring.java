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

import java.awt.Desktop;
import java.awt.Font;
import java.io.IOException;
import java.net.URISyntaxException;
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
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


public class JanelaSobreTuring extends JDialog
{
	
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JEditorPane txtrAlanMathisonTuring;
	
	
	public JanelaSobreTuring()
	{
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setModal(true);
		setTitle("Sobre Alan Turing");
		setBounds(100, 100, 443, 277);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setResizable(false);
		
		JPanel panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup().addComponent(panel, GroupLayout.PREFERRED_SIZE, 420, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup().addComponent(panel, GroupLayout.PREFERRED_SIZE, 245, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		
		JLabel fotoTuring = new JLabel("");
		fotoTuring.setFont(new Font("Monospaced", Font.BOLD, 11));
		fotoTuring.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_alan_turing.png")));
		fotoTuring.setToolTipText("Alan Turing");
		
		JLabel lblAlan = new JLabel("Alan Mathison Turing");
		lblAlan.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		JScrollPane scrollPane = new JScrollPane();
		
		txtrAlanMathisonTuring = new JEditorPane();
		txtrAlanMathisonTuring.setContentType("text/html");
		txtrAlanMathisonTuring.setEditable(false);
		txtrAlanMathisonTuring.setText("<div style=\"font-family: monospace; font-size: 12;\">\r\n\r\n<p>    Alan Mathison Turing foi um matemático,  lógico, criptoanalista e cientista da computação britânico. Foi influente no desenvolvimento da Ciência da Computação e na formalização do conceito de algoritmo e computação  através da Máquina de Turing (modelo teórico de computador, que representa o limite de computacionalidade de qualquer computador atual).</p>\r\n\r\n<p>    Trabalhou na Segunda Guerra Mundial para a inteligência britânica, atuando num centro especializado em quebra de códigos.</p>\r\n\r\n<p>    Suicidou-se aos 41 anos, após uma série  de humilhações sofridas em público, em razão de sua orientação homossexual declarada.</p><br>\r\n\r\nSaiba mais em: <a href=\"http://pt.wikipedia.org/wiki/Alan_Turing\">Wikipédia</a><br>\r\n\r\nImagem extraída de: <a href=\"http://www.dailymail.co.uk/news/article-2528697/Queen-pardons-wartime-codebreaking-hero-Alan-Turing.html\"> Mail Online </a>\r\n\r\n</div>");
		txtrAlanMathisonTuring.setCaretPosition(0);
		txtrAlanMathisonTuring.setFont(new Font("Monospaced", Font.PLAIN, 11));
		scrollPane.setViewportView(txtrAlanMathisonTuring);
		txtrAlanMathisonTuring.addHyperlinkListener(new HyperlinkListener()
		{
			
			
			public void hyperlinkUpdate(HyperlinkEvent e)
			{
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED && Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(e.getURL().toURI());
					}
					catch (IOException e1) {
						e1.printStackTrace();
					}
					catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel.createSequentialGroup().addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(fotoTuring)).addGroup(Alignment.LEADING, gl_panel.createSequentialGroup().addComponent(lblAlan).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addComponent(lblAlan).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false).addGroup(gl_panel.createSequentialGroup().addComponent(fotoTuring).addGap(134)).addComponent(scrollPane, 0, 0, Short.MAX_VALUE))));
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		setVisible(false);
	}
	
	
	public void exibir()
	{
		txtrAlanMathisonTuring.setCaretPosition(0);
		revalidate();
		repaint();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
}
