/*
 * Main.java 14/08/2017
 *
 * Created by Bondarenko Oleh
 */


package com.boast.task5;

public class Main {

    public static void main(String[] args) {
        Data data = new Data();

        new Thread(new Counter(data)).start();
        new Thread(new Printer(data)).start();
    }
}

class Data{
    int value = 0;
    private boolean changed = true;

    synchronized int get(){
        while (!changed){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int value = this.value;

        changed = false;
        notify();
        return value;
    }

    synchronized void set(int value){
        while (changed){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.value = value;
        changed = true;
        notify();
    }
}

class Counter implements Runnable{
    Data data;

    Counter(Data data){
        this.data = data;
    }

    @Override
    public void run(){
        while (data.value < 1_000_000){
            data.set(data.value + 1);             //with wait/notify
            //data.value++;                         //without wait/notify
        }
    }
}
class Printer implements Runnable{
    Data data;

    Printer(Data data){
        this.data = data;
    }

    @Override
    public void run(){
        int currentValue = 0;
        do {
            currentValue = data.get();            //with wait/notify
            //currentValue = data.value;            //without wait/notify
            System.out.println(currentValue);
        } while (currentValue < 1_000_000);
    }
}


