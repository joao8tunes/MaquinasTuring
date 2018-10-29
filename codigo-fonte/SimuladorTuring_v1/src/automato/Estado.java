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

import gui.JanelaTipoEstado;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Estado extends JLabel implements MouseListener, MouseMotionListener
{
	
	
	private static final long serialVersionUID = 1L;
	
	public final static String ESTADO_NORMAL = "/imagens/estado_normal.png";
	public final static String ESTADO_INICIAL = "/imagens/estado_inicial.png";
	public final static String ESTADO_FINAL = "/imagens/estado_final.png";
	public final static String ESTADO_INICIAL_FINAL = "/imagens/estado_inicial_final.png";
	public final static String ESTADO_NORMAL_SELECIONADO = "/imagens/estado_normal_sel.png";
	public final static String ESTADO_INICIAL_SELECIONADO = "/imagens/estado_inicial_sel.png";
	public final static String ESTADO_FINAL_SELECIONADO = "/imagens/estado_final_sel.png";
	public final static String ESTADO_INICIAL_FINAL_SELECIONADO = "/imagens/estado_inicial_final_sel.png";
	
	/* Utilizado somente na seleção da simulação da classe 'PainelAnimacao': */
	public final static char SELECIONADO = 0;
	public final static char CORRETO = 1;
	public final static char ERRADO = 2;
	
	/* Utilizado somente na classe 'TipoEstado' e na seleção da simulação da classe 'PainelAnimacao': */
	public final static String ESTADO_NORMAL_CORRETO = "/imagens/estado_normal_correto.png";
	public final static String ESTADO_INICIAL_CORRETO = "/imagens/estado_inicial_correto.png";
	public final static String ESTADO_FINAL_CORRETO = "/imagens/estado_final_correto.png";
	public final static String ESTADO_INICIAL_FINAL_CORRETO = "/imagens/estado_inicial_final_correto.png";
	public final static String ESTADO_NORMAL_ERRADO = "/imagens/estado_normal_errado.png";
	public final static String ESTADO_INICIAL_ERRADO = "/imagens/estado_inicial_errado.png";
	public final static String ESTADO_FINAL_ERRADO = "/imagens/estado_final_errado.png";
	public final static String ESTADO_INICIAL_FINAL_ERRADO = "/imagens/estado_inicial_final_errado.png";
	
	private int baseX = -1;
	private int baseY = -1;
	private ImageIcon imagemEstado = new ImageIcon(getClass().getResource(ESTADO_NORMAL));
	private String estadoAtual = ESTADO_NORMAL;
	private int idxNome;
	
	private PainelAnimacao painelAnimacao;
	private ArrayList<Transicao> transicoesEntrada = new ArrayList<Transicao>(); //Transições que entram nesse estado.
	private ArrayList<Transicao> transicoesSaida = new ArrayList<Transicao>(); //Transições que saem desse estado.
	private JanelaTipoEstado tipoEstado = new JanelaTipoEstado(this);
	
	
	public Estado(int posX, int posY, int idxNome, PainelAnimacao painelAnimacao)
	{
		this.idxNome = idxNome;
		this.painelAnimacao = painelAnimacao;
		addMouseListener(this);
		addMouseMotionListener(this);
		setBounds(posX, posY, 50, 50); //Criando o componente na posição atual do cursor.
		setIcon(imagemEstado);
		setBorder(null);
		setText("q" + idxNome);
		setFont(new Font("Monospaced", Font.BOLD, 12));
		
		//Posicionamento necessário para exibição de texto sobre o JLabel:
		setVerticalTextPosition(CENTER);
		setHorizontalTextPosition(CENTER);
		setVisible(true);
	}
	
	
	/**************************************************************************/
	/*** MÉTODOS SOBRESCRITOS *************************************************/
	/**************************************************************************/
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON3) { //Editar estado com clique no BOTÃO DIREITO do mouse.
			if (!painelAnimacao.getMaquinaModificada()) {
				painelAnimacao.setMaquinaModificada(true);
				
				painelAnimacao.getJanelaPrincipal().setNomeArquivo("* " + painelAnimacao.getJanelaPrincipal().getNomeArquivo(), true);
			}
			
			tipoEstado.exibir();
			painelAnimacao.revalidate();
			painelAnimacao.repaint();
		}
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) //Componente arrastado na frente dos demais.
	{
		if (!painelAnimacao.getMaquinaModificada()) {
			painelAnimacao.setMaquinaModificada(true);
			
			painelAnimacao.getJanelaPrincipal().setNomeArquivo("* " + painelAnimacao.getJanelaPrincipal().getNomeArquivo(), true);
		}
		
		Component c = e.getComponent();
		Container parent = c.getParent();
		Component[] all = parent.getComponents();
		
		for (int i = 0; i < all.length; i++) {
			parent.remove(all[i]);
		}
		
		parent.add(c);
		
		for (int i = 0; i < all.length; i++) {
			if (all[i] != c) parent.add(all[i]);
		}
		
		this.baseX = e.getX();
		this.baseY = e.getY();
		
		if (painelAnimacao.getInfoFuncionalidade().getFuncao() == InfoFuncionalidade.TRANSICAO) {
			if (painelAnimacao.getTransicaoTemporaria() == null) { //Criando novo arco de transição temporária.
				Transicao novaTransicao;
				
				if (eInicial()) novaTransicao = new Transicao(getX() + 50, getY() + 25, painelAnimacao);
				else novaTransicao = new Transicao(getX() + 25, getY() + 25, painelAnimacao);
				
				novaTransicao.adicionarEstadoInicial(this); //Adicionando esse estado no início do arco.
				painelAnimacao.setTransicaoTemporaria(novaTransicao); //Sobrescreve o arco temporário atual.
			}
			else { //Criando nova transição para esse estado, utilizando o arco de transição temporária.
				   //Verificando se já existe um arco para essa transição:
				for (Transicao arcoInicioAutomatoInicio : painelAnimacao.getTransicaoTemporaria().getEstadoInicial().getTransicoesSaida()) {
					if (arcoInicioAutomatoInicio.getEstadoFinal() == this) { //Já existe um arco criado para essa transição. Impossível criar mais do que duas linhas entre dois estados; pode editar apenas.
						painelAnimacao.removerTransicaoTemporaria();
						arcoInicioAutomatoInicio.setSimbolo(); //Invocando a criação de um novo símbolo.
						
						return;
					}
				}
				
				painelAnimacao.getTransicaoTemporaria().setSimbolo(); //Invocando a criação do primeiro símbolo.
				
				if (!painelAnimacao.getSimbolo().equals("")) { //Quando o símbolo é "" (vazio), significa que a criação do símbolo foi cancelada. 
					//Ainda não existe o arco de transição para essa relação de estados:
					painelAnimacao.getTransicaoTemporaria().getEstadoInicial().setTransicoesSaida(painelAnimacao.getTransicaoTemporaria()); //Conectando o arco temporário ao estado criador.
					painelAnimacao.getTransicaoTemporaria().adicionarEstadoFinal(this); //Adicionando esse estado no fim do arco.
					setTransicoesEntrada(painelAnimacao.getTransicaoTemporaria()); //Adicionando o arco na lista de arcos finais desse estado.
					
					//Atualizando as coordenadas do fim do arco:
					if (painelAnimacao.getTransicaoTemporaria().getEstadoFinal().eInicial()) painelAnimacao.getTransicaoTemporaria().setPontoFinal(getX() + 50, getY() + 25);
					else painelAnimacao.getTransicaoTemporaria().setPontoFinal(getX() + 25, getY() + 25);
					
					painelAnimacao.getTransicaoTemporaria().setFixacao(); //Deixando a seta apontando para a borda do estado.
					
					//Há ciclo:
					if (painelAnimacao.getTransicaoTemporaria().getEstadoInicial() == painelAnimacao.getTransicaoTemporaria().getEstadoFinal()) {
						painelAnimacao.getTransicaoTemporaria().setPontoFinal(painelAnimacao.getTransicaoTemporaria().getEstadoFinal().getX() + 40, painelAnimacao.getTransicaoTemporaria().getEstadoFinal().getY() + 15);
						painelAnimacao.getTransicaoTemporaria().setPontoInicial(painelAnimacao.getTransicaoTemporaria().getEstadoInicial().getX() + 10, painelAnimacao.getTransicaoTemporaria().getEstadoInicial().getY() + 15);
						painelAnimacao.getTransicaoTemporaria().setCurva(0, 80);
					}
					else { //Verificando se possui transição no sentido inverso:
						Transicao transicaoInversa = null; //Supondo que não existe transição inversa entre os dois estados.
						
						for (Transicao t : transicoesSaida) {
							if (t.getEstadoFinal() == painelAnimacao.getTransicaoTemporaria().getEstadoInicial()) {
								transicaoInversa = t; //Existe transição inversa entre os estados.
								break;
							}
						}
						
						if (transicaoInversa != null) { //Tem que formar curva nos arcos que conectam este autômato e o autômato inicial do arco atual.
							painelAnimacao.getTransicaoTemporaria().setEspacoCurva(true);
							painelAnimacao.getTransicaoTemporaria().setCurva(0, 50);
							
							transicaoInversa.setEspacoCurva(true);
							transicaoInversa.setCurva(0, 50);
						}
					}
				}
				
				painelAnimacao.getTransicaoTemporaria().atualizarTransicao();
				painelAnimacao.removerTransicaoTemporaria();
			}
		}
		else if (painelAnimacao.getInfoFuncionalidade().getFuncao() == InfoFuncionalidade.REMOCAO) {
			removerEstado();
		}
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) //Evento de soltar o pressionamento do clique do mouse.
	{
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
		setCursor(new Cursor(painelAnimacao.getInfoFuncionalidade().getPonteiroCursor()));
	}
	
	
	@Override
	public void mouseExited(MouseEvent e)
	{
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) //Evento de arrastar o componente.
	{
		if (painelAnimacao.getInfoFuncionalidade().getFuncao() == InfoFuncionalidade.MOVIMENTACAO) {
			if (!painelAnimacao.getMaquinaModificada()) {
				painelAnimacao.setMaquinaModificada(true);
				
				painelAnimacao.getJanelaPrincipal().setNomeArquivo("* " + painelAnimacao.getJanelaPrincipal().getNomeArquivo(), true);
			}
			
			Component c = e.getComponent();
			int x = c.getX() + e.getX() - this.baseX;
			int y = c.getY() + e.getY() - this.baseY;
			Container parent = c.getParent();
			Dimension pSize = parent.getSize();
			int deslocamentoInicioFim = 0;
			
			if (eInicial()) deslocamentoInicioFim = 25;
			
			x = Math.max(x, 0);
			x = Math.min(x, pSize.width);
			y = Math.max(y, 0);
			y = Math.min(y, pSize.height);
			c.setLocation(x, y);
			c.getParent().repaint();
			
			for (Transicao l : transicoesSaida) {
				if (l.getEstadoInicial() == l.getEstadoFinal()) l.setPontoInicial(getX() + 10 + deslocamentoInicioFim, getY() + 15);
				else l.setPontoInicial(getX() + 25 + deslocamentoInicioFim, getY() + 25);
			}
			
			for (Transicao l : transicoesEntrada) {
				if (l.getEstadoInicial() == l.getEstadoFinal()) l.setPontoFinal(getX() + 40 + deslocamentoInicioFim, getY() + 15);
				else l.setPontoFinal(getX() + 25 + deslocamentoInicioFim, getY() + 25);
			}
			
			//Redimensionamento automático do JPanel:
			painelAnimacao.redimensionarPainel(e.getXOnScreen(), e.getYOnScreen());
		}
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if (painelAnimacao.getInfoFuncionalidade().getFuncao() == InfoFuncionalidade.TRANSICAO) {
			if (!painelAnimacao.getMaquinaModificada()) {
				painelAnimacao.setMaquinaModificada(true);
				
				painelAnimacao.getJanelaPrincipal().setNomeArquivo("* " + painelAnimacao.getJanelaPrincipal().getNomeArquivo(), true);
			}
			
			if (painelAnimacao.getTransicaoTemporaria() != null) {
				painelAnimacao.getTransicaoTemporaria().setPontoFinal(this.getX() + e.getX(), this.getY() + e.getY()); //Movendo o arco temporário de acordo com o cursor do mouse.
				painelAnimacao.revalidate();
				painelAnimacao.repaint();
			}
		}
	}
	
	
	/**************************************************************************/
	/*** MÉTODOS FUNCIONAIS ***************************************************/
	/**************************************************************************/
	
	public void adicionarTransicao(Transicao novoArco)
	{
		transicoesSaida.add(novoArco);
	}
	
	
	public ArrayList<Transicao> getTransicoesEntrada()
	{
		return (transicoesEntrada);
	}
	
	
	public ArrayList<Transicao> getTransicoesSaida()
	{
		return (transicoesSaida);
	}
	
	
	public void setTransicoesSaida(Transicao t)
	{
		transicoesSaida.add(t);
	}
	
	
	public void setTransicoesEntrada(Transicao t)
	{
		transicoesEntrada.add(t);
	}
	
	
	public void removerTransicaoEntrada(Transicao t)
	{
		transicoesEntrada.remove(t);
	}
	
	
	public void removerTransicaoSaida(Transicao t)
	{
		transicoesSaida.remove(t);
	}
	
	
	public void setEstado(String estado, boolean conflito)
	{
		if (conflito && (estado.equals(ESTADO_INICIAL) || estado.equals(ESTADO_INICIAL_FINAL))) { //Como só deve ter um único estado inicial, se for detectado a atualização de um novo estado inicial, o outro inicial deve ser não inicial.
		
			for (Estado e : painelAnimacao.getEstados()) {
				if (e.eInicial() && !e.equals(this)) { //Não verifica o estado q esá sendo atualizado.
					e.getTipoEstado().deselecionarTipoInicial();
					
					if (e.eFinal()) e.setEstadoAtual(ESTADO_FINAL); //INICIAL_FINAL -> FINAL.
					else e.setEstadoAtual(ESTADO_NORMAL); //INICIAL -> NORMAL.
					
					//Alterando as coordenadas de localização do estado na tela, e de suas transições:
					e.setBounds(e.getX() + 25, e.getY(), 50, 50);
					
					for (Transicao t : e.getTransicoesSaida()) {
						t.setPontoInicial(e.getX() + 25, e.getY() + 25);
					}
					
					for (Transicao t : e.getTransicoesEntrada()) {
						t.setPontoFinal(e.getX() + 25, e.getY() + 25);
					}
					
					break; //Não precisa percorrer os demais estados, pq só tem (tinha) um único estado inicial (agora não inicial).
				}
			}
		}
		
		setEstadoAtual(estado);
	}
	
	
	public boolean eInicial()
	{
		switch (estadoAtual) {
			case ESTADO_INICIAL:
				return (true);
			case ESTADO_INICIAL_SELECIONADO:
				return (true);
			case ESTADO_INICIAL_FINAL:
				return (true);
			case ESTADO_INICIAL_FINAL_SELECIONADO:
				return (true);
		}
		
		return (false);
	}
	
	
	public boolean eFinal()
	{
		switch (estadoAtual) {
			case ESTADO_FINAL:
				return (true);
			case ESTADO_FINAL_SELECIONADO:
				return (true);
			case ESTADO_INICIAL_FINAL:
				return (true);
			case ESTADO_INICIAL_FINAL_SELECIONADO:
				return (true);
		}
		
		return (false);
	}
	
	
	public String getEstadoAtual()
	{
		return (estadoAtual);
	}
	
	
	public void setEstadoAtual(String estado)
	{
		estadoAtual = estado;
		imagemEstado = new ImageIcon(getClass().getResource(estado));
		setIcon(imagemEstado);
	}
	
	
	public JanelaTipoEstado getTipoEstado() //Chamado em 'setEstado()' (nessa classe) Se houver um estado inicial e for requisitada a troca por um outro estado inicial, o estado anterior antigo deve ser atualizado dentro da classe 'TipoEstado', pra atualizar tbm o RadioButtom 'Inicial'.
	{
		return tipoEstado;
	}
	
	
	public PainelAnimacao getPainelAnimacao()
	{
		return painelAnimacao;
	}
	
	
	public void removerEstado()
	{
		for (Transicao t : transicoesEntrada) {
			for (Simbolo s : t.getSimbolos()) {
				painelAnimacao.removerComponente(s);
			}
			
			t.getEstadoInicial().removerTransicaoSaida(t);
		}
		
		for (Transicao t : transicoesSaida) {
			for (Simbolo s : t.getSimbolos()) {
				painelAnimacao.removerComponente(s);
			}
			
			t.getEstadoFinal().removerTransicaoEntrada(t);
		}
		
		painelAnimacao.removerEstado(this);
		painelAnimacao.revalidate();
		painelAnimacao.repaint();
	}
	
	
	public void selecionarEstado(char seletor)
	{
		switch (seletor) {
			case CORRETO: {
				if (eInicial()) {
					if (eFinal()) setIcon(new ImageIcon(getClass().getResource(ESTADO_INICIAL_FINAL_CORRETO)));
					else setIcon(new ImageIcon(getClass().getResource(ESTADO_INICIAL_CORRETO)));
				}
				else if (eFinal()) setIcon(new ImageIcon(getClass().getResource(ESTADO_FINAL_CORRETO)));
				else setIcon(new ImageIcon(getClass().getResource(ESTADO_NORMAL_CORRETO)));
			}
			break;
			case ERRADO: {
				if (eInicial()) {
					if (eFinal()) setIcon(new ImageIcon(getClass().getResource(ESTADO_INICIAL_FINAL_ERRADO)));
					else setIcon(new ImageIcon(getClass().getResource(ESTADO_INICIAL_ERRADO)));
				}
				else if (eFinal()) setIcon(new ImageIcon(getClass().getResource(ESTADO_FINAL_ERRADO)));
				else setIcon(new ImageIcon(getClass().getResource(ESTADO_NORMAL_ERRADO)));
			}
			break;
			default: { //SELECIONADO
				if (eInicial()) {
					if (eFinal()) setIcon(new ImageIcon(getClass().getResource(ESTADO_INICIAL_FINAL_SELECIONADO)));
					else setIcon(new ImageIcon(getClass().getResource(ESTADO_INICIAL_SELECIONADO)));
				}
				else if (eFinal()) setIcon(new ImageIcon(getClass().getResource(ESTADO_FINAL_SELECIONADO)));
				else setIcon(new ImageIcon(getClass().getResource(ESTADO_NORMAL_SELECIONADO)));
			}
		}
	}
	
	
	public void deselecionarEstado()
	{
		setIcon(new ImageIcon(getClass().getResource(estadoAtual)));
	}
	
	
	public int getIdxNome()
	{
		return idxNome;
	}
	
}
