package com.example.hangman.view.fragment;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman.R;
import com.example.hangman.view.activity.MainActivity;
import com.example.hangman.viewmodel.MainActivityViewModel;

public class RegisterFragment extends Fragment {
    private MainActivityViewModel viewModel;
    private EditText nameInput;
    private Spinner genderSpinner;
    private EditText ageInput;
    private Spinner difficultySpinner;
    private Button playButton;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_register, container, false);

        setVariables(v);
        customizeSpinner(genderSpinner, getResources().getStringArray(R.array.gender_spinner_items));
        customizeSpinner(difficultySpinner, getResources().getStringArray(R.array.difficulty_spinner_items));
        setOnClickListeners();

        return v;
    }

    private void setVariables(final View v) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        nameInput = v.findViewById(R.id.registerNameInput);
        genderSpinner = v.findViewById(R.id.registerGenderSpinner);
        ageInput = v.findViewById(R.id.registerAgeInput);
        difficultySpinner = v.findViewById(R.id.registerDifficultySpinner);
        playButton = v.findViewById(R.id.registerButton);
    }

    private void setOnClickListeners() {
        playButton.setOnClickListener(v -> {
            if (checkIfRegistrationIsOK()) {
                final String personName = String.valueOf(nameInput.getText()).trim();
                final int personGender = genderSpinner.getSelectedItemPosition();
                final String personAge = String.valueOf(ageInput.getText()).trim();
                final int gameDifficulty = difficultySpinner.getSelectedItemPosition();

                viewModel.setPlayerName(personName);
                viewModel.setPlayerGender(personGender);
                viewModel.setPlayerAge(Integer.parseInt(personAge));
                viewModel.setPlayerDifficulty(gameDifficulty);

                ((MainActivity) requireActivity()).setFragment(new GameFragment());
            } else {
                Toast.makeText(requireActivity(), getResources().getString(R.string.register_error),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean containsOnlyLettersAndSpaces(final String input) {
        for (final char character : input.toCharArray())
            if (character != 32 && (character < 65 || character > 90) && (character < 97 || character > 122)) {
                return false;
            }

        return true;
    }

    private boolean checkIfRegistrationIsOK() {
        final String personName = String.valueOf(nameInput.getText()).trim();
        final String personAge = String.valueOf(ageInput.getText()).trim();

        // returning true if person's name has at least 2 characters, it starts with a capital letter
        // and all of its characters are letters or spaces and age is greater than 0 years
        return personName.length() > 1 &&
                containsOnlyLettersAndSpaces(personName) &&
                personName.charAt(0) >= 65 &&
                personName.charAt(0) <= 90 &&
                personAge.length() > 0 &&
                Integer.parseInt(personAge) > 0;
    }

    private void customizeSpinner(final Spinner spinner, final String[] items) {
        final int spinnerBackgroundColorFilter = ContextCompat.getColor(requireContext(), R.color.lightBlueColor);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(),
                R.layout.custom_spinner_item, items) {
            @Override
            public View getDropDownView(final int position, final @Nullable View convertView,
                                        final @NonNull ViewGroup parent) {
                final View v = super.getDropDownView(position, convertView, parent);

                ((TextView) v).setTextColor(ContextCompat.getColor(requireContext(), R.color.blueColor));
                ((TextView) v).setTypeface(Typeface.DEFAULT_BOLD);
                v.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lightBlueColor));

                return v;
            }
        };

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view,
                                       final int position, final long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.lightBlueColor));
                ((TextView) parent.getChildAt(0)).setTextSize(18);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.getBackground().setColorFilter(spinnerBackgroundColorFilter, PorterDuff.Mode.SRC_ATOP);
        spinner.setAdapter(adapter);
    }
}