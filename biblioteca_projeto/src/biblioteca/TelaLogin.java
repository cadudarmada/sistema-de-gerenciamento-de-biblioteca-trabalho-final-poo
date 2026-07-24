package biblioteca;

import javax.swing.*;
import java.awt.*;

// Tela de login do bibliotecário. Isso faz a classe Bibliotecario ser
// realmente usada no sistema: antes de mexer nas telas, o funcionário
// precisa se identificar com o registro funcional dele.
public class TelaLogin extends JDialog {

    private Biblioteca biblioteca;
    private Bibliotecario bibliotecarioLogado; // fica null se o usuário cancelar

    private CardLayout cardLayout;
    private JPanel painelCentral;

    // campos da tela de login
    private JTextField campoRegistroLogin;

    // campos da tela de cadastro (usada só na primeira vez que o registro aparece)
    private JTextField campoNomeCadastro;
    private JTextField campoCpfCadastro;
    private JTextField campoTelefoneCadastro;
    private String registroDigitado;

    public TelaLogin(Biblioteca biblioteca) {
        super((Frame) null, "Login do Bibliotecário", true); // true = modal, trava até fechar
        this.biblioteca = biblioteca;

        setSize(380, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        cardLayout = new CardLayout();
        painelCentral = new JPanel(cardLayout);
        painelCentral.add(criarPainelLogin(), "login");
        painelCentral.add(criarPainelCadastro(), "cadastro");

        add(painelCentral);
        cardLayout.show(painelCentral, "login");
    }

    // ---------- PAINEL DE LOGIN ----------
    private JPanel criarPainelLogin() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Identifique-se para acessar o sistema", SwingConstants.CENTER);
        painel.add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 10));
        campoRegistroLogin = new JTextField();
        JButton botaoEntrar = new JButton("Entrar");

        form.add(new JLabel("Registro funcional:"));
        form.add(campoRegistroLogin);
        form.add(new JLabel());
        form.add(botaoEntrar);
        painel.add(form, BorderLayout.CENTER);

        JLabel dica = new JLabel("<html><i>Se o registro ainda não existir, você poderá se cadastrar.</i></html>");
        painel.add(dica, BorderLayout.SOUTH);

        botaoEntrar.addActionListener(e -> {
            String registro = campoRegistroLogin.getText().trim();
            if (registro.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite o registro funcional.");
                return;
            }

            // procura se já existe um bibliotecário com esse registro
            Bibliotecario encontrado = null;
            for (Bibliotecario b : biblioteca.getBibliotecarios()) {
                if (b.getRegistroFuncional().equalsIgnoreCase(registro)) {
                    encontrado = b;
                    break;
                }
            }

            if (encontrado != null) {
                bibliotecarioLogado = encontrado;
                dispose(); // fecha a tela de login e deixa o programa seguir
            } else {
                // registro novo: manda pra tela de cadastro rápido
                registroDigitado = registro;
                cardLayout.show(painelCentral, "cadastro");
            }
        });

        return painel;
    }

    // ---------- PAINEL DE CADASTRO (só aparece na primeira vez de um registro novo) ----------
    private JPanel criarPainelCadastro() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Registro novo, complete seu cadastro", SwingConstants.CENTER);
        painel.add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 8));
        campoNomeCadastro = new JTextField();
        campoCpfCadastro = new JTextField();
        campoTelefoneCadastro = new JTextField();
        JButton botaoConcluir = new JButton("Concluir Cadastro");

        form.add(new JLabel("Nome:"));
        form.add(campoNomeCadastro);
        form.add(new JLabel("CPF:"));
        form.add(campoCpfCadastro);
        form.add(new JLabel("Telefone:"));
        form.add(campoTelefoneCadastro);
        form.add(new JLabel());
        form.add(botaoConcluir);

        painel.add(form, BorderLayout.CENTER);

        botaoConcluir.addActionListener(e -> {
            String nome = campoNomeCadastro.getText().trim();
            String cpf = campoCpfCadastro.getText().trim();
            String telefone = campoTelefoneCadastro.getText().trim();

            if (nome.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha ao menos nome e CPF.");
                return;
            }

            Bibliotecario novo = new Bibliotecario(nome, cpf, telefone, registroDigitado);
            biblioteca.cadastrarBibliotecario(novo);
            bibliotecarioLogado = novo;
            dispose();
        });

        return painel;
    }

    // usado pelo Main.java pra saber quem entrou no sistema
    public Bibliotecario getBibliotecarioLogado() {
        return bibliotecarioLogado;
    }
}
