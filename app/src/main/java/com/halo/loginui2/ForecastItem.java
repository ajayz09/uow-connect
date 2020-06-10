package com.halo.loginui2;

public class ForecastItem {
    public String dayText;
    public String conditionText;
    public String highTemp;
    public String lowTemp;
    public String forecastImage;

    public ForecastItem() {
    }

    public ForecastItem(String dayText, String conditionText, String highTemp, String lowTemp, String forecastImage) {
        this.dayText = dayText;
        this.conditionText = conditionText;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.forecastImage = forecastImage;
    }

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }

    public String getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(String highTemp) {
        this.highTemp = highTemp;
    }

    public String getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(String lowTemp) {
        this.lowTemp = lowTemp;
    }

    public String getForecastImage() {
        return forecastImage;
    }

    public void setForecastImage(String forecastImage) {
        forecastImage = forecastImage.replace("64", "128");
        forecastImage = "http:" + forecastImage;
        this.forecastImage = forecastImage;
    }
}
