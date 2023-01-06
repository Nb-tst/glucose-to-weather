package de.shaase9.xdripgb_gateway;

import static java.util.Date.from;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import de.shaase9.xdripgb_gateway.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public Context mycontext;




    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mycontext = getContext();
        DataStorage MyDataStorage = new DataStorage();
        super.onViewCreated(view, savedInstanceState);
        final int[] cyclecount = {0};
        // Test cyclic task
        final Runnable testcycle = new Runnable() {
            public void run() {
                // read Data from xDRIP
                InputStream myStream=getStream("http://127.0.0.1:17580/pebble");
                InputStreamReader isReader = new InputStreamReader(myStream);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str = "";
                try {
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }
                }
                catch(Exception e){
                //    Toast.makeText(getContext(), "can't read xDrip data from http://127.0.0.1:17580/pebble",Toast.LENGTH_SHORT).show();
                }

                String BG = "";
                String bgdelta = "";
                String bgTrend = "";
                String Datetime = "";
                int timedelay = 0;

                // decode JSON
                try {
                    String jsonStr = sb.toString();
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    BG = String.valueOf(jsonObject.getJSONArray("bgs").getJSONObject(0).get("sgv"));
                    bgdelta = String.valueOf(jsonObject.getJSONArray("bgs").getJSONObject(0).get("bgdelta"));
                    bgTrend = String.valueOf(jsonObject.getJSONArray("bgs").getJSONObject(0).get("trend"));
                    Datetime = String.valueOf(jsonObject.getJSONArray("bgs").getJSONObject(0).get("datetime"));
                    timedelay = (int) (Long.parseLong(Datetime) / 1000) - (int) (Calendar.getInstance().getTimeInMillis()/1000);


                    //     Toast.makeText(getContext(), BG,Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                  //  Toast.makeText(getContext(), "JSON Decode from xdrip failed",Toast.LENGTH_SHORT).show();
                }


                // end read Data xDRIP


                //cyclecount[0]++;
/*                MyDataStorage.SetData(1,String.valueOf(cyclecount[0]));
                MyDataStorage.SetDataInt(2,cyclecount[0]); // Temp
                MyDataStorage.SetDataInt(0,601); // snow
                MyDataStorage.SetDataInt(1,0); // min Temp
                MyDataStorage.SetDataInt(3,50); // max Temp
                MyDataStorage.SetData(1,"Text1b,"); // weather location
                MyDataStorage.SetData(2,"Text2"); // weather condition
*/

                //long blub;
                //blub = Long.valueOf(Datetime);
                //final Time myTime = new Time(123456789);
                final Time myTime = new Time(Long.valueOf(Datetime));
                DateFormat format = new SimpleDateFormat("HH:mm");
                //format.format(myTime.getTime());

                /*Integer BGsmall = Integer.valueOf(BG) / 10;
                if (Integer.valueOf(bgdelta) < 0) { // Blutzucker fallend
                        BGsmall = -BGsmall; // -minuszeichen hinzufügen für fallend.
                }
                */

                /*Integer BGmin = Integer.valueOf(BG) % 10;
                if (BGsmall < BGmin) {
                    BGmin = -BGmin; // minuszeichen hinzufügen, damit es immer kleiner bleibt. 89 => 8/-9
                }
                //BGmin = -12;
                */
                Integer CBG = Integer.valueOf(BG);
                if (CBG > 254 ) {CBG = 0; } //sets an upper bound as humidity could diplay up to 254
                
                /*
                Integer BGmin = Integer.valueOf(BG) / 100;
                Integer BGmax = Integer.valueOf(BG) - BGmin*100;
MyDataStorage.SetDataInt(3,minutes); // max Temp
                if (BGsmall < 10){  // wenn unter 100, dann nur die max Anzeige
                    BGmin = 0;MyDataStorage.SetDataInt(3,minutes); // max Temp
                    BGmax = Integer.valueOf(BG);

                }

                if (BGsmall < 0) { // fall fallend
                    if (Integer.valueOf(BG)< 100){ // nur bei min was anzeigen
                        BGmin = -Integer.valueOf(BG);
                        BGmax = 0;
                    }
                    BGmin = -Integer.valueOf(BG) / 10;
                    BGmax = Integer.valueOf(BG) + BGmin * 10;

                }
                */

                Integer bgdeltaInt = Integer.valueOf(bgdelta);
                Integer weatherIcon = 800; // default 800 - clear sky
                if (bgdeltaInt > 5) { weatherIcon = 500; } //moderat steigend
                if (bgdeltaInt > 10) { weatherIcon = 501; } // schnell steigend
                if (bgdeltaInt > 20) { weatherIcon = 502; } // wahnsinn steigend
                if (bgdeltaInt < -5) { weatherIcon = 600; } //moderat fallend
                if (bgdeltaInt < -10) { weatherIcon = 601; } // schnell fallend
                if (bgdeltaInt < -20) { weatherIcon = 602; } // wahnsinn fallend

                //Date date = format.parse(datetime);


                int hours = myTime.getHours();
                int minutes = myTime.getMinutes();





                MyDataStorage.SetDataInt(2,CBG); // Humidity  current BG
                MyDataStorage.SetDataInt(0,weatherIcon); // 800 clear sky
                MyDataStorage.SetDataInt(1,bgdeltaInt); // min temp - delta bg
                MyDataStorage.SetDataInt(3,minutes); // max Temp
                MyDataStorage.SetDataInt(4,hours); // max Temp
                MyDataStorage.SetData(1,format.format(myTime.getTime())); // weather location
                MyDataStorage.SetData(2,BG + " " + bgdelta +  "(" +String.valueOf(timedelay/60) + ")"); // weather condition



                // update watch
//                context = context.getApplicationContext();
  //              GadgetbridgeAPI gadgetbridgeAPI = new GadgetbridgeAPI(context);
    //            gadgetbridgeAPI.sendWeatherBroadcastIfEnabled();
                GadgetbridgeAPI gadgetbridgeAPI = new GadgetbridgeAPI(mycontext);
                gadgetbridgeAPI.sendWeatherBroadcastIfEnabled();
    //            Toast.makeText(mycontext, "Cycle TEst",Toast.LENGTH_SHORT).show();

            }
        };
        scheduler.scheduleAtFixedRate(testcycle, 20, 20, TimeUnit.SECONDS); // alle 2 sec ausführen
        Toast.makeText(getContext(), "Los gehts",Toast.LENGTH_SHORT).show();
        // end cycle test

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //     Toast.makeText(getContext(),"geklickt.... NexT",Toast.LENGTH_SHORT).show();
                //String action="blabla";
                InputStream myStream=getStream("http://127.0.0.1:17580/pebble");
                //InputStream myStream=getStream("http://127.0.0.1:17580/sgv.json");
                //InputStream myStream=getStream("http://192.168.0.44");
                //String mystreamtoString = new String(myStream.toString());

                InputStreamReader isReader = new InputStreamReader(myStream);
                //Creating a BufferedReader object
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str = "";
                try {
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }
                }
                catch(Exception e){
                    Toast.makeText(getContext(), "can't read xDrip data from http://127.0.0.1:17580/pebble",Toast.LENGTH_SHORT).show();
                }

                String BG = "";
                String bgdelta = "";
                String bgTrend = "";
                String Datetime = "";
                int timedelay = 0;

                // decode JSON
                try {
                    String jsonStr = sb.toString();
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    BG = String.valueOf(jsonObject.getJSONArray("bgs").getJSONObject(0).get("sgv"));
                    bgdelta = String.valueOf(jsonObject.getJSONArray("bgs").getJSONObject(0).get("bgdelta"));
                    bgTrend = String.valueOf(jsonObject.getJSONArray("bgs").getJSONObject(0).get("trend"));
                    Datetime = String.valueOf(jsonObject.getJSONArray("bgs").getJSONObject(0).get("datetime"));
                    timedelay = (int) (Long.parseLong(Datetime) / 1000) - (int) (Calendar.getInstance().getTimeInMillis()/1000);


               //     Toast.makeText(getContext(), BG,Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(getContext(), "JSON Decode from xdrip failed",Toast.LENGTH_SHORT).show();
                }


                Toast.makeText(getContext(), BG + " " + bgdelta + " age: " + timedelay,Toast.LENGTH_SHORT).show();

                //NavHostFragment.findNavController(FirstFragment.this)
                //        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

   // JSON READ TEST
private void test() {
    //Response resp = new ObjectMapper().readValue(new URL("http://dot.com/api/?customerId=1234").openStream(),Response.class);
}
    private InputStream getStream(String url1) {
        // disable strict mode do allow netwok in main thread => issue non responsive app
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        try {
            URL url = new URL(url1);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(1000);
            return urlConnection.getInputStream();
        } catch (Exception ex) {
            return null;
        }
    }



   // JSON READ TEST END


}
