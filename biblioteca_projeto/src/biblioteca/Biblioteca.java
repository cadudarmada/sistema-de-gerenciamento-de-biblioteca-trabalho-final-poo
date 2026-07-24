package biblioteca;

import java.io.*;
import java.util.ArrayList;

// Classe principal do sistema. Ela é COMPOSTA pelas listas de livros, leitores
// e empréstimos: se a Biblioteca "morrer", essas listas somem junto (composição).
public class Biblioteca implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Livro> livros;
    private ArrayList<Leitor> leitores;
    private ArrayList<Bibliotecario> bibliotecarios;
    private ArrayList<Emprestimo> emprestimos;

    // nome do arquivo onde os dados vão ser salvos (persistência)
    private static final String ARQUIVO_DADOS = "biblioteca.dat";

    public Biblioteca() {
        livros = new ArrayList<>();
        leitores = new ArrayList<>();
        bibliotecarios = new ArrayList<>();
        emprestimos = new ArrayList<>();
    }

    // ---------- CADASTROS ----------

    public void cadastrarLivro(Livro livro) {
        livros.add(livro);
    }

    public void cadastrarLeitor(Leitor leitor) {
        leitores.add(leitor);
    }

    public void cadastrarBibliotecario(Bibliotecario bibliotecario) {
        bibliotecarios.add(bibliotecario);
    }

    // ---------- REGRAS DE EMPRÉSTIMO ----------

    // conta quantos empréstimos em aberto (não devolvidos) um leitor tem
    private int contarEmprestimosAbertos(Leitor leitor) {
        int contador = 0;
        for (Emprestimo e : emprestimos) {
            if (e.getLeitor().equals(leitor) && !e.isDevolvido()) {
                contador++;
            }
        }
        return contador;
    }

    // realiza um empréstimo, lançando exceções customizadas quando não for possível
    public Emprestimo realizarEmprestimo(Leitor leitor, Livro livro, Bibliotecario bibliotecarioResponsavel)
            throws LivroIndisponivelException, LimiteEmprestimosException {

        if (!livro.isDisponivel()) {
            throw new LivroIndisponivelException("O livro \"" + livro.getTitulo() + "\" não tem exemplares disponíveis.");
        }

        if (contarEmprestimosAbertos(leitor) >= leitor.getLimiteEmprestimos()) {
            throw new LimiteEmprestimosException("O leitor " + leitor.getNome()
                    + " já atingiu o limite de " + leitor.getLimiteEmprestimos() + " empréstimos.");
        }

        livro.retirarExemplar();
        Emprestimo novoEmprestimo = new Emprestimo(leitor, livro, bibliotecarioResponsavel);
        emprestimos.add(novoEmprestimo);
        return novoEmprestimo;
    }

    // devolve o livro de um empréstimo específico e retorna o valor da multa (se houver)
    public double devolverLivro(Emprestimo emprestimo) {
        return emprestimo.registrarDevolucao();
    }

    // ---------- LISTAGENS (getters) ----------

    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public ArrayList<Leitor> getLeitores() {
        return leitores;
    }

    public ArrayList<Bibliotecario> getBibliotecarios() {
        return bibliotecarios;
    }

    public ArrayList<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    // retorna só os empréstimos que ainda não foram devolvidos
    public ArrayList<Emprestimo> getEmprestimosEmAberto() {
        ArrayList<Emprestimo> abertos = new ArrayList<>();
        for (Emprestimo e : emprestimos) {
            if (!e.isDevolvido()) {
                abertos.add(e);
            }
        }
        return abertos;
    }

    // ---------- PERSISTÊNCIA (salvar e carregar os dados em arquivo) ----------

    // salva todo o objeto Biblioteca (com as 4 listas dentro) em um arquivo .dat
    public void salvarDados() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    // carrega os dados salvos anteriormente. Se não existir arquivo, cria uma biblioteca nova.
    public static Biblioteca carregarDados() {
        File arquivo = new File(ARQUIVO_DADOS);
        if (!arquivo.exists()) {
            return new Biblioteca();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (Biblioteca) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar os dados, iniciando biblioteca vazia: " + e.getMessage());
            return new Biblioteca();
        }
    }
}
