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

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;


public class Transicao
{
	
	
	private double[] coordInicio = new double[2];
	private double[] coordCurva = new double[2];
	private double[] coordFim = new double[2];
	private QuadCurve2D arco;
	private boolean temporaria = true; //Inicialmente, todo arco de transição é temporário.
	private int espacoCurva = 8; //Necessário para deslocar o conjunto de símbolos se houverem transições reversas (uma vai e outra vem do mesmo par de estados).
	
	private Seta seta;
	private Estado estadoInicial;
	private Estado estadoFinal;
	private ArrayList<Simbolo> simbolos = new ArrayList<Simbolo>(); //Símbolos reconhecidos por essa transição.
	private PainelAnimacao painelAnimacao;
	
	
	public Transicao(int iniX, int iniY, PainelAnimacao painelAnimacao)
	{
		this.painelAnimacao = painelAnimacao;
		
		//Criando o arco:
		coordInicio[0] = iniX;
		coordInicio[1] = iniY;
		coordCurva[0] = 0;
		coordCurva[1] = 0;
		coordFim[0] = iniX;
		coordFim[1] = iniY;
		
		seta = new Seta(iniX, iniY);
		
		/* Inicialmente, não há curvatura no arco, que só é adicionada quando da criação de duas linhas ligando os mesmos dois estados: */
		arco = new QuadCurve2D.Double(coordInicio[0], coordInicio[1], coordInicio[0], coordInicio[1], coordFim[0], coordFim[1]);
		atualizarTransicao();
	}
	
	
	public Transicao(int iniX, int iniY)
	{
		//Criando o arco:
		coordInicio[0] = iniX;
		coordInicio[1] = iniY;
		coordCurva[0] = 0;
		coordCurva[1] = 0;
		coordFim[0] = iniX;
		coordFim[1] = iniY;
		
		seta = new Seta(iniX, iniY);
		
		/* Inicialmente, não há curvatura no arco, que só é adicionada quando da criação de duas linhas ligando os mesmos dois autômatos: */
		arco = new QuadCurve2D.Double(coordInicio[0], coordInicio[1], coordInicio[0], coordInicio[1], coordFim[0], coordFim[1]);
		atualizarTransicao();
	}
	
	
	/***************************************************/
	/* MÉTODOS DE CONTROLE DAS CONEXÕES DOS AUTÔMATOS: */
	/***************************************************/
	public void desenhar(Graphics2D g2d)
	{
		g2d.draw(arco);
		g2d.draw(seta.getSeta());
	}
	
	
	public void setPontoInicial(int x, int y)
	{
		coordInicio[0] = x;
		coordInicio[1] = y;
		atualizarTransicao();
	}
	
	
	public void setPontoFinal(int x, int y)
	{
		coordFim[0] = x;
		coordFim[1] = y;
		atualizarTransicao();
	}
	
	
	public void gambi() //Nem queira saber...
	{
		double angulo;
		
		angulo = Math.toRadians(-73);
		seta.rotacionar(coordFim[0] + 30, coordFim[1], Math.sin(angulo), Math.cos(angulo), temporaria, true); //Rotacionando a seta.
	}
	
	
	public void setCurva(int x, int y)
	{
		if (getEstadoInicial() == getEstadoFinal()) { //Há ciclo.
			double xMedio, yMedio, seno = 0, cosseno = -1, xCurva, yCurva, angulo;
			
			angulo = Math.toRadians(-73);
			coordCurva[0] = x;
			coordCurva[1] = y;
			xMedio = (coordInicio[0] + coordFim[0]) / 2;
			yMedio = (coordInicio[1] + coordFim[1]) / 2;
			
			//Coordenada x do ponto central de curvatura:
			xCurva = xMedio + (x * cosseno - y * seno); //Ponto médio mais a rotação do ponto central da curvatura.
			
			//Coordenada y do ponto central de curvatura:
			yCurva = yMedio + (y * cosseno + x * seno); //Ponto médio mais a rotação do ponto central da curvatura.
			
			arco.setCurve(coordInicio[0], coordInicio[1], xCurva, yCurva, coordFim[0], coordFim[1]); //Rotacionando o arco.
			seta.rotacionar(coordFim[0], coordFim[1], Math.sin(angulo), Math.cos(angulo), temporaria, true); //Rotacionando a seta.
			int posSimbolo = 0;
			
			for (Simbolo s : simbolos) { //Rotacionando os símbolos.
				s.rotacionar(xMedio, yMedio, seno, cosseno, posSimbolo, true);
				++posSimbolo;
			}
		}
		else {
			double deltaX = coordFim[0] - coordInicio[0], deltaY = coordFim[1] - coordInicio[1];
			double tangente = deltaY / deltaX; //Cateto oposto/cateto adjacente.
			double anguloCurva, seno, cosseno;
			double xCurva, yCurva, xMedio, yMedio;
			
			anguloCurva = Math.atan(tangente); //O ângulo de inclinação do arco é o arctg da tg.
			
			if (deltaX > 0) {
				if (deltaY > 0) anguloCurva = (Math.PI - anguloCurva); //2º quadrante.
				else anguloCurva = (Math.PI - anguloCurva); //3º quadrante.
			}
			else {
				if (deltaY > 0)
				; //1º quadrante.
				else anguloCurva = (anguloCurva - 2 * Math.PI); //4º quadrante.
			}
			
			if (deltaX >= 0) anguloCurva *= -1;
			
			seno = Math.sin(anguloCurva);
			cosseno = Math.cos(anguloCurva);
			
			//Armazena a posição relativa da curvatura do arco:
			coordCurva[0] = x;
			coordCurva[1] = y;
			
			xMedio = (coordInicio[0] + coordFim[0]) / 2;
			yMedio = (coordInicio[1] + coordFim[1]) / 2;
			
			//Coordenada x do ponto central de curvatura:
			xCurva = xMedio + (x * cosseno - y * seno); //Ponto médio mais a rotação do ponto central da curvatura.
			
			//Coordenada y do ponto central de curvatura:
			yCurva = yMedio + (y * cosseno + x * seno); //Ponto médio mais a rotação do ponto central da curvatura.
			
			arco.setCurve(coordInicio[0], coordInicio[1], xCurva, yCurva, coordFim[0], coordFim[1]); //Rotacionando o arco.
			
			//----------------------------------------------------------------------
			double dX = coordFim[0] - xCurva, dY = coordFim[1] - yCurva;
			double tg = dY / dX, ang = Math.atan(tg);
			
			//Cálculos somente para a rotação da 'Seta':
			if (dX > 0) {
				if (dY > 0) ang = (Math.PI - ang); //2º quadrante.
				else ang = (Math.PI - ang); //3º quadrante.
			}
			else {
				if (dY > 0)
				; //1º quadrante.
				else ang = (ang - 2 * Math.PI); //4º quadrante.
			}
			
			if (dX >= 0) ang *= -1;
			
			seta.rotacionar(coordFim[0], coordFim[1], Math.sin(ang), Math.cos(ang), temporaria, false); //Rotacionando a seta.
			//----------------------------------------------------------------------
			int posSimbolo = 0;
			
			for (Simbolo s : simbolos) { //Rotacionando os símbolos.
				s.rotacionar(xMedio, yMedio, seno, cosseno, posSimbolo, false);
				++posSimbolo;
			}
		}
	}
	
	
	public Shape getArco()
	{
		return (arco);
	}
	
	
	public Seta getSeta()
	{
		return (seta);
	}
	
	
	public void adicionarEstadoInicial(Estado estado) //Automato de onde sai a linha.
	{
		estadoInicial = estado;
	}
	
	
	public void adicionarEstadoFinal(Estado estado) //Automato de onde termina a linha.
	{
		estadoFinal = estado;
	}
	
	
	public Estado getEstadoInicial()
	{
		return (estadoInicial);
	}
	
	
	public Estado getEstadoFinal()
	{
		return (estadoFinal);
	}
	
	
	public void setSimbolo()
	{
		double deltaX = coordFim[0] - coordInicio[0], deltaY = coordFim[1] - coordInicio[1];
		Simbolo s;
		String novoSimbolo = "";
		
		for (int i = 0; i < painelAnimacao.getNumFitas(); i++) {
			novoSimbolo += "□ ; □ ; D -";
		}
		
		painelAnimacao.setSimbolo(novoSimbolo);
		
		if (!painelAnimacao.getSimbolo().equals("")) { //Opção não cancelada.
			s = new Simbolo(painelAnimacao.getSimbolo(), 0, 0, (int) (deltaX / 2 + coordInicio[0]), (int) (deltaY / 2 + coordInicio[1]), this, painelAnimacao.getNumFitas(), simbolos.size());
			simbolos.add(s); //Adicionando um novo símbolo à transição.
			painelAnimacao.add(s); //Adicionando o símbolo ao painel de animação.
			atualizarTransicao();
		}
	}
	
	
	public void setSimboloPorArquivo(String simbolo)
	{
		double deltaX = coordFim[0] - coordInicio[0], deltaY = coordFim[1] - coordInicio[1];
		Simbolo s;
		
		s = new Simbolo(simbolo, 0, 0, (int) (deltaX / 2 + coordInicio[0]), (int) (deltaY / 2 + coordInicio[1]), this, painelAnimacao.getNumFitas(), simbolos.size());
		simbolos.add(s); //Adicionando um novo símbolo à transição.
		painelAnimacao.add(s); //Adicionando o símbolo ao painel de animação.
		atualizarTransicao();
	}
	
	
	public void editarSimbolo(Simbolo s)
	{
		painelAnimacao.setSimbolo(s.getText());
		
		if (painelAnimacao.getSimbolo().equals("")) { //Opção cancelada.
			return;
		}
		
		s.setText(painelAnimacao.getSimbolo());
		atualizarTransicao();
	}
	
	
	public ArrayList<Simbolo> getSimbolos()
	{
		return (simbolos);
	}
	
	
	public void removerSimbolo(Simbolo s)
	{
		simbolos.remove(s);
		painelAnimacao.removerComponente(s);
		
		if (simbolos.size() == 0) { //Não existem mais símbolos para essa transição.
			estadoInicial.removerTransicaoSaida(this); //Removendo a referência com o estado inicial.
			estadoFinal.removerTransicaoEntrada(this); //Removendo a referência com o estado final.
			
			for (Transicao t : estadoFinal.getTransicoesSaida()) {
				if (t.getEstadoFinal() == estadoInicial) { //Atualizando a curvatura de um único arco: reta quando há somente um caminho de transição entre dois estados.
					t.setCurva(0, 0);
					t.atualizarTransicao();
					break;
				}
			}
		}
		
		atualizarTransicao();
		painelAnimacao.revalidate();
		painelAnimacao.repaint();
	}
	
	
	public void adicionarSimbolo(String novoSimbolo)
	{
		double deltaX = coordFim[0] - coordInicio[0], deltaY = coordFim[1] - coordInicio[1];
		Simbolo s;
		
		s = new Simbolo(novoSimbolo, 0, 0, (int) (deltaX / 2 + coordInicio[0]), (int) (deltaY / 2 + coordInicio[1]), this, painelAnimacao.getNumFitas(), simbolos.size());
		simbolos.add(s); //Adicionando um novo símbolo à transição.
		if (painelAnimacao != null) painelAnimacao.add(s); //Adicionando o símbolo ao painel de animação.
		atualizarTransicao();
	}
	
	
	public void setFixacao()
	{
		temporaria = false;
		seta.setFixacao();
	}
	
	
	public void atualizarTransicao() //Sim, isso atualiza tudo por algum motivo "sombrio"; sejamos práticos, então nada de hipocrisia! :D
	{
		setCurva((int) coordCurva[0], (int) coordCurva[1]);
	}
	
	
	public void setEspacoCurva(boolean b)
	{
		if (b) espacoCurva = 33;
		else espacoCurva = 8;
	}
	
	
	public int getEspacoCurva()
	{
		return espacoCurva;
	}
	
}
