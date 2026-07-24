package biblioteca;

import java.io.Serializable;

// Classe que representa um livro físico do acervo da biblioteca.
// LivroDigital vai herdar dela e mudar o comportamento de alguns métodos (polimorfismo).
public class Livro implements Serializable {
    private static final long serialVersionUID = 1L;

    private String titulo;
    private String autor;
    private String isbn;
    private int quantidadeExemplares; // quantos exemplares existem no total
    private int exemplaresDisponiveis; // quantos estão disponíveis pra empréstimo agora

    public Livro(String titulo, String autor, String isbn, int quantidadeExemplares) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.quantidadeExemplares = quantidadeExemplares;
        this.exemplaresDisponiveis = quantidadeExemplares;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getExemplaresDisponiveis() {
        return exemplaresDisponiveis;
    }

    public boolean isDisponivel() {
        return exemplaresDisponiveis > 0;
    }

    // baixa 1 exemplar disponível quando alguém pega o livro emprestado
    public void retirarExemplar() {
        exemplaresDisponiveis--;
    }

    // devolve 1 exemplar disponível quando o livro é devolvido
    public void devolverExemplar() {
        if (exemplaresDisponiveis < quantidadeExemplares) {
            exemplaresDisponiveis++;
        }
    }

    // prazo padrão de empréstimo de um livro físico: 14 dias
    // esse método vai ser sobrescrito em LivroDigital (polimorfismo)
    public int calcularPrazoEmprestimo() {
        return 14;
    }

    // valor da multa por dia de atraso na devolução (livro físico)
    public double calcularMultaPorDia() {
        return 1.0; // R$ 1,00 por dia de atraso
    }

    // método que descreve o tipo do livro, usado na tela e nos relatórios
    public String getTipoLivro() {
        return "Físico";
    }

    @Override
    public String toString() {
        return "[" + getTipoLivro() + "] " + titulo + " - " + autor + " (ISBN: " + isbn
                + ") | Disponíveis: " + exemplaresDisponiveis + "/" + quantidadeExemplares
                + " | Prazo: " + calcularPrazoEmprestimo() + " dias";
    }
}
