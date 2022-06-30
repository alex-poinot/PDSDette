package episen.backend.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class ServerConfiguration {

    private final String configvariable = "CONF";
    private final String locationsystem;
    private ServerProperties configuration;
    private int n;

    public ServerConfiguration(int n) {
        locationsystem = System.getenv(configvariable);
        this.n = n;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try{
            configuration = mapper.readValue(new File(locationsystem), ServerProperties.class);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public ServerProperties getConfiguration() {
        return configuration; }

        public int getN() {
        return n;
        }
}
