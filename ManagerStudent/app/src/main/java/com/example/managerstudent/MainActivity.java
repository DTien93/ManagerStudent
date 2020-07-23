package com.example.managerstudent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.managerstudent.adapter.CustomAdapter;
import com.example.managerstudent.data.DBManager;
import com.example.managerstudent.model.Student;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edtName, editAddress, edtPhoneNumber, edtEmail, edtID;
    private Button btnSave, btnUpdate;
    private ListView lvStudent;
    private DBManager dbManager;
    private CustomAdapter customAdapter;
    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(this);
        anhXa();
        studentList = dbManager.getAllStudent();
        setAdapter();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student student = createStudent();
                if (student != null) {
                    dbManager.addStudent(student);
                }
                //studentList.clear();
                //studentList.addAll(dbManager.getAllStudent());
                //setAdapter();
                updateListStudent();
                setAdapter();
            }
        });
        lvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Student student = studentList.get(position);
                edtID.setText(String.valueOf(student.getmID()));
                edtName.setText(student.getmName());
                editAddress.setText(student.getmAddress());
                edtEmail.setText(student.getmEmail());
                edtPhoneNumber.setText(student.getmPhoneNumber());
                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student student = new Student();
                student.setmID(Integer.parseInt(String.valueOf(edtID.getText())));
                student.setmName(edtName.getText() + "");
                student.setmAddress(editAddress.getText() + "");
                student.setmEmail(edtEmail.getText() + "");
                student.setmPhoneNumber(edtPhoneNumber.getText() + "");
                int result = dbManager.updateStudent(student);
                if (result > 0) {
                    studentList.clear();
                    studentList.addAll(dbManager.getAllStudent());
                    customAdapter.notifyDataSetChanged();
                } else {
                    btnSave.setEnabled(false);
                    btnUpdate.setEnabled(true);
                }
            }
        });
        lvStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Student student = studentList.get(position);
                int result = dbManager.deleteStudent(student.getmID());
                if(result > 0){
                    Toast.makeText(MainActivity.this, "Delete successfuly", Toast.LENGTH_SHORT).show();
                    updateListStudent();
                }else{
                    Toast.makeText(MainActivity.this, "Delete fail", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }

    private Student createStudent() {
        String name = edtName.getText().toString();
        String address = String.valueOf(editAddress.getText());
        String phoneNumber = edtPhoneNumber.getText() + "";
        String email = edtEmail.getText().toString();
        Student student = new Student(name, address, phoneNumber, email);
        return student;
    }

    private void anhXa() {
        edtName = (EditText) findViewById(R.id.edt_name);
        editAddress = (EditText) findViewById(R.id.edt_address);
        edtPhoneNumber = (EditText) findViewById(R.id.edt_number);
        edtID = (EditText) findViewById(R.id.edt_id);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        lvStudent = (ListView) findViewById(R.id.lv_student);
    }

    private void setAdapter() {
        if (customAdapter == null) {
            customAdapter = new CustomAdapter(this, R.layout.item_listview_student, studentList);
            lvStudent.setAdapter(customAdapter);
        } else {
            customAdapter.notifyDataSetChanged();
            lvStudent.setSelection(customAdapter.getCount() - 1);
        }
    }

    public void updateListStudent() {
        studentList.clear();
        studentList.addAll(dbManager.getAllStudent());
        if (customAdapter != null) {
            customAdapter.notifyDataSetChanged();
        }
    }
}