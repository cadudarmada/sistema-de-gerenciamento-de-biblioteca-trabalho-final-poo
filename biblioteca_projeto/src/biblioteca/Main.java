package biblioteca;

import javax.swing.SwingUtilities;

// Classe principal, é ela que a gente roda para iniciar o sistema.
public class Main {
    public static void main(String[] args) {
        // carrega os dados salvos anteriormente (se existir o arquivo biblioteca.dat)
        Biblioteca biblioteca = Biblioteca.carregarDados();

        // SwingUtilities.invokeLater é usado pra garantir que a interface
        // gráfica seja criada de forma segura (isso o professor comentou em aula)
        SwingUtilities.invokeLater(() -> {
            // antes de tudo, o bibliotecário precisa se identificar no sistema
            TelaLogin telaLogin = new TelaLogin(biblioteca);
            telaLogin.setVisible(true); // como é modal, o código para aqui até a tela fechar

            Bibliotecario bibliotecarioLogado = telaLogin.getBibliotecarioLogado();
            if (bibliotecarioLogado == null) {
                // usuário fechou a janela sem entrar, então encerra o programa
                System.exit(0);
            }

            TelaPrincipal tela = new TelaPrincipal(biblioteca, bibliotecarioLogado);
            tela.setVisible(true);
        });
    }
}
