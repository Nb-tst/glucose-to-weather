# glucose-to-weather
An info and link collector where to discuss about the idea of using weather info on smartwatches to display glucose values.

1) @SaschaH42 [fake weather provider for gadgetbridge](https://github.com/NightscoutFoundation/xDrip/discussions/2125)

2) [Discussion in GadgetBridge repo](https://codeberg.org/Freeyourgadget/Gadgetbridge/issues/2949)

3) [FreeThree and some watchfaces](https://insulinclub.de/index.php?thread/33795-free-three-ein-xposed-lsposed-modul-f%C3%BCr-libre-3-aktueller-wert-am-sperrbildschir/) - discussion in German

4) [Discussion in Watchdrip repo](https://github.com/bigdigital/watchdrip/issues/6#)


# Proof of concept:
Playing with @Sascha42 's application I've managed to display glucose values to an Amazfit 5.
All I had to do was to patch two files in ist source code: GadgetBridgeAPI.java used to manage info from xDrip webserver, and FirsFragment.java which is used to decide what to send to the watch. For testing purposes I set weather values to constants
Finally I had to create an appropriate watchface. 
I used a minimalistic watchface created by netrunner.exe and added some elements from @bigdigital watchdrip project, and tweaked elements position.
As for now I used this scheme:
- Humidity  ( range 0-254 ) as current glucose value. When the value is higher a 0 is displayed.
- Weather icons as trend arrows ( there are 26 differents icons, so with proper images this value could be used for diplay hours )
- Current temperature ( range -128 to 127 ) as bg delta
- Max ( day ) temperature as last reading hour
- Min ( night ) temp as last reading minute
- Wind speed is unused. Could be used as another digit for bg, or as trend arrow so to let weather icons for another info ( eg hours ).
- UV is still not implemented by gadgetbridge api, so it is not usable for now.


# Current limitations:
- No BG alarms. This is maybe the bigger missing feature. Only standard notification could be used for xDrip.
- No graph. It won't be possible anytime given the nature of the approach.
- You cannot have weather
- Watchface has bad fonts and sometimes values ( eg HH:mm) overlaps, so it has to be tweaked
- There are probably tons of bugs which will display in the worst time

# Status of other bands:
Unknown.
With the Amazfit 5 there are no reality checks on temperature and humidity, you can send a minTemp value which is bigger than a maxTemp one, or you can set humidity to 250%. Others models needs to be tested, for those which this do not apply it would be trickier to find a working arrangement of weather values.
Mi band5 is the next natural tester, as it is very similar to amazfit5 and could even share the same wacthface. Then mi band6 and mi band 7, which in case will require a new watchface.

# Useful resources:
- [Watch faces creator for mi band 5-6](https://github.com/Johnson070/MiBand-5-watchface-editor) ( useful as it has a gui which allows to drag elements and set specific value to test the rendering )
- [Watchface creator for mi band 5-6-7](https://watchface-web-editor.vercel.app/). It can run even on locale. More stable than the first one, has a better interface to diplay and load images.
- [How to build you app from source](https://github.com/openaps/AndroidAPSdocs/blob/master/docs/EN/Installing-AndroidAPS/Building-APK.md)
