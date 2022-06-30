package client;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import episen.backend.server.ClientJsonProperties;

import java.io.File;
import java.io.IOException;

public class ClientConfigurationJson {

    private final String configvariable = "JSONCONF";
    private final String locationsystem;
    private ClientJsonProperties configuration;

    public ClientConfigurationJson() {
        // to get the value of environment variable
        locationsystem = System.getenv(configvariable); // récupérer la valeur de la variable d'environnement
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());

        try {
            configuration = mapper.readValue(new File(locationsystem), ClientJsonProperties.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientJsonProperties getConfiguration() {
        return configuration;
    }
}
