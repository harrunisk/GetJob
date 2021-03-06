package com.example.harun.getjob.SignInUp;


import android.content.Intent;
import android.util.Log;

import com.daimajia.androidanimations.library.Techniques;
import com.example.harun.getjob.R;
import com.example.harun.getjob.UserIntro;
import com.google.firebase.auth.FirebaseAuth;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;


//extends AwesomeSplash!
public class Intro extends AwesomeSplash {
    private FirebaseAuth auth;

    //Bu ıd bana heryerde lazım olacak oyuzden static yaptım .
    public static String userID;

    private static final String TAG = "Intro";


    public static final String DROID_LOGO = "M 31.00,53.70\n" +
            "           C 31.00,53.70 64.00,67.28 64.00,67.28\n" +
            "             72.46,70.89 70.68,71.21 75.09,73.46\n" +
            "             75.09,73.46 94.00,80.68 94.00,80.68\n" +
            "             105.20,84.93 101.65,87.07 106.22,89.57\n" +
            "             108.75,90.95 119.76,91.96 123.00,92.00\n" +
            "             123.00,92.00 134.00,92.00 134.00,92.00\n" +
            "             134.00,92.00 156.00,93.00 156.00,93.00\n" +
            "             168.08,92.84 168.54,85.88 181.00,88.90\n" +
            "             181.00,88.90 209.00,99.67 209.00,99.67\n" +
            "             212.28,100.77 217.66,102.97 221.00,102.52\n" +
            "             227.48,101.64 242.03,90.86 249.00,87.26\n" +
            "             249.00,87.26 305.00,58.75 305.00,58.75\n" +
            "             305.00,58.75 333.00,45.00 333.00,45.00\n" +
            "             333.00,45.00 333.00,174.00 333.00,174.00\n" +
            "             333.00,176.18 333.23,179.68 331.98,181.51\n" +
            "             330.52,183.63 318.09,189.19 315.00,190.76\n" +
            "             311.18,192.70 299.05,200.34 296.00,200.62\n" +
            "             289.03,201.25 278.99,187.99 277.00,182.00\n" +
            "             269.99,183.58 264.35,185.62 257.00,184.57\n" +
            "             254.57,184.23 251.18,183.73 249.00,182.69\n" +
            "             246.25,181.38 244.41,179.15 242.00,178.93\n" +
            "             240.46,178.79 236.74,180.45 235.00,181.00\n" +
            "             236.14,187.49 236.12,193.90 229.91,197.99\n" +
            "             227.14,199.82 224.48,199.75 222.56,201.58\n" +
            "             220.51,203.54 220.60,206.49 216.89,209.35\n" +
            "             212.24,212.94 207.47,211.79 204.00,213.06\n" +
            "             200.93,214.18 198.08,217.12 194.00,218.35\n" +
            "             194.00,218.35 184.00,219.69 184.00,219.69\n" +
            "             180.55,220.80 179.24,222.55 175.00,222.88\n" +
            "             169.74,223.28 166.42,220.65 163.00,220.31\n" +
            "             159.00,219.92 144.41,228.77 138.45,218.79\n" +
            "             137.50,217.20 137.34,215.75 137.00,214.00\n" +
            "             128.77,213.81 122.89,211.88 121.00,203.00\n" +
            "             113.72,202.99 109.12,202.90 104.74,196.00\n" +
            "             103.01,193.28 103.12,190.94 101.99,189.31\n" +
            "             99.68,185.98 95.59,187.61 92.01,181.96\n" +
            "             88.55,176.50 91.03,171.36 89.69,168.09\n" +
            "             88.48,165.14 80.75,159.26 78.00,157.00\n" +
            "             76.67,160.59 74.84,164.80 70.96,166.25\n" +
            "             67.50,167.54 64.14,165.72 61.00,164.42\n" +
            "             61.00,164.42 42.00,157.00 42.00,157.00\n" +
            "             40.27,163.06 35.13,172.49 31.80,178.00\n" +
            "             30.41,180.29 27.95,184.65 25.68,185.97\n" +
            "             21.48,188.42 11.41,182.80 7.00,181.00\n" +
            "             5.09,180.22 2.22,179.25 1.02,177.50\n" +
            "             -0.23,175.68 0.00,172.17 0.00,170.00\n" +
            "             0.00,170.00 0.00,40.00 0.00,40.00\n" +
            "             0.00,40.00 31.00,53.70 31.00,53.70 Z\n" +
            "           M 62.00,160.40\n" +
            "           C 64.27,161.29 67.40,162.83 69.82,161.79\n" +
            "             73.19,160.35 77.05,151.43 78.75,148.00\n" +
            "             84.49,136.45 90.84,118.46 95.00,106.00\n" +
            "             96.31,102.07 100.93,91.80 98.94,88.27\n" +
            "             97.69,86.07 94.25,84.91 92.00,84.00\n" +
            "             86.14,81.63 79.23,78.96 73.00,78.00\n" +
            "             72.30,86.38 68.77,94.21 65.81,102.00\n" +
            "             65.81,102.00 52.00,136.00 52.00,136.00\n" +
            "             49.48,141.89 46.19,147.66 45.00,154.00\n" +
            "             45.00,154.00 62.00,160.40 62.00,160.40 Z\n" +
            "           M 222.70,170.83\n" +
            "           C 224.38,172.60 228.05,177.05 230.09,177.85\n" +
            "             234.24,179.48 242.78,173.03 243.65,168.96\n" +
            "             244.36,165.65 241.09,155.62 239.98,152.00\n" +
            "             235.85,138.58 231.72,123.77 223.76,112.02\n" +
            "             219.03,105.04 208.56,103.72 201.00,100.95\n" +
            "             194.29,98.49 181.22,91.58 175.00,92.20\n" +
            "             175.00,92.20 150.00,100.58 150.00,100.58\n" +
            "             146.15,101.71 141.31,102.74 138.85,106.21\n" +
            "             136.64,109.34 136.04,119.02 136.00,123.00\n" +
            "             144.68,121.11 148.29,126.59 143.49,132.95\n" +
            "             139.96,137.62 136.67,137.00 132.00,139.00\n" +
            "             145.77,145.38 153.32,131.87 158.00,121.00\n" +
            "             163.22,122.15 168.84,121.26 174.00,120.00\n" +
            "             175.30,135.96 185.45,147.32 201.00,151.10\n" +
            "             206.34,152.40 212.48,152.00 218.00,152.00\n" +
            "             218.00,152.00 209.00,156.00 209.00,156.00\n" +
            "             212.93,161.49 218.07,165.94 222.70,170.83 Z\n" +
            "           M 125.00,96.00\n" +
            "           C 125.00,96.00 103.00,94.00 103.00,94.00\n" +
            "             101.36,104.62 92.98,126.66 88.57,137.00\n" +
            "             87.10,140.44 81.50,150.82 82.41,154.00\n" +
            "             83.56,157.98 91.45,162.96 95.00,165.00\n" +
            "             97.80,163.00 101.39,160.40 105.00,160.42\n" +
            "             112.82,160.46 116.60,168.16 116.00,175.00\n" +
            "             125.82,173.92 129.76,175.70 134.00,185.00\n" +
            "             140.37,184.18 146.27,183.83 149.34,191.02\n" +
            "             150.38,193.44 149.94,195.67 151.07,197.55\n" +
            "             152.28,199.55 155.09,200.67 156.83,204.02\n" +
            "             158.51,207.28 157.48,210.21 159.02,212.61\n" +
            "             162.28,217.70 172.64,219.25 178.00,218.00\n" +
            "             178.00,218.00 178.00,216.00 178.00,216.00\n" +
            "             168.93,210.78 160.17,204.61 154.00,196.00\n" +
            "             165.29,204.07 175.20,218.04 191.00,214.61\n" +
            "             193.91,213.98 196.44,212.45 199.00,211.00\n" +
            "             199.00,211.00 199.00,209.00 199.00,209.00\n" +
            "             189.40,203.60 175.95,192.29 170.00,183.00\n" +
            "             180.16,190.28 189.19,204.52 202.00,207.47\n" +
            "             207.23,208.68 219.44,207.61 216.38,199.17\n" +
            "             215.21,195.93 206.01,189.56 203.00,186.83\n" +
            "             197.20,181.59 185.74,168.16 183.00,161.00\n" +
            "             192.92,170.17 199.31,185.91 216.00,194.43\n" +
            "             224.69,198.87 233.83,191.99 230.64,185.00\n" +
            "             228.82,181.00 215.91,169.05 212.04,165.00\n" +
            "             209.36,162.21 206.28,157.94 202.99,156.16\n" +
            "             199.03,154.03 193.85,154.72 185.00,147.62\n" +
            "             176.20,140.57 173.87,134.97 170.00,125.00\n" +
            "             167.91,125.37 162.06,126.01 160.61,126.99\n" +
            "             157.05,129.39 150.18,145.72 138.00,144.88\n" +
            "             136.62,144.79 135.30,144.53 134.02,143.99\n" +
            "             122.83,139.33 130.39,128.99 131.91,121.00\n" +
            "             132.99,115.25 132.59,108.83 136.42,104.04\n" +
            "             139.72,99.91 142.89,99.54 147.00,97.00\n" +
            "             147.00,97.00 125.00,96.00 125.00,96.00 Z\n" +
            "           M 231.82,117.00\n" +
            "           C 231.82,117.00 239.95,138.00 239.95,138.00\n" +
            "             239.95,138.00 247.35,163.00 247.35,163.00\n" +
            "             248.05,166.07 249.53,177.25 251.57,178.83\n" +
            "             255.28,181.70 269.77,179.67 274.00,178.00\n" +
            "             276.50,177.01 280.06,175.96 280.50,172.87\n" +
            "             280.94,169.84 276.92,162.25 275.68,159.00\n" +
            "             269.78,143.52 261.29,130.14 252.80,116.00\n" +
            "             250.62,112.36 244.45,101.68 240.91,100.13\n" +
            "             237.32,98.56 229.26,103.20 226.00,105.00\n" +
            "             227.52,109.78 229.75,112.49 231.82,117.00 Z\n" +
            "           M 137.00,135.09\n" +
            "           C 142.00,134.12 144.37,129.11 143.33,126.46\n" +
            "             142.03,123.06 136.83,124.40 134.99,126.46\n" +
            "             134.14,127.45 133.54,128.81 133.11,130.02\n" +
            "             132.51,131.66 132.27,133.29 132.00,135.09\n" +
            "             133.88,135.30 135.04,135.60 137.00,135.09 Z\n" +
            "           M 106.95,173.37\n" +
            "           C 111.04,173.37 112.72,166.37 105.05,165.58\n" +
            "             98.32,168.26 103.85,173.37 106.95,173.37 Z\n" +
            "           M 102.00,183.24\n" +
            "           C 106.82,182.95 109.79,178.74 112.00,175.00\n" +
            "             104.05,176.32 102.79,174.30 98.00,168.00\n" +
            "             89.01,175.51 96.94,183.56 102.00,183.24 Z\n" +
            "           M 118.42,186.89\n" +
            "           C 119.11,187.71 120.08,188.63 121.02,189.12\n" +
            "             129.05,193.20 131.22,177.22 121.02,179.69\n" +
            "             119.21,180.12 117.60,181.13 116.00,182.00\n" +
            "             116.69,184.01 116.97,185.18 118.42,186.89 Z\n" +
            "           M 107.74,187.04\n" +
            "           C 105.50,192.84 110.84,199.68 117.00,199.26\n" +
            "             121.25,198.97 124.64,195.14 127.00,192.00\n" +
            "             119.63,192.47 115.48,187.94 114.00,181.00\n" +
            "             111.32,182.61 108.94,183.94 107.74,187.04 Z\n" +
            "           M 129.00,189.00\n" +
            "           C 129.00,189.00 130.00,190.00 130.00,190.00\n" +
            "             130.00,190.00 130.00,189.00 130.00,189.00\n" +
            "             130.00,189.00 129.00,189.00 129.00,189.00 Z\n" +
            "           M 135.34,193.11\n" +
            "           C 134.52,195.97 138.03,199.48 140.89,198.66\n" +
            "             143.67,197.88 148.37,189.78 139.99,189.54\n" +
            "             138.09,190.24 135.98,190.82 135.34,193.11 Z\n" +
            "           M 128.00,190.00\n" +
            "           C 128.00,190.00 129.00,191.00 129.00,191.00\n" +
            "             129.00,191.00 129.00,190.00 129.00,190.00\n" +
            "             129.00,190.00 128.00,190.00 128.00,190.00 Z\n" +
            "           M 127.00,191.00\n" +
            "           C 127.00,191.00 128.00,192.00 128.00,192.00\n" +
            "             128.00,192.00 128.00,191.00 128.00,191.00\n" +
            "             128.00,191.00 127.00,191.00 127.00,191.00 Z\n" +
            "           M 125.54,202.00\n" +
            "           C 124.73,207.03 130.49,210.59 134.96,209.45\n" +
            "             138.70,208.49 140.41,205.16 142.00,202.00\n" +
            "             136.68,200.44 134.35,197.96 132.00,193.00\n" +
            "             129.46,195.24 126.10,198.48 125.54,202.00 Z\n" +
            "           M 142.24,209.04\n" +
            "           C 136.85,218.20 148.93,223.68 157.00,216.00\n" +
            "             157.00,216.00 152.00,217.00 152.00,217.00\n" +
            "             152.00,217.00 154.00,210.00 154.00,210.00\n" +
            "             149.12,210.40 148.11,209.29 146.00,205.00\n" +
            "             144.38,206.39 143.37,207.13 142.24,209.04 Z";

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!


    @Override
    public void initSplash(ConfigSplash configSplash) {

			/* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.mygreen); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_launcher); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.BounceInUp); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Path
        configSplash.setPathSplash(DROID_LOGO); //set path String
        configSplash.setOriginalHeight(350); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(350); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(1000);
        configSplash.setPathSplashStrokeSize(4); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.Wheat); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(1000);

        configSplash.setPathSplashFillColor(R.color.mypurple); //path object filling color


        //Customize Title
        configSplash.setTitleSplash("GET-JOB");
        configSplash.setTitleTextColor(R.color.Wheat);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(0);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        configSplash.setTitleFont("fonts/volatire.ttf"); //provide string to your font located in assets/fonts/

    }

    /**
     * Animasyon bittiğinde Eğer Kullanıcı tanımlanabilmiş ise Direk UserIntro ya Geçiş Yapılacak
     * Eğer Kullanıcı Tanımlaması Yapılmamıs İse Login After gidip elemanmı işmi aradıgını sorup kayıt ekranlarına
     * yönlendirilmesi yapılacak .
     */
    @Override
    public void animationsFinished() {

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "Kullanici ADİ @@ \t" + auth.getCurrentUser().getEmail());

            userID = auth.getCurrentUser().getUid();
            Log.i(TAG, "onCreate: KULLANICI GİRİŞİ TANIMLANDI ");
            Log.d(TAG, "onCreate: USERID @@\t" + userID);

            // Intent intent = new Intent(getApplicationContext(), UserIntro.class);
            Intent intent = new Intent(this, UserIntro.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        } else {

            Log.d(TAG, "animationsFinished: Kullanıcı Tanımlanmadı Login Aftera yönlendiriliyor.");
            Log.i(TAG, "animationsFinished: Kullanıcı Tanımlanmadı");
            Intent Login = new Intent(this, LoginAfter.class);
            startActivity(Login);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            finish();


        }



       /* Intent Login= new Intent(this,LoginAfter.class);
        startActivity(Login);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        finish();*/
        //transit to another activity here
        //or do whatever you want
    }
}