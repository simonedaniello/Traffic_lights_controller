package main.java.rabbit.controllers;

import main.java.rabbit.entities.Message;
import main.java.rabbit.entities.Semaphore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Author : Simone D'Aniello
 * Date :  19-Feb-18.
 */
public class SemaphoreController {

    private Semaphore semaphore;

    public SemaphoreController(Integer ID){
        this.semaphore = new Semaphore(ID);
    }

    public void addToCrossRoad(CrossroadController c){
        c.addSemaphore(this);
        //addCrossroad(String.valueOf(c.getID()));
    }

    private void start(){
        try {
            semaphore.getReceive().receiveMessage("localhost", semaphore.getCrossroads());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStreet(){
        return semaphore.getStreet();
    }

    public void getState() {
        System.out.println("\n------\nID: " + semaphore.getID() + "\nState: " + semaphore.getState()+ "\nBindings: ");
        for(String c : semaphore.getCrossroads()){
            System.out.println("\t'" + c + "'");
        }
        System.out.println("------\n");

    }

    public void cangeState(Integer state) {
        semaphore.setState(state);
        //do things
    }

    public void addCrossroad(String crossroad) {
        System.out.println("added crossroad : " + crossroad);
        ArrayList<String> temp;
        temp = semaphore.getCrossroads();
        temp.add(crossroad);
        semaphore.setCrossroads(temp);
        semaphore.getReceive().addBindings(crossroad);
    }

    public void removeCrossroad(String crossroad) {
        System.out.println("added crossroad : " + crossroad);
        ArrayList<String> temp;
        temp = semaphore.getCrossroads();
        temp.remove(crossroad);
        semaphore.setCrossroads(temp);

        semaphore.getReceive().removeBinding(crossroad);
    }

    public void sendRequest(String queue, String topic) {
        Message m = new Message(semaphore.getID());
        Send s1 = new Send(semaphore.getID());
        try {
            s1.sendMessage("localhost", m, queue, topic);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public Integer getID() {
        return semaphore.getID();
    }

}
