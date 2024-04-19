package com.example.diplom1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.diplom1.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Настройка слушателя нажатия кнопки "Назад"
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Настройка слушателя нажатия кнопки "Зарегистрироваться"
        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailEt.getText().toString();
                String password = binding.passwordEt.getText().toString();
                String username = binding.usernameEt.getText().toString();

                if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                    // Проверка на пустые поля
                    Toast.makeText(getApplicationContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    // Проверка на корректность email
                    Toast.makeText(getApplicationContext(), "Некорректный email", Toast.LENGTH_SHORT).show();
                } else {
                    // Регистрация пользователя в Firebase
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Если регистрация успешна, сохранение информации о пользователе в базе данных
                                        HashMap<String, String> userInfo = new HashMap<>();
                                        userInfo.put("email", email);
                                        userInfo.put("username", username);
                                        userInfo.put("profileImage", "");
                                        FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(userInfo);

                                        // Переход к главному экрану
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        // Если произошла ошибка при регистрации, вывод сообщения об ошибке
                                        Toast.makeText(RegisterActivity.this, "Ошибка при регистрации: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    // Метод для проверки корректности email
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
