package com.example.sqllite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lst;
    EditText name, phone, dateofbirth;
    ImageButton insert, select, delete, edit;
    DatabaseHelper databaseHelper;
    String[] datalist;
    String selectedlistname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lst=findViewById(R.id.list);
        name =findViewById(R.id.txtName);
        phone =findViewById(R.id.txtNumber);
        dateofbirth =findViewById(R.id.txtDate);
        insert =findViewById(R.id.btnInsert);
        select =findViewById(R.id.btnSelect);
        edit =findViewById(R.id.btnEdit);
        delete =findViewById(R.id.btnDelete);
        databaseHelper=new DatabaseHelper(this);

        insert.setOnClickListener(view->{
            Boolean checkInsertData=databaseHelper.insert(name.getText().toString(),
                    phone.getText().toString(), dateofbirth.getText().toString());
            if (checkInsertData){
                Toast.makeText(getApplicationContext(),"Данные успешно добавлены", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_LONG).show();
            }
            Cursor res=databaseHelper.getdata();
            int a=0;
            datalist=new String[res.getCount()];
            while(res.moveToNext()){
                datalist[a]=(res.getString(0));
                a++;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_single_choice, datalist
            );
            lst.setAdapter(adapter);
        });

        delete.setOnClickListener(view->{
            if (selectedlistname!="")
            {
                try {
                    databaseHelper=new DatabaseHelper(this);
                    databaseHelper.delete(selectedlistname);
                Cursor res=databaseHelper.getdata();
                int a=0;
                datalist=new String[res.getCount()];
                while(res.moveToNext()){
                    datalist[a]=(res.getString(0));
                    a++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_list_item_single_choice, datalist
                );
                lst.setAdapter(adapter);
                } catch (Exception e) {}

            }
        });

        edit.setOnClickListener(view->{
            if (selectedlistname!="")
            {
                try {
                    databaseHelper=new DatabaseHelper(this);
                    databaseHelper.edit(selectedlistname, phone.getText().toString(), dateofbirth.getText().toString());
                    Cursor res=databaseHelper.getdata();
                    int a=0;
                    datalist=new String[res.getCount()];
                    while(res.moveToNext()){
                        datalist[a]=(res.getString(0));
                        a++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this, android.R.layout.simple_list_item_single_choice, datalist
                    );
                    lst.setAdapter(adapter);
                } catch (Exception e) {}

            }
        });

        select.setOnClickListener(view->{
            databaseHelper=new DatabaseHelper(this);
            Cursor res=databaseHelper.getdata();

            if (res.getCount()==0){
                Toast.makeText(MainActivity.this, "Нет данных", Toast.LENGTH_LONG).show();
                return;
            }

            StringBuilder buffer=new StringBuilder();
            int a=0;
            datalist=new String[res.getCount()];
            while(res.moveToNext()){
                datalist[a]=(res.getString(0));
                buffer.append("Имя: ").append(res.getString(0)).append("\n");
                buffer.append("Тел. номер: ").append(res.getString(1)).append("\n");
                buffer.append("Дата рождения: ").append(res.getString(2)).append("\n\n");
                a++;
            }
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Данные пользователей");
            builder.setMessage(buffer.toString());
            builder.show();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_single_choice, datalist
            );
            lst.setAdapter(adapter);
        });


        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor res = databaseHelper.getdata();
                    res.move(lst.getCheckedItemPosition()+1);
                    name.setText(res.getString(0));
                    phone.setText(res.getString(1));
                    dateofbirth.setText(res.getString(2));
                selectedlistname=res.getString(0);
            }
        });
    }
}