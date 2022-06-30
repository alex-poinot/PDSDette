package client.prototype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static client.prototype.ClientPrototype.getSendd;

public class ClientThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ClientThread.class.getName());


    @Override
    public void run() {
        getSendd("demoOneSelectFirst", "read");

    }




}
