package biblioteca;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// Tela principal do sistema, feita com javax.swing.
// Tem abas separadas pra cada funcionalidade, pra ficar mais organizado.
public class TelaPrincipal extends JFrame {

    private Biblioteca biblioteca;
    private Bibliotecario bibliotecarioLogado;

    // combo boxes usados em mais de uma aba, por isso são atributos da classe
    private JComboBox<Livro> comboLivrosEmprestimo;
    private JComboBox<Leitor> comboLeitoresEmprestimo;
    private JComboBox<Emprestimo> comboEmprestimosDevolucao;
    private JTextArea areaListagem;

    public TelaPrincipal(Biblioteca biblioteca, Bibliotecario bibliotecarioLogado) {
        this.biblioteca = biblioteca;
        this.bibliotecarioLogado = bibliotecarioLogado;

        setTitle("Sistema de Gerenciamento de Biblioteca - Bibliotecário: " + bibliotecarioLogado.getNome());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // vamos tratar o fechamento pra salvar os dados
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Cadastrar Livro", criarAbaCadastroLivro());
        abas.addTab("Cadastrar Leitor", criarAbaCadastroLeitor());
        abas.addTab("Realizar Empréstimo", criarAbaEmprestimo());
        abas.addTab("Devolver Livro", criarAbaDevolucao());
        abas.addTab("Listagem Geral", criarAbaListagem());

        add(abas);

        // ao fechar a janela, salva os dados no arquivo antes de encerrar o programa
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                biblioteca.salvarDados();
                dispose();
                System.exit(0);
            }
        });
    }

    // ---------- ABA: CADASTRAR LIVRO ----------
    private JPanel criarAbaCadastroLivro() {
        JPanel painel = new JPanel(new GridLayout(7, 2, 5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField campoTitulo = new JTextField();
        JTextField campoAutor = new JTextField();
        JTextField campoIsbn = new JTextField();
        JTextField campoQuantidade = new JTextField();
        JCheckBox checkDigital = new JCheckBox("É um livro digital (e-book)?");
        JTextField campoLink = new JTextField();
        campoLink.setEnabled(false);

        checkDigital.addActionListener(e -> campoLink.setEnabled(checkDigital.isSelected()));

        JButton botaoCadastrar = new JButton("Cadastrar Livro");

        painel.add(new JLabel("Título:"));
        painel.add(campoTitulo);
        painel.add(new JLabel("Autor:"));
        painel.add(campoAutor);
        painel.add(new JLabel("ISBN:"));
        painel.add(campoIsbn);
        painel.add(new JLabel("Quantidade de exemplares:"));
        painel.add(campoQuantidade);
        painel.add(checkDigital);
        painel.add(new JLabel("Link de acesso (se digital):"));
        painel.add(campoLink);
        painel.add(new JLabel());
        painel.add(botaoCadastrar);

        botaoCadastrar.addActionListener(e -> {
            try {
                String titulo = campoTitulo.getText().trim();
                String autor = campoAutor.getText().trim();
                String isbn = campoIsbn.getText().trim();
                int quantidade = Integer.parseInt(campoQuantidade.getText().trim());

                if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                    return;
                }

                Livro novoLivro;
                if (checkDigital.isSelected()) {
                    novoLivro = new LivroDigital(titulo, autor, isbn, quantidade, campoLink.getText().trim());
                } else {
                    novoLivro = new Livro(titulo, autor, isbn, quantidade);
                }

                biblioteca.cadastrarLivro(novoLivro);
                atualizarComboLivros();
                JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");

                campoTitulo.setText("");
                campoAutor.setText("");
                campoIsbn.setText("");
                campoQuantidade.setText("");
                campoLink.setText("");
                checkDigital.setSelected(false);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "A quantidade de exemplares precisa ser um número inteiro.");
            }
        });

        return painel;
    }

    // ---------- ABA: CADASTRAR LEITOR ----------
    private JPanel criarAbaCadastroLeitor() {
        JPanel painel = new JPanel(new GridLayout(5, 2, 5, 5));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField campoNome = new JTextField();
        JTextField campoCpf = new JTextField();
        JTextField campoTelefone = new JTextField();
        JTextField campoMatricula = new JTextField();
        JButton botaoCadastrar = new JButton("Cadastrar Leitor");

        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(new JLabel("CPF:"));
        painel.add(campoCpf);
        painel.add(new JLabel("Telefone:"));
        painel.add(campoTelefone);
        painel.add(new JLabel("Matrícula:"));
        painel.add(campoMatricula);
        painel.add(new JLabel());
        painel.add(botaoCadastrar);

        botaoCadastrar.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            String cpf = campoCpf.getText().trim();
            String telefone = campoTelefone.getText().trim();
            String matricula = campoMatricula.getText().trim();

            if (nome.isEmpty() || cpf.isEmpty() || matricula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha ao menos nome, CPF e matrícula.");
                return;
            }

            Leitor novoLeitor = new Leitor(nome, cpf, telefone, matricula);
            biblioteca.cadastrarLeitor(novoLeitor);
            atualizarComboLeitores();
            JOptionPane.showMessageDialog(this, "Leitor cadastrado com sucesso!");

            campoNome.setText("");
            campoCpf.setText("");
            campoTelefone.setText("");
            campoMatricula.setText("");
        });

        return painel;
    }

    // ---------- ABA: REALIZAR EMPRÉSTIMO ----------
    private JPanel criarAbaEmprestimo() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel painelForm = new JPanel(new GridLayout(3, 2, 5, 5));
        comboLeitoresEmprestimo = new JComboBox<>();
        comboLivrosEmprestimo = new JComboBox<>();
        JButton botaoEmprestar = new JButton("Emprestar");

        painelForm.add(new JLabel("Leitor:"));
        painelForm.add(comboLeitoresEmprestimo);
        painelForm.add(new JLabel("Livro:"));
        painelForm.add(comboLivrosEmprestimo);
        painelForm.add(new JLabel());
        painelForm.add(botaoEmprestar);

        painel.add(painelForm, BorderLayout.NORTH);

        botaoEmprestar.addActionListener(e -> {
            Leitor leitorSelecionado = (Leitor) comboLeitoresEmprestimo.getSelectedItem();
            Livro livroSelecionado = (Livro) comboLivrosEmprestimo.getSelectedItem();

            if (leitorSelecionado == null || livroSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Cadastre um leitor e um livro antes de emprestar.");
                return;
            }

            // aqui usamos try/catch pra tratar as exceções customizadas do projeto
            try {
                Emprestimo emprestimo = biblioteca.realizarEmprestimo(leitorSelecionado, livroSelecionado, bibliotecarioLogado);
                atualizarComboLivros();
                atualizarComboEmprestimos();
                JOptionPane.showMessageDialog(this, "Empréstimo realizado!\nDevolução prevista: "
                        + emprestimo.getDataPrevistaDevolucao());
            } catch (LivroIndisponivelException | LimiteEmprestimosException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Não foi possível emprestar", JOptionPane.WARNING_MESSAGE);
            }
        });

        return painel;
    }

    // ---------- ABA: DEVOLVER LIVRO ----------
    private JPanel criarAbaDevolucao() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel painelForm = new JPanel(new GridLayout(2, 2, 5, 5));
        comboEmprestimosDevolucao = new JComboBox<>();
        JButton botaoDevolver = new JButton("Registrar Devolução");

        painelForm.add(new JLabel("Empréstimo em aberto:"));
        painelForm.add(comboEmprestimosDevolucao);
        painelForm.add(new JLabel());
        painelForm.add(botaoDevolver);

        painel.add(painelForm, BorderLayout.NORTH);

        botaoDevolver.addActionListener(e -> {
            Emprestimo emprestimoSelecionado = (Emprestimo) comboEmprestimosDevolucao.getSelectedItem();
            if (emprestimoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Não há empréstimos em aberto.");
                return;
            }

            double multa = biblioteca.devolverLivro(emprestimoSelecionado);
            atualizarComboLivros();
            atualizarComboEmprestimos();

            if (multa > 0) {
                JOptionPane.showMessageDialog(this, "Devolução registrada com atraso!\nMulta a pagar: R$ "
                        + String.format("%.2f", multa));
            } else {
                JOptionPane.showMessageDialog(this, "Devolução registrada dentro do prazo, sem multa.");
            }
        });

        return painel;
    }

    // ---------- ABA: LISTAGEM GERAL ----------
    private JPanel criarAbaListagem() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        areaListagem = new JTextArea();
        areaListagem.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaListagem);

        JButton botaoAtualizar = new JButton("Atualizar Listagem");
        botaoAtualizar.addActionListener(e -> atualizarListagem());

        painel.add(scroll, BorderLayout.CENTER);
        painel.add(botaoAtualizar, BorderLayout.SOUTH);

        return painel;
    }

    private void atualizarListagem() {
        StringBuilder texto = new StringBuilder();

        texto.append("=== LIVROS CADASTRADOS ===\n");
        for (Livro l : biblioteca.getLivros()) {
            texto.append(l.toString()).append("\n");
        }

        texto.append("\n=== LEITORES CADASTRADOS ===\n");
        for (Leitor leit : biblioteca.getLeitores()) {
            texto.append(leit.toString()).append("\n");
        }

        texto.append("\n=== BIBLIOTECÁRIOS CADASTRADOS ===\n");
        for (Bibliotecario bib : biblioteca.getBibliotecarios()) {
            texto.append(bib.toString()).append("\n");
        }

        texto.append("\n=== EMPRÉSTIMOS ===\n");
        for (Emprestimo emp : biblioteca.getEmprestimos()) {
            texto.append(emp.toString()).append("\n");
        }

        areaListagem.setText(texto.toString());
    }

    // ---------- MÉTODOS AUXILIARES PRA ATUALIZAR OS COMBOBOX ----------

    private void atualizarComboLivros() {
        comboLivrosEmprestimo.removeAllItems();
        for (Livro l : biblioteca.getLivros()) {
            if (l.isDisponivel()) {
                comboLivrosEmprestimo.addItem(l);
            }
        }
    }

    private void atualizarComboLeitores() {
        comboLeitoresEmprestimo.removeAllItems();
        for (Leitor l : biblioteca.getLeitores()) {
            comboLeitoresEmprestimo.addItem(l);
        }
    }

    private void atualizarComboEmprestimos() {
        comboEmprestimosDevolucao.removeAllItems();
        ArrayList<Emprestimo> abertos = biblioteca.getEmprestimosEmAberto();
        for (Emprestimo e : abertos) {
            comboEmprestimosDevolucao.addItem(e);
        }
    }
}
