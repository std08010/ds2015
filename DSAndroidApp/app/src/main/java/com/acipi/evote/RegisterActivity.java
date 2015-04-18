package com.acipi.evote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.acipi.evote.db.service.UserService;
import com.acipi.evote.helpers.BitmapHelper;
import com.acipi.evote.helpers.ImageCircular;
import com.acipi.evote.helpers.UIHelper;
import com.acipi.evote.rest.AbstractWebServiceTask;
import com.acipi.evote.rest.dto.AbstractInDTO;
import com.acipi.evote.rest.dto.AbstractOutDTO;
import com.acipi.evote.rest.dto.ErrorDTO;
import com.acipi.evote.rest.dto.general.CountryAllOutDTO;
import com.acipi.evote.rest.dto.general.CountryGetFlagURLInDTO;
import com.acipi.evote.rest.dto.general.CountryGetFlagURLOutDTO;
import com.acipi.evote.rest.dto.user.RegistrationCheckUsernameAvailabilityInDTO;
import com.acipi.evote.rest.dto.user.RegistrationCheckUsernameAvailabilityOutDTO;
import com.acipi.evote.rest.dto.user.RegistrationCreateInDTO;
import com.acipi.evote.rest.dto.user.RegistrationCreateOutDTO;
import com.acipi.evote.utils.EmailValidator;
import com.acipi.evote.utils.GlobalConstants;
import com.acipi.evote.utils.PasswordValidator;
import com.acipi.evote.utils.PropertiesUtils;
import com.acipi.evote.utils.SecurityUtils;
import com.acipi.evote.utils.UsernameValidator;
import com.acipi.evote.utils.VolleySingleton;
import com.android.volley.toolbox.NetworkImageView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RegisterActivity extends Activity
{
    private String selectedAvatarPath;
    private Bitmap selectedAvatarBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);

        ((EditText) findViewById(R.id.reg_username)).setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    if (new UsernameValidator().validate(((EditText) v).getText().toString()))
                    {
                        checkUsernameAvailability(v);
                    }
                    else
                    {
                        displayUsernameInfo("Invalid username. Please use only valid characters.");
                    }
                }
                else
                {
                    ((TextView) findViewById(R.id.reg_username_info)).setVisibility(View.GONE);
                }
            }
        });

        ((EditText) findViewById(R.id.reg_password)).setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    if (!new PasswordValidator().validate(((EditText) v).getText().toString()))
                    {
                        displayPasswordInfo("Invalid password. Please use only valid characters.");
                    }
                }
                else
                {
                    ((TextView) findViewById(R.id.reg_password_info)).setVisibility(View.GONE);
                }
            }
        });

        ((EditText) findViewById(R.id.reg_email)).setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    if (!new EmailValidator().validate(((EditText) v).getText().toString()))
                    {
                        displayEmailInfo("Invalid email. Please use a valid email.");
                    }
                }
                else
                {
                    ((TextView) findViewById(R.id.reg_email_info)).setVisibility(View.GONE);
                }
            }
        });

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                // Closing registration screen
                // Switching to LoginActivity
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);

                finish();
            }
        });

        RegistrationGetAllCountriesTask wst = new RegistrationGetAllCountriesTask(RegistrationGetAllCountriesTask.GET, null);
        wst.execute(new String[]{PropertiesUtils.getUrlsProps(this).getProperty("general.country.all")});

        //Get default avatar picture and make it circular
        int logoID = getResources().getIdentifier("avatar", "drawable", getApplicationContext().getApplicationInfo().packageName);
        selectedAvatarBitmap = BitmapFactory.decodeResource(getResources(), logoID);
        ImageCircular mImageCircular = new ImageCircular(selectedAvatarBitmap);
        ImageView avatar = (ImageView) findViewById(R.id.reg_avatar);
        avatar.setImageDrawable(mImageCircular);
    }

    public void chooseAvatar(View vw)
    {
        Intent mIntent = new Intent();
        mIntent.setType("image/*");
        mIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(mIntent, "Select Picture"), GlobalConstants.SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == GlobalConstants.SELECT_PICTURE)
            {
                Uri selectedImageUri = data.getData();

                if (selectedAvatarBitmap != null)
                {
                    selectedAvatarBitmap.recycle();
                    selectedAvatarBitmap = null;
                }

                if (Build.VERSION.SDK_INT < 19)
                {
                    selectedAvatarPath = BitmapHelper.getImagePath(selectedImageUri, getContentResolver());
                    selectedAvatarBitmap = BitmapHelper.decodeSampledBitmapFromResourceV2(selectedAvatarPath, 256, 256);
                }
                else
                {
                    selectedAvatarPath = BitmapHelper.getImagePathForKitKat(selectedImageUri, getContentResolver());
                    ParcelFileDescriptor parcelFileDescriptor;

                    try
                    {
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        selectedAvatarBitmap = BitmapHelper.decodeSampledBitmapFromFileDescriptorV2(fileDescriptor, 256, 256);
                        parcelFileDescriptor.close();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                selectedAvatarBitmap = BitmapHelper.fixOrientation(selectedAvatarPath, selectedAvatarBitmap);

                ImageCircular mImageCircular = new ImageCircular(selectedAvatarBitmap);
                ImageView avatar = (ImageView) findViewById(R.id.reg_avatar);
                avatar.setImageDrawable(mImageCircular);

                if (avatar.getVisibility() != View.VISIBLE)
                {
                    avatar.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void checkUsernameAvailability(View vw)
    {
        RegistrationCheckUsernameAvailabilityInDTO inDTO = new RegistrationCheckUsernameAvailabilityInDTO();
        inDTO.setUsername(((EditText) vw).getText().toString());

        RegistrationCheckUsernameAvailabilityTask wst = new RegistrationCheckUsernameAvailabilityTask(RegistrationCheckUsernameAvailabilityTask.POST, inDTO);

        wst.execute(new String[]{PropertiesUtils.getUrlsProps(this).getProperty("user.register.check_username_availability")});
    }

    public void readCheckUsernameAvailabilityResponse(RegistrationCheckUsernameAvailabilityOutDTO outDTO)
    {
        try
        {
            if (outDTO.isAvailable())
            {
                ((EditText) findViewById(R.id.reg_username)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.check_icon), null);
            }
            else
            {
                ((EditText) findViewById(R.id.reg_username)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.no_icon), null);
                displayUsernameInfo(outDTO.getReason());
            }
        }
        catch (Exception e)
        {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
        }
    }

    public void readCheckUsernameAvailabilityError(ErrorDTO errorDTO)
    {
        displayUsernameInfo(errorDTO.getMessage());
    }

    public void displayUsernameInfo(String info)
    {
        TextView usernameInfo = (TextView) findViewById(R.id.reg_username_info);
        usernameInfo.setText(info);
        usernameInfo.setVisibility(View.VISIBLE);
    }

    public void displayPasswordInfo(String info)
    {
        TextView passwordInfo = (TextView) findViewById(R.id.reg_password_info);
        passwordInfo.setText(info);
        passwordInfo.setVisibility(View.VISIBLE);
    }

    public void displayEmailInfo(String info)
    {
        TextView emailInfo = (TextView) findViewById(R.id.reg_email_info);
        emailInfo.setText(info);
        emailInfo.setVisibility(View.VISIBLE);
    }

    public void displayRegisterBtnInfo(String info)
    {
        TextView regBtnInfo = (TextView) findViewById(R.id.reg_registerBtn_info);
        regBtnInfo.setText(info);
        regBtnInfo.setVisibility(View.VISIBLE);
    }

    public void changePasswordVisibility(View view)
    {
        EditText pwd = (EditText) findViewById(R.id.reg_password);

        if (pwd.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
        {
            pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        else
        {
            pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        pwd.setSelection(pwd.getText().length());
    }

    public void clearPasswordFields(View vw)
    {
        EditText edPassword = (EditText) findViewById(R.id.reg_password);

        edPassword.setText("");
    }

    public void askForRegistration(View vw)
    {
        ((TextView) findViewById(R.id.reg_registerBtn_info)).setVisibility(View.GONE);

        EditText edUsername = (EditText) findViewById(R.id.reg_username);
        EditText edPassword = (EditText) findViewById(R.id.reg_password);
        EditText edEmail = (EditText) findViewById(R.id.reg_email);
        Spinner countrySpinner = (Spinner) findViewById(R.id.reg_country);

        String username = edUsername.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String country = String.valueOf(countrySpinner.getSelectedItem());

        boolean usernameValidation = new UsernameValidator().validate(username);
        boolean passwordValidation = new PasswordValidator().validate(password);
        boolean emailValidation = new EmailValidator().validate(email);

        if (!usernameValidation)
        {
            displayUsernameInfo("Invalid username. Please use only valid characters.");
        }
        if (!passwordValidation)
        {
            displayPasswordInfo("Invalid password. Please use only valid characters.");
        }
        if (!emailValidation)
        {
            displayEmailInfo("Invalid email. Please use a valid email.");
        }
        if (!(usernameValidation && passwordValidation && emailValidation))
        {
            return;
        }

        RegistrationCreateInDTO inDTO = new RegistrationCreateInDTO();
        inDTO.setUsername(username);
        inDTO.setPassword(SecurityUtils.get_SHA_1_SecurePassword(password));
        inDTO.setEmail(email);
        inDTO.setCountry(country);
        inDTO.setAvatar(BitmapHelper.encodeBitmap(selectedAvatarBitmap));

        RegistrationCreateTask wst = new RegistrationCreateTask(RegistrationCreateTask.POST, this, "Asking for Registration...", inDTO);

        // the passed String is the URL we will POST to
        wst.execute(new String[]{PropertiesUtils.getUrlsProps(this).getProperty("user.register.create")});
    }

    public void hideKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager) RegisterActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(
                RegisterActivity.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void readResponse(RegistrationCreateOutDTO outDTO)
    {
        EditText edUsername = (EditText) findViewById(R.id.reg_username);
        EditText edPassword = (EditText) findViewById(R.id.reg_password);
        EditText edEmail = (EditText) findViewById(R.id.reg_email);
        Spinner countrySpinner = (Spinner) findViewById(R.id.reg_country);

        String username = edUsername.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String country = String.valueOf(countrySpinner.getSelectedItem());

        try
        {
            UserService userService = new UserService(getApplicationContext());
            userService.logoutUser();

            userService.addUser(username, SecurityUtils.get_SHA_1_SecurePassword(password), email, outDTO.getSessionToken(), country, outDTO.getCountryURL(), outDTO.getAvatarURL());

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            finish();
        }
        catch (Exception e)
        {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
        }
    }

    public void readError(ErrorDTO errorDTO)
    {
        displayRegisterBtnInfo(errorDTO.getMessage());
    }

    public void readGetAllCountriesResponse(CountryAllOutDTO outDTO)
    {
        try
        {
            Spinner countrySpinner = (Spinner) findViewById(R.id.reg_country);
            ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, outDTO.getCountries());
            countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countrySpinner.setAdapter(countryAdapter);
            countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    CountryGetFlagURLInDTO inDTO = new CountryGetFlagURLInDTO();
                    inDTO.setCountryName(parent.getItemAtPosition(position).toString());

                    RegistrationGetFlagURLTask wst = new RegistrationGetFlagURLTask(RegistrationGetFlagURLTask.POST, inDTO);

                    // the passed String is the URL we will POST to
                    wst.execute(new String[]{PropertiesUtils.getUrlsProps(getApplicationContext()).getProperty("general.country.get_flag_url")});
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });
        }
        catch (Exception e)
        {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
        }
    }

    public void readGetAllCountriesError(ErrorDTO errorDTO)
    {
        displayRegisterBtnInfo(errorDTO.getMessage());
    }

    public void readGetFlagURLResponse(CountryGetFlagURLOutDTO outDTO)
    {
        try
        {
            NetworkImageView avatar = (NetworkImageView) findViewById(R.id.reg_country_flag);
            avatar.setImageUrl(outDTO.getCountryFlagURL(), VolleySingleton.getInstance(this).getImageLoader());
        }
        catch (Exception e)
        {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
        }
    }

    public void readGetFlagURLError(ErrorDTO errorDTO)
    {
        displayRegisterBtnInfo(errorDTO.getMessage());
    }

    private class RegistrationCreateTask extends AbstractWebServiceTask<RegistrationCreateInDTO, RegistrationCreateOutDTO>
    {
        private Context mContext;
        private String processMessage = "Processing...";

        public RegistrationCreateTask(int taskType, Context mContext, String processMessage, RegistrationCreateInDTO inDTO)
        {
            super(taskType, inDTO, RegistrationCreateOutDTO.class);
            this.mContext = mContext;
            this.processMessage = processMessage;
        }

        @Override
        public void onInitActions()
        {
            hideKeyboard();
            UIHelper.showProgressDialog(this.mContext, this.processMessage);
        }

        @Override
        public void handleResponse(AbstractOutDTO outDTO)
        {
            readResponse((RegistrationCreateOutDTO) outDTO);
            UIHelper.hideProgressDialog();
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readError(errorDTO);
            UIHelper.hideProgressDialog();
        }
    }

    private class RegistrationCheckUsernameAvailabilityTask extends AbstractWebServiceTask<RegistrationCheckUsernameAvailabilityInDTO, RegistrationCheckUsernameAvailabilityOutDTO>
    {
        public RegistrationCheckUsernameAvailabilityTask(int taskType, RegistrationCheckUsernameAvailabilityInDTO inDTO)
        {
            super(taskType, inDTO, RegistrationCheckUsernameAvailabilityOutDTO.class);
        }

        @Override
        public void onInitActions()
        {
        }

        @Override
        public void handleResponse(AbstractOutDTO outDTO)
        {
            readCheckUsernameAvailabilityResponse((RegistrationCheckUsernameAvailabilityOutDTO) outDTO);
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readCheckUsernameAvailabilityError(errorDTO);
        }
    }

    private class RegistrationGetAllCountriesTask extends AbstractWebServiceTask<AbstractInDTO, CountryAllOutDTO>
    {
        public RegistrationGetAllCountriesTask(int taskType, AbstractInDTO inDTO)
        {
            super(taskType, inDTO, CountryAllOutDTO.class);
        }

        @Override
        public void onInitActions()
        {
        }

        @Override
        public void handleResponse(AbstractOutDTO outDTO)
        {
            readGetAllCountriesResponse((CountryAllOutDTO) outDTO);
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readGetAllCountriesError(errorDTO);
        }
    }

    private class RegistrationGetFlagURLTask extends AbstractWebServiceTask<CountryGetFlagURLInDTO, CountryGetFlagURLOutDTO>
    {
        public RegistrationGetFlagURLTask(int taskType, CountryGetFlagURLInDTO inDTO)
        {
            super(taskType, inDTO, CountryGetFlagURLOutDTO.class);
        }

        @Override
        public void onInitActions()
        {
        }

        @Override
        public void handleResponse(AbstractOutDTO outDTO)
        {
            readGetFlagURLResponse((CountryGetFlagURLOutDTO) outDTO);
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readGetFlagURLError(errorDTO);
        }
    }
}