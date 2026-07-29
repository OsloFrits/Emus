import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonSave {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void salvar(Object objeto, String nomeArquivo) {
        try {
            Path pasta = Path.of("historico");
            Files.createDirectories(pasta);

            Path arquivo = pasta.resolve(nomeArquivo + ".json");

            mapper.writeValue(arquivo.toFile(), objeto);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
