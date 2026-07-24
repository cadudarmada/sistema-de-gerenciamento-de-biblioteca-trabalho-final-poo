package biblioteca;

// Bibliotecario é o funcionário responsável por cadastrar livros e leitores.
// Também herda de Pessoa, mas tem um atributo próprio (registro funcional).
public class Bibliotecario extends Pessoa {
    private static final long serialVersionUID = 1L;

    private String registroFuncional;

    public Bibliotecario(String nome, String cpf, String telefone, String registroFuncional) {
        super(nome, cpf, telefone);
        this.registroFuncional = registroFuncional;
    }

    public String getRegistroFuncional() {
        return registroFuncional;
    }

    @Override
    public String getTipo() {
        return "Bibliotecário";
    }

    @Override
    public String toString() {
        return super.toString() + " | Registro: " + registroFuncional;
    }
}
