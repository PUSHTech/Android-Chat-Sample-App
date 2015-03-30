package com.pushtech.pushchat.androidapplicationexample.chat.registration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.pushtech.sdk.Callbacks.GenericCallback;
import com.pushtech.sdk.PushtechApp;
import com.pushtech.sdk.PushtechError;
import com.pushtech.sdk.chatAndroidExample.R;


/**
 * Created by goda87 on 26/08/14.
 */
public class RegistrationValidationFragment extends Fragment
        implements View.OnClickListener, GenericCallback {

    private View finishSecretVerificationBtt;
    private EditText secretVerificationET;
    private String secretTyped;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment RegistrationFragment.
     */
    public static RegistrationValidationFragment newInstance() {
        return new RegistrationValidationFragment();
    }

    public RegistrationValidationFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(
                R.layout.fragment_registration_validation, container, false);
        findViews(fragmentView);
        setUpViews();
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        finishSecretVerificationBtt = fragmentView.findViewById(R.id.finishSecretVerificationBtt);
        secretVerificationET = (EditText) fragmentView.findViewById(R.id.secretVerificationET);
    }

    private void setUpViews() {
        finishSecretVerificationBtt.setOnClickListener(this);
        secretVerificationET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(secretVerificationET.getText())) {
                    secretTyped = secretVerificationET.getText().toString();
                    finishSecretVerificationBtt.setEnabled(true);
                } else {
                    finishSecretVerificationBtt.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });
    }

    protected void validateUserRegistration(final String secret) {
        PushtechApp.with(getActivity()).getChatRegister()
                .registerWithSecretCode(secret, this);
    }

    private void registrationFinished() {
        RegistrationActivity activity = (RegistrationActivity) getActivity();
        activity.registrationFinished();
    }

    private void showActionBarProgress(final boolean active) {
        if (getActivity() != null) {
            getActivity().setProgressBarIndeterminateVisibility(active);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finishSecretVerificationBtt:
                if (v.isEnabled()) {
                    finishSecretVerificationBtt.setEnabled(false);
                    showActionBarProgress(true);
                    validateUserRegistration(secretTyped);
                }
                break;
            default:
        }
    }


    @Override
    public void onSuccess() {
        registrationFinished();
    }

    @Override
    public void onError(PushtechError error) {
        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

    }


}
