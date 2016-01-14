import java.util.List;
import java.awt.Color;

class Principal{
	public static void main(String[] args) {
		GeradorCoresAleatorias geradorDeCores = new GeradorCoresAleatorias();		

		List<Color> cores = geradorDeCores.gerarCores(100);
		for(Color cor : cores){
			System.out.println(cor);
		}
		List<String> coresHexa = geradorDeCores.gerarCoresHexadecimal(cores);
		for(String cor : coresHexa){
			System.out.println(cor);
		}
	}	
}