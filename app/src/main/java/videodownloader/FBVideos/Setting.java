package videodownloader.FBVideos;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import videodownloader.FBVideos.adapter.spinneradapter1;


public class Setting extends AppCompatActivity {


    String CurrentLang;
    private Spinner spinner_language;
    androidx.appcompat.widget.Toolbar mytoolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mytoolbar=findViewById(R.id.toolbarsetting);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);





        spinner_language=findViewById(R.id.languagespinner);
        List<CharSequence> list=new ArrayList<>();
        list.addAll(Arrays.asList(getResources().getStringArray(R.array.language)));

    spinneradapter1 madapter=new spinneradapter1(this,android.R.layout.simple_spinner_item,list);
       madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       spinner_language.setAdapter(madapter);

        spinner_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 1:{

                        changelanguage("ar");
                        getSharedPreferences("language",Context.MODE_PRIVATE).edit().putString("lang","ar").apply();

                        restart();




                        break;
                    }
                    case 2:{

                        changelanguage("en");
                        getSharedPreferences("language",Context.MODE_PRIVATE).edit().putString("lang","en").apply();


                        restart();



                        break;
                    }
                    case 3:{


                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CurrentLang = Locale.getDefault().getLanguage();




    }


    public void changelanguage(String localcode)
    {




        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf=res.getConfiguration();
        if (Build.VERSION.SDK_INT >= 17)
        {
            conf.setLocale(new Locale(localcode.toLowerCase()));
        }
        else{
            conf.locale=new Locale(localcode.toLowerCase());
        }
        res.updateConfiguration(conf,dm);
    }

    public void restart ()
    {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
