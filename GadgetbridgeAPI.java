package de.shaase9.xdripgb_gateway;


import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.widget.Toast;

import nodomain.freeyourgadget.gadgetbridge.model.WeatherSpec;

import java.util.Calendar;

public class GadgetbridgeAPI {

    public final static String WEATHER_EXTRA="WeatherSpec";
    public final static String WEATHER_ACTION="de.kaffeemitkoffein.broadcast.WEATHERDATA";

    private WeatherSpec weatherSpec;
    private Context context;
    //   CurrentWeatherInfo weatherCard;

    public GadgetbridgeAPI(Context context){
        this.context = context;
    }

    /*
    currentConditionode
    public final static int THUNDERSTORM = 211;
    public final static int FREEZING_RAIN = 511;
    public final static int HEAVY_SHOWER_SNOW = 622;
    public final static int LIGHT_SHOWER_SNOW = 620;
    public final static int RAIN_AND_SNOW = 616;
    public final static int LIGHT_RAIN_AND_SNOW = 615;
    public final static int HEAVY_INTENSITY_SHOWER_RAIN = 522;
    public final static int EXTREME_RAIN = 504;
    public final static int LIGHT_INTENSITY_SHOWER_RAIN = 520;
    public final static int HEAVY_SNOW = 602;
    public final static int SNOW = 601;
    public final static int LIGHT_SNOW = 600;
    public final static int HEAVY_INTENSITY_DRIZZE = 302;
    public final static int DRIZZLE = 301;
    public final static int LIGHT_INTENSITY_DRIZZLE = 300;
    public final static int HEAVY_INTENSITY_RAIN = 502;
    public final static int MODERATE_RAIN = 501;
    public final static int LIGHT_RAIN = 500;
    public final static int FOG = 741;
    public final static int OVERCAST_CLOUDS = 804;
    public final static int BROKEN_CLOUDS = 803;
    public final static int SCATTERED_CLOUDS = 802;
    public final static int FEW_CLOUDS = 801;
    public final static int CLEAR_SKY = 800;
    public final static int UNKNOWN = 3200;
    */

    private void setWeatherData(){
       DataStorage MyDataStorage = new DataStorage();

        // WeatherSettings weatherSettings = new WeatherSettings(context);
 /*       if (weatherCard==null){
            weatherCard = new Weather().getCurrentWeatherInfo(context);
        } */
        //if (weatherCard!=null){
        // build the WeatherSpec instance with current weather
        weatherSpec = new WeatherSpec();
        weatherSpec.location             =  MyDataStorage.GetData(1);//"Test";//weatherCard.getCity();
        // fake timestamp for current weather if desired by user; some wearables do not accept a forecast
        // for current weather.
        //if (weatherSettings.gadgetbridge_fake_timestamp){
        weatherSpec.timestamp            = (int) (Calendar.getInstance().getTimeInMillis()/1000);
        //} else {
        //    weatherSpec.timestamp            = (int) (weatherCard.currentWeather.getTimestamp() / 1000);
        //}
            //if (weatherCard.currentWeather.hasCondition()){
                weatherSpec.currentConditionCode = MyDataStorage.GetDataInt(0);//601; // snow // WeatherCodeContract.translateToOpenWeatherCode(weatherCard.currentWeather.getCondition());
                weatherSpec.currentCondition     = MyDataStorage.GetData(2);//"317 + 7"; //WeatherCodeContract.getWeatherConditionText(context,weatherCard.currentWeather.getCondition());
          //  }
        //if (weatherCard.currentWeather.hasTemperature()){
        // weatherSpec.currentTemp          = 0;//55 + 17;  // .. immer -17 ? //weatherCard.currentWeather.getTemperatureInt();
        //}
        //if (weatherCard.currentWeather.hasRH()){
        weatherSpec.currentHumidity      = MyDataStorage.GetDataInt(2);//weatherCard.currentWeather.getRHInt();
        //}
        //if (weatherCard.currentWeather.hasMinTemperature()){
        Integer bgdelta = MyDataStorage.GetDataInt(1);
        Integer delta = 0;
        if (bgdelta >= 0) { delta = bgdelta + 17; }
        if (bgdelta < 0) { delta = 273 + bgdelta; }
        
        weatherSpec.currentTemp         = delta;//weatherSpec.currentTemp - 5;//weatherCard.currentWeather.getMinTemperatureInt();
        //}
        weatherSpec.todayMinTemp         = MyDataStorage.GetDataInt(3) + 17;
        //if (weatherCard.currentWeather.hasMaxTemperature()){
        weatherSpec.todayMaxTemp         = MyDataStorage.GetDataInt(4) + 17;//weatherSpec.currentTemp + 6; //128; //weatherCard.currentWeather.getMaxTemperatureInt();
        //}
            /*if (weatherCard.currentWeather.hasWindSpeed()){
                weatherSpec.windSpeed            = (float) weatherCard.currentWeather.getWindSpeedInKmhInt();
            }
            if (weatherCard.currentWeather.hasWindDirection()){
                weatherSpec.windDirection        = (int) weatherCard.currentWeather.getWindDirection();
            }*/

        // Try Forecast ///
        WeatherSpec.Forecast forecast = new WeatherSpec.Forecast(
                -20,
                -15,
                601,
                50);
        weatherSpec.forecasts.add(forecast);



        // END Try Forecast


        // build the forecast instance
            /*for (int i=0; i<weatherCard.forecast24hourly.size(); i++){
                // do not add and/or stop adding forecast if values are unknown
                if (!weatherCard.forecast24hourly.get(i).hasMinTemperature()||
                        (!weatherCard.forecast24hourly.get(i).hasMaxTemperature())||
                        (!weatherCard.forecast24hourly.get(i).hasCondition())||
                        (!weatherCard.forecast24hourly.get(i).hasRH())){
                    break;
                }
                WeatherSpec.Forecast forecast = new WeatherSpec.Forecast(
                        weatherCard.forecast24hourly.get(i).getMinTemperatureInt(),
                        weatherCard.forecast24hourly.get(i).getMaxTemperatureInt(),
                        WeatherCodeContract.translateToOpenWeatherCode(weatherCard.forecast24hourly.get(i).getCondition()),
                        weatherCard.forecast24hourly.get(i).getRHInt());
                weatherSpec.forecasts.add(forecast);
            }*/
            /*
            PrivateLog.log(context,Tag.GB,"Timestamp          : "+weatherSpec.timestamp);
            PrivateLog.log(context,Tag.GB,"Condition Code     : "+weatherSpec.currentConditionCode);
            PrivateLog.log(context,Tag.GB,"Condition          : "+weatherSpec.currentCondition);
            PrivateLog.log(context,Tag.GB,"Temperature current: "+weatherSpec.currentTemp);
            PrivateLog.log(context,Tag.GB,"Temperature min    : "+weatherSpec.todayMinTemp);
            PrivateLog.log(context,Tag.GB,"Temperature max    : "+weatherSpec.todayMaxTemp);
            PrivateLog.log(context,Tag.GB,"FC-Temperature max : "+weatherSpec.forecasts.get(0).minTemp);
            PrivateLog.log(context,Tag.GB,"FC-Temperature max : "+weatherSpec.forecasts.get(0).maxTemp);
            PrivateLog.log(context,Tag.GB,"FC-Condition       : "+weatherSpec.forecasts.get(0).conditionCode);
            PrivateLog.log(context,Tag.GB,"# of day forecasts : "+weatherSpec.forecasts.size());
            PrivateLog.log(context,Tag.GB,"Windspeed          : "+weatherSpec.windSpeed);
            PrivateLog.log(context,Tag.GB,"Windspeed direct.  : "+weatherSpec.windDirection);
            */
        //}
    }

    private final void sendWeatherBroadcast(){
       // WeatherSettings weatherSettings = new WeatherSettings(context);
        setWeatherData();
        if (weatherSpec!=null){
            Intent intent = new Intent();
            intent.putExtra(WEATHER_EXTRA,weatherSpec);
            // going by the docs, this requires at least api level 14
            // read the package name from the settings. Users may change the package name to
            // be able to use forks.
            //intent.setPackage(weatherSettings.gadgetbridge_packagename);
            intent.setPackage("nodomain.freeyourgadget.gadgetbridge");
            intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setAction(WEATHER_ACTION);
            context.sendBroadcast(intent);  // <<====  NEEDS FIX !!!!!!! => chrashes...
           // Toast.makeText(context,"Action done",Toast.LENGTH_SHORT).show();
   //         PrivateLog.log(context,PrivateLog.GB,PrivateLog.INFO,"Sent weather broadcast to GadgetBridge:");
   //         PrivateLog.log(context,PrivateLog.GB,PrivateLog.INFO,"+-> package name: "+weatherSettings.gadgetbridge_packagename);
        } else {
   //         PrivateLog.log(context,PrivateLog.GB,PrivateLog.ERR,"GadgetBridge could not be served because there is no weather data.");
        }
    }

    public final void sendWeatherBroadcastIfEnabled(){
        //WeatherSettings weatherSettings = new WeatherSettings(context);
        //if (weatherSettings.serve_gadgetbridge){
            sendWeatherBroadcast();
        //}
    }

    /*public final void sendWeatherBroadcastIfEnabled(CurrentWeatherInfo currentWeatherInfo){
        this.weatherCard = currentWeatherInfo;
        sendWeatherBroadcastIfEnabled();
    }*/

}


