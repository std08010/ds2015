package com.acipi.evote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.acipi.evote.db.service.UserService;
import com.acipi.evote.helpers.UIHelper;
import com.acipi.evote.rest.AbstractWebServiceTask;
import com.acipi.evote.rest.dto.AbstractOutDTO;
import com.acipi.evote.rest.dto.ErrorDTO;
import com.acipi.evote.rest.dto.user.LoginAskInDTO;
import com.acipi.evote.rest.dto.user.LoginAskOutDTO;
import com.acipi.evote.utils.PropertiesUtils;
import com.acipi.evote.utils.SecurityUtils;

/**
 * Created by Altin Cipi on 1/11/2015.
 */
public class LoginActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        UserService userService = new UserService(getApplicationContext());
        if (userService.isUserLoggedIn())
        {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            finish();
        }
        else
        {
            // setting default screen to login.xml
            setContentView(R.layout.login);

            TextView registerScreen = (TextView) findViewById(R.id.link_to_register);

            // Listening to register new account link
            registerScreen.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    // Switching to Register screen
                    Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(i);

                    finish();
                }
            });
        }
    }

    public void displayLoginBtnInfo(String info)
    {
        TextView loginBtnInfo = (TextView) findViewById(R.id.login_loginBtn_info);
        loginBtnInfo.setText(info);
        loginBtnInfo.setVisibility(View.VISIBLE);
    }

    public void askForLogin(View vw)
    {
        ((TextView) findViewById(R.id.login_loginBtn_info)).setVisibility(View.GONE);

        EditText edUsername = (EditText) findViewById(R.id.login_username);
        EditText edPassword = (EditText) findViewById(R.id.login_password);

        String username = edUsername.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        LoginAskInDTO inDTO = new LoginAskInDTO();
        inDTO.setUsername(username);
        inDTO.setPassword(SecurityUtils.get_SHA_1_SecurePassword(password));
        LoginAskTask wst = new LoginAskTask(LoginAskTask.POST, this, "Asking for Login...", inDTO);

        // the passed String is the URL we will POST to
        wst.execute(new String[]{PropertiesUtils.getUrlsProps(this).getProperty("user.login.ask")});
    }

    public void hideKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager) LoginActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(
                LoginActivity.this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void readResponse(LoginAskOutDTO outDTO)
    {
        try
        {
            UserService userService = new UserService(getApplicationContext());
            userService.logoutUser();

            EditText edUsername = (EditText) findViewById(R.id.login_username);
            EditText edPassword = (EditText) findViewById(R.id.login_password);

            String username = edUsername.getText().toString().trim();
            String password = edPassword.getText().toString().trim();

            userService.addUser(username, password, outDTO.getEmail(), outDTO.getSessionToken(), outDTO.getCountry(), outDTO.getCountryURL(), outDTO.getAvatarURL());

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
        displayLoginBtnInfo(errorDTO.getMessage());
    }

    private class LoginAskTask extends AbstractWebServiceTask<LoginAskInDTO, LoginAskOutDTO>
    {
        private Context mContext;
        private String processMessage = "Processing...";

        public LoginAskTask(int taskType, Context mContext, String processMessage, LoginAskInDTO inDTO)
        {
            super(taskType, inDTO, LoginAskOutDTO.class);
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
            readResponse((LoginAskOutDTO) outDTO);
            UIHelper.hideProgressDialog();
        }

        @Override
        public void handleError(ErrorDTO errorDTO)
        {
            readError(errorDTO);
            UIHelper.hideProgressDialog();
        }
    }
}
