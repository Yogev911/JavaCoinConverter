package model.entities;


public class Currency {
    private String mName;
    private String mUnit;
    private String mRate;
    private String mCountry;
    private String mChange;
    private String mCurrencyCode;

    public Currency(String name, String unit, String rate,
                    String country, String change, String currencyCode){
        mName = name;
        mUnit = unit;
        mRate = rate;
        mCountry = country;
        mChange = change;
        mCurrencyCode = currencyCode;
    }
    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String mUnit) {
        this.mUnit = mUnit;
    }

    public String getRate() {
        return mRate;
    }

    public void setRate(String mRate) {
        this.mRate = mRate;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getChange() {
        return mChange;
    }

    public void setChange(String mChange) {
        this.mChange = mChange;
    }

    public String getCurrencyCode() {
        return mCurrencyCode;
    }

    public void setCurrencyCode(String mCurrencyCode) {
        this.mCurrencyCode = mCurrencyCode;
    }

}
