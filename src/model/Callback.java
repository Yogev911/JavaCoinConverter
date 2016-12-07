package model;

import java.io.InputStream;


public interface Callback {
    void onSuccess(InputStream inputStream);
    void onFailed(String error);
}
