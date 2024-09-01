import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Perceptron {
    private double[] pesos;
    private double taxaAprendizado;
    
    public Perceptron(int numEntradas, double taxaAprendizado) {
        this.pesos = new double[numEntradas + 1]; // +1 para o bias
        this.taxaAprendizado = taxaAprendizado;
        inicializarPesos();
    }

    private void inicializarPesos() {
        Random random = new Random();
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = random.nextDouble() - 0.5; // Pesos iniciais aleatórios entre -0.5 e 0.5
        }
    }

    public int prever(double[] entradas) {
        double soma = pesos[0]; // Bias
        for (int i = 0; i < entradas.length; i++) {
            soma += entradas[i] * pesos[i + 1];
        }
        return ativacao(soma);
    }

    private int ativacao(double soma) {
        return soma >= 0 ? 1 : 0;
    }

    public void treinar(double[][] dataset, int[] rotulos, int epocas) {
        for (int epoca = 0; epoca < epocas; epoca++) {
            for (int i = 0; i < dataset.length; i++) {
                int previsao = prever(dataset[i]);
                int erro = rotulos[i] - previsao;
                pesos[0] += taxaAprendizado * erro; // Atualização do bias
                for (int j = 0; j < dataset[i].length; j++) {
                    pesos[j + 1] += taxaAprendizado * erro * dataset[i][j];
                }
            }
        }
    }
    
    public double calcularAcuracia(double[][] dataset, int[] rotulos) {
        int total = dataset.length;
        int corretos = 0;

        for (int i = 0; i < total; i++) {
            int previsao = prever(dataset[i]);
            if (previsao == rotulos[i]) {
                corretos++;
            }
        }

        return (double) corretos / total;
    }
}

class TestePerceptron {
    public static void main(String[] args) {
        // Exemplo de uso
        
     //   double[][] entradas = {
       //         {0, 0},
         //       {0, 1},
           //     {1, 0},
             //   {1, 1}
            //};
        
    	
        String caminhoArquivo = "C:\\Users\\Jéssica Soler\\Downloads//Teste.txt"; // Nome do arquivo de dados
        List<double[]> entradas = new ArrayList<>();
        List<Integer> rotulos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(" "); 
                double[] entrada = new double[partes.length - 1];
                for (int i = 0; i < partes.length - 1; i++) {
                    entrada[i] = Double.parseDouble(partes[i]);
                }
                int rotulo = Integer.parseInt(partes[partes.length - 1]);
                entradas.add(entrada);
                rotulos.add(rotulo);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            return;
        }
    	
    	
        // Converter List para array
        double[][] entradasArray = entradas.toArray(new double[0][0]);
        int[] rotulosArray = rotulos.stream().mapToInt(Integer::intValue).toArray();

        // Inicializar e treinar o perceptron
        Perceptron perceptron = new Perceptron(entradasArray[0].length, 0.1);
        perceptron.treinar(entradasArray, rotulosArray, 1000);

        // Calcular acurácia
        double acuracia = perceptron.calcularAcuracia(entradasArray, rotulosArray);
        System.out.println("Acurácia: " + acuracia * 100 + "%");

        // previsões
        for (double[] entrada : entradasArray) {
            int previsao = perceptron.prever(entrada);
            System.out.println("Entrada: " + entrada[0] + ", " + entrada[1] + " -> Saída: " + previsao);
        }
    }
}
