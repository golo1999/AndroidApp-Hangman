package com.example.hangman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class RegisterFragment extends Fragment
{
    private MainActivityViewModel viewModel;
    private EditText nameInput;
    private Spinner genderSpinner;
    private EditText ageInput;
    private Spinner difficultySpinner;
    private Button playButton;

    public RegisterFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        setVariables(v);
        setOnClickListeners();

        return v;
    }

    private void setVariables(View v)
    {
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        nameInput = v.findViewById(R.id.registerNameInput);
        genderSpinner = v.findViewById(R.id.registerGenderSpinner);
        ageInput = v.findViewById(R.id.registerAgeInput);
        difficultySpinner = v.findViewById(R.id.registerDifficultySpinner);
        playButton = v.findViewById(R.id.registerButton);
    }

    private void setOnClickListeners()
    {
        playButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(checkIfRegistrationIsOK())
                {
                    String personName = String.valueOf(nameInput.getText()).trim();
                    int personGender = genderSpinner.getSelectedItemPosition();
                    String personAge = String.valueOf(ageInput.getText()).trim();
                    int gameDifficulty = difficultySpinner.getSelectedItemPosition();

                    viewModel.setPlayerName(personName);
                    viewModel.setPlayerGender(personGender);
                    viewModel.setPlayerAge(Integer.parseInt(personAge));
                    viewModel.setPlayerDifficulty(gameDifficulty);

                    ((MainActivity)requireActivity()).setFragment(new GameFragment());
                }
                else Toast.makeText(requireActivity(), "Please enter your details correctly", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean containsOnlyLettersAndSpaces(String input)
    {
        boolean ok = true;

        for(char character : input.toCharArray())
            if(character != 32 && (character < 65 || character > 90) && (character < 97 || character > 122))
            {
                ok = false;
                break;
            }

        return ok;
    }

    private boolean checkIfRegistrationIsOK()
    {
        String personName = String.valueOf(nameInput.getText()).trim();
        String personAge = String.valueOf(ageInput.getText()).trim();
        boolean ok = false;

        if(personName.length() > 1 && containsOnlyLettersAndSpaces(personName) && personAge.length() > 0 && Integer.parseInt(personAge) > 0) // if person's name has at least 2 characters and all of its characters are letters or spaces and age is greater than 0 years
            ok = true;

        return  ok;
    }
}