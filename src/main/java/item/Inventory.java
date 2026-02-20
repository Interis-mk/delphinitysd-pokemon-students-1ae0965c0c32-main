package item;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashMap;

@Entity
@Table(name = "inventory")
public class Inventory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    protected final HashMap<ItemType, Integer> items;

    public Inventory() {
        items = new HashMap<>();
    }

    public HashMap<ItemType, Integer> getItems() {
        return items;
    }

//    public void addItem(int amount, ItemType type) 
//      {
//        int currentAmount = items.getOrDefault(type, 0);
//        if (currentAmount > 0) 
//     {
//            items.put(type, currentAmount + amount);
//        } else 
//   	  {
//            items.put(type, amount);
//        }
//    }

    public void removeItem(ItemType type) {
        int currentAmount = items.getOrDefault(type, 0);
        if (currentAmount > 0) {
            items.put(type, currentAmount - 1);
        } else {
            System.out.println("You don't have this item in your inventory!");
        }
    }

    public void addItem(int amount, ItemType type) {
        items.merge(type, amount, Integer::sum);
    }

    public void show() {
        for (ItemType type : items.keySet()) {
            System.out.println(type + " in stock: " + items.get(type));
        }
    }
}
