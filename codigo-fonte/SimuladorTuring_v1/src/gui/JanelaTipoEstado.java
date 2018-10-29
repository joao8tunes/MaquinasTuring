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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import automato.Estado;
import automato.Transicao;


public class JanelaTipoEstado extends JDialog
{
	
	
	private static final long serialVersionUID = 1L;
	private JRadioButton tipoInicial = new JRadioButton("Inicial");
	private JRadioButton tipoFinal = new JRadioButton("Final");
	private JLabel previewTipoEstado = new JLabel("");
	private String estadoAtual = Estado.ESTADO_NORMAL;
	private boolean estadoInicial = false;
	private boolean estadoFinal = false;
	
	
	public JanelaTipoEstado(final Estado estado)
	{
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setModal(true);
		setTitle("Tipo do Estado");
		setBounds(100, 100, 190, 92);
		setResizable(false);
		setLocationRelativeTo(null);
		
		tipoInicial.setBounds(10, 8, 74, 20);
		tipoFinal.setBounds(10, 40, 74, 20);
		previewTipoEstado.setBounds(120, 8, 50, 50);
		
		JPanel panel = new JPanel();
		
		panel.setLayout(null);
		previewTipoEstado.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(estadoAtual)));
		previewTipoEstado.setText("OK");
		previewTipoEstado.setToolTipText("Substituir o tipo do estado");
		
		//Posicionamento necessário para exibição de texto sobre o JLabel:
		previewTipoEstado.setVerticalTextPosition(JLabel.CENTER);
		previewTipoEstado.setHorizontalTextPosition(JLabel.CENTER);
		tipoInicial.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		tipoInicial.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				atualizarTipoEstado();
				
				revalidate();
				repaint();
			}
		});
		tipoFinal.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		tipoFinal.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				atualizarTipoEstado();
				
				revalidate();
				repaint();
			}
		});
		
		previewTipoEstado.addMouseListener(new MouseListener()
		{
			
			
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				estadoInicial = tipoInicial.isSelected();
				estadoFinal = tipoFinal.isSelected();
				
				for (Estado e : estado.getPainelAnimacao().getEstados()) {
					e.setText(e.getText().trim());
				}
				
				if (eInicial()) {
					boolean conflito = true; //Usado para evitar a correção de coordenadas mais de uma vez na classe 'Estado'.
					
					if (estado.eInicial()) conflito = false;
					else { //Evita alterar as configurações do Estado múltiplas vezes: ainda não é INICIAL.
						   //Na classe 'Estado' é feita a verificação para garantir q apenas um estado seja inicial. 
						
						estado.setBounds(estado.getX() - 25, estado.getY(), 75, 50);
						
						for (Transicao t : estado.getTransicoesSaida()) {
							t.setPontoInicial(estado.getX() + 50, estado.getY() + 25);
						}
						
						for (Transicao t : estado.getTransicoesEntrada()) {
							t.setPontoFinal(estado.getX() + 50, estado.getY() + 25);
						}
					}
					
					if (eFinal()) estado.setEstado(Estado.ESTADO_INICIAL_FINAL, conflito);
					else estado.setEstado(Estado.ESTADO_INICIAL, conflito);
					
					switch (estado.getText().length()) {
						case 3:
							estado.setText("    " + estado.getText());
						break;
						default:
							estado.setText("   " + estado.getText());
					}
				}
				else {
					if (estado.eInicial()) {
						estado.setBounds(estado.getX() + 25, estado.getY(), 50, 50);
						
						for (Transicao t : estado.getTransicoesSaida()) {
							t.setPontoInicial(estado.getX() + 25, estado.getY() + 25);
						}
						
						for (Transicao t : estado.getTransicoesEntrada()) {
							t.setPontoFinal(estado.getX() + 25, estado.getY() + 25);
						}
					}
					
					if (eFinal()) estado.setEstado(Estado.ESTADO_FINAL, false);
					else estado.setEstado(Estado.ESTADO_NORMAL, false);
				}
				
				setVisible(false);
				
				for (Transicao t : estado.getTransicoesEntrada()) {
					if (t.getEstadoInicial() == t.getEstadoFinal()) {
						int deslocamentoInicioFim = 0;
						
						if (estado.eInicial()) deslocamentoInicioFim = 25;
						
						t.setPontoInicial(t.getEstadoInicial().getX() + 40 + deslocamentoInicioFim, t.getEstadoInicial().getY() + 15);
						t.setPontoFinal(t.getEstadoInicial().getX() + 10 + deslocamentoInicioFim, t.getEstadoInicial().getY() + 15);
						t.gambi();
						break;
					}
				}
			}
			
			
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				if (eInicial()) {
					if (eFinal()) previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(Estado.ESTADO_INICIAL_FINAL_CORRETO)));
					else previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(Estado.ESTADO_INICIAL_CORRETO)));
				}
				else if (eFinal()) previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(Estado.ESTADO_FINAL_CORRETO)));
				else previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(Estado.ESTADO_NORMAL_CORRETO)));
			}
			
			
			@Override
			public void mouseExited(MouseEvent arg0)
			{
				if (eInicial()) {
					if (eFinal()) previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(Estado.ESTADO_INICIAL_FINAL)));
					else previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(Estado.ESTADO_INICIAL)));
				}
				else if (eFinal()) previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(Estado.ESTADO_FINAL)));
				else previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(Estado.ESTADO_NORMAL)));
			}
			
			
			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				if (eInicial()) {
					if (eFinal()) previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(Estado.ESTADO_INICIAL_FINAL_SELECIONADO)));
					else previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(Estado.ESTADO_INICIAL_SELECIONADO)));
				}
				else if (eFinal()) previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(Estado.ESTADO_FINAL_SELECIONADO)));
				else previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(Estado.ESTADO_NORMAL_SELECIONADO)));
			}
			
			
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			}
		});
		
		panel.add(tipoInicial);
		panel.add(tipoFinal);
		panel.add(previewTipoEstado);
		
		getContentPane().add(panel);
		setVisible(false); //Exibe apenas com o click do BOTÃO DIREITO sobre um 'Estado'.
	}
	
	
	/**************************************************************************/
	/*** MÉTODOS FUNCIONAIS ***************************************************/
	/**************************************************************************/
	
	public boolean eInicial()
	{
		switch (estadoAtual) {
			case Estado.ESTADO_INICIAL:
				return (true);
			case Estado.ESTADO_INICIAL_SELECIONADO:
				return (true);
			case Estado.ESTADO_INICIAL_CORRETO:
				return (true);
			case Estado.ESTADO_INICIAL_FINAL:
				return (true);
			case Estado.ESTADO_INICIAL_FINAL_SELECIONADO:
				return (true);
			case Estado.ESTADO_INICIAL_FINAL_CORRETO:
				return (true);
		}
		
		return (false);
	}
	
	
	public boolean eFinal()
	{
		switch (estadoAtual) {
			case Estado.ESTADO_FINAL:
				return (true);
			case Estado.ESTADO_FINAL_SELECIONADO:
				return (true);
			case Estado.ESTADO_FINAL_CORRETO:
				return (true);
			case Estado.ESTADO_INICIAL_FINAL:
				return (true);
			case Estado.ESTADO_INICIAL_FINAL_SELECIONADO:
				return (true);
			case Estado.ESTADO_INICIAL_FINAL_CORRETO:
				return (true);
		}
		
		return (false);
	}
	
	
	public void exibir()
	{
		tipoInicial.setSelected(estadoInicial);
		tipoFinal.setSelected(estadoFinal);
		setLocationRelativeTo(null);
		atualizarTipoEstado();
		revalidate();
		repaint();
		setVisible(true);
	}
	
	
	private void atualizarTipoEstado() //Usado somente nessa classe pra economizar código.
	{
		boolean eraInicial = eInicial();
		int x = previewTipoEstado.getX(), y = previewTipoEstado.getY();
		
		if (tipoInicial.isSelected()) {
			if (tipoFinal.isSelected()) estadoAtual = Estado.ESTADO_INICIAL_FINAL;
			else estadoAtual = Estado.ESTADO_INICIAL;
		}
		else if (tipoFinal.isSelected()) estadoAtual = Estado.ESTADO_FINAL;
		else estadoAtual = Estado.ESTADO_NORMAL;
		
		previewTipoEstado.setIcon(new ImageIcon(getClass().getResource(estadoAtual)));
		
		if (eraInicial) {
			if (!eInicial()) previewTipoEstado.setBounds(x + 25, y, 50, 50); //Avanço.
		}
		else if (eInicial()) previewTipoEstado.setBounds(x - 25, y, 75, 50); //Recuo.
		
		if (eInicial()) previewTipoEstado.setText("   " + previewTipoEstado.getText().trim());
		else previewTipoEstado.setText(previewTipoEstado.getText().trim());
	}
	
	
	public void deselecionarTipoInicial() //Usado pela classe 'Estado' pra evitar conflito entre 2 possíveis estados iniciais.
	{
		estadoInicial = false;
		tipoInicial.setSelected(estadoInicial);
		atualizarTipoEstado();
	}
	
}
