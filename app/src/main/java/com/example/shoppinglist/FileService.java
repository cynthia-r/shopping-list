package com.example.shoppinglist;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.MarketItems;
import com.example.shoppinglist.model.ShoppingList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FileService {
    private Context mContext;

    public FileService(Context context) {
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getCurrentList(String filename) {
        if (!createFileIfNotExists(filename)) {
            return "";
        }

        String currentList = "";
        InputStreamReader inputStreamReader = openFile(filename);
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            currentList = null == line ? "" : line;
        } catch (IOException e) {
            Toast.makeText(mContext, "Failed to read from file", Toast.LENGTH_SHORT);
        }

        closeFile(inputStreamReader);

        return currentList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveCurrentList(String filename, String currentList) {
        if (!createFileIfNotExists(filename) || currentList.isEmpty()) {
            return;
        }

        try (FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(currentList);
            bw.newLine();
            bw.flush();
        }
        catch (IOException e) {
            Toast.makeText(mContext, "Failed to write to file", Toast.LENGTH_SHORT);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ShoppingList readShoppingList(String filename) {

        MarketItems marketItems = this.readMarketItems("catalog");

        if (!createFileIfNotExists(filename)) {
            return new ShoppingList(marketItems);
        }

        InputStreamReader inputStreamReader = openFile(filename);
        if (inputStreamReader == null) {
            return new ShoppingList(marketItems);
        }

        ShoppingList shoppingList = new ShoppingList(marketItems);
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ":");
                String itemName = tokenizer.nextToken();
                int isSelected = tokenizer.hasMoreTokens()
                        ? Integer.parseInt(tokenizer.nextToken()) : 0;
                Item item = new Item(itemName);
                shoppingList.add(item);
                if (isSelected > 0) {
                    shoppingList.select(itemName);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            Toast.makeText(mContext, "Failed to read from file", Toast.LENGTH_SHORT);
        }

        closeFile(inputStreamReader);

        return shoppingList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MarketItems readMarketItems(String filename) {
        if (!createFileIfNotExists(filename)) {
            return new MarketItems();
        }

        InputStreamReader inputStreamReader = openFile(filename);

        List<Item> itemList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                Item item = new Item(line);
                itemList.add(item);

                line = reader.readLine();
            }
        } catch (IOException e) {
            Toast.makeText(mContext, "Failed to read from file", Toast.LENGTH_SHORT);
        }

        closeFile(inputStreamReader);

        Item[] items = new Item[itemList.size()];
        for (int i=0; i<itemList.size(); i++) {
            items[i] = itemList.get(i);
        }

        return new MarketItems(items);
    }

    /**
     * Writes the specified items to the file.
     * This rewrites the file instead of appending, so only the selected items will be saved.
     * @param filename
     * @param items
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveShoppingList(String filename, List<Item> items) {
        if (!createFileIfNotExists(filename)) {
            return;
        }

        try (FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (Item item: items) {
                bw.write(item.getName());
                bw.write(':');
                bw.write('0');
                bw.newLine();
            }

            bw.flush();
        }
        catch (IOException e) {
            Toast.makeText(mContext, "Failed to write to file", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Writes the specified items to the file.
     * This rewrites the file instead of appending, so only the selected items will be saved.
     * @param filename
     * @param shoppingList
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveShoppingList(String filename, ShoppingList shoppingList) {
        if (!createFileIfNotExists(filename)) {
            return;
        }

        try (FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (Item item: shoppingList.toList(false)) {
                bw.write(item.getName());
                bw.write(':');
                bw.write(shoppingList.isSelected(item.getName()) ? '1':'0');
                bw.newLine();
            }

            bw.flush();
        }
        catch (IOException e) {
            Toast.makeText(mContext, "Failed to write to file", Toast.LENGTH_SHORT);
        }
    }


    /**
     * Writes the specified items to the file.
     * @param filename
     * @param marketItems
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveMarketItems(String filename, MarketItems marketItems) {
        if (!createFileIfNotExists(filename)) {
            return;
        }

        try (FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (Item item: marketItems.toList()) {
                bw.write(item.getName());
                bw.newLine();
            }

            bw.flush();
        }
        catch (IOException e) {
            Toast.makeText(mContext, "Failed to write to file", Toast.LENGTH_SHORT);
        }
    }

    private boolean createFileIfNotExists(String filename) {
        File directory = mContext.getFilesDir();
        File file = new File(directory, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                Toast.makeText(mContext, "Failed to create file", Toast.LENGTH_SHORT);
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private InputStreamReader openFile(String filename) {
        FileInputStream fis;
        try {
            fis = mContext.openFileInput(filename);
        } catch (FileNotFoundException e) {
            Toast.makeText(mContext, "Could not find file", Toast.LENGTH_SHORT);
            return null;
        }
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);
        return inputStreamReader;
    }

    private void closeFile(InputStreamReader inputStreamReader) {
        if (inputStreamReader == null) {
            Toast.makeText(mContext, "Input stream is null", Toast.LENGTH_SHORT);
        }

        try {
            inputStreamReader.close();
        } catch (IOException e) {
            Toast.makeText(mContext, "Failed to close file", Toast.LENGTH_SHORT);
        }
    }
}
