package model.currencyXmlRestApi;

import model.Callback;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by YogevHeskia on 17/08/2016.
 */
public class CurrencyXmlRestInvocationImpl{

    private Thread mThread;


    public void getCurrencyXml(final String url, final Callback callback) {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL mUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection)mUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    if(inputStream != null){
                        callback.onSuccess(inputStream);
                    } else {
                        callback.onFailed("No Content");
                    }
                } catch (IOException e){
                    callback.onFailed(String.format("%s - %s", "failed on exception", e.getMessage()));
                    Logger.getLogger(CurrencyXmlRestInvocationImpl.class).trace("There is no connetion to the restful url of the boi");
                }
            }
        });
        mThread.start();

    }
}
