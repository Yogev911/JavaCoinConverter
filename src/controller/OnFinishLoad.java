package controller;


public interface OnFinishLoad {
    void onFinishLoad(String[] currencyNames, String[] currencyCodes);
    void onUpdateView(String[] currencyNames, String[] currencyCodes);
}
