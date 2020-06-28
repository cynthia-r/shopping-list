package com.example.shoppinglist;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.shoppinglist.model.Item;

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

public class FileService {
    private Context mContext;

    public FileService(Context context) {
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<Item> openFile(String filename) {
        File directory = mContext.getFilesDir();
        File file = new File(directory, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                Toast.makeText(mContext, "Failed to create file", Toast.LENGTH_SHORT);
                return new ArrayList<>();
            }
        }

        FileInputStream fis = null;
        try {
            fis = mContext.openFileInput(filename);
        } catch (FileNotFoundException e) {
            Toast.makeText(mContext, "Could not find file", Toast.LENGTH_SHORT);
            return new ArrayList<>();
        }
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);

        List<Item> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                Item item = new Item(line);
                items.add(item);
                line = reader.readLine();
            }
        } catch (IOException e) {
            Toast.makeText(mContext, "Failed to write to file", Toast.LENGTH_SHORT);
        }

        return items;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void writeToFile(String filename, List<Item> items) {
        try (FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (Item item: items) {
                bw.write(item.getName());
                bw.newLine();
            }

            bw.flush();
        }
        catch (IOException e) {
            Toast.makeText(mContext, "Failed to write to file", Toast.LENGTH_SHORT);
        }
    }
}
