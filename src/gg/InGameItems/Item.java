package gg.InGameItems;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class Item  implements Cloneable{

    private String name;
    private int healthEffect;
    private int cost;
    private int resellPrice;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private int quantity;
    private String type;


    public Item(){}

    Item(String name, int healingEffect, int cost, int resellPrice, int quantity, String type)
    {
        this.name = name;
        this.healthEffect = healingEffect;
        this.cost = cost;
        this.resellPrice = resellPrice;
        this.type = type;
    }


    public HashMap<String, Item> createItem() {

        HashMap<String, Item> items = new HashMap<>();

        items.put("Potion", new Item("Potion", 20, 100, 50, 1, "Healing"));
        items.put("Super Potion", new Item("Super Potion", 50, 250, 125, 0, "Healing"));
        items.put("Hyper Potion", new Item("Hyper Potion", 150, 500, 250, 0, "Healing"));
        items.put("Max Potion", new Item("Max Potion", 1000, 1000, 500, 0, "Healing"));

        return items;
    }

        protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
