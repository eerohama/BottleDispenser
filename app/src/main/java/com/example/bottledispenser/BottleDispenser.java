package com.example.bottledispenser;

import java.util.ArrayList;

public class BottleDispenser {
    private float money;
    private static BottleDispenser instance = null;

    public BottleDispenser() {
        money = 0f;
    }

    public static BottleDispenser getInstance() {
        if (instance == null) {
            instance = new BottleDispenser();
        }
        return instance;
    }

    public ArrayList<Bottle> createList(){
        ArrayList<Bottle> list = new ArrayList<Bottle>();
        Bottle plo1 = new Bottle("Pepsi Max", "Pepsi", 0.5, 1.8);
        Bottle plo2 = new Bottle("Pepsi Max", "Pepsi", 1.5, 2.2);
        Bottle plo3 = new Bottle("Coca-Cola Zero", "Coca-Cola", 0.5, 2.0);
        Bottle plo4 = new Bottle("Coca-Cola Zero", "Coca-Cola", 1.5, 2.5);
        Bottle plo5 = new Bottle("Fanta Zero", "Fanta", 0.5, 1.95);
        Bottle plo6 = new Bottle("Coca-Cola", "Coca-Cola", 0.5, 1.95);
        Bottle plo7 = new Bottle("Coca-Cola", "Coca-Cola", 1.5, 2.5);
        list.add(plo1);
        list.add(plo2);
        list.add(plo3);
        list.add(plo4);
        list.add(plo5);
        list.add(plo6);
        list.add(plo7);
        return list;
    }

    public void addMoney(int i) {
        money += i;
    }

    public int buyBottle(ArrayList<Bottle> list, int choice) {

        if(money >= list.get(choice).getPrice()){
            if(list.size() == 0){
                return 0;
            } else {
                money -= list.get(choice).getPrice();
                return 1;
            }
        } else {
            return 2;
        }
    }

    public void returnMoney() {
        money = 0;
    }

    public float getMoney(){
        return money;
    }

    public ArrayList<Bottle> removeBottle(ArrayList<Bottle> list, int index) {
        list.remove(index);
        return list;
    }


}






