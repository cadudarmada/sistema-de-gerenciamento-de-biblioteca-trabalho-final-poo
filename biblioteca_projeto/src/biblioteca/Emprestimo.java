package biblioteca;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Classe que representa um empréstimo feito por um Leitor de um Livro.
// Aqui usamos AGREGAÇÃO: o Emprestimo "tem um" Leitor e "tem um" Livro,
// mas o Leitor e o Livro continuam existindo mesmo se o empréstimo for apagado.
public class Emprestimo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Leitor leitor;
    private Livro livro;
    private Bibliotecario bibliotecarioResponsavel; // quem atendeu esse empréstimo
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucaoReal; // fica null enquanto o livro não é devolvido
    private boolean devolvido;

    public Emprestimo(Leitor leitor, Livro livro, Bibliotecario bibliotecarioResponsavel) {
        this.leitor = leitor;
        this.livro = livro;
        this.bibliotecarioResponsavel = bibliotecarioResponsavel;
        this.dataEmprestimo = LocalDate.now();
        // o prazo depende do tipo do livro (físico ou digital) - isso é polimorfismo,
        // pois calcularPrazoEmprestimo() é chamado sem saber qual subclasse é
        this.dataPrevistaDevolucao = dataEmprestimo.plusDays(livro.calcularPrazoEmprestimo());
        this.devolvido = false;
    }

    public Leitor getLeitor() {
        return leitor;
    }

    public Livro getLivro() {
        return livro;
    }

    public Bibliotecario getBibliotecarioResponsavel() {
        return bibliotecarioResponsavel;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    // marca o empréstimo como devolvido e calcula se houve atraso
    public double registrarDevolucao() {
        this.dataDevolucaoReal = LocalDate.now();
        this.devolvido = true;
        livro.devolverExemplar();

        long diasAtraso = ChronoUnit.DAYS.between(dataPrevistaDevolucao, dataDevolucaoReal);
        if (diasAtraso > 0) {
            return diasAtraso * livro.calcularMultaPorDia();
        }
        return 0.0;
    }

    @Override
    public String toString() {
        String status = devolvido ? "Devolvido" : "Em aberto";
        return "Livro: " + livro.getTitulo() + " | Leitor: " + leitor.getNome()
                + " | Atendido por: " + bibliotecarioResponsavel.getNome()
                + " | Emprestado em: " + dataEmprestimo
                + " | Devolução prevista: " + dataPrevistaDevolucao
                + " | Status: " + status;
    }
}
