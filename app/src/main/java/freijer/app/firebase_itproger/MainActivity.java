package freijer.app.firebase_itproger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    Button sign, registr;
    RelativeLayout root_layout;
    FirebaseAuth auth; //регистрация
    FirebaseDatabase db; //название БД
    DatabaseReference users; //название таблицы

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sign = findViewById(R.id.sign);
        registr = findViewById(R.id.registr);
        root_layout = findViewById(R.id.root_layout);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        registr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenRegistorWindow();
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               OpenSignWindow();
            }
        });
    }
    private void OpenRegistorWindow() {
        final MaterialEditText emailField, passField, nameField, phoneField;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflayter = LayoutInflater.from(this);
        View register_window = inflayter.inflate(R.layout.register_window, null);
        dialog.setView(register_window);

            emailField = register_window.findViewById(R.id.emailField);
            passField = register_window.findViewById(R.id.passField);
            nameField = register_window.findViewById(R.id.nameField);
            phoneField = register_window.findViewById(R.id.phoneField);


        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Зарегестрироваться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (TextUtils.isEmpty(emailField.getText().toString())){
                    Snackbar.make(root_layout, "Введите почту!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phoneField.getText().toString())){
                    Snackbar.make(root_layout, "Введите телефон!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(nameField.getText().toString())){
                    Snackbar.make(root_layout, "Введите имя!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (passField.getText().toString().length() <6){
                    Snackbar.make(root_layout, "Введите пароль более 3 символов!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //регистрация пользователя----:
                //код вызовется в том слукчае ,если пользоватль добавлен в БД
                auth.createUserWithEmailAndPassword(emailField.getText().toString(), passField.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseDB firebaseDB = new FirebaseDB();
                                    firebaseDB.setEmael(emailField.getText().toString());
                                    firebaseDB.setPass(passField.getText().toString());
                                    firebaseDB.setName(nameField.getText().toString());
                                    firebaseDB.setPhone(phoneField.getText().toString());

                             users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                     .setValue(firebaseDB) //ключ по которому идентифицируется пользователь - это id
                                     .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(root_layout, "Регистрация успешна", Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                   // ошибка! случае попытки регистрации на такой же меил
                        Snackbar.make(root_layout, "Ошибка авторизации" +e.getMessage(), Snackbar.LENGTH_SHORT).show();
//                        if(emailField.getText().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                           // Snackbar.make(root_layout, "Пользователь с таким имейлом уже зарегестрирован", Snackbar.LENGTH_SHORT).show();
                      //  }
                    }
                });
            }
        });
        dialog.show();
    }


    private void OpenSignWindow() {
        final MaterialEditText emailField, passField;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите все данные для входа");

        LayoutInflater inflayter = LayoutInflater.from(this);
        View sign_window = inflayter.inflate(R.layout.sign_in_window, null);
        dialog.setView(sign_window);

        emailField = sign_window.findViewById(R.id.emailField);
        passField = sign_window.findViewById(R.id.passField);


        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (TextUtils.isEmpty(emailField.getText().toString())){
                    Snackbar.make(root_layout, "Введите почту!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (passField.getText().toString().length() <3){
                    Snackbar.make(root_layout, "Введите пароль более 3 символов!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
        auth.signInWithEmailAndPassword(emailField.getText().toString(), passField.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //запускает код при успешной авторизации
                        startActivity(new Intent(MainActivity.this, Map.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Запускати код при провальной авторизации
                        Snackbar.make(root_layout, "Ошибка авторизации"  + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });

            }
        });
        dialog.show();
    }

}
