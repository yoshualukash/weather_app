package com.yoshualukas.weatherapp.model;
import android.os.Parcel;
import android.os.Parcelable;

public class Hour implements Parcelable{
    private String time;
    private double temp_c, wind_kph, pressure_mb, precip_mm, gust_kph;
    private int humidity, cloud;
    private Condition condition;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemp_c() {
        return temp_c;
    }

    public void setTemp_c(double temp_c) {
        this.temp_c = temp_c;
    }

    public double getWind_kph() {
        return wind_kph;
    }

    public void setWind_kph(double wind_kph) {
        this.wind_kph = wind_kph;
    }

    public double getPressure_mb() {
        return pressure_mb;
    }

    public void setPressure_mb(double pressure_mb) {
        this.pressure_mb = pressure_mb;
    }

    public double getPrecip_mm() {
        return precip_mm;
    }

    public void setPrecip_mm(double precip_mm) {
        this.precip_mm = precip_mm;
    }

    public double getGust_kph() {
        return gust_kph;
    }

    public void setGust_kph(double gust_kph) {
        this.gust_kph = gust_kph;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getCloud() {
        return cloud;
    }

    public void setCloud(int cloud) {
        this.cloud = cloud;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.condition, flags);
        dest.writeString(this.time);
        dest.writeValue(this.temp_c);
        dest.writeValue(this.wind_kph);
        dest.writeValue(this.pressure_mb);
        dest.writeValue(this.precip_mm);
        dest.writeValue(this.gust_kph);
        dest.writeInt(this.humidity);
        dest.writeInt(this.cloud);
    }

    public void readFromParcel(Parcel source) {
        this.condition = source.readParcelable(Condition.class.getClassLoader());
        this.time = source.readString();
        this.temp_c = (Double) source.readValue(Double.class.getClassLoader());
        this.wind_kph = (Double) source.readValue(Double.class.getClassLoader());
        this.pressure_mb = (Double) source.readValue(Double.class.getClassLoader());
        this.precip_mm = (Double) source.readValue(Double.class.getClassLoader());
        this.gust_kph = (Double) source.readValue(Double.class.getClassLoader());
        this.humidity = source.readInt();
        this.cloud = source.readInt();
    }

    public Hour() {
    }

    protected Hour(Parcel in) {
        this.condition = in.readParcelable(Condition.class.getClassLoader());
        this.time = in.readString();
        this.temp_c = (Double) in.readValue(Double.class.getClassLoader());
        this.wind_kph = (Double) in.readValue(Double.class.getClassLoader());
        this.pressure_mb = (Double) in.readValue(Double.class.getClassLoader());
        this.precip_mm = (Double) in.readValue(Double.class.getClassLoader());
        this.gust_kph = (Double) in.readValue(Double.class.getClassLoader());
        this.humidity = in.readInt();
        this.cloud = in.readInt();
    }

    public static final Parcelable.Creator<Hour> CREATOR = new Parcelable.Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };
}
