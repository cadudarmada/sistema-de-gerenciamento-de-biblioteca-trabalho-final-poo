package biblioteca;

import java.io.Serializable;

// Classe abstrata que representa uma pessoa genérica do sistema.
// Leitor e Bibliotecario vão herdar dessa classe (Herança).
public abstract class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String nome;
    protected String cpf;
    protected String telefone;

    public Pessoa(String nome, String cpf, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    // Método abstrato que obriga cada subclasse a dizer qual é o seu tipo.
    // Isso é usado para mostrar polimorfismo (cada classe filha responde diferente).
    public abstract String getTipo();

    @Override
    public String toString() {
        return getTipo() + " - Nome: " + nome + " | CPF: " + cpf + " | Telefone: " + telefone;
    }
}
