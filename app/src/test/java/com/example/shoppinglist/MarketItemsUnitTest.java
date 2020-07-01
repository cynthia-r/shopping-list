package com.example.shoppinglist;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.MarketItems;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MarketItemsUnitTest {
    @Test
    public void getPosition() {
        Item[] items = {
            new Item("Barres"),
            new Item("Cereales cuicui"),
            new Item("Echalotes"),
            new Item("Salade cuicui"),
            new Item("Eau gallon"),
            new Item("Oeufs"),
            new Item("Yaourt doudou"),
            new Item("Petits pains"),
            new Item("Fromage"),
            new Item("Lardons")
        };
        MarketItems marketItems = new MarketItems(items);

        assertEquals(0, marketItems.getPosition("Barres"));
        assertEquals(5, marketItems.getPosition("Oeufs"));
        assertEquals(9, marketItems.getPosition("Lardons"));
    }

    @Test
    public void moveDown() {
        Item[] items = {
                new Item("Barres"),
                new Item("Cereales cuicui"),
                new Item("Echalotes"),
                new Item("Salade cuicui"),
                new Item("Eau gallon"),
                new Item("Oeufs"),
                new Item("Yaourt doudou"),
                new Item("Petits pains"),
                new Item("Fromage"),
                new Item("Lardons")
        };
        MarketItems marketItems = new MarketItems(items);

        marketItems.move(2, 5);

        assertEquals("Barres", marketItems.get(0).getName());
        assertEquals("Salade cuicui", marketItems.get(2).getName());
        assertEquals("Eau gallon", marketItems.get(3).getName());
        assertEquals("Echalotes", marketItems.get(5).getName());
        assertEquals("Yaourt doudou", marketItems.get(6).getName());

        assertEquals(0, marketItems.getPosition("Barres"));
        assertEquals(2, marketItems.getPosition("Salade cuicui"));
        assertEquals(3, marketItems.getPosition("Eau gallon"));
        assertEquals(5, marketItems.getPosition("Echalotes"));
        assertEquals(6, marketItems.getPosition("Yaourt doudou"));
    }

    @Test
    public void moveUp() {
        Item[] items = {
                new Item("Barres"),
                new Item("Cereales cuicui"),
                new Item("Echalotes"),
                new Item("Salade cuicui"),
                new Item("Eau gallon"),
                new Item("Oeufs"),
                new Item("Yaourt doudou"),
                new Item("Petits pains"),
                new Item("Fromage"),
                new Item("Lardons")
        };
        MarketItems marketItems = new MarketItems(items);

        marketItems.move(7, 0);

        assertEquals("Petits pains", marketItems.get(0).getName());
        assertEquals("Barres", marketItems.get(1).getName());
        assertEquals("Yaourt doudou", marketItems.get(7).getName());
        assertEquals("Fromage", marketItems.get(8).getName());
        assertEquals("Lardons", marketItems.get(9).getName());

        assertEquals(0, marketItems.getPosition("Petits pains"));
        assertEquals(1, marketItems.getPosition("Barres"));
        assertEquals(7, marketItems.getPosition("Yaourt doudou"));
        assertEquals(8, marketItems.getPosition("Fromage"));
        assertEquals(9, marketItems.getPosition("Lardons"));
    }
}