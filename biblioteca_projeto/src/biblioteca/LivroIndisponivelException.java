package biblioteca;

// Exceção customizada lançada quando o leitor tenta pegar emprestado
// um livro que não tem exemplares disponíveis no momento.
public class LivroIndisponivelException extends Exception {
    private static final long serialVersionUID = 1L;

    public LivroIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
