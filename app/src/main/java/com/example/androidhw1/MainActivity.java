package com.example.androidhw1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.ui.AppBarConfiguration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String FILE_NAME = "testFile.txt";
    ArrayList<String> countries = new ArrayList<String>();

    ArrayList<String> taskLists = new ArrayList<String>();
    int arraySize = 0;
    int sizeTasks = 0;

    TextView nameView;
    private static String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain", "Belorussian"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameView = findViewById(R.id.taskList);

        SharedPreferences sharedPreferences = getSharedPreferences("List", MODE_PRIVATE);

        arraySize = sharedPreferences.getInt("arraySize", 0);
        for(int i = 0; i < arraySize; i++){
            countries.add(sharedPreferences.getString("Country" + String.valueOf(i), ""));
            nameView.append("\n" + countries.get(i));
        }
        sizeTasks = sharedPreferences.getInt("sizeTask", 0);
        for(int i=0; i < sizeTasks; i++){
            taskLists.add((sharedPreferences.getString("task" + String.valueOf(i), "")));
        }

        if (taskLists.isEmpty()){
            for (int i = 0; i < COUNTRIES.length; i++){
                taskLists.add(COUNTRIES[i]);
            }
        }


        // Dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, taskLists);
        AutoCompleteTextView search = (AutoCompleteTextView)
                findViewById(R.id.newTask);
        search.setAdapter(adapter);

        // Save button
        Button b = findViewById(R.id.Button);
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AutoCompleteTextView text = findViewById(R.id.newTask);
                        text.clearComposingText();

                        DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm");
                        String date = df.format(Calendar.getInstance().getTime());

                        TextView textView = findViewById(R.id.taskList);
                        textView.append("\n" + search.getText() + " " + date);

                        if (!taskLists.contains(search.getText().toString())) {
                            taskLists.add(search.getText().toString());

                            search.setAdapter(adapter);
                        }


                        countries.add(search.getText().toString() + " " + date);

                        FileOutputStream fos = null;
                        try {
                            String str = text.getText().toString() + " " + date;

                            fos = openFileOutput(FILE_NAME, MODE_APPEND);
                            fos.write(str.getBytes());
                        }
                        catch (IOException ex) {
                            System.out.println(ex.toString());
                        } finally {
                            try {
                                if (fos != null)
                                    fos.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                });
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Сохраняем текст перед тем, как приложение будет закрыто

        SharedPreferences sharedPreferences = getSharedPreferences("List", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        arraySize = countries.size();
        editor.putInt("arraySize", arraySize);
        for (int i = 0; i< countries.size(); i++){
            editor.putString("Country" + String.valueOf(i), countries.get(i));
        }
        sizeTasks = taskLists.size();
        editor.putInt("sizeTask", sizeTasks);
        for (int i = 0; i< taskLists.size(); i++){
         editor.putString("task" + String.valueOf(i), taskLists.get(i));
        }
        editor.apply();  // Применяем изменения
    }


}