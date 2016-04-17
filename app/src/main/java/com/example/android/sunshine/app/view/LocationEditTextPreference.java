package com.example.android.sunshine.app.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.sunshine.app.R;

/**
 * Custom EditTextPreference that disables the positive button
 * if the actual length of the text is less than the minimum required text length.
 */
public class LocationEditTextPreference extends EditTextPreference {

    private static final int DEFAULT_MIN_LENGTH = 2;
    private int minimumLength;

    public LocationEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LocationEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LocationEditTextPreference, 0, 0);

        try {
            minimumLength = a.getInteger(
                    R.styleable.LocationEditTextPreference_minLength,
                    DEFAULT_MIN_LENGTH);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        getEditText().addTextChangedListener(textChangeListener);
    }

    private TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Dialog d = getDialog();
            if (d instanceof AlertDialog) {
                AlertDialog dialog = (AlertDialog) d;
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                boolean enabled = s.length() >= minimumLength;
                positiveButton.setEnabled(enabled);
            }
        }
    };

}
