package biblioteca;

// Livro digital (e-book) da biblioteca. Herda de Livro, mas muda algumas regras:
// - prazo de empréstimo mais curto (é mais rápido de ler / não estraga)
// - como não é um objeto físico, não tem multa por atraso, só bloqueio de renovação
// Isso mostra o polimorfismo pedido no projeto (mesma classe pai, comportamentos diferentes).
public class LivroDigital extends Livro {
    private static final long serialVersionUID = 1L;

    private String linkAcesso; // link ou código de acesso ao e-book

    public LivroDigital(String titulo, String autor, String isbn, int quantidadeExemplares, String linkAcesso) {
        super(titulo, autor, isbn, quantidadeExemplares);
        this.linkAcesso = linkAcesso;
    }

    public String getLinkAcesso() {
        return linkAcesso;
    }

    // sobrescreve o prazo: livro digital só pode ficar emprestado por 7 dias
    @Override
    public int calcularPrazoEmprestimo() {
        return 7;
    }

    // sobrescreve a multa: não tem multa em dinheiro para livro digital
    @Override
    public double calcularMultaPorDia() {
        return 0.0;
    }

    @Override
    public String getTipoLivro() {
        return "Digital";
    }
}
