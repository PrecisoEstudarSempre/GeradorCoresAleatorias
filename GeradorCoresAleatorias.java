import java.awt.Color;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

class GeradorCoresAleatorias {
	

	/**
	 * Método responsável pela geração de cores aleatoriamente.
	 * @return A cor
	 */
	private Color gerarCorAleatoriamente(){
		Random randColor = new Random();  
		int r = randColor.nextInt(256);
		int g = randColor.nextInt(256);
		int b = randColor.nextInt(256);
		return new Color(r, g, b);
	}

	/**
	 * Método que exporta a cor para o formato hexadecimal. 
	 * @param color Representa a cor.
	 * @return Retorna a cor no formato hexadecimal.
	 */
	private String gerarCorHexadecimal(Color color){
		return '#'+
			this.tratarHexString(Integer.toHexString(color.getRed()))+
			this.tratarHexString(Integer.toHexString(color.getGreen()))+
			this.tratarHexString(Integer.toHexString(color.getBlue()));
	}

	/**
	 * Método responsável por tratar a string hexadecimal. Caso a string possua tamanho 1, o método adiciona o número 0 na frente da string.
	 * @param hexString A string hexadecimal
	 * @return A string hexadecimal
	 */
	private String tratarHexString(String hexString){
		String hex = null;
		if(hexString.length() == 1){
			hex = '0'+hexString;
		}else{
			hex = hexString;
		}
		return hex;
	}

	/**
	 * Método que trata o brilho de uma cor. 
	 * Para que uma cor seja escolhida para o gráfico seu nível de brilho deve estar acima ou igual de 0.5 e abaixo ou igual de 0.9.
	 * @param brightness Representa o brilho.
	 * @return Retorna true caso a cor esteja de acordo com o ponto necessário (brilho), false caso contrário.
	 */
	private boolean isBrilhoCorreto(float brightness){
		if (brightness >= 0.5 && brightness <= 0.9) {
			return true;
		}
		return false;
	}
	
	/**
	 * Método que trata a saturação de uma cor. 
	 * Para que uma cor seja escolhida para o gráfico seu nível de saturação deve estar acima ou igual de 0.7.
	 * @param saturation Representa a saturação
	 * @return Retorna true caso a cor esteja de acordo com o ponto necessário (saturação), false caso contrário.
	 */
	private boolean isSaturacaoCorreta(float saturation){
		if (saturation >= 0.7){
			return true;
		}
		return false;
	}

	/**
	 * Método que realiza a carga inicial de cores proibidas.
	 * @param coresProibidas Representa a lista de cores proibidas.
	 */
	private void carregarCoresProibidasDefault(List<Color> coresProibidas){
		coresProibidas.add(new Color(0,0,0)); //preto
		coresProibidas.add(new Color(255,255,255)); //branco
	}

	/**
	 * Método que calcula a distância entre cores.
	 * @param cor1 Representa uma cor.
	 * @param cor2 Representa uma cor.
	 * @return Retorna a distância.
	 */
	private double calcularDistanciaDeCores(Color cor1, Color cor2){
		long meanRed = (cor1.getRed() + cor2.getRed())/2;
		long deltaRed = cor1.getRed() - cor2.getRed();
		long deltaGreen = cor1.getGreen() - cor2.getGreen();
		long deltaBlue = cor1.getBlue() - cor2.getBlue();
		return Math.sqrt((2+meanRed/256)*Math.pow(deltaRed, 2)+4*Math.pow(deltaGreen, 2)+(2+(255-meanRed)/256)*Math.pow(deltaBlue, 2));
	}
	
	/**
	 * Método que avalia se a distância entre duas é aceitável. Uma distância é dita aceita se é menor que 200. 
	 * @param cor1 Representa uma cor.
	 * @param cor2 Representa uma cor. 
	 * @return boolean Retorna true caso a distância seja menor que 200, false caso contrário.
	 */
	private boolean isDistanciaAceitavel(Color cor1, Color cor2){
		if (this.calcularDistanciaDeCores(cor1, cor2) < 200) {
			return false;
		}
		return true;
	}
	
	/**
	 * Método que valida se uma cor gerada aleatoriamente possui uma distância aceitável com as cores previamente permitidas. 
	 * @param coresPermitidas Representa as cores permitidas.
	 * @param corAleatoria Representa a cor gerada aleatoriamente.
	 * @return Retorna true caso a cor seja parecida, retorna false caso contrário.
	 */
	private boolean isCorParecidaComCorPermitida(List<Color> coresPermitidas, Color corAleatoria){
		boolean isCorParecida = false;
		for (Color corPermitida : coresPermitidas) {
			if(!this.isDistanciaAceitavel(corPermitida, corAleatoria)){
				isCorParecida = true;
				break;
			}
		}
		return isCorParecida;
	}

	/**
	 * Método responsável por gerar cores aleatoriamente no formato RGB seguindo os padrões de qualidade especificados.
	 * @param qtdDeCores Representa a quantidade de cores que deseja-se gerar.
	 * @return List<Color> Representa a lista de cores permitidas em formato RGB.
	 */
	public List<Color> gerarCores(int qtdDeCores){
		List<Color> coresPermitidas = new ArrayList<Color>();		
		List<Color> coresProibidas = new ArrayList<Color>();
		this.carregarCoresProibidasDefault(coresProibidas);
		
		Color corAleatoria = null;
		boolean isCorProibida = false;
		for(int i=0; i<qtdDeCores; i++){
			while(true){
				corAleatoria = this.gerarCorAleatoriamente();
				float[] hsb = Color.RGBtoHSB(corAleatoria.getRed(), corAleatoria.getGreen(), corAleatoria.getBlue(), null);
				float saturation = hsb[1];
				float brightness = hsb[2];
				for(Color corProibida : coresProibidas){
					isCorProibida = corProibida.equals(corAleatoria) 
									|| !this.isBrilhoCorreto(brightness) 
									|| !this.isSaturacaoCorreta(saturation)
									|| !this.isDistanciaAceitavel(corAleatoria, corProibida) 
									|| this.isCorParecidaComCorPermitida(coresPermitidas, corAleatoria);
					if(isCorProibida){
						coresProibidas.add(corProibida);
						break;
					}
				}
				if(isCorProibida){
					isCorProibida = false;
					continue;
				}
				break;
			}
			coresProibidas.clear();
			coresPermitidas.add(corAleatoria);
		}
				
		return coresPermitidas;
	}

	/**
	 * Método responsável por gerar cores aleatoriamente no formato hexadecimal seguindo os padrões de qualidade especificados.
	 * @param qtdDeCores Representa a quantidade de cores que deseja-se gerar.
	 * @return List<String> Representa a lista de cores permitidas em formato hexadecimal.
	 */
	public List<String> gerarCoresHexadecimal(int qtdDeCores){		
		List<Color> coresPermitidas = this.gerarCores(qtdDeCores);		
		return this.gerarCoresHexadecimal(coresPermitidas);
	}

	/**
	 * Método responsável por gerar cores aleatoriamente no formato hexadecimal seguindo os padrões de qualidade especificados.
	 * @param coresPermitidas Representa cores aleatorias já selecionadas previamente.
	 * @return List<String> Representa a lista de cores permitidas em formato hexadecimal.
	 */
	public List<String> gerarCoresHexadecimal(List<Color> coresPermitidas){
		List<String> coresPermitidasHexadecimal = new ArrayList<String>();		
		for(Color corPermitida : coresPermitidas){
			coresPermitidasHexadecimal.add(this.gerarCorHexadecimal(corPermitida));
		}		
		return coresPermitidasHexadecimal;
	}
}