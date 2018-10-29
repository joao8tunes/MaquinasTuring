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

import java.awt.geom.GeneralPath;


public class Seta
{
	
	
	private GeneralPath seta;
	private double posIniX[] = new double[3]; //A seta simples é definida por 3 pontos.
	private double posIniY[] = new double[3];
	
	
	public Seta(int x, int y)
	{
		x = y = 100;
		double largura = 35, altura = 5;
		
		//Criando a seta do arco (para a direita, de cima pra baixo):
		posIniX[0] = -largura;
		posIniY[0] = altura;
		posIniX[1] = 0;
		posIniY[1] = 0;
		posIniX[2] = -largura;
		posIniY[2] = -altura;
		
		seta = new GeneralPath(GeneralPath.WIND_EVEN_ODD, posIniX.length);
		seta.moveTo(posIniX[0] + x, posIniY[0] + y); //Desenhando no sentido horário.
		seta.lineTo(posIniX[1] + x, posIniY[1] + y);
		seta.lineTo(posIniX[2] + x, posIniY[2] + y);
	}
	
	
	public GeneralPath getSeta()
	{
		return seta;
	}
	
	
	public void transladar(double x, double y)
	{
		seta = new GeneralPath(GeneralPath.WIND_EVEN_ODD, posIniX.length);
		seta.moveTo(x + posIniX[0], y + posIniY[0]); //Desenhando no sentido horário.
		seta.lineTo(x + posIniX[1], y + posIniY[1]);
		seta.lineTo(x + posIniX[2], y + posIniY[2]);
	}
	
	
	public void rotacionar(double x, double y, double seno, double cosseno, boolean temporaria, boolean ciclo)
	{
		cosseno *= -1;
		seno *= -1;
		
		double xP = 0, yP = 0, xR = 0, yR = 0;
		
		if (ciclo) {
			xR = x - 28;
			yR = y - 10;
		}
		else {
			if (!temporaria) xP = -25; //Para a 'Seta' ficar na borda do 'Estado'.
			xR = xP * cosseno - yP * seno + x;
			yR = xP * seno + yP * cosseno + y;
		}
		
		seta = new GeneralPath(GeneralPath.WIND_EVEN_ODD, posIniX.length);
		seta.moveTo((posIniX[0] * cosseno - posIniY[0] * seno + xR), (posIniX[0] * seno + posIniY[0] * cosseno + yR)); //Desenhando no sentido horário.
		seta.lineTo(posIniX[1] * cosseno - posIniY[1] * seno + xR, posIniX[1] * seno + posIniY[1] * cosseno + yR);
		seta.lineTo(posIniX[2] * cosseno - posIniY[2] * seno + xR, posIniX[2] * seno + posIniY[2] * cosseno + yR);
	}
	
	
	public void gambi(double x, double y, double seno, double cosseno)
	{
		cosseno *= -1;
		seno *= -1;
		
		double xR = x, yR = y - 10;
		
		seta = new GeneralPath(GeneralPath.WIND_EVEN_ODD, posIniX.length);
		seta.moveTo((posIniX[0] * cosseno - posIniY[0] * seno + xR), (posIniX[0] * seno + posIniY[0] * cosseno + yR)); //Desenhando no sentido horário.
		seta.lineTo(posIniX[1] * cosseno - posIniY[1] * seno + xR, posIniX[1] * seno + posIniY[1] * cosseno + yR);
		seta.lineTo(posIniX[2] * cosseno - posIniY[2] * seno + xR, posIniX[2] * seno + posIniY[2] * cosseno + yR);
	}
	
	
	public void setFixacao() //Transforma uma 'Transicao' temporária em real (fixa).
	{
		posIniX[0] = +-25;
		posIniX[2] = +-25;
	}
	
}
