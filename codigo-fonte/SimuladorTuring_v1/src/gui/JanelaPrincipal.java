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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import automato.Estado;
import automato.InfoFuncionalidade;
import automato.PainelAnimacao;
import automato.Simbolo;
import automato.Transicao;
import auxiliar_gui.AbaAutomato;


public class JanelaPrincipal extends JFrame implements WindowListener
{
	
	
	private static final long serialVersionUID = 1L;
	private JPanel painelPrincipal;
	private JLabel botaoEstado = new JLabel("");
	private JLabel botaoRemocao = new JLabel("");
	private JLabel botaoMover = new JLabel("");
	private JLabel botaoTransicao = new JLabel("");
	private JanelaSobreTuring janelaSobreTuring;
	private JanelaSimulacaoPassoAPasso janelaSimulacaoPassoAPasso;
	private JanelaSimulacaoMultiplasEntradas janelaSimulacaoMultiplasEntradas;
	private JanelaCreditos janelaCreditos = new JanelaCreditos();
	private JanelaAutomatoIncorreto janelaAutomatoIncorreto = new JanelaAutomatoIncorreto();
	private JanelaMaquinaInexistente janelaMaquinaInexistente = new JanelaMaquinaInexistente();
	private JTextField textField;
	private ArrayList<AbaAutomato> abas;
	private InfoFuncionalidade infoFuncionalidade = new InfoFuncionalidade();
	private JanelaNumeroFitas janelaNumeroFitas;
	private JanelaLimitePilha janelaLimitePilha;
	private int idxAbas = 0;
	private JTextField textField_1;
	protected final JTabbedPane tabbedPane;
	protected JButton button_1;
	
	
	public JanelaPrincipal()
	{
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		final JanelaPrincipal essaJanela = this;
		
		janelaNumeroFitas = new JanelaNumeroFitas();
		janelaLimitePilha = new JanelaLimitePilha(infoFuncionalidade);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Monospaced", Font.BOLD, 11));
		textField = new JTextField();
		
		tabbedPane.addChangeListener(new ChangeListener()
		{
			
			
			@Override
			public void stateChanged(ChangeEvent e)
			{
				for (AbaAutomato aba : abas) {
					if (aba.getScrollAnimacao() == tabbedPane.getSelectedComponent()) {
						if (aba.getArquivo() != null) {
							if (aba.getPainelAnimacao().getMaquinaModificada()) textField.setText("* " + aba.getArquivo().getName());
							else textField.setText(aba.getArquivo().getName());
						}
						else textField.setText("* Novo arquivo! *");
						
						textField_1.setText(aba.getNumFitas() + "");
						break;
					}
				}
			}
		});
		
		abas = new ArrayList<AbaAutomato>();
		janelaSimulacaoMultiplasEntradas = new JanelaSimulacaoMultiplasEntradas();
		setFont(new Font("Monospaced", Font.BOLD, 12));
		setTitle("Simulador de Máquinas de Turing - v1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 464);
		setMinimumSize(new Dimension(400, 300));
		setLocationRelativeTo(null);
		
		JMenuBar barraMenu = new JMenuBar();
		setJMenuBar(barraMenu);
		
		JMenu mnArquivo = new JMenu("Arquivo");
		mnArquivo.setFont(new Font("Monospaced", Font.PLAIN, 12));
		barraMenu.add(mnArquivo);
		
		JMenuItem mntmAbrir = new JMenuItem("Abrir");
		mntmAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmAbrir.setFont(new Font("Monospaced", Font.PLAIN, 12));
		mntmAbrir.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();
				
				fileChooser.setDialogTitle("Abrir");
				fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos do JFlap (.jff)", "jff"));
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				int userSelection = fileChooser.showOpenDialog(getParent());
				
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					ArrayList<Estado> estados = new ArrayList<Estado>();
					File file = fileChooser.getSelectedFile();
					BufferedReader reader = null;
					
					try {
						PainelAnimacao pA;
						Estado origem = null, destino = null;
						String linha, aux[], leitura, escrita, movimento;
						int numFitas, indice, idxOrigem, idxDestino;
						float x, y;
						
						reader = new BufferedReader(new FileReader(file));
						reader.readLine(); //<?xml version="1.0" encoding="UTF-8" standalone="no"?><!--Criado com o Simulador de Turing v1.--><structure>
						reader.readLine(); //	<type>turing</type>
						numFitas = Integer.valueOf(reader.readLine().replaceAll("<tapes>", "").replaceAll("</tapes>", "").trim()); //	<tapes>número de fitas</tapes>
						reader.readLine(); //	<automaton>
						reader.readLine(); //		<!--Lista de estados.-->
						
						pA = new PainelAnimacao(infoFuncionalidade, numFitas, essaJanela);
						
						while ((linha = reader.readLine()) != null) {
							if (linha.contains("<!--")) break; //		<!--Lista de transições.-->
							
							aux = linha.replaceAll("<block", "").replaceAll(">", "").trim().split(" "); //		<block id="índice do estado" name="nome do estado">
							indice = Integer.valueOf(aux[0].replaceAll("\"", "").replaceAll("id=", "").trim());
							reader.readLine(); //			<tag>Machine<índice></tag>
							x = Float.valueOf(reader.readLine().replaceAll("<x>", "").replaceAll("</x>", "").trim()); //			<x>posição horizontal</x>
							y = Float.valueOf(reader.readLine().replaceAll("<y>", "").replaceAll("</y>", "").trim()); //			<y>posição vertical</y>
							linha = reader.readLine();
							
							estados.add(new Estado((int) x, (int) y, indice, pA));
							
							if (!linha.contains("</block>")) { //Então tem INICIAL ou FINAL.
								boolean inicial = false;
								
								//O estado INICIAL é SEMPRE (SEMPRE) escrito antes do FINAL no arquivo:
								if (linha.contains("<initial/>")) {
									inicial = true;
									estados.get(estados.size() - 1).setBounds(estados.get(estados.size() - 1).getX(), estados.get(estados.size() - 1).getY(), 75, 50);
									estados.get(estados.size() - 1).setEstadoAtual(Estado.ESTADO_INICIAL);
								}
								else { //Então é FINAL: só tem os 2 estados.
									estados.get(estados.size() - 1).setEstadoAtual(Estado.ESTADO_FINAL);
									linha = reader.readLine();
									continue;
								}
								
								if ((linha = reader.readLine()).contains("<final/>")) {
									if (inicial) estados.get(estados.size() - 1).setEstadoAtual(Estado.ESTADO_INICIAL_FINAL);
									else estados.get(estados.size() - 1).setEstadoAtual(Estado.ESTADO_FINAL);
								}
								else
								; //Se não for FINAL, é o final do bloco.
							}
						}
						
						//<!--Lista de transições.-->
						
						while ((linha = reader.readLine()) != null) {
							if (linha.contains("<!--")) break; //		<!--Lista de autômatos.-->
							
							//	                        reader.readLine();    //		<transition>
							idxOrigem = Integer.valueOf(reader.readLine().replaceAll("<from>", "").replaceAll("</from>", "").trim()); //			<from>índice do estado</from>
							
							for (Estado e2 : estados) {
								if (e2.getIdxNome() == idxOrigem) {
									origem = e2;
									break;
								}
							}
							
							idxDestino = Integer.valueOf(reader.readLine().replaceAll("<to>", "").replaceAll("</to>", "").trim()); //			<to>índice do estado</to>
							
							for (Estado e2 : estados) {
								if (e2.getIdxNome() == idxDestino) {
									destino = e2;
									break;
								}
							}
							
							//ADICIONAR TRANSIÇÃO:
							Transicao novaTransicao;
							String simbolo = "";
							
							for (int i = 0; i < numFitas; i++) {
								aux = reader.readLine().replaceAll("<read", "").replaceAll("/>", "").replaceAll("</read>", "").replaceAll(">", "").replaceAll("tape=\"", "").trim().split("\"");
								
								if (aux.length == 1) leitura = "□"; //Símbolo vazio.
								else leitura = aux[1];
								
								aux = reader.readLine().replaceAll("<write", "").replaceAll("/>", "").replaceAll("</write>", "").replaceAll(">", "").replaceAll("tape=\"", "").trim().split("\"");
								
								if (aux.length == 1) escrita = "□"; //Símbolo vazio.
								else escrita = aux[1];
								
								aux = reader.readLine().replaceAll("<move", "").replaceAll("</move>", "").replaceAll(">", "").replaceAll("tape=\"", "").trim().split("\"");
								
								switch (aux[1]) {
									case "R": { //Right.
										movimento = "D";
									}
									break;
									case "L": { //Left.
										movimento = "E";
									}
									break;
									default: { //Stop.
										movimento = "P";
									}
								}
								
								simbolo += leitura + ";" + escrita + ";" + movimento;
								
								if (i + 1 < numFitas) simbolo += "-";
							}
							
							if (origem.eInicial()) novaTransicao = new Transicao(origem.getX() + 50, origem.getY() + 25, pA);
							else novaTransicao = new Transicao(origem.getX() + 25, origem.getY() + 25, pA);
							
							novaTransicao.adicionarEstadoInicial(origem); //Adicionando esse estado no início do arco.
							
							boolean jaExisteTransicao = false;
							
							//Verificando se já existe um arco para essa transição:
							for (Transicao arcoInicioAutomatoInicio : origem.getTransicoesSaida()) {
								if (arcoInicioAutomatoInicio.getEstadoFinal() == destino) { //Já existe um arco criado para essa transição. Impossível criar mais do que duas linhas entre dois estados; pode editar apenas.
									arcoInicioAutomatoInicio.setSimboloPorArquivo(simbolo); //Invocando a criação de um novo símbolo.
									
									jaExisteTransicao = true;
								}
							}
							
							if (!jaExisteTransicao) {
								novaTransicao.setSimboloPorArquivo(simbolo); //Invocando a criação do primeiro símbolo.
								
								//Ainda não existe o arco de transição para essa relação de estados:
								origem.setTransicoesSaida(novaTransicao); //Conectando o arco temporário ao estado criador.
								novaTransicao.adicionarEstadoFinal(destino); //Adicionando esse estado no fim do arco.
								destino.setTransicoesEntrada(novaTransicao); //Adicionando o arco na lista de arcos finais desse estado.
								
								//Atualizando as coordenadas do fim do arco:
								if (destino.eInicial()) novaTransicao.setPontoFinal(destino.getX() + 50, destino.getY() + 25);
								else novaTransicao.setPontoFinal(destino.getX() + 25, destino.getY() + 25);
								
								novaTransicao.setFixacao(); //Deixando a seta apontando para a borda do estado.
								
								//Há ciclo:
								if (origem == destino) {
									novaTransicao.setPontoFinal(destino.getX() + 40, destino.getY() + 15);
									novaTransicao.setPontoInicial(origem.getX() + 10, origem.getY() + 15);
									novaTransicao.setCurva(0, 80);
								}
								else { //Verificando se possui transição no sentido inverso:
									Transicao transicaoInversa = null; //Supondo que não existe transição inversa entre os dois estados.
									
									for (Transicao t : destino.getTransicoesSaida()) {
										if (t.getEstadoFinal() == origem) {
											transicaoInversa = t; //Existe transição inversa entre os estados.
											break;
										}
									}
									
									if (transicaoInversa != null) { //Tem que formar curva nos arcos que conectam este autômato e o autômato inicial do arco atual.
										novaTransicao.setEspacoCurva(true);
										novaTransicao.setCurva(0, 50);
										
										transicaoInversa.setEspacoCurva(true);
										transicaoInversa.setCurva(0, 50);
									}
								}
								
								novaTransicao.atualizarTransicao();
							}
							
							reader.readLine();
						}
						
						reader.close(); //O resto do arquivo é inútil!
						int maiorIndice = 0;
						
						//Adicionando graficamente ao painel:
						for (Estado e2 : estados) {
							pA.add(e2);
							
							if (e2.getIdxNome() > maiorIndice) maiorIndice = e2.getIdxNome();
							pA.redimensionarPainel(e2.getX(), e2.getY());
							
							for (Transicao t2 : e2.getTransicoesSaida()) {
								for (Simbolo s2 : t2.getSimbolos()) {
									pA.add(s2); //Adicionando corações... Ahhh, o amor.
								}
							}
						}
						
						pA.setEstados(estados); //Adicionando logicamente ao painel.
						pA.setIdEstado(maiorIndice + 1);
						
						if (pA.getIdEstado() == 100000) {
							infoFuncionalidade.exibirExcesso();
							return;
						}
						
						pA.revalidate();
						pA.repaint();
						
						abas.add(new AbaAutomato(infoFuncionalidade, numFitas, essaJanela, pA));
						abas.get(abas.size() - 1).getScrollAnimacao().setName(String.valueOf(idxAbas));
						tabbedPane.addTab(String.valueOf(idxAbas), null, abas.get(abas.size() - 1).getScrollAnimacao(), null);
						abas.get(abas.size() - 1).setNumFitas(numFitas);
						abas.get(abas.size() - 1).setArquivo(file);
						abas.get(abas.size() - 1).getPainelAnimacao().setMaquinaModificada(false);
						
						if (abas.size() == 1) {
							tabbedPane.setSelectedComponent(abas.get(abas.size() - 1).getScrollAnimacao()); //PENSAR SE É MESMO SELECIONADA QUANDO ABRE...
							textField.setText(file.getName());
							textField_1.setText(numFitas + "");
						}
						
						++idxAbas;
					}
					catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(null, "Erro ao abrir arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
					catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Erro ao abrir arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
			}
		});
		mnArquivo.add(mntmAbrir);
		
		JMenuItem mntmSalvar = new JMenuItem("Salvar");
		mntmSalvar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmSalvar.setFont(new Font("Monospaced", Font.PLAIN, 12));
		mntmSalvar.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (abas.size() == 0) {
					JOptionPane.showMessageDialog(null, "Não há nada pra salvar!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				JFileChooser fileChooser = new JFileChooser();
				AbaAutomato abaSelecionada = null;
				
				fileChooser.setDialogTitle("Salvar");
				fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos do JFlap (.jff)", "jff"));
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				for (AbaAutomato aba : abas) {
					if (aba.getScrollAnimacao() == tabbedPane.getSelectedComponent()) {
						abaSelecionada = aba;
						break;
					}
				}
				
				if (abaSelecionada.getArquivo() != null) { //Arquivo já existe.
				
					File fileToSave = abaSelecionada.getArquivo();
					
					FileWriter writer;
					
					try {
						writer = new FileWriter(fileToSave);
						
						String aux = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!--Criado com o Simulador de Turing v1.--><structure>\n	<type>turing</type>\n	<tapes>";
						
						abaSelecionada.getPainelAnimacao().removerTransicaoTemporaria();
						
						aux += abaSelecionada.getPainelAnimacao().getNumFitas() + "</tapes>\n	<automaton>\n		<!--Lista de estados.-->\n";
						writer.write(aux);
						aux = "";
						
						for (Estado e : abaSelecionada.getPainelAnimacao().getEstados()) {
							aux += "		<block id=\"" + e.getIdxNome() + "\" name=\"" + e.getText().trim() + "\">\n			<tag>Machine" + e.getIdxNome() + "</tag>\n			<x>" + e.getX() + "</x>\n			<y>" + e.getY() + "</y>\n";
							
							if (e.eInicial()) aux += "			<initial/>\n";
							if (e.eFinal()) aux += "			<final/>\n";
							
							aux += "		</block>\n";
						}
						
						aux += "		<!--Lista de transições.-->\n";
						writer.write(aux);
						aux = "";
						
						for (Estado e : abaSelecionada.getPainelAnimacao().getEstados()) {
							for (Transicao t : e.getTransicoesSaida()) {
								for (Simbolo s : t.getSimbolos()) {
									aux += "		<transition>\n			<from>" + e.getIdxNome() + "</from>\n			<to>" + t.getEstadoFinal().getIdxNome() + "</to>\n";
									
									for (int i = 0; i < abaSelecionada.getPainelAnimacao().getNumFitas(); i++) {
										aux += "			<read tape=\"" + (i + 1) + "\"";
										
										if (s.getLeitura().get(i).equals("□")) aux += "/>\n";
										else {
											aux += ">" + s.getLeitura().get(i) + "</read>\n";
										}
										
										aux += "			<write tape=\"" + (i + 1) + "\"";
										
										if (s.getEscrita().get(i).equals("□")) aux += "/>\n";
										else {
											aux += ">" + s.getEscrita().get(i) + "</write>\n";
										}
										
										aux += "			<move tape=\"" + (i + 1) + "\"";
										
										switch (s.getMovimento().get(i)) {
											case "D": {
												aux += ">R</move>\n";
											}
											break;
											case "E": {
												aux += ">L</move>\n";
											}
											break;
											default: {
												aux += ">S</move>\n";
											}
										}
									}
									
									aux += "		</transition>\n";
									
									writer.write(aux);
									aux = "";
								}
							}
						}
						
						aux += "		<!--Lista de autômatos.-->\n";
						
						for (Estado e : abaSelecionada.getPainelAnimacao().getEstados()) {
							aux += "		<Machine" + e.getIdxNome() + "/>\n";
						}
						
						aux += "	</automaton>\n</structure>";
						
						writer.write(aux);
						writer.close();
						
						abaSelecionada.setArquivo(fileToSave);
						textField.setText(fileToSave.getName());
						abaSelecionada.getPainelAnimacao().setMaquinaModificada(false);
						
					}
					catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Erro ao salvar arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
				else { //Arquivo ainda não salvo.
					int userSelection = fileChooser.showSaveDialog(getParent());
					
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						File fileToSave = fileChooser.getSelectedFile();
						
						fileToSave = new File(fileToSave.getPath() + ".jff");
						
						if (fileToSave.exists()) JOptionPane.showMessageDialog(null, "Arquivo já existe!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
						else {
							FileWriter writer;
							
							try {
								writer = new FileWriter(fileToSave);
								
								String aux = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!--Criado com o Simulador de Turing v1.--><structure>\n	<type>turing</type>\n	<tapes>";
								
								for (AbaAutomato aba : abas) {
									if (aba.getScrollAnimacao() == tabbedPane.getSelectedComponent()) {
										aba.getPainelAnimacao().removerTransicaoTemporaria();
										
										aux += aba.getPainelAnimacao().getNumFitas() + "</tapes>\n	<automaton>\n		<!--Lista de estados.-->\n";
										writer.write(aux);
										aux = "";
										
										for (Estado e : aba.getPainelAnimacao().getEstados()) {
											aux += "		<block id=\"" + e.getIdxNome() + "\" name=\"" + e.getText().trim() + "\">\n			<tag>Machine" + e.getIdxNome() + "</tag>\n			<x>" + e.getX() + "</x>\n			<y>" + e.getY() + "</y>\n";
											
											if (e.eInicial()) aux += "			<initial/>\n";
											if (e.eFinal()) aux += "			<final/>\n";
											
											aux += "		</block>\n";
										}
										
										aux += "		<!--Lista de transições.-->\n";
										writer.write(aux);
										aux = "";
										
										for (Estado e : aba.getPainelAnimacao().getEstados()) {
											for (Transicao t : e.getTransicoesSaida()) {
												for (Simbolo s : t.getSimbolos()) {
													aux += "		<transition>\n			<from>" + e.getIdxNome() + "</from>\n			<to>" + t.getEstadoFinal().getIdxNome() + "</to>\n";
													
													for (int i = 0; i < aba.getPainelAnimacao().getNumFitas(); i++) {
														aux += "			<read tape=\"" + (i + 1) + "\"";
														
														if (s.getLeitura().get(i).equals("□")) aux += "/>\n";
														else {
															aux += ">" + s.getLeitura().get(i) + "</read>\n";
														}
														
														aux += "			<write tape=\"" + (i + 1) + "\"";
														
														if (s.getEscrita().get(i).equals("□")) aux += "/>\n";
														else {
															aux += ">" + s.getEscrita().get(i) + "</write>\n";
														}
														
														aux += "			<move tape=\"" + (i + 1) + "\"";
														
														switch (s.getMovimento().get(i)) {
															case "D": {
																aux += ">R</move>\n";
															}
															break;
															case "E": {
																aux += ">L</move>\n";
															}
															break;
															default: {
																aux += ">S</move>\n";
															}
														}
													}
													
													aux += "		</transition>\n";
													
													writer.write(aux);
													aux = "";
												}
											}
										}
										
										aux += "		<!--Lista de autômatos.-->\n";
										
										for (Estado e : aba.getPainelAnimacao().getEstados()) {
											aux += "		<Machine" + e.getIdxNome() + "/>\n";
										}
										
										aux += "	</automaton>\n</structure>";
										
										writer.write(aux);
										writer.close();
										
										aba.setArquivo(fileToSave);
										textField.setText(fileToSave.getName());
										aba.getPainelAnimacao().setMaquinaModificada(false);
										
										break;
									}
								}
							}
							catch (IOException e1) {
								JOptionPane.showMessageDialog(null, "Erro ao salvar arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
		
		JMenuItem mntmAbrirVrios = new JMenuItem("Abrir vários...");
		mntmAbrirVrios.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmAbrirVrios.setFont(new Font("Monospaced", Font.PLAIN, 12));
		mntmAbrirVrios.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();
				
				fileChooser.setDialogTitle("Abrir vários...");
				fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos do JFlap (.jff)", "jff"));
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setMultiSelectionEnabled(true);
				
				int userSelection = fileChooser.showOpenDialog(getParent());
				
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File[] arquivos = fileChooser.getSelectedFiles();
					
					for (int f = 0; f < arquivos.length; f++) { //Abrindo vários arquivos ao mesmo tempo.
						ArrayList<Estado> estados = new ArrayList<Estado>();
						File file = arquivos[f];
						BufferedReader reader = null;
						
						try {
							PainelAnimacao pA;
							Estado origem = null, destino = null;
							String linha, aux[], leitura, escrita, movimento;
							int numFitas, indice, idxOrigem, idxDestino;
							float x, y;
							
							reader = new BufferedReader(new FileReader(file));
							reader.readLine(); //<?xml version="1.0" encoding="UTF-8" standalone="no"?><!--Criado com o Simulador de Turing v1.--><structure>
							reader.readLine(); //	<type>turing</type>
							numFitas = Integer.valueOf(reader.readLine().replaceAll("<tapes>", "").replaceAll("</tapes>", "").trim()); //	<tapes>número de fitas</tapes>
							reader.readLine(); //	<automaton>
							reader.readLine(); //		<!--Lista de estados.-->
							
							pA = new PainelAnimacao(infoFuncionalidade, numFitas, essaJanela);
							
							while ((linha = reader.readLine()) != null) {
								if (linha.contains("<!--")) break; //		<!--Lista de transições.-->
								
								aux = linha.replaceAll("<block", "").replaceAll(">", "").trim().split(" "); //		<block id="índice do estado" name="nome do estado">
								indice = Integer.valueOf(aux[0].replaceAll("\"", "").replaceAll("id=", "").trim());
								reader.readLine(); //			<tag>Machine<índice></tag>
								x = Float.valueOf(reader.readLine().replaceAll("<x>", "").replaceAll("</x>", "").trim()); //			<x>posição horizontal</x>
								y = Float.valueOf(reader.readLine().replaceAll("<y>", "").replaceAll("</y>", "").trim()); //			<y>posição vertical</y>
								linha = reader.readLine();
								
								estados.add(new Estado((int) x, (int) y, indice, pA));
								
								if (!linha.contains("</block>")) { //Então tem INICIAL ou FINAL.
									boolean inicial = false;
									
									//O estado INICIAL é SEMPRE (SEMPRE) escrito antes do FINAL no arquivo:
									if (linha.contains("<initial/>")) {
										inicial = true;
										estados.get(estados.size() - 1).setBounds(estados.get(estados.size() - 1).getX(), estados.get(estados.size() - 1).getY(), 75, 50);
										estados.get(estados.size() - 1).setEstadoAtual(Estado.ESTADO_INICIAL);
									}
									else { //Então é FINAL: só tem os 2 estados.
										estados.get(estados.size() - 1).setEstadoAtual(Estado.ESTADO_FINAL);
										linha = reader.readLine();
										continue;
									}
									
									if ((linha = reader.readLine()).contains("<final/>")) {
										if (inicial) estados.get(estados.size() - 1).setEstadoAtual(Estado.ESTADO_INICIAL_FINAL);
										else estados.get(estados.size() - 1).setEstadoAtual(Estado.ESTADO_FINAL);
									}
									else
									; //Se não for FINAL, é o final do bloco.
								}
							}
							
							//<!--Lista de transições.-->
							
							while ((linha = reader.readLine()) != null) {
								if (linha.contains("<!--")) break; //		<!--Lista de autômatos.-->
								
								//	                        reader.readLine();    //		<transition>
								idxOrigem = Integer.valueOf(reader.readLine().replaceAll("<from>", "").replaceAll("</from>", "").trim()); //			<from>índice do estado</from>
								
								for (Estado e2 : estados) {
									if (e2.getIdxNome() == idxOrigem) {
										origem = e2;
										break;
									}
								}
								
								idxDestino = Integer.valueOf(reader.readLine().replaceAll("<to>", "").replaceAll("</to>", "").trim()); //			<to>índice do estado</to>
								
								for (Estado e2 : estados) {
									if (e2.getIdxNome() == idxDestino) {
										destino = e2;
										break;
									}
								}
								
								//ADICIONAR TRANSIÇÃO:
								Transicao novaTransicao;
								String simbolo = "";
								
								for (int i = 0; i < numFitas; i++) {
									aux = reader.readLine().replaceAll("<read", "").replaceAll("/>", "").replaceAll("</read>", "").replaceAll(">", "").replaceAll("tape=\"", "").trim().split("\"");
									
									if (aux.length == 1) leitura = "□"; //Símbolo vazio.
									else leitura = aux[1];
									
									aux = reader.readLine().replaceAll("<write", "").replaceAll("/>", "").replaceAll("</write>", "").replaceAll(">", "").replaceAll("tape=\"", "").trim().split("\"");
									
									if (aux.length == 1) escrita = "□"; //Símbolo vazio.
									else escrita = aux[1];
									
									aux = reader.readLine().replaceAll("<move", "").replaceAll("</move>", "").replaceAll(">", "").replaceAll("tape=\"", "").trim().split("\"");
									
									switch (aux[1]) {
										case "R": { //Right.
											movimento = "D";
										}
										break;
										case "L": { //Left.
											movimento = "E";
										}
										break;
										default: { //Stop.
											movimento = "P";
										}
									}
									
									simbolo += leitura + ";" + escrita + ";" + movimento;
									
									if (i + 1 < numFitas) simbolo += "-";
								}
								
								if (origem.eInicial()) novaTransicao = new Transicao(origem.getX() + 50, origem.getY() + 25, pA);
								else novaTransicao = new Transicao(origem.getX() + 25, origem.getY() + 25, pA);
								
								novaTransicao.adicionarEstadoInicial(origem); //Adicionando esse estado no início do arco.
								
								boolean jaExisteTransicao = false;
								
								//Verificando se já existe um arco para essa transição:
								for (Transicao arcoInicioAutomatoInicio : origem.getTransicoesSaida()) {
									if (arcoInicioAutomatoInicio.getEstadoFinal() == destino) { //Já existe um arco criado para essa transição. Impossível criar mais do que duas linhas entre dois estados; pode editar apenas.
										arcoInicioAutomatoInicio.setSimboloPorArquivo(simbolo); //Invocando a criação de um novo símbolo.
										
										jaExisteTransicao = true;
									}
								}
								
								if (!jaExisteTransicao) {
									novaTransicao.setSimboloPorArquivo(simbolo); //Invocando a criação do primeiro símbolo.
									
									//Ainda não existe o arco de transição para essa relação de estados:
									origem.setTransicoesSaida(novaTransicao); //Conectando o arco temporário ao estado criador.
									novaTransicao.adicionarEstadoFinal(destino); //Adicionando esse estado no fim do arco.
									destino.setTransicoesEntrada(novaTransicao); //Adicionando o arco na lista de arcos finais desse estado.
									
									//Atualizando as coordenadas do fim do arco:
									if (destino.eInicial()) novaTransicao.setPontoFinal(destino.getX() + 50, destino.getY() + 25);
									else novaTransicao.setPontoFinal(destino.getX() + 25, destino.getY() + 25);
									
									novaTransicao.setFixacao(); //Deixando a seta apontando para a borda do estado.
									
									//Há ciclo:
									if (origem == destino) {
										novaTransicao.setPontoFinal(destino.getX() + 40, destino.getY() + 15);
										novaTransicao.setPontoInicial(origem.getX() + 10, origem.getY() + 15);
										novaTransicao.setCurva(0, 80);
									}
									else { //Verificando se possui transição no sentido inverso:
										Transicao transicaoInversa = null; //Supondo que não existe transição inversa entre os dois estados.
										
										for (Transicao t : destino.getTransicoesSaida()) {
											if (t.getEstadoFinal() == origem) {
												transicaoInversa = t; //Existe transição inversa entre os estados.
												break;
											}
										}
										
										if (transicaoInversa != null) { //Tem que formar curva nos arcos que conectam este autômato e o autômato inicial do arco atual.
											novaTransicao.setEspacoCurva(true);
											novaTransicao.setCurva(0, 50);
											
											transicaoInversa.setEspacoCurva(true);
											transicaoInversa.setCurva(0, 50);
										}
									}
									
									novaTransicao.atualizarTransicao();
								}
								
								reader.readLine();
							}
							
							reader.close(); //O resto do arquivo é inútil!
							int maiorIndice = 0;
							
							//Adicionando graficamente ao painel:
							for (Estado e2 : estados) {
								pA.add(e2);
								
								if (e2.getIdxNome() > maiorIndice) maiorIndice = e2.getIdxNome();
								pA.redimensionarPainel(e2.getX(), e2.getY());
								
								for (Transicao t2 : e2.getTransicoesSaida()) {
									for (Simbolo s2 : t2.getSimbolos()) {
										pA.add(s2); //Adicionando corações... Ahhh, o amor.
									}
								}
							}
							
							pA.setEstados(estados); //Adicionando logicamente ao painel.
							pA.setIdEstado(maiorIndice + 1);
							
							if (pA.getIdEstado() == 100000) {
								infoFuncionalidade.exibirExcesso();
								return;
							}
							
							pA.revalidate();
							pA.repaint();
							
							abas.add(new AbaAutomato(infoFuncionalidade, numFitas, essaJanela, pA));
							abas.get(abas.size() - 1).getScrollAnimacao().setName(String.valueOf(idxAbas));
							tabbedPane.addTab(String.valueOf(idxAbas), null, abas.get(abas.size() - 1).getScrollAnimacao(), null);
							abas.get(abas.size() - 1).setNumFitas(numFitas);
							abas.get(abas.size() - 1).setArquivo(file);
							abas.get(abas.size() - 1).getPainelAnimacao().setMaquinaModificada(false);
							
							if (abas.size() == 1) {
								tabbedPane.setSelectedComponent(abas.get(abas.size() - 1).getScrollAnimacao()); //PENSAR SE É MESMO SELECIONADA QUANDO ABRE...
								textField.setText(file.getName());
								textField_1.setText(numFitas + "");
							}
							
							++idxAbas;
						}
						catch (FileNotFoundException e1) {
							JOptionPane.showMessageDialog(null, "Erro ao abrir arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
						catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "Erro ao abrir arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
					}
				}
			}
		});
		mnArquivo.add(mntmAbrirVrios);
		
		mnArquivo.add(mntmSalvar);
		
		JMenuItem mntmSalvarComo = new JMenuItem("Salvar como...");
		mntmSalvarComo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmSalvarComo.setFont(new Font("Monospaced", Font.PLAIN, 12));
		mntmSalvarComo.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (abas.size() == 0) {
					JOptionPane.showMessageDialog(null, "Não há nada pra salvar!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				JFileChooser fileChooser = new JFileChooser();
				
				fileChooser.setDialogTitle("Salvar como...");
				fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos do JFlap (.jff)", "jff"));
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				int userSelection = fileChooser.showSaveDialog(getParent());
				
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					
					fileToSave = new File(fileToSave.getPath() + ".jff");
					
					if (fileToSave.exists()) JOptionPane.showMessageDialog(null, "Arquivo já existe!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					else {
						FileWriter writer;
						
						try {
							writer = new FileWriter(fileToSave);
							
							String aux = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!--Criado com o Simulador de Turing v1.--><structure>\n	<type>turing</type>\n	<tapes>";
							
							for (AbaAutomato aba : abas) {
								if (aba.getScrollAnimacao() == tabbedPane.getSelectedComponent()) {
									aba.getPainelAnimacao().removerTransicaoTemporaria();
									
									aux += aba.getPainelAnimacao().getNumFitas() + "</tapes>\n	<automaton>\n		<!--Lista de estados.-->\n";
									writer.write(aux);
									aux = "";
									
									for (Estado e : aba.getPainelAnimacao().getEstados()) {
										aux += "		<block id=\"" + e.getIdxNome() + "\" name=\"" + e.getText().trim() + "\">\n			<tag>Machine" + e.getIdxNome() + "</tag>\n			<x>" + e.getX() + "</x>\n			<y>" + e.getY() + "</y>\n";
										
										if (e.eInicial()) aux += "			<initial/>\n";
										if (e.eFinal()) aux += "			<final/>\n";
										
										aux += "		</block>\n";
									}
									
									aux += "		<!--Lista de transições.-->\n";
									writer.write(aux);
									aux = "";
									
									for (Estado e : aba.getPainelAnimacao().getEstados()) {
										for (Transicao t : e.getTransicoesSaida()) {
											for (Simbolo s : t.getSimbolos()) {
												aux += "		<transition>\n			<from>" + e.getIdxNome() + "</from>\n			<to>" + t.getEstadoFinal().getIdxNome() + "</to>\n";
												
												for (int i = 0; i < aba.getPainelAnimacao().getNumFitas(); i++) {
													aux += "			<read tape=\"" + (i + 1) + "\"";
													
													if (s.getLeitura().get(i).equals("□")) aux += "/>\n";
													else {
														aux += ">" + s.getLeitura().get(i) + "</read>\n";
													}
													
													aux += "			<write tape=\"" + (i + 1) + "\"";
													
													if (s.getEscrita().get(i).equals("□")) aux += "/>\n";
													else {
														aux += ">" + s.getEscrita().get(i) + "</write>\n";
													}
													
													aux += "			<move tape=\"" + (i + 1) + "\"";
													
													switch (s.getMovimento().get(i)) {
														case "D": {
															aux += ">R</move>\n";
														}
														break;
														case "E": {
															aux += ">L</move>\n";
														}
														break;
														default: {
															aux += ">S</move>\n";
														}
													}
												}
												
												aux += "		</transition>\n";
												
												writer.write(aux);
												aux = "";
											}
										}
									}
									
									aux += "		<!--Lista de autômatos.-->\n";
									
									for (Estado e : aba.getPainelAnimacao().getEstados()) {
										aux += "		<Machine" + e.getIdxNome() + "/>\n";
									}
									
									aux += "	</automaton>\n</structure>";
									
									writer.write(aux);
									writer.close();
									
									break;
								}
							}
						}
						catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "Erro ao salvar arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		mnArquivo.add(mntmSalvarComo);
		
		JMenu mnExecutar = new JMenu("Simulação");
		mnExecutar.setFont(new Font("Monospaced", Font.PLAIN, 12));
		barraMenu.add(mnExecutar);
		
		JMenuItem mntmEntradasMltiplas = new JMenuItem("Múltiplas entradas");
		mntmEntradasMltiplas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_MASK));
		mntmEntradasMltiplas.setFont(new Font("Monospaced", Font.PLAIN, 12));
		mntmEntradasMltiplas.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (abas.size() == 0) {
					JOptionPane.showMessageDialog(null, "Não há nada pra simular!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					
					return;
				}
				
				for (AbaAutomato aba : abas) {
					if (aba.getScrollAnimacao().getName().equals(tabbedPane.getComponentAt(tabbedPane.getSelectedIndex()).getName())) {
						if (aba.getPainelAnimacao().getEstados().size() < 2) janelaMaquinaInexistente.exibir();
						else if (!aba.getPainelAnimacao().validarAutomato()) janelaAutomatoIncorreto.exibir();
						else {
							janelaSimulacaoMultiplasEntradas.setPainelAnimacao(aba.getPainelAnimacao());
							janelaSimulacaoMultiplasEntradas.exibir();
						}
						
						break;
					}
				}
			}
		});
		mnExecutar.add(mntmEntradasMltiplas);
		
		JMenuItem mntmPassoAPasso = new JMenuItem("Passo a passo");
		mntmPassoAPasso.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_MASK));
		mntmPassoAPasso.setFont(new Font("Monospaced", Font.PLAIN, 12));
		mntmPassoAPasso.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (abas.size() == 0) {
					JOptionPane.showMessageDialog(null, "Não há nada pra simular!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					
					return;
				}
				
				for (AbaAutomato aba : abas) {
					if (aba.getScrollAnimacao().getName().equals(tabbedPane.getComponentAt(tabbedPane.getSelectedIndex()).getName())) {
						if (aba.getPainelAnimacao().getEstados().size() < 2) janelaMaquinaInexistente.exibir();
						else if (!aba.getPainelAnimacao().validarAutomato()) janelaAutomatoIncorreto.exibir();
						else {
							janelaSimulacaoPassoAPasso.setPainelAnimacao(aba.getPainelAnimacao());
							janelaSimulacaoPassoAPasso.exibir();
						}
						
						break;
					}
				}
			}
		});
		mnExecutar.add(mntmPassoAPasso);
		
		JMenu mnAjuda = new JMenu("Ajuda");
		mnAjuda.setFont(new Font("Monospaced", Font.PLAIN, 12));
		barraMenu.add(mnAjuda);
		
		JMenuItem mntmManualDoUsurio = new JMenuItem("Utilização");
		mntmManualDoUsurio.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.ALT_MASK));
		mntmManualDoUsurio.setFont(new Font("Monospaced", Font.PLAIN, 12));
		mntmManualDoUsurio.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try {
					File dir = new File(System.getProperty("java.io.tmpdir") + File.separatorChar + "SimuladorTuring_v1"); //Criando uma cópia do manual na pasta temporária do Java.
					
					dir.setWritable(true);
					
					File temp = new File(dir.getAbsolutePath() + File.separatorChar + "RelatorioFinal_SimuladorTuring_Carolina_e_Joao.pdf"); //Criando um link para um arquivo temporário com o mesmo nome do arquivo original.
					InputStream is = this.getClass().getResourceAsStream("/arquivos/RelatorioFinal_SimuladorTuring_Carolina_e_Joao.pdf");
					FileOutputStream link = new FileOutputStream(temp);
					FileWriter fw = new FileWriter(temp);
					byte[] buffer = new byte[512 * 1024]; //Definindo um buffer não tão grande.
					int read;
					
					while ((read = is.read(buffer)) != -1) {
						link.write(buffer, 0, read);
					}
					
					fw.close();
					link.close();
					is.close();
					Desktop.getDesktop().open(temp);
				}
				catch (IOException ex) {
					JOptionPane.showMessageDialog(null, "Não foi possível abrir o manual de instruções.\nÉ necessária a configuração de um leitor\nde arquivos .pdf para executar essa função.", "Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnAjuda.add(mntmManualDoUsurio);
		
		JMenuItem mntmCrditos = new JMenuItem("Créditos");
		mntmCrditos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
		mntmCrditos.setFont(new Font("Monospaced", Font.PLAIN, 12));
		mntmCrditos.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				janelaCreditos.exibir();
			}
		});
		mnAjuda.add(mntmCrditos);
		
		JMenuItem mntmConfigurao = new JMenuItem("Limite de pilha");
		mntmConfigurao.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK));
		mntmConfigurao.setFont(new Font("Monospaced", Font.PLAIN, 12));
		mntmConfigurao.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				janelaLimitePilha.exibir();
			}
		});
		
		mnAjuda.add(mntmConfigurao);
		painelPrincipal = new JPanel();
		painelPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(painelPrincipal);
		
		JPanel painelFuncionalidade = new JPanel();
		painelFuncionalidade.setForeground(Color.GRAY);
		painelFuncionalidade.setBorder(new LineBorder(Color.GRAY));
		botaoEstado.setFont(new Font("Monospaced", Font.PLAIN, 11));
		
		botaoEstado.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_estado_over.png")));
		botaoEstado.setToolTipText("Adicionar estado");
		
		botaoEstado.addMouseListener(new MouseListener()
		{
			
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				botaoEstado.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_estado_over.png")));
			}
			
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				deselecionarBotoes();
				botaoEstado.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_estado_click.png")));
				infoFuncionalidade.setFuncao(InfoFuncionalidade.ADICAO);
			}
			
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				if (infoFuncionalidade.getFuncao() != InfoFuncionalidade.ADICAO) botaoEstado.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_estado.png")));
			}
			
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				botaoEstado.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_estado_over.png")));
			}
			
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
			}
		});
		botaoRemocao.setFont(new Font("Monospaced", Font.PLAIN, 11));
		
		botaoRemocao.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_caveira.png")));
		botaoRemocao.setToolTipText("Remover estado ou símbolo");
		
		botaoRemocao.addMouseListener(new MouseListener()
		{
			
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				botaoRemocao.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_caveira_over.png")));
			}
			
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				deselecionarBotoes();
				botaoRemocao.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_caveira_click.png")));
				infoFuncionalidade.setFuncao(InfoFuncionalidade.REMOCAO);
			}
			
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				if (infoFuncionalidade.getFuncao() != InfoFuncionalidade.REMOCAO) botaoRemocao.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_caveira.png")));
			}
			
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				botaoRemocao.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_caveira_over.png")));
			}
			
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
			}
		});
		botaoMover.setFont(new Font("Monospaced", Font.PLAIN, 11));
		
		botaoMover.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_mao.png")));
		botaoMover.setToolTipText("Movimentar estado");
		
		botaoMover.addMouseListener(new MouseListener()
		{
			
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				botaoMover.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_mao_over.png")));
			}
			
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				deselecionarBotoes();
				botaoMover.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_mao_click.png")));
				infoFuncionalidade.setFuncao(InfoFuncionalidade.MOVIMENTACAO);
			}
			
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				if (infoFuncionalidade.getFuncao() != InfoFuncionalidade.MOVIMENTACAO) botaoMover.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_mao.png")));
			}
			
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				botaoMover.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_mao_over.png")));
			}
			
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
			}
		});
		botaoTransicao.setFont(new Font("Monospaced", Font.PLAIN, 11));
		
		botaoTransicao.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_conexao.png")));
		botaoTransicao.setToolTipText("Adicionar transição entre estados");
		
		botaoTransicao.addMouseListener(new MouseListener()
		{
			
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				botaoTransicao.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_conexao_over.png")));
			}
			
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				deselecionarBotoes();
				botaoTransicao.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_conexao_click.png")));
				infoFuncionalidade.setFuncao(InfoFuncionalidade.TRANSICAO);
			}
			
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				if (infoFuncionalidade.getFuncao() != InfoFuncionalidade.TRANSICAO) botaoTransicao.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_conexao.png")));
			}
			
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				botaoTransicao.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_conexao_over.png")));
			}
			
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
			}
		});
		
		janelaSimulacaoPassoAPasso = new JanelaSimulacaoPassoAPasso();
		
		JLabel lblMquinas = new JLabel("Arquivo:");
		lblMquinas.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		tabbedPane.setBackground(Color.LIGHT_GRAY);
		
		JButton button = new JButton("");
		button.setToolTipText("Adicionar aba");
		button.setIcon(new ImageIcon(getClass().getResource("/imagens/nova_aba.png")));
		button.addActionListener(new ActionListener()
		{
			
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				janelaNumeroFitas.exibir();
				
				if (!janelaNumeroFitas.cancelado()) {
					abas.add(new AbaAutomato(infoFuncionalidade, janelaNumeroFitas.getNumFitas(), essaJanela));
					abas.get(abas.size() - 1).getScrollAnimacao().setName(String.valueOf(idxAbas));
					tabbedPane.addTab(String.valueOf(idxAbas), null, abas.get(abas.size() - 1).getScrollAnimacao(), null);
					abas.get(abas.size() - 1).setNumFitas(janelaNumeroFitas.getNumFitas());
					textField.setText("* Novo arquivo! *");
					textField_1.setText(abas.get(abas.size() - 1).getNumFitas() + "");
					tabbedPane.setSelectedComponent(abas.get(abas.size() - 1).getScrollAnimacao());
					++idxAbas;
				}
			}
		});
		
		button_1 = new JButton("");
		button_1.addActionListener(new ActionListener()
		{
			
			
			public void actionPerformed(ActionEvent e)
			{
				for (AbaAutomato aba : abas) {
					if (aba.getScrollAnimacao() == tabbedPane.getSelectedComponent()) {
						if (aba.getPainelAnimacao().getMaquinaModificada()) { //Se foi modificado o arquivo, ainda não pode excluir.
							if (aba.getArquivo() != null) {
								Object options[] = { "Sim", "Não" };
								
								int close = JOptionPane.showOptionDialog(null, "Deseja salvar essa máquina?\n", "Sair", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
								
								if (close == JOptionPane.YES_OPTION) {
									File fileToSave = aba.getArquivo();
									
									FileWriter writer;
									
									try {
										writer = new FileWriter(fileToSave);
										
										String aux = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!--Criado com o Simulador de Turing v1.--><structure>\n	<type>turing</type>\n	<tapes>";
										
										for (AbaAutomato aba2 : abas) {
											if (aba2.getScrollAnimacao() == tabbedPane.getSelectedComponent()) {
												aba2.getPainelAnimacao().removerTransicaoTemporaria();
												
												aux += aba2.getPainelAnimacao().getNumFitas() + "</tapes>\n	<automaton>\n		<!--Lista de estados.-->\n";
												writer.write(aux);
												aux = "";
												
												for (Estado e2 : aba2.getPainelAnimacao().getEstados()) {
													aux += "		<block id=\"" + e2.getIdxNome() + "\" name=\"" + e2.getText().trim() + "\">\n			<tag>Machine" + e2.getIdxNome() + "</tag>\n			<x>" + e2.getX() + "</x>\n			<y>" + e2.getY() + "</y>\n";
													
													if (e2.eInicial()) aux += "			<initial/>\n";
													if (e2.eFinal()) aux += "			<final/>\n";
													
													aux += "		</block>\n";
												}
												
												aux += "		<!--Lista de transições.-->\n";
												writer.write(aux);
												aux = "";
												
												for (Estado e2 : aba2.getPainelAnimacao().getEstados()) {
													for (Transicao t : e2.getTransicoesSaida()) {
														for (Simbolo s : t.getSimbolos()) {
															aux += "		<transition>\n			<from>" + e2.getIdxNome() + "</from>\n			<to>" + t.getEstadoFinal().getIdxNome() + "</to>\n";
															
															for (int i = 0; i < aba2.getPainelAnimacao().getNumFitas(); i++) {
																aux += "			<read tape=\"" + (i + 1) + "\"";
																
																if (s.getLeitura().get(i).equals("□")) aux += "/>\n";
																else {
																	aux += ">" + s.getLeitura().get(i) + "</read>\n";
																}
																
																aux += "			<write tape=\"" + (i + 1) + "\"";
																
																if (s.getEscrita().get(i).equals("□")) aux += "/>\n";
																else {
																	aux += ">" + s.getEscrita().get(i) + "</write>\n";
																}
																
																aux += "			<move tape=\"" + (i + 1) + "\"";
																
																switch (s.getMovimento().get(i)) {
																	case "D": {
																		aux += ">R</move>\n";
																	}
																	break;
																	case "E": {
																		aux += ">L</move>\n";
																	}
																	break;
																	default: {
																		aux += ">S</move>\n";
																	}
																}
															}
															
															aux += "		</transition>\n";
															
															writer.write(aux);
															aux = "";
														}
													}
												}
												
												aux += "		<!--Lista de autômatos.-->\n";
												
												for (Estado e2 : aba2.getPainelAnimacao().getEstados()) {
													aux += "		<Machine" + e2.getIdxNome() + "/>\n";
												}
												
												aux += "	</automaton>\n</structure>";
												
												writer.write(aux);
												writer.close();
												
												aba2.setArquivo(fileToSave);
												textField.setText(fileToSave.getName());
												aba2.getPainelAnimacao().setMaquinaModificada(false);
												
												break;
											}
										}
									}
									catch (IOException e1) {
										JOptionPane.showMessageDialog(null, "Erro ao salvar arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
										e1.printStackTrace();
									}
								}
							}
							else {
								Object options[] = { "Sim", "Não" };
								
								int close = JOptionPane.showOptionDialog(null, "Deseja salvar essa máquina?\n", "Sair", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
								
								if (close == JOptionPane.YES_OPTION) {
									JFileChooser fileChooser = new JFileChooser();
									
									fileChooser.setDialogTitle("Salvar");
									
									int userSelection = fileChooser.showSaveDialog(getParent());
									
									if (userSelection == JFileChooser.APPROVE_OPTION) {
										File fileToSave = fileChooser.getSelectedFile();
										
										fileToSave = new File(fileToSave.getPath() + ".jff");
										
										if (fileToSave.exists()) JOptionPane.showMessageDialog(null, "Arquivo já existe!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
										else {
											FileWriter writer;
											
											try {
												writer = new FileWriter(fileToSave);
												
												String aux = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!--Criado com o Simulador de Turing v1.--><structure>\n	<type>turing</type>\n	<tapes>";
												
												for (AbaAutomato aba2 : abas) {
													if (aba2.getScrollAnimacao() == tabbedPane.getSelectedComponent()) {
														aba2.getPainelAnimacao().removerTransicaoTemporaria();
														
														aux += aba2.getPainelAnimacao().getNumFitas() + "</tapes>\n	<automaton>\n		<!--Lista de estados.-->\n";
														writer.write(aux);
														aux = "";
														
														for (Estado e2 : aba2.getPainelAnimacao().getEstados()) {
															aux += "		<block id=\"" + e2.getIdxNome() + "\" name=\"" + e2.getText().trim() + "\">\n			<tag>Machine" + e2.getIdxNome() + "</tag>\n			<x>" + e2.getX() + "</x>\n			<y>" + e2.getY() + "</y>\n";
															
															if (e2.eInicial()) aux += "			<initial/>\n";
															if (e2.eFinal()) aux += "			<final/>\n";
															
															aux += "		</block>\n";
														}
														
														aux += "		<!--Lista de transições.-->\n";
														writer.write(aux);
														aux = "";
														
														for (Estado e2 : aba2.getPainelAnimacao().getEstados()) {
															for (Transicao t : e2.getTransicoesSaida()) {
																for (Simbolo s : t.getSimbolos()) {
																	aux += "		<transition>\n			<from>" + e2.getIdxNome() + "</from>\n			<to>" + t.getEstadoFinal().getIdxNome() + "</to>\n";
																	
																	for (int i = 0; i < aba2.getPainelAnimacao().getNumFitas(); i++) {
																		aux += "			<read tape=\"" + (i + 1) + "\"";
																		
																		if (s.getLeitura().get(i).equals("□")) aux += "/>\n";
																		else {
																			aux += ">" + s.getLeitura().get(i) + "</read>\n";
																		}
																		
																		aux += "			<write tape=\"" + (i + 1) + "\"";
																		
																		if (s.getEscrita().get(i).equals("□")) aux += "/>\n";
																		else {
																			aux += ">" + s.getEscrita().get(i) + "</write>\n";
																		}
																		
																		aux += "			<move tape=\"" + (i + 1) + "\"";
																		
																		switch (s.getMovimento().get(i)) {
																			case "D": {
																				aux += ">R</move>\n";
																			}
																			break;
																			case "E": {
																				aux += ">L</move>\n";
																			}
																			break;
																			default: {
																				aux += ">S</move>\n";
																			}
																		}
																	}
																	
																	aux += "		</transition>\n";
																	
																	writer.write(aux);
																	aux = "";
																}
															}
														}
														
														aux += "		<!--Lista de autômatos.-->\n";
														
														for (Estado e2 : aba2.getPainelAnimacao().getEstados()) {
															aux += "		<Machine" + e2.getIdxNome() + "/>\n";
														}
														
														aux += "	</automaton>\n</structure>";
														
														writer.write(aux);
														writer.close();
														
														aba2.setArquivo(fileToSave);
														textField.setText(fileToSave.getName());
														aba2.getPainelAnimacao().setMaquinaModificada(false);
														
														break;
													}
												}
											}
											catch (IOException e1) {
												JOptionPane.showMessageDialog(null, "Erro ao salvar arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
												e1.printStackTrace();
											}
										}
									}
								}
							}
						}
						
						tabbedPane.remove(tabbedPane.getSelectedComponent());
						abas.remove(aba);
						
						if (abas.size() == 0) {
							idxAbas = 0;
							textField.setText("");
							textField_1.setText("");
						}
						
						break;
					}
				}
				
				for (AbaAutomato aba : abas) {
					if (aba.getScrollAnimacao() == tabbedPane.getSelectedComponent()) {
						if (aba.getArquivo() != null) {
							if (aba.getPainelAnimacao().getMaquinaModificada()) textField.setText("* " + aba.getArquivo().getName());
							else textField.setText(aba.getArquivo().getName());
						}
						else textField.setText("* Novo arquivo! *");
						
						textField_1.setText(aba.getNumFitas() + "");
						
						break;
					}
				}
				
			}
		});
		
		button_1.setToolTipText("Remover aba");
		button_1.setIcon(new ImageIcon(getClass().getResource("/imagens/fechar_aba.png")));
		
		textField.setEditable(false);
		textField.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textField.setColumns(10);
		
		JSeparator separator = new JSeparator();
		
		final JLabel sobreTuring = new JLabel("");
		sobreTuring.setFont(new Font("Monospaced", Font.PLAIN, 11));
		sobreTuring.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_turing.png")));
		sobreTuring.addMouseListener(new MouseListener()
		{
			
			
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				sobreTuring.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_turing_over.png")));
				janelaSobreTuring.exibir();
			}
			
			
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				sobreTuring.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_turing_click.png")));
			}
			
			
			@Override
			public void mouseExited(MouseEvent arg0)
			{
				sobreTuring.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_turing.png")));
			}
			
			
			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				sobreTuring.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_turing_over.png")));
			}
			
			
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			}
		});
		
		sobreTuring.setToolTipText("Sobre Alan Turing");
		GroupLayout gl_painelFuncionalidade = new GroupLayout(painelFuncionalidade);
		gl_painelFuncionalidade.setHorizontalGroup(gl_painelFuncionalidade.createParallelGroup(Alignment.LEADING).addGroup(gl_painelFuncionalidade.createSequentialGroup().addGroup(gl_painelFuncionalidade.createParallelGroup(Alignment.LEADING).addGroup(gl_painelFuncionalidade.createSequentialGroup().addGap(18).addComponent(botaoEstado)).addGroup(gl_painelFuncionalidade.createSequentialGroup().addGap(18).addComponent(botaoMover, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)).addGroup(gl_painelFuncionalidade.createSequentialGroup().addGap(15).addComponent(botaoTransicao, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)).addGroup(gl_painelFuncionalidade.createSequentialGroup().addGap(15).addGroup(gl_painelFuncionalidade.createParallelGroup(Alignment.LEADING).addComponent(sobreTuring).addComponent(botaoRemocao, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))).addComponent(separator, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)).addContainerGap(45, Short.MAX_VALUE)));
		gl_painelFuncionalidade.setVerticalGroup(gl_painelFuncionalidade.createParallelGroup(Alignment.LEADING).addGroup(gl_painelFuncionalidade.createSequentialGroup().addGap(6).addComponent(botaoEstado).addGap(6).addComponent(botaoMover, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addGap(6).addComponent(botaoTransicao, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addGap(6).addComponent(botaoRemocao, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(separator, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE).addGap(4).addComponent(sobreTuring, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addContainerGap()));
		painelFuncionalidade.setLayout(gl_painelFuncionalidade);
		
		JLabel lblFitas = new JLabel("Fitas:");
		lblFitas.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		textField_1 = new JTextField();
		textField_1.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_1.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		GroupLayout gl_painelPrincipal = new GroupLayout(painelPrincipal);
		gl_painelPrincipal.setHorizontalGroup(gl_painelPrincipal.createParallelGroup(Alignment.LEADING).addGroup(gl_painelPrincipal.createSequentialGroup().addComponent(lblMquinas).addPreferredGap(ComponentPlacement.RELATED).addComponent(textField, GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblFitas).addGap(6).addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(button, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addGap(12).addComponent(button_1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)).addGroup(Alignment.TRAILING, gl_painelPrincipal.createSequentialGroup().addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(painelFuncionalidade, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)));
		gl_painelPrincipal.setVerticalGroup(gl_painelPrincipal.createParallelGroup(Alignment.LEADING).addGroup(gl_painelPrincipal.createSequentialGroup().addGroup(gl_painelPrincipal.createParallelGroup(Alignment.LEADING).addComponent(lblMquinas, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addComponent(button).addComponent(button_1).addGroup(gl_painelPrincipal.createSequentialGroup().addGap(2).addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGroup(gl_painelPrincipal.createSequentialGroup().addGap(2).addGroup(gl_painelPrincipal.createParallelGroup(Alignment.BASELINE).addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblFitas, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)))).addGap(6).addGroup(gl_painelPrincipal.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE).addComponent(painelFuncionalidade, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE))));
		painelPrincipal.setLayout(gl_painelPrincipal);
		revalidate();
		repaint();
		
	}
	
	
	/**************************************************************************/
	/*** MÉTODOS FUNCIONAIS ***************************************************/
	/**************************************************************************/
	
	private void deselecionarBotoes()
	{
		botaoEstado.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_estado.png")));
		botaoRemocao.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_caveira.png")));
		botaoTransicao.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_conexao.png")));
		botaoMover.setIcon(new ImageIcon(getClass().getResource("/imagens/icn_mao.png")));
	}
	
	
	public void setJanelaSobreTuring(JanelaSobreTuring janelaSobreTuring)
	{
		this.janelaSobreTuring = janelaSobreTuring;
	}
	
	
	public void setNomeArquivo(String nome, boolean foiModificado)
	{
		textField.setText(nome);
		
		if (foiModificado) {
			for (AbaAutomato aba : abas) {
				if (aba.getScrollAnimacao() == tabbedPane.getSelectedComponent()) {
					aba.getPainelAnimacao().setMaquinaModificada(true);
					
					break;
				}
			}
		}
	}
	
	
	public String getNomeArquivo()
	{
		return textField.getText();
	}
	
	
	@Override
	public void windowOpened(WindowEvent e)
	{
	}
	
	
	@Override
	public void windowClosing(WindowEvent e)
	{
		Object options[] = { "Sim", "Não" };
		
		int close = JOptionPane.showOptionDialog(e.getComponent(), "Você está querendo me abandonar?\n", "Sair", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
		
		if (close == JOptionPane.YES_OPTION) {
			JOptionPane.showMessageDialog(e.getComponent(), "Vai lá com seus amiguinhos então...", "Abandono!", JOptionPane.ERROR_MESSAGE);
			boolean arquivosPraSalvar = false;
			
			for (AbaAutomato aba : abas) {
				if (aba.getPainelAnimacao().getMaquinaModificada()) {
					arquivosPraSalvar = true;
					
					break;
				}
			}
			
			if (arquivosPraSalvar) {
				close = JOptionPane.showOptionDialog(e.getComponent(), "Salvar máquinas em edição?\n", "Salvar máquinas", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
				
				if (close == JOptionPane.YES_OPTION) {
					while (abas.size() > 0) { //Requisitando salvamento para todos os arquivos.
						button_1.doClick();
					}
				}
			}
			
			((JFrame) e.getSource()).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		else {
			JOptionPane.showMessageDialog(null, "♥", "Obrigado!", JOptionPane.INFORMATION_MESSAGE);
			((JFrame) e.getSource()).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
	}
	
	
	@Override
	public void windowClosed(WindowEvent e)
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
	public void windowActivated(WindowEvent e)
	{
	}
	
	
	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}
	
}
