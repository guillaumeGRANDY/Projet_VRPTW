package org.polytech.parser;

import org.polytech.model.Depot;
import org.polytech.model.Client;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class LocationParser {
    private final ArrayList<Client> clients = new ArrayList<>();
    private Depot depot;

    private int maxQuantity;

    public ArrayList<Client> getClients() {
        return clients;
    }

    public Depot getDepot() {
        return depot;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public LocationParser() {
    }

    public void parseFile(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            File file = null;
            try {
                file = new File(resource.toURI());
                parseFile(file);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void parseFile(File file) throws IOException {

        String line = "";
        Scanner scanner = new Scanner(file);

        for (int i = 0; i < 7; i++) {  //skip les lignes inutiles du début
            line = scanner.nextLine();
            System.out.println(line);
        }

        String[] entries = line.split(" ");
        this.maxQuantity= Integer.parseInt(entries[1]);

        for (int i = 0; i < 3; i++) {  //skip les lignes inutiles du début
            line = scanner.nextLine();
            System.out.println(line);
        }

        entries = line.split(" "); // assert that Data_Depot has 5 entries
        this.depot = new Depot(
                entries[0],
                Integer.parseInt(entries[1]),
                Integer.parseInt(entries[2]),
                Integer.parseInt(entries[3]),
                Integer.parseInt(entries[4])
        );

        for (int i = 0; i < 3; i++) {  //skip les lignes inutiles au millieu
            line = scanner.nextLine();
        }

        while (scanner.hasNext()) {
            entries = line.split(" "); // assert that Data_Location has 7 entries

            this.clients.add(new Client(
                    entries[0],
                    Integer.parseInt(entries[1]),
                    Integer.parseInt(entries[2]),
                    0,
                    200,
                    //Integer.parseInt(entries[3]),
                    //Integer.parseInt(entries[4]),
                    Integer.parseInt(entries[5]),
                    0
                    //Integer.parseInt(entries[6])
            ));
            line = scanner.nextLine();
        }
    }

    public void showwDatas() {
        System.out.println(this.depot.toString());

        for (int i = 0; i < this.clients.size(); i++) {
            System.out.println(this.clients.get(i).toString());
        }
    }
}
