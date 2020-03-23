import java.util.stream.Stream;

public enum Service {
    PAGAR_BOLETO('B'),
    PAGAR_IMPOSTO('I'),
    TRANSFERIR_DINHEIRO('T'),
    REALIZAR_EMPRESTIMO('E');

    private char key;

    Service(char key) {
        this.key = key;
    }

    public char getKey() {
        return key;
    }

    public static Stream<Service> stream() {
        return Stream.of(Service.values());
    }
}
