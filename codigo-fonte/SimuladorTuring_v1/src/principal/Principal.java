/*
 * Simulador de Máquinas de Turing - v1
 * 
 * Data: 11/10/2014
 * Programadores: Carol Chaves (RA 1304984), João Antunes (RA 1305093)
 * 
 * Implementado com o Eclipse Kepler, utilizando WindowBuilder Pro GUI Designer. 
 * Abra as classes do pacote 'gui' com este programa.
 */

package principal;

import gui.JanelaPrincipal;
import gui.JanelaSobreTuring;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Principal
{
	
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		
		JanelaSobreTuring janelaSobreTuring = new JanelaSobreTuring();
		JanelaPrincipal janelaPrincipal = new JanelaPrincipal();
		janelaPrincipal.setJanelaSobreTuring(janelaSobreTuring);
		
		janelaPrincipal.setVisible(true);
	}
	
}
