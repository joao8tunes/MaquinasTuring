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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import automato.Estado;
import automato.PainelAnimacao;
import auxiliar_automato.Passo;
import auxiliar_gui.PosicaoFita;
import auxiliar_gui.RestringeCaracteres;


public class JanelaSimulacaoPassoAPasso extends JDialog
{
	
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField entrada;
	private JLabel lblProcesso;
	private JSeparator separator;
	private JLabel botaoPlayPause;
	private JLabel botaoVoltar;
	private JLabel botaoComecar;
	private JLabel botaoAvancar;
	private JLabel botaoTerminar;
	private JPanel panel_1;
	private ArrayList<Passo> passosSimulacao;
	private PainelAnimacao painelAnimacao;
	private int passo;
	private boolean emExecucao = false;
	private JTextArea log;
	private JanelaAutomatoIncorreto janelaAutomatoIncorreto = new JanelaAutomatoIncorreto();
	private JTable table;
	private JanelaLoopInfinito janelaLoopInfinito = new JanelaLoopInfinito();
	private PosicaoFita renderer = new PosicaoFita();
	
	
	public JanelaSimulacaoPassoAPasso()
	{
		setModal(true);
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		setTitle("Simulação Passo a Passo");
		setBounds(100, 100, 458, 369);
		setMinimumSize(new Dimension(458, 369));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(panel, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup().addComponent(panel, GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE).addGap(0)));
		
		addWindowListener(new WindowAdapter()
		{
			
			
			public void windowClosing(WindowEvent evt)
			{
				if (passosSimulacao != null) {
					entrada.setEditable(true);
					passosSimulacao = null;
					painelAnimacao.deselecionarEstados();
					setBordaNormal();
					entrada.setText("");
					botaoPlayPause.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_play_pause.png")));
				}
			}
		});
		
		JLabel lblEntrada = new JLabel("Entrada");
		lblEntrada.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		entrada = new JTextField(new RestringeCaracteres("0123456789abcdefghijklmnopkrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWYXZ□ ", -1), "", 10); //-1: tamanho "ilimitado".
		entrada.setFont(new Font("Monospaced", Font.PLAIN, 13));
		entrada.setColumns(10);
		
		lblProcesso = new JLabel("Processamento");
		lblProcesso.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		separator = new JSeparator();
		
		JScrollPane scrollPane = new JScrollPane();
		
		log = new JTextArea();
		log.setFont(new Font("Monospaced", Font.PLAIN, 11));
		log.setEditable(false);
		scrollPane.setViewportView(log);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.GRAY));
		
		botaoVoltar = new JLabel("");
		botaoVoltar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_voltar.png")));
		botaoVoltar.setToolTipText("Voltar instrução");
		botaoVoltar.addMouseListener(new MouseListener()
		{
			
			
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				botaoVoltar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_voltar_over.png")));
				setBordaNormal();
				
				if (passosSimulacao != null && passo > 0) {
					--passo;
					
					painelAnimacao.deselecionarEstados();
					log.append("\n" + passo + "º Passo :\n");
					
					renderer.resetPosicoes();
					((DefaultTableModel) table.getModel()).setRowCount(0);
					int novaLargura = 0;
					
					for (int i = 0; i < passosSimulacao.get(passo).getFitas().size(); i++) {
						((DefaultTableModel) table.getModel()).addRow(new Object[] { i + 1, passosSimulacao.get(passo).getFitas().get(i) });
						renderer.addPosicao(i, 1, passosSimulacao.get(passo).getPosicaoFita().get(i));
						
						if ((passosSimulacao.get(passo).getFitas().get(i).length() * 11) > novaLargura) novaLargura = passosSimulacao.get(passo).getFitas().get(i).length() * 11;
					}
					
					if (novaLargura > 120) table.getColumnModel().getColumn(1).setPreferredWidth(novaLargura);
					
					table.updateUI();
					table.revalidate();
					table.repaint();
					
					if (passosSimulacao.get(passo).possuiFalha()) painelAnimacao.selecionarEstado(passosSimulacao.get(passo).getEstado(), Estado.ERRADO);
					else painelAnimacao.selecionarEstado(passosSimulacao.get(passo).getEstado(), Estado.SELECIONADO);
				}
			}
			
			
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				botaoVoltar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_voltar_click.png")));
			}
			
			
			@Override
			public void mouseExited(MouseEvent arg0)
			{
				botaoVoltar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_voltar.png")));
			}
			
			
			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				botaoVoltar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_voltar_over.png")));
			}
			
			
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			}
		});
		
		botaoComecar = new JLabel("");
		botaoComecar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_comecar.png")));
		botaoComecar.setToolTipText("Processar do início");
		botaoComecar.addMouseListener(new MouseListener()
		{
			
			
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				botaoComecar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_comecar_over.png")));
				setBordaNormal();
				
				if (passosSimulacao != null) {
					passo = 0;
					painelAnimacao.deselecionarEstados();
					log.append("\n" + passo + "º Passo :\n");
					
					renderer.resetPosicoes();
					((DefaultTableModel) table.getModel()).setRowCount(0);
					int novaLargura = 0;
					
					for (int i = 0; i < passosSimulacao.get(passo).getFitas().size(); i++) {
						((DefaultTableModel) table.getModel()).addRow(new Object[] { i + 1, passosSimulacao.get(passo).getFitas().get(i) });
						renderer.addPosicao(i, 1, passosSimulacao.get(passo).getPosicaoFita().get(i));
						
						if ((passosSimulacao.get(passo).getFitas().get(i).length() * 11) > novaLargura) novaLargura = passosSimulacao.get(passo).getFitas().get(i).length() * 11;
					}
					
					if (novaLargura > 120) table.getColumnModel().getColumn(1).setPreferredWidth(novaLargura);
					
					table.updateUI();
					table.revalidate();
					table.repaint();
					
					if (passosSimulacao.get(passo).possuiFalha()) painelAnimacao.selecionarEstado(passosSimulacao.get(passo).getEstado(), Estado.ERRADO);
					else painelAnimacao.selecionarEstado(passosSimulacao.get(passo).getEstado(), Estado.SELECIONADO);
					
					//					posicionarPonteiro(passosSimulacao.get(passo).getPosicaoFita());
				}
			}
			
			
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				botaoComecar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_comecar_click.png")));
			}
			
			
			@Override
			public void mouseExited(MouseEvent arg0)
			{
				botaoComecar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_comecar.png")));
			}
			
			
			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				botaoComecar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_comecar_over.png")));
			}
			
			
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			}
		});
		
		botaoPlayPause = new JLabel("");
		botaoPlayPause.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_play_pause.png")));
		botaoPlayPause.setToolTipText("Executar/Pausar processamento");
		botaoPlayPause.addMouseListener(new MouseListener()
		{
			
			
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				if (!emExecucao) {
					botaoPlayPause.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_play_pause_over.png")));
					
					if (entrada.getText().length() == 0) entrada.setText("□");
					else entrada.setText(entrada.getText().replace(" ", "□"));
					
					if (painelAnimacao.validarAutomato()) {
						log.setText("");
						botaoPlayPause.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_play_pause_click.png")));
						emExecucao = true;
						
						entrada.setEditable(false);
						passosSimulacao = painelAnimacao.testarEntradaPP(entrada.getText().trim()); //Armazenando a simulação da entrada.
						
						if (passosSimulacao == null) { //Loop infinito.
							botaoPlayPause.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_play_pause_click.png")));
							
							emExecucao = false;
							entrada.setEditable(true);
							painelAnimacao.deselecionarEstados();
							setBordaNormal();
							janelaLoopInfinito.exibir();
							
							return;
						}
						
						passo = 0;
						
						//Primeiro passo:
						painelAnimacao.deselecionarEstados();
						painelAnimacao.selecionarEstado(passosSimulacao.get(passo).getEstado(), Estado.SELECIONADO);
						
						log.append("\n" + passo + "º Passo :\n");
						
						renderer.resetPosicoes();
						((DefaultTableModel) table.getModel()).setRowCount(0);
						
						for (int i = 0; i < passosSimulacao.get(passo).getFitas().size(); i++) {
							((DefaultTableModel) table.getModel()).addRow(new Object[] { i + 1, passosSimulacao.get(passo).getFitas().get(i) });
							renderer.addPosicao(i, 1, passosSimulacao.get(passo).getPosicaoFita().get(i));
						}
						
						int novaLargura = passosSimulacao.get(passo).getFitas().get(0).length() * 11;
						
						if (novaLargura > 120) table.getColumnModel().getColumn(1).setPreferredWidth(novaLargura);
						
						table.updateUI();
						table.revalidate();
						table.repaint();
					}
					else janelaAutomatoIncorreto.exibir();
				}
				else {
					table.getColumnModel().getColumn(1).setPreferredWidth(120);
					table.updateUI();
					table.revalidate();
					table.repaint();
					
					botaoPlayPause.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_play_pause_click.png")));
					
					emExecucao = false;
					entrada.setEditable(true);
					passosSimulacao = null;
					painelAnimacao.deselecionarEstados();
					setBordaNormal();
				}
			}
			
			
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				botaoPlayPause.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_play_pause_click.png")));
			}
			
			
			@Override
			public void mouseExited(MouseEvent arg0)
			{
				if (emExecucao) botaoPlayPause.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_play_pause_click.png")));
				else botaoPlayPause.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_play_pause.png")));
			}
			
			
			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				if (emExecucao) botaoPlayPause.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_play_pause_click.png")));
				else botaoPlayPause.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_play_pause_over.png")));
			}
			
			
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			}
		});
		
		botaoAvancar = new JLabel("");
		botaoAvancar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_avancar.png")));
		botaoAvancar.setToolTipText("Avançar instrução");
		botaoAvancar.addMouseListener(new MouseListener()
		{
			
			
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				botaoAvancar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_avancar_over.png")));
				
				if (passosSimulacao != null && passo < passosSimulacao.size() - 1) {
					++passo;
					painelAnimacao.deselecionarEstados();
					
					renderer.resetPosicoes();
					((DefaultTableModel) table.getModel()).setRowCount(0);
					int novaLargura = 0;
					
					for (int i = 0; i < passosSimulacao.get(passo).getFitas().size(); i++) {
						((DefaultTableModel) table.getModel()).addRow(new Object[] { i + 1, passosSimulacao.get(passo).getFitas().get(i) });
						renderer.addPosicao(i, 1, passosSimulacao.get(passo).getPosicaoFita().get(i));
						
						if ((passosSimulacao.get(passo).getFitas().get(i).length() * 11) > novaLargura) novaLargura = passosSimulacao.get(passo).getFitas().get(i).length() * 11;
					}
					
					if (novaLargura > 120) table.getColumnModel().getColumn(1).setPreferredWidth(novaLargura);
					
					table.updateUI();
					table.revalidate();
					table.repaint();
					
					log.append("\n" + passo + "º Passo :\n");
					
					if (passo == passosSimulacao.size() - 1) { //Último caractere da string de entrada.
						if (passosSimulacao.get(passo).possuiFalha() || !passosSimulacao.get(passo).getEstado().eFinal()) {
							painelAnimacao.selecionarEstado(passosSimulacao.get(passo).getEstado(), Estado.ERRADO);
							setBordaErrado();
							log.append("REJEITA TUDO;\n"); //Se rejeita, rejeita de todas as combinações.
						}
						else {
							painelAnimacao.selecionarEstado(passosSimulacao.get(passo).getEstado(), Estado.CORRETO);
							log.append("  Ler        " + passosSimulacao.get(passo).getLeitura() + ";\n");
							log.append("  Escrever   " + passosSimulacao.get(passo).getEscrita() + ";\n");
							log.append("  Movimentar " + passosSimulacao.get(passo).getMovimento() + ";\n");
							setBordaCorreto();
							log.append("ACEITA: q" + passosSimulacao.get(passo).getTransicao().getEstadoInicial().getIdxNome() + " -> q" + passosSimulacao.get(passo).getTransicao().getEstadoFinal().getIdxNome() + ", " + (passosSimulacao.get(passo).getIdxSimbolo() + 1) + "º símbolo;\n"); //Aceita pelo menos por 1 caminho.
						}
					}
					else {
						if (passosSimulacao.get(passo).possuiFalha()) {
							painelAnimacao.selecionarEstado(passosSimulacao.get(passo).getEstado(), Estado.ERRADO);
							log.append("REJEITA: q" + passosSimulacao.get(passo).getTransicao().getEstadoInicial().getIdxNome() + " -> q" + passosSimulacao.get(passo).getTransicao().getEstadoFinal().getIdxNome() + ", " + (passosSimulacao.get(passo).getIdxSimbolo() + 1) + "º símbolo, " + passosSimulacao.get(passo).getNumFita() + "ª fita;\n");
						}
						else {
							painelAnimacao.selecionarEstado(passosSimulacao.get(passo).getEstado(), Estado.SELECIONADO);
							if (passosSimulacao.get(passo).getLeitura() != null) {
								log.append("  Ler        " + passosSimulacao.get(passo).getLeitura() + ";\n");
								log.append("  Escrever   " + passosSimulacao.get(passo).getEscrita() + ";\n");
								log.append("  Movimentar " + passosSimulacao.get(passo).getMovimento() + ";\n");
							}
						}
					}
					
				}
			}
			
			
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				botaoAvancar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_avancar_click.png")));
			}
			
			
			@Override
			public void mouseExited(MouseEvent arg0)
			{
				botaoAvancar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_avancar.png")));
			}
			
			
			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				botaoAvancar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_avancar_over.png")));
			}
			
			
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			}
		});
		
		botaoTerminar = new JLabel("");
		botaoTerminar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_terminar.png")));
		botaoTerminar.setToolTipText("Terminar processamento");
		botaoTerminar.addMouseListener(new MouseListener()
		{
			
			
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				botaoTerminar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_terminar_over.png")));
				
				if (passosSimulacao != null && passo < passosSimulacao.size() - 1) {
					for (int i = passo; i < passosSimulacao.size() - 2; ++i) {
						log.append("\n" + i + "º Passo :\n");
						if (passosSimulacao.get(i).getLeitura() != null) {
							log.append("  Ler        " + passosSimulacao.get(i).getLeitura() + ";\n");
							log.append("  Escrever   " + passosSimulacao.get(i).getEscrita() + ";\n");
							log.append("  Movimentar " + passosSimulacao.get(i).getMovimento() + ";\n");
						}
					}
					
					passo = passosSimulacao.size() - 1;
					painelAnimacao.deselecionarEstados();
					log.append("\n" + passo + "º Passo :\n");
					
					renderer.resetPosicoes();
					((DefaultTableModel) table.getModel()).setRowCount(0);
					int novaLargura = 0;
					
					for (int i = 0; i < passosSimulacao.get(passo).getFitas().size(); i++) {
						((DefaultTableModel) table.getModel()).addRow(new Object[] { i + 1, passosSimulacao.get(passo).getFitas().get(i) });
						renderer.addPosicao(i, 1, passosSimulacao.get(passo).getPosicaoFita().get(i));
						
						if ((passosSimulacao.get(passo).getFitas().get(i).length() * 11) > novaLargura) novaLargura = passosSimulacao.get(passo).getFitas().get(i).length() * 11;
					}
					
					if (novaLargura > 120) table.getColumnModel().getColumn(1).setPreferredWidth(novaLargura);
					
					table.updateUI();
					table.revalidate();
					table.repaint();
					
					if (passosSimulacao.size() == 2 && passosSimulacao.get(passo).getLeitura() != null) {
						log.append("  Ler        " + passosSimulacao.get(passo).getLeitura() + ";\n");
						log.append("  Escrever   " + passosSimulacao.get(passo).getEscrita() + ";\n");
						log.append("  Movimentar " + passosSimulacao.get(passo).getMovimento() + ";\n");
					}
					
					if (passosSimulacao.get(passo).possuiFalha()) {
						painelAnimacao.selecionarEstado(passosSimulacao.get(passo).getEstado(), Estado.ERRADO);
						log.append("REJEITA;\n");
						setBordaErrado();
					}
					else {
						painelAnimacao.selecionarEstado(passosSimulacao.get(passo).getEstado(), Estado.CORRETO);
						log.append("ACEITA;\n");
						setBordaCorreto();
					}
				}
			}
			
			
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				botaoTerminar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_terminar_click.png")));
			}
			
			
			@Override
			public void mouseExited(MouseEvent arg0)
			{
				botaoTerminar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_terminar.png")));
			}
			
			
			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				botaoTerminar.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_terminar_over.png")));
			}
			
			
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup().addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(botaoVoltar, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE).addComponent(botaoComecar)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(botaoPlayPause).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addComponent(botaoTerminar).addComponent(botaoAvancar)).addContainerGap(24, Short.MAX_VALUE)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup().addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup().addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(botaoVoltar).addComponent(botaoAvancar)).addGap(15).addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addComponent(botaoComecar, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE).addComponent(botaoTerminar, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))).addGroup(gl_panel_1.createSequentialGroup().addGap(15).addComponent(botaoPlayPause))).addContainerGap(15, Short.MAX_VALUE)));
		panel_1.setLayout(gl_panel_1);
		
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setFillsViewportHeight(true);
		table.setFont(new Font("Monospaced", Font.PLAIN, 11));
		table.setModel(new DefaultTableModel(new Object[][] { { null, null }, }, new String[] { "N\u00BA fita", "Processamento" })
		{
			
			
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false };
			
			
			public boolean isCellEditable(int row, int column)
			{
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(0).setMaxWidth(60);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.setDefaultRenderer(Object.class, renderer);
		
		table.getTableHeader().setReorderingAllowed(false);
		JScrollPane scrollPane_1 = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JSeparator separator_1 = new JSeparator();
		
		JLabel lblControle = new JLabel("Controle");
		lblControle.setFont(new Font("Monospaced", Font.BOLD, 11));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblEntrada).addComponent(separator, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE).addComponent(lblControle).addGroup(gl_panel.createSequentialGroup().addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)).addGroup(gl_panel.createSequentialGroup().addComponent(lblProcesso).addContainerGap()).addComponent(entrada, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE).addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE).addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup().addComponent(lblEntrada).addGap(10).addComponent(entrada, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(11).addComponent(separator, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE).addGap(6).addComponent(lblControle).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE).addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblProcesso).addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
		table.setDefaultRenderer(Object.class, renderer);
		setVisible(false);
	}
	
	
	public void setPainelAnimacao(PainelAnimacao painelAnimacao)
	{
		this.painelAnimacao = painelAnimacao;
		janelaLoopInfinito.setInfoFuncionalidade(painelAnimacao.getInfoFuncionalidade());
	}
	
	
	public void exibir()
	{
		DefaultTableModel modeloTabela = (DefaultTableModel) table.getModel();
		
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.updateUI();
		table.revalidate();
		table.repaint();
		
		setLocationRelativeTo(null);
		log.setText("");
		modeloTabela.setRowCount(0);
		revalidate();
		repaint();
		setVisible(true);
	}
	
	
	public void setBordaNormal()
	{
		entrada.setBorder(new LineBorder(Color.BLACK, 1));
	}
	
	
	public void setBordaCorreto()
	{
		entrada.setBorder(new LineBorder(Color.GREEN, 2));
	}
	
	
	public void setBordaErrado()
	{
		entrada.setBorder(new LineBorder(Color.RED, 2));
	}
	
}
