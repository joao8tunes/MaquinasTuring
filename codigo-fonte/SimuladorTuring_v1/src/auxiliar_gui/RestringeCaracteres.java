/*
 * Simulador de Máquinas de Turing - v1
 * 
 * Data: 11/10/2014
 * Programadores: Carol Chaves (RA 1304984), João Antunes (RA 1305093)
 * 
 * Implementado com o Eclipse Kepler, utilizando WindowBuilder Pro GUI Designer. 
 * Abra as classes do pacote 'gui' com este programa.
 */

package auxiliar_gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


//Extraído de: http://javafree.uol.com.br/topic-10276-Restringindo-caracteres-em-um-JTextField.html
public class RestringeCaracteres extends PlainDocument
{
	
	
	private static final long serialVersionUID = 1L;
	final String permitidos;
	final int tamanho;
	
	
	public RestringeCaracteres(String permitidos, int tam)
	{
		this.permitidos = permitidos;
		tamanho = tam;
	}
	
	
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
	{
		//Aqui é feito o 'filtro', retirando os caracteres não permitidos.  
		//Esta lógica pode ser substituída, para criar qualquer tipo de filtro  
		if (tamanho <= 0) { // aceitara qualquer no. de caracteres  
			StringBuffer sb = constroiPermitidos(str);
			super.insertString(offset, sb.toString(), attr);
			return;
		}
		
		int ilen = (getLength() + str.length());
		if (ilen <= tamanho) { // se o comprimento final for menor... 
			StringBuffer sb = constroiPermitidos(str);
			super.insertString(offset, sb.toString(), attr); // ...aceita str  
		}
		else {
			if (getLength() == tamanho) {
				return; // nada a fazer  
			}
			
			StringBuffer sb = constroiPermitidos(str);
			String newStr = sb.toString().substring(0, (tamanho - getLength()));
			super.insertString(offset, newStr, attr);
		}
	}
	
	
	private StringBuffer constroiPermitidos(String str)
	{
		StringBuffer sb = new StringBuffer(str);
		for (int i = sb.length() - 1; i >= 0; i--) {
			if (permitidos.indexOf(sb.charAt(i)) < 0) {
				sb.replace(i, i + 1, "");
			}
		}
		return sb;
	}
	
}
