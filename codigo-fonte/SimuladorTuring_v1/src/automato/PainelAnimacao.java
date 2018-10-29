/*
 * Simulador de Máquinas de Turing - v1
 * 
 * Data: 11/10/2014
 * Programadores: Carol Chaves (RA 1304984), João Antunes (RA 1305093)
 * 
 * Implementado com o Eclipse Kepler, utilizando WindowBuilder Pro GUI Designer. 
 * Abra as classes do pacote 'gui' com este programa.
 */

package automato;

import gui.JanelaPrincipal;
import gui.JanelaSimboloTransicao;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import auxiliar_automato.EstadoEmProcessamento;
import auxiliar_automato.Passo;


public class PainelAnimacao extends JPanel implements MouseListener, MouseMotionListener
{
	
	
	private static final long serialVersionUID = 1L;
	private int idEstado = 0; //Identificador autoincremental dos estados.
	private int alturaAtual; //Variáveis necessárias para fazer o redimensionamento do JPanel quando um estado transcede o tamanho atual visível.
	private int larguraAtual;
	private int numFitas;
	protected boolean estadoFinalEncontrado;
	private boolean maquinaModificada;
	
	private JanelaSimboloTransicao janelaSimbolo; //Janela para entrada dos símbolos de uma transição.
	private Transicao transicaoTemporaria = null; //Arco que está sendo criado atualmente, mas ainda não está ligando dois autômatos.
	private ArrayList<Estado> estados = new ArrayList<Estado>(); //Estados do autômato, necessários para reconhecer uma entrada.
	private InfoFuncionalidade infoFuncionalidade; //Padrão Singleton: Controle de compartilhamento de informações funcionais entre a classe 'PainelAnimacao' e o menu de funcionalidades do autômato.
	private Graphics2D g2d = (Graphics2D) getGraphics();
	private JanelaPrincipal janelaPrincipal;
	
	
	public PainelAnimacao(InfoFuncionalidade infoFuncionalidade, int numFitas, JanelaPrincipal janelaPrincipal)
	{
		maquinaModificada = true;
		this.janelaPrincipal = janelaPrincipal;
		this.numFitas = numFitas;
		janelaSimbolo = new JanelaSimboloTransicao(numFitas);
		this.infoFuncionalidade = infoFuncionalidade;
		this.setLayout(null);
		
		//Adicionando os eventos implementados para as ações do mouse:
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		//Dimensões iniciais do JPanel:
		alturaAtual = getHeight();
		larguraAtual = getWidth();
	}
	
	
	/**************************************************************************/
	/*** MÉTODOS SOBRESCRITOS *************************************************/
	/**************************************************************************/
	
	@Override
	public void paintComponent(Graphics g)
	{
		int larguraTemp = -1;
		int alturaTemp = -1;
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		
		//Otimizando saída gráfica:
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		//Definindo o estilo da borda dos desenhos:
		g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));
		
		for (Estado e : estados) {
			//Esses 2 'if´s' são para redimensionar o JPanel automaticamente:
			if (e.getLocation().getX() > larguraTemp) larguraTemp = (int) e.getLocation().getX();
			if (e.getLocation().getY() > alturaTemp) alturaTemp = (int) e.getLocation().getY();
			
			for (Transicao t : e.getTransicoesSaida()) { //Reimprimindo arcos e setas.
				t.desenhar(g2d);
			}
		}
		
		if (transicaoTemporaria != null) { //Reimprimindo a transição temporária.
			transicaoTemporaria.desenhar(g2d);
		}
		
		g2d.dispose();
		
		if (estados.size() == 0) idEstado = 0; //Resetando o id dos estados.
		
		//Redimensionamento automático do JPanel:
		larguraAtual = larguraTemp + 100;
		alturaAtual = alturaTemp + 100;
		setPreferredSize(new Dimension(larguraAtual, alturaAtual));
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) //Ação de clique.
	{
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) //Ação de pressionamento.
	{
		if (idEstado == 100000) {
			infoFuncionalidade.exibirExcesso();
			return;
		}
		
		/* Ao invés de usar o evento de click, esse evento dá a impressão de adição mais rápida do componente, pois é utilizado apenas o evento de pressionamento do click: */
		removerTransicaoTemporaria();
		
		if (infoFuncionalidade.getFuncao() == InfoFuncionalidade.ADICAO) { //Criando um novo estado.
			if (!maquinaModificada) {
				maquinaModificada = true;
				
				janelaPrincipal.setNomeArquivo("* " + janelaPrincipal.getNomeArquivo(), true);
			}
			
			int posX = e.getX(), posY = e.getY();
			
			/* Ajustando a posição de criação do estado para caber na tela: */
			if (posX - 25 <= 0) posX = 25;
			
			if (posY - 25 <= 0) posY = 25;
			
			Estado estado = new Estado(posX - 25, posY - 25, idEstado, this); //Criando o estado na posição atual do cursor.
			
			++idEstado; //Incrementando o controle de ids de estados.
			
			add(estado); //Adicionando graficamente.
			estados.add(estado); //Adicionando logicamente.			
			
			this.revalidate();
			this.repaint();
		}
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) //Ação de despressionamento.
	{
		removerTransicaoTemporaria();
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) //Ação quando o cursor entra no componente.
	{
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	
	@Override
	public void mouseExited(MouseEvent e) //Ação quando o cursor deixa o componente.
	{
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if (transicaoTemporaria != null) {
			transicaoTemporaria.setPontoFinal(e.getX(), e.getY()); //Movendo a linha temporária de acordo com o cursor do mouse.
			
			this.revalidate();
			this.repaint();
		}
	}
	
	
	/**************************************************************************/
	/*** MÉTODOS FUNCIONAIS ***************************************************/
	/**************************************************************************/
	
	public void setInfoFuncionalidade(InfoFuncionalidade infoFuncionalidade)
	{
		this.infoFuncionalidade = infoFuncionalidade;
	}
	
	
	public InfoFuncionalidade getInfoFuncionalidade()
	{
		return infoFuncionalidade;
	}
	
	
	public void removerTransicaoTemporaria() //Remove o arco temporário.
	{
		transicaoTemporaria = null;
		
		revalidate();
		repaint();
	}
	
	
	public void setTransicaoTemporaria(Transicao transicaoTemporaria)
	{
		this.transicaoTemporaria = transicaoTemporaria;
	}
	
	
	public Transicao getTransicaoTemporaria()
	{
		return (transicaoTemporaria);
	}
	
	
	public void removerEstado(Estado estado)
	{
		estados.remove(estado);
		remove(estado);
		
		if (estados.size() == 0) idEstado = 0;
	}
	
	
	public void removerComponente(Component c)
	{
		remove(c);
	}
	
	
	public void setIdEstado(int id)
	{
		idEstado = id;
	}
	
	
	public int getIdEstado()
	{
		return (idEstado);
	}
	
	
	public ArrayList<Estado> getEstados()
	{
		return (estados);
	}
	
	
	public Estado adicionarEstado(int x, int y)
	{
		Estado estado = new Estado(x, y, idEstado, this);
		
		++idEstado;
		add(estado); //Adicionando graficamente.
		estados.add(estado); //Adicionando logicamente.
		//estado.setEstadoAtual(Estado.ESTADO_FINAL);
		this.revalidate();
		this.repaint();
		return estado;
	}
	
	
	public void redimensionarPainel(int xOnScreen, int yOnScreen)
	{
		if (xOnScreen > larguraAtual) larguraAtual = xOnScreen;
		if (yOnScreen > alturaAtual) alturaAtual = yOnScreen;
		
		setPreferredSize(new Dimension(larguraAtual, alturaAtual));
		revalidate();
		repaint();
	}
	
	
	public Graphics2D getGraphics2d()
	{
		return g2d;
	}
	
	
	public void setSimbolo(String s)
	{
		janelaSimbolo.exibir(s);
	}
	
	
	public String getSimbolo()
	{
		return janelaSimbolo.getSimbolo();
	}
	
	
	public void selecionarEstado(Estado e, char seletor)
	{
		e.selecionarEstado(seletor);
	}
	
	
	public void deselecionarEstados()
	{
		for (Estado e : estados) {
			e.deselecionarEstado();
		}
	}
	
	
	public boolean validarAutomato()
	{
		ArrayList<Estado> estadosVisitados = new ArrayList<Estado>();
		boolean correto = true;
		boolean peloMenos1Final = false;
		
		//Obtendo estado inicial:
		for (Estado e : estados) {
			if (e.eInicial()) {
				estadosVisitados.add(e);
				break;
			}
		}
		
		//Verificando conexidade (todos os estados conectados) do autômato:
		if (estadosVisitados.size() > 0) {
			buscaProfundidade(estadosVisitados);
			
			if (estadosVisitados.size() == estados.size()) {
				for (Estado e : estados) {
					if (e.eFinal()) {
						peloMenos1Final = true;
						
						if (e.getTransicoesSaida().size() > 0) correto = false;
						
						break;
					}
				}
			}
		}
		else correto = false;
		
		return correto && peloMenos1Final;
	}
	
	
	private void buscaProfundidade(ArrayList<Estado> estadosVisitados)
	{
		for (Transicao t : estadosVisitados.get(estadosVisitados.size() - 1).getTransicoesSaida()) {
			if (!buscarEstado(t.getEstadoFinal(), estadosVisitados)) {
				estadosVisitados.add(t.getEstadoFinal());
				buscaProfundidade(estadosVisitados);
			}
		}
	}
	
	
	private boolean buscarEstado(Estado estado, ArrayList<Estado> estadosVisitados)
	{
		for (Estado e : estadosVisitados) {
			if (e == estado) return true;
		}
		
		return false;
	}
	
	
	public ArrayList<Passo> testarEntradaPP(String entrada)
	{
		ArrayList<String> fitas = new ArrayList<String>();
		ArrayList<char[]> caracteresFita = new ArrayList<char[]>();
		ArrayList<Integer> posicaoFita = new ArrayList<Integer>();
		ArrayList<Passo> passos = new ArrayList<Passo>();
		ArrayList<EstadoEmProcessamento> estadosEmProcessamento = new ArrayList<EstadoEmProcessamento>();
		boolean loopInfinito = false;
		ArrayList<String> fitas_ORIG = new ArrayList<String>();
		
		estadoFinalEncontrado = false;
		fitas.add(entrada); //1ª fita tem o conteúdo da entrada.
		caracteresFita.add(fitas.get(0).toCharArray());
		posicaoFita.add(0);
		fitas_ORIG.add(entrada);
		
		for (int i = 1; i < numFitas; i++) { //Demais fitas são "zeradas".
			fitas.add("□");
			fitas_ORIG.add("□");
			caracteresFita.add(fitas.get(i).toCharArray());
			posicaoFita.add(0);
		}
		
		//Procurando estado inicial (garantido q existe):
		for (Estado e : estados) {
			if (e.eInicial()) {
				passos.add(new Passo(e, fitas, caracteresFita, posicaoFita, false, null, null, null)); //Primeiro passo não tem "aceita/rejeita".
				loopInfinito = buscaProfundidadePP(passos, 0, estadosEmProcessamento, loopInfinito); //Lançando busca em profundidade a partir do estado inicial.
				
				break;
			}
		}
		
		if (loopInfinito) passos = null;
		else {
			passos.get(0).setFitas(fitas_ORIG);
			
			ArrayList<Passo> passosNaoDuplicados = new ArrayList<Passo>();
			
			//Removendo os retornos duplicados das chamadas recursivas:
			for (int i = 0; i < passos.size(); i++) {
				if (!passos.get(i).getVisitado()) {
					passos.get(i).setVisitado(true);
					passosNaoDuplicados.add(passos.get(i));
					if (passos.get(i).getBacktrack() != null) passosNaoDuplicados.add(passos.get(i).getBacktrack());
					
					for (Passo ps : passos) {
						if (ps.possuiFalha() && !ps.getVisitado() && ps.getEstado() == passos.get(i).getEstado()) {
							if ((ps.getBacktrack() == null && passos.get(i).getBacktrack() == null) || (ps.getBacktrack() != null && passos.get(i).getBacktrack() != null && ps.getBacktrack().getEstado() == passos.get(i).getBacktrack().getEstado())) ps.setVisitado(true);
						}
					}
				}
			}
			
			passos = passosNaoDuplicados;
		}
		
		return passos;
	}
	
	
	protected boolean buscaProfundidadePP(ArrayList<Passo> passos, int passoAtual, ArrayList<EstadoEmProcessamento> estadosEmProcessamento, boolean loopInfinito)
	{
		boolean todasFitasAceitas = true;
		ArrayList<String> fitas_ORIG = new ArrayList<String>();
		ArrayList<char[]> caracteresFita_ORIG = new ArrayList<char[]>();
		ArrayList<Integer> posicaoFita_ORIG = new ArrayList<Integer>();
		
		addEstadoProcessamento(estadosEmProcessamento, passos.get(passoAtual).getEstado()); //Evita loop infinito.
		
		if (getEstadoEmProcessamento(estadosEmProcessamento, passos.get(passoAtual).getEstado()).getChamadas() > infoFuncionalidade.getLoopMaximo()) {
			loopInfinito = true; //Cancelando a simulação: entrou em loop "infinito" (maior do que o permitido).
			
			return loopInfinito;
		}
		
		for (int i = 0; i < numFitas; i++) {
			fitas_ORIG.add(passos.get(passoAtual).getFitas().get(i));
			caracteresFita_ORIG.add(passos.get(passoAtual).getCaracteresFitas().get(i));
			posicaoFita_ORIG.add(passos.get(passoAtual).getPosicaoFita().get(i));
		}
		
		for (Transicao t : passos.get(passoAtual).getEstado().getTransicoesSaida()) { //Procurando uma transição que aceite o símbolo atual.
			for (int idxSimb = 0; idxSimb < t.getSimbolos().size(); idxSimb++) { //Testar a combinação de todas as transições com todas as transições.
				todasFitasAceitas = true; //Verificando se algum estado é alcançável por todas as fitas de uma transição.
				
				for (int idx = 0; idx < numFitas; idx++) { //n símbolos para n fitas.
					if (t.getSimbolos().get(idxSimb).getLeitura(idx).equals(String.valueOf(passos.get(passoAtual).getCaracteresFitas().get(idx)[passos.get(passoAtual).getPosicaoFita().get(idx)]))) { //Símbolo lido aceito.
						passos.get(passoAtual).getCaracteresFitas().get(idx)[passos.get(passoAtual).getPosicaoFita().get(idx)] = t.getSimbolos().get(idxSimb).getEscrita(idx).toCharArray()[0]; //Escrevendo na fita (atualizando).
						passos.get(passoAtual).getFitas().set(idx, String.valueOf(passos.get(passoAtual).getCaracteresFitas().get(idx))); //Atualizando fita.
						
						if (t.getSimbolos().get(idxSimb).getMovimento(idx).equals("D")) { //MOVIMENTA PRA DIREITA.
							passos.get(passoAtual).getPosicaoFita().set(idx, passos.get(passoAtual).getPosicaoFita().get(idx) + 1);
							
							if (passos.get(passoAtual).getPosicaoFita().get(idx) == passos.get(passoAtual).getFitas().get(idx).length()) {
								passos.get(passoAtual).getFitas().set(idx, String.valueOf(passos.get(passoAtual).getCaracteresFitas().get(idx)) + "□"); //Aumentando fita ("infinita" pra direita).
								passos.get(passoAtual).getCaracteresFitas().set(idx, passos.get(passoAtual).getFitas().get(idx).toCharArray()); //Atualizando tamanho da fita.
							}
						}
						else if (t.getSimbolos().get(idxSimb).getMovimento(idx).equals("E")) { //MOVIMENTA PRA ESQUERDA.
							passos.get(passoAtual).getPosicaoFita().set(idx, passos.get(passoAtual).getPosicaoFita().get(idx) - 1);
							
							if (passos.get(passoAtual).getPosicaoFita().get(idx) == -1) {
								passos.get(passoAtual).getFitas().set(idx, "□" + String.valueOf(passos.get(passoAtual).getCaracteresFitas().get(idx))); //Aumentando fita ("infinita" pra esquerda).
								passos.get(passoAtual).getCaracteresFitas().set(idx, passos.get(passoAtual).getFitas().get(idx).toCharArray()); //Atualizando tamanho da fita.
								passos.get(passoAtual).getPosicaoFita().set(idx, 0);
							}
						}
						else
						; //MOVIMENTO "PARADO".
					}
					else { //Nem todas as fitas são aceitas por essa transição => testa o próximo símbolo.
						todasFitasAceitas = false;
						passos.add(new Passo(t.getEstadoFinal(), passos.get(passoAtual).getFitas(), passos.get(passoAtual).getCaracteresFitas(), passos.get(passoAtual).getPosicaoFita(), true, t.getSimbolos().get(idxSimb).getLeitura(), t.getSimbolos().get(idxSimb).getEscrita(), t.getSimbolos().get(idxSimb).getMovimento()));
						
						//Registrando a localização do erro dessa transição:
						passos.get(passos.size() - 1).setTransicao(t);
						passos.get(passos.size() - 1).setIdxSimbolo(idxSimb);
						passos.get(passos.size() - 1).setNumFita(idx + 1);
						
						//"Realçando" backtracking de simulação (pai chamador):
						Passo backtrack = new Passo(passos.get(passoAtual).getEstado(), fitas_ORIG, caracteresFita_ORIG, posicaoFita_ORIG, false, null, null, null);
						passos.get(passos.size() - 1).setBacktrack(backtrack);
						
						break;
					}
				}
				
				if (todasFitasAceitas) { //PROCURAR OUTROS POSSÍVEIS ESTADOS: NÃO DETERMINÍSTICO
					passos.add(new Passo(t.getEstadoFinal(), passos.get(passoAtual).getFitas(), passos.get(passoAtual).getCaracteresFitas(), passos.get(passoAtual).getPosicaoFita(), false, t.getSimbolos().get(idxSimb).getLeitura(), t.getSimbolos().get(idxSimb).getEscrita(), t.getSimbolos().get(idxSimb).getMovimento()));
					
					if (t.getEstadoFinal().eFinal()) {
						estadoFinalEncontrado = true; //Quando um final é encontrado, a fita é aceita.
						
						//Registrando o local da aceitação dessa transição:
						passos.get(passos.size() - 1).setTransicao(t);
						passos.get(passos.size() - 1).setIdxSimbolo(idxSimb);
					}
					else {
						loopInfinito = buscaProfundidadePP(passos, passos.size() - 1, estadosEmProcessamento, loopInfinito); //Buscando estado final em profundidade.
						
						if (loopInfinito) return loopInfinito;
					}
				}
				
				//Reseta para testar o próximo símbolo. 
				passos.get(passoAtual).setFitas(new ArrayList<String>());
				passos.get(passoAtual).setCaracteresFitas(new ArrayList<char[]>());
				passos.get(passoAtual).setPosicoesFitas(new ArrayList<Integer>());
				
				for (int i = 0; i < fitas_ORIG.size(); i++) {
					passos.get(passoAtual).getFitas().add(fitas_ORIG.get(i));
					passos.get(passoAtual).getCaracteresFitas().add(passos.get(passoAtual).getFitas().get(i).toCharArray());
					passos.get(passoAtual).getPosicaoFita().add(posicaoFita_ORIG.get(i));
				}
				
				if (estadoFinalEncontrado) return loopInfinito;
			}
		}
		
		return loopInfinito;
	}
	
	
	public char testarEntradaME(String entrada)
	{
		ArrayList<String> fitas = new ArrayList<String>();
		ArrayList<char[]> caracteresFita = new ArrayList<char[]>();
		ArrayList<Integer> posicaoFita = new ArrayList<Integer>();
		ArrayList<EstadoEmProcessamento> estadosEmProcessamento = new ArrayList<EstadoEmProcessamento>();
		boolean loopInfinito = false;
		
		estadoFinalEncontrado = false;
		fitas.add(entrada); //1ª fita tem o conteúdo da entrada.
		caracteresFita.add(fitas.get(0).toCharArray());
		posicaoFita.add(0);
		
		for (int i = 1; i < numFitas; i++) { //Demais fitas são "zeradas".
			fitas.add("□");
			caracteresFita.add(fitas.get(i).toCharArray());
			posicaoFita.add(0);
		}
		
		//Procurando estado inicial (garantido q existe):
		for (Estado e : estados) {
			if (e.eInicial()) {
				loopInfinito = buscaProfundidadeME(e, fitas, caracteresFita, posicaoFita, estadosEmProcessamento, loopInfinito); //Lançando Busca em Profundidade a partir do estado inicial.
				
				break;
			}
		}
		
		if (loopInfinito) return 2;
		else if (estadoFinalEncontrado) return 0;
		else return 1;
	}
	
	
	protected boolean buscaProfundidadeME(Estado estadoAtual, ArrayList<String> fitas, ArrayList<char[]> caracteresFita, ArrayList<Integer> posicaoFita, ArrayList<EstadoEmProcessamento> estadosEmProcessamento, boolean loopInfinito)
	{
		boolean todasFitasAceitas = true;
		ArrayList<String> fitas_ORIG = new ArrayList<String>();
		ArrayList<char[]> caracteresFita_ORIG = new ArrayList<char[]>();
		ArrayList<Integer> posicaoFita_ORIG = new ArrayList<Integer>();
		
		addEstadoProcessamento(estadosEmProcessamento, estadoAtual); //Evita loop infinito.
		
		if (getEstadoEmProcessamento(estadosEmProcessamento, estadoAtual).getChamadas() > infoFuncionalidade.getLoopMaximo()) {
			loopInfinito = true; //Cancelando a simulação: entrou em loop "infinito" (maior do que o permitido).
			
			return loopInfinito;
		}
		
		for (int i = 0; i < fitas.size(); i++) {
			fitas_ORIG.add(fitas.get(i));
			caracteresFita_ORIG.add(caracteresFita.get(i));
			posicaoFita_ORIG.add(posicaoFita.get(i));
		}
		
		for (Transicao t : estadoAtual.getTransicoesSaida()) { //Procurando uma transição que aceite o símbolo atual.
			for (Simbolo s : t.getSimbolos()) { //Testar a combinação de todas as transições com todas as transições.
				todasFitasAceitas = true; //Verificando se algum estado é alcançável por todas as fitas de uma transição.
				
				for (int idx = 0; idx < numFitas; idx++) { //n símbolos para n fitas.
					if (s.getLeitura(idx).equals(String.valueOf(caracteresFita.get(idx)[posicaoFita.get(idx)]))) { //Símbolo lido aceito.
						caracteresFita.get(idx)[posicaoFita.get(idx)] = s.getEscrita(idx).toCharArray()[0]; //Escrevendo na fita (atualizando).
						fitas.set(idx, String.valueOf(caracteresFita.get(idx))); //Atualizando fita.
						
						if (s.getMovimento(idx).equals("D")) { //MOVIMENTA PRA DIREITA.
							posicaoFita.set(idx, posicaoFita.get(idx) + 1);
							
							if (posicaoFita.get(idx) == fitas.get(idx).length()) {
								fitas.set(idx, String.valueOf(caracteresFita.get(idx)) + "□"); //Aumentando fita ("infinita" pra direita).
								caracteresFita.set(idx, fitas.get(idx).toCharArray()); //Atualizando tamanho da fita.
							}
						}
						else if (s.getMovimento(idx).equals("E")) { //MOVIMENTA PRA ESQUERDA.
							posicaoFita.set(idx, posicaoFita.get(idx) - 1);
							
							if (posicaoFita.get(idx) == -1) {
								fitas.set(idx, "□" + String.valueOf(caracteresFita.get(idx))); //Aumentando fita ("infinita" pra esquerda).
								caracteresFita.set(idx, fitas.get(idx).toCharArray()); //Atualizando tamanho da fita.
								posicaoFita.set(idx, 0);
							}
						}
						else
						; //MOVIMENTO "PARADO".
					}
					else { //Nem todas as fitas são aceitas por essa transição => testa o próximo símbolo.
						todasFitasAceitas = false;
						
						break;
					}
				}
				
				if (todasFitasAceitas) { //PROCURAR OUTROS POSSÍVEIS ESTADOS: NÃO DETERMINÍSTICO							
					if (t.getEstadoFinal().eFinal()) {
						estadoFinalEncontrado = true; //Quando um final é encontrado, a fita é aceita.
						
						return loopInfinito;
					}
					else {
						loopInfinito = buscaProfundidadeME(t.getEstadoFinal(), fitas, caracteresFita, posicaoFita, estadosEmProcessamento, loopInfinito); //Buscando estado final em profundidade.
						
						if (loopInfinito) return loopInfinito;
						
						if (estadoFinalEncontrado) return loopInfinito;
					}
				}
				
				//Reseta para testar o próximo símbolo. 
				fitas = new ArrayList<String>();
				caracteresFita = new ArrayList<char[]>();
				posicaoFita = new ArrayList<Integer>();
				
				for (int i = 0; i < fitas_ORIG.size(); i++) {
					fitas.add(fitas_ORIG.get(i));
					caracteresFita.add(fitas.get(i).toCharArray());
					posicaoFita.add(posicaoFita_ORIG.get(i));
				}
			}
		}
		
		return loopInfinito;
	}
	
	
	public int getNumFitas()
	{
		return numFitas;
	}
	
	
	protected void addEstadoProcessamento(ArrayList<EstadoEmProcessamento> estadosEmProcessamento, Estado e)
	{
		for (EstadoEmProcessamento ep : estadosEmProcessamento) {
			if (ep.getEstado() == e) {
				ep.setChamadas(ep.getChamadas() + 1);
				
				return;
			}
		}
		
		estadosEmProcessamento.add(new EstadoEmProcessamento(e));
		estadosEmProcessamento.get(estadosEmProcessamento.size() - 1).setChamadas(1);
	}
	
	
	public EstadoEmProcessamento getEstadoEmProcessamento(ArrayList<EstadoEmProcessamento> estadosEmProcessamento, Estado e)
	{
		for (EstadoEmProcessamento ep : estadosEmProcessamento) {
			if (ep.getEstado() == e) return ep;
		}
		
		return null;
	}
	
	
	public void setMaquinaModificada(boolean maquinaModificada)
	{
		this.maquinaModificada = maquinaModificada;
	}
	
	
	public boolean getMaquinaModificada()
	{
		return maquinaModificada;
	}
	
	
	public JanelaPrincipal getJanelaPrincipal()
	{
		return janelaPrincipal;
	}
	
	
	public void setEstados(ArrayList<Estado> estados)
	{
		this.estados = estados;
	}
	
}
