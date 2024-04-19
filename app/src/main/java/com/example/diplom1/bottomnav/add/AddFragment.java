package com.example.diplom1.bottomnav.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.diplom1.R;
import com.example.diplom1.databinding.FragmentAddBinding;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private EditText nameEditText, authorEditText;
    private Spinner genreSpinner;
    private Button addButton;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Инициализация элементов пользовательского интерфейса
        nameEditText = view.findViewById(R.id.name_et);
        authorEditText = view.findViewById(R.id.author_et);
        genreSpinner = view.findViewById(R.id.genre_spinner);
        addButton = view.findViewById(R.id.add_btn);

        // Получение ссылки на базу данных Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("books");

        // Получение массива значений для списка жанров из ресурсов
        String[] genres = getResources().getStringArray(R.array.genres);

        // Создание адаптера для Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, genres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(adapter);

        // Обработчик нажатия на кнопку добавления
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение данных из полей формы
                String name = nameEditText.getText().toString().trim();
                String author = authorEditText.getText().toString().trim();
                String genre = genreSpinner.getSelectedItem().toString();

                // Проверка наличия данных
                if (name.isEmpty() || author.isEmpty()) {
                    Toast.makeText(getActivity(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                } else {
                    // Создание объекта для хранения данных о книге
                    Book book = new Book(name, author, genre);

                    // Отправка данных в базу данных Firebase
                    databaseReference.push().setValue(book);

                    // Очистка полей формы после отправки данных
                    nameEditText.setText("");
                    authorEditText.setText("");
                    genreSpinner.setSelection(0); // Сброс выбранного элемента в начальное значение

                    Toast.makeText(getActivity(), "Данные успешно добавлены", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
