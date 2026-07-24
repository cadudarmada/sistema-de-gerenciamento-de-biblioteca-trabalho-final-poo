package biblioteca;

// Leitor é o aluno/usuário que pega livros emprestados.
// Ele herda nome, cpf e telefone de Pessoa e adiciona um limite de empréstimos.
public class Leitor extends Pessoa {
    private static final long serialVersionUID = 1L;

    // número de matrícula do leitor (identificador dele na biblioteca)
    private String matricula;

    // quantidade máxima de livros que ele pode pegar emprestado ao mesmo tempo
    private int limiteEmprestimos;

    public Leitor(String nome, String cpf, String telefone, String matricula) {
        super(nome, cpf, telefone); // chama o construtor da classe Pessoa
        this.matricula = matricula;
        this.limiteEmprestimos = 3; // regra da biblioteca: máximo 3 livros por leitor
    }

    public String getMatricula() {
        return matricula;
    }

    public int getLimiteEmprestimos() {
        return limiteEmprestimos;
    }

    // sobrescreve o método abstrato da classe mãe (polimorfismo)
    @Override
    public String getTipo() {
        return "Leitor";
    }

    @Override
    public String toString() {
        return super.toString() + " | Matrícula: " + matricula;
    }
}
