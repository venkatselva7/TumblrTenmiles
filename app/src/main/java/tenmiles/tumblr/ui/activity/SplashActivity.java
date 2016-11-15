package tenmiles.tumblr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import tenmiles.tumblr.helpers.Config;
import tenmiles.tumblr.helpers.PrefUtil;

/**
 * Created by Venkatesh S on 13-Nov-16.
 * venkatselva8@gmail.com
 */

public class SplashActivity extends AppCompatActivity {

    Context context;
    public static final String CONSUMER_KEY = Config.CONSUMER_KEY;
    public static final String CONSUMER_SECRET = Config.CONSUMER_SECRET;

    public static final String REQUEST_URL = Config.REQUEST_URL;
    public static final String ACCESS_URL = Config.ACCESS_URL;
    public static final String AUTHORIZE_URL = Config.AUTHORIZE_URL;

    public static final String OAUTH_CALLBACK_URL = Config.OAUTH_CALLBACK_SCHEME + "://" + Config.OAUTH_CALLBACK_HOST;

    private static Intent newIntent = null;

    PrefUtil pref;
    private static String token, secret, authURL, uripath;

    private static CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
    private static CommonsHttpOAuthProvider provider = new CommonsHttpOAuthProvider(REQUEST_URL, ACCESS_URL, AUTHORIZE_URL);

    private static boolean auth = false, browser = false, browser2 = false;//These booleans determine which code is run every time onResume is executed.
    private static boolean loggedin = false;

    private static int SPLASH_TIME_OUT = 1500;  //1.5 Seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        pref = new PrefUtil(context);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        token = pref.getStringPref("TUMBLR_OAUTH_TOKEN", "");
        secret = pref.getStringPref("TUMBLR_OAUTH_TOKEN_SECRET", "");

        if (token != null && token != "" && secret != null && secret != "") {
            auth = true;
            loggedin = true;
            intentToMainActivity();
        } else {
            setAuthURL();
        }
    }

    private void intentToMainActivity() {
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    private void checkUserLogin() {
        if (auth == false) {
            if (browser == true)
                browser2 = true;

            if (browser == false) {
                browser = true;
                Toast.makeText(context, "Please login your Tumblr account & allow credentials", Toast.LENGTH_LONG).show();
                newIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authURL));
                startActivity(newIntent);
            }

            if (browser2 == true) {
                Uri uri = getIntent().getData();
                if (uri != null) {
                    uripath = uri.toString();
                    if (uri != null && uripath.startsWith(OAUTH_CALLBACK_URL)) {
                        String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
                        try {

                            provider.retrieveAccessToken(consumer, verifier);

                            token = consumer.getToken();
                            secret = consumer.getTokenSecret();
                            pref.setStringPref(Config.TUMBLR_OAUTH_TOKEN, token);
                            pref.setStringPref(Config.TUMBLR_OAUTH_TOKEN_SECRET, secret);
                            pref.commit();
                            auth = true;
                            loggedin = true;
                            Toast.makeText(context, "Login success", Toast.LENGTH_LONG).show();
                            SPLASH_TIME_OUT = 1000;
                            intentToMainActivity();
                        } catch (OAuthMessageSignerException e) {
                            e.printStackTrace();
                        } catch (OAuthNotAuthorizedException e) {
                            e.printStackTrace();
                        } catch (OAuthExpectationFailedException e) {
                            e.printStackTrace();
                        } catch (OAuthCommunicationException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    browser = false;
                    someThingWentWrong();
                }
            }
        }
    }

    private void someThingWentWrong() {
        Toast.makeText(context, "Something went wrong, try again later", Toast.LENGTH_LONG).show();
        finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        checkUserLogin();
    }

    //Grabs the authorization URL from OAUTH and sets it to the String authURL member
    private void setAuthURL() {
        if ((token == null || token == "") && (secret == null || secret == "") && auth == false && browser == false) {
            try {
                authURL = provider.retrieveRequestToken(consumer, OAUTH_CALLBACK_URL);
            } catch (OAuthMessageSignerException e) {
                e.printStackTrace();
            } catch (OAuthNotAuthorizedException e) {
                e.printStackTrace();
            } catch (OAuthExpectationFailedException e) {
                e.printStackTrace();
            } catch (OAuthCommunicationException e) {
                e.printStackTrace();
            }
        }
    }
}