package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class ClientConfiguration {

    private final String configvariable = "CONF";
    private final String locationsystem;
    private ClientProperties configuration;

    public ClientConfiguration() {
        // to get the value of environment variable
        locationsystem = System.getenv(configvariable);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            configuration = mapper.readValue(new File(locationsystem),ClientProperties.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientProperties getConfiguration() {
        return configuration;
    }
}
