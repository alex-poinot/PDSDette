package client;

public class ClientProperties {

    private String adressIP;
    private int port;

    public ClientProperties() {
    }

    public String getAdressIP() {
        return adressIP;
    }

    public void setAdresseIP(String adressIP) {
        this.adressIP = adressIP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
