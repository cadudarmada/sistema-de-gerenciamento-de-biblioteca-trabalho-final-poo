package biblioteca;

// Exceção customizada lançada quando o leitor já atingiu o número
// máximo de livros emprestados ao mesmo tempo (definido em Leitor).
public class LimiteEmprestimosException extends Exception {
    private static final long serialVersionUID = 1L;

    public LimiteEmprestimosException(String mensagem) {
        super(mensagem);
    }
}
