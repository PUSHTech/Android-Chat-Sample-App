package com.pushtech.pushchat.androidapplicationexample.chat.registration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pushtech.sdk.Callbacks.GenericCallback;
import com.pushtech.sdk.Country;
import com.pushtech.sdk.CountryHelper;
import com.pushtech.sdk.NotValidCountryException;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.PushtechError;
import com.pushtech.sdk.chatAndroidExample.R;

import java.io.File;

/**
 * Created by goda87 on 26/08/14.
 */
public class RegistrationFragment extends Fragment
        implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, GenericCallback {

    private Spinner countriesSpinner;
    private EditText phoneNumberET, userNameET;
    private TextView phonePrefixEText;
    private Button signInBtt;

    private CountriesAdapter countriesAdapter;
    private com.pushtech.sdk.UserManager userManager;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment RegistrationFragment.
     */
    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_registration, container, false);
        findViews(fragmentView);
        setUpViews();
        userManager = PushtechApp.with(getActivity()).getBaseManager().getUserManager();
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        countriesSpinner = (Spinner) fragmentView.findViewById(R.id.countriesSpinner);
        phonePrefixEText = (TextView) fragmentView.findViewById(R.id.phonePrefixEText);
        phoneNumberET = (EditText) fragmentView.findViewById(R.id.phoneNumberET);
        userNameET = (EditText) fragmentView.findViewById(R.id.userNameET);
        signInBtt = (Button) fragmentView.findViewById(R.id.signInBtt);
    }

    private void setUpViews() {
        countriesAdapter = new CountriesAdapter(getActivity(), CountryHelper.getCountryList());
        countriesSpinner.setAdapter(countriesAdapter);
        countriesSpinner.setOnItemSelectedListener(this);
        int position = getPhoneDefaultCountryPosition();
        if (position != -1) {
            countriesSpinner.setSelection(position);
        } else {
            countriesSpinner.setSelection(0);
        }
        signInBtt.setOnClickListener(this);
    }

    private int getPhoneDefaultCountryPosition() {
        try {
            return CountryHelper.getPositionOfCountryWithIsoCode(
                    CountryHelper.getDefaultCountryISO(getActivity()));
        } catch (NotValidCountryException e) {
            return 0;
        }
    }

    private void changePhonePrefix(int callingCode) {
        phonePrefixEText.setText("+" + callingCode);
    }

    /**
     * Registers a new user into the chat.
     *
     * @param userName   is the user name that will be registered.
     * @param country    is the country from the user who owns the phone number. (this will find
     *                   the calling code of that country).
     * @param avatarFile is the picture file of the avatar that this user wants to set as his
     *                   avatar.
     */
    protected final void registerUser(final String phoneNumber, final String userName,
                                      final Country country, final File avatarFile) {
        PushtechApp.with(getActivity()).getChatRegister()
                .sendCodeRegistration(phoneNumber, userName, country, this);
    }

    private boolean allFieldsFilledIn() {
        return !TextUtils.isEmpty(phonePrefixEText.getText())
                && !TextUtils.isEmpty(phoneNumberET.getText())
                && !TextUtils.isEmpty(userNameET.getText());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInBtt:
                if (allFieldsFilledIn()) {
                    signInBtt.setEnabled(false);
                    showActionBarProgress(true);

                    final Country userCountry = countriesAdapter.getItem(countriesSpinner
                            .getSelectedItemPosition());
                    final String mName = userNameET.getText().toString();
                    final String mPhone = phoneNumberET.getText().toString();

                    registerUser(mPhone, mName, userCountry, null);
                } else {
                    Toast.makeText(getActivity(),
                            R.string.registering_blank_fields_warning_toast, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void showActionBarProgress(final boolean active) {
        if (getActivity() != null) {
            getActivity().setProgressBarIndeterminateVisibility(active);
        }
    }

    private void openRegistrationValidationFragment() {
        RegistrationActivity activity = (RegistrationActivity) getActivity();
        activity.openRegistrationValidationFragment();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int phoneCallingCode =
                countriesAdapter.getItem(position).getPhoneCode();
        changePhonePrefix(phoneCallingCode);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSuccess() {
        showActionBarProgress(false);
        openRegistrationValidationFragment();
        signInBtt.setEnabled(true);

    }

    @Override
    public void onError(PushtechError error) {
        //TODO REMOVE ONLY FOR TEST
        onSuccess();
        /*showActionBarProgress(false);
        signInBtt.setEnabled(true);
        Toast.makeText(getActivity(),
                R.string.registering_error_warning_toast, Toast.LENGTH_SHORT).show();*/

    }


}
