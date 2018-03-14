package all.controllers;

import all.front.FirstConsumer;
import all.front.FirstProducer;
import main.java.Crossroad;
import main.java.Message;
import main.java.Semaphore;
import main.java.system.Printer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author : Simone D'Aniello
 * Date :  21-Feb-18.
 */
public class CrossroadController implements Runnable{

    private Crossroad crossroad;

    public CrossroadController(String ID, String address){
        this.crossroad = new Crossroad(ID, address);
//        s = new Sender(crossroad.getID());
//        Receiver r = new Receiver(crossroad.getID(), "traffic", this);
        FirstConsumer.getInstance().setController(this);
        Monitorer.getInstance().setCrossroad(crossroad);
        try {
            FirstConsumer.getInstance().subscribeToTopic("monitor");
            FirstConsumer.getInstance().subscribeToTopic(crossroad.getID());
            (new Thread(this)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Timer timer = new Timer();
        timer.schedule(new TimerClass(), 10000, 30000); // every 30 seconds
        new Decider(this);
    }

    public ArrayList<Semaphore> getSemaphoreInCrossroad(){
        return crossroad.getSemaphores();
    }

    public void addSemaphore(Semaphore semaphore){
        crossroad.getSemaphores().add(semaphore);
        sendCurrentState();
        Monitorer.getInstance().setNumberOfSemaphores(crossroad.getSemaphores().size());
        printSemaphores();
    }

    public void removeSemaphore(Semaphore semaphore){
        for(Semaphore s : crossroad.getSemaphores()){
            if(s.getID().equals(semaphore.getID())){
                System.out.println("removing semaphore " + s.getID());
                crossroad.getSemaphores().remove(s);
                Monitorer.getInstance().setNumberOfSemaphores(crossroad.getSemaphores().size());
                break;
            }
        }
        printSemaphores();
    }

    private void printSemaphores(){
        Printer.getInstance().print("semaphores binded: ", "green");
        for(Semaphore s : crossroad.getSemaphores())
            Printer.getInstance().print(s.getID(), "green");
    }

    private void sendCurrentState(){
        Message m = new Message(crossroad.getID(), 10);
        m.setListOfSemaphores(crossroad.getSemaphores());
        m.setCurrentCycle(Monitorer.getInstance().getCurrentCycle());
        FirstProducer.getInstance().sendMessage("address", m, crossroad.getID());
    }

    public void sendGreen(Semaphore greenSemaphore) {

    }

    @Override
    public void run() {
        FirstConsumer.getInstance().runConsumer();
    }

    private class TimerClass extends TimerTask {
        @Override
        public void run() {
            sendCurrentState();
        }
    }
}


