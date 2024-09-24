package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.Test;

import area.Area;
import item.ItemType;
import item.Shop;
import trainer.Trainer;

public class TestShop {
	
	@Test
	private void TestShop() {
		Shop shop = new Shop("G");
		Trainer t = new Trainer("Gino", new Area("V", null, true, null, null));
		shop.restockShop();
		shop.showInventory();
		System.out.println(t.getMoney());
		boolean sellResult = shop.sell(5, ItemType.POKEBALL, t);
		System.out.println(sellResult);
		System.out.println(t.getMoney());
		System.out.println(shop.getMoney());
		shop.showInventory();

	}
}
