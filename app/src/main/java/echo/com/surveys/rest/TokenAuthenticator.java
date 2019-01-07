package echo.com.surveys.rest;

import android.text.TextUtils;
import echo.com.surveys.SurveyApplication;
import echo.com.surveys.model.Auth;
import echo.com.surveys.model.AuthRequest;
import echo.com.surveys.util.Constants;
import echo.com.surveys.util.SharedPrefUtility;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Route;
import retrofit2.Response;

import java.io.IOException;

public class TokenAuthenticator implements Authenticator {


    @Override
    public Request authenticate(Route route, okhttp3.Response response) throws IOException {
        if (response.code() == 400 &&  response.body()!= null && TextUtils.isEmpty(response.body().toString())) {
            //make it as retrofit synchronous call
            Response<Auth> refreshResponse = ApiUtils.getAPIService(SurveyApplication.getContext()).getToken(new AuthRequest()).execute();
            if (refreshResponse != null && refreshResponse.code() == 200) {
                String newToken = updateRefreshToken(refreshResponse.body());
                String oldToken = response.request().url().queryParameter("access_token");
                String newUrl = response.request().url().toString().replace(oldToken, newToken);
                return response.request().newBuilder().url(newUrl).build();
            }
        } else {
            // some problem other then token expiry;
        }
        return null;
    }

    private String updateRefreshToken(Auth auth){
        SharedPrefUtility.getInstance(SurveyApplication.getContext()).savePrefrences(Constants.Keys.AUTH, auth);
        return auth.getAccessToken();
    }


}
