package skarnulis.tomas.one.version.payseraapp.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ScaleInAnimation;
import com.easyandroidanimations.library.ScaleOutAnimation;
import com.google.gson.Gson;

import skarnulis.tomas.one.version.payseraapp.Models.MainDataObject;
import skarnulis.tomas.one.version.payseraapp.ProjectMethods;
import skarnulis.tomas.one.version.payseraapp.R;

public class StartActivity extends AppCompatActivity {

    private boolean firstStart;
    private double eur = 1000.0;
    private double usd = 0.0;
    private double jpy = 0.0;
    private double commissionsTax = 0.007;
    private int freeCommissionsAmount = 5;
    private long ANIMATION_DURATION = 250;

    private String SP_KEY, SP_FIRST_START_KEY, SP_MAIN_DATA_OBJECT_KEY, SP_COMMISSION_OBJECT_KEY;
    private String EUR = " EUR", USD = " USD", JPY = " JPY", KEY_DEFAULT = "", EMPTY = "";

    private MainDataObject mainDataObject;

    SharedPreferences  mPrefs  ;
    SharedPreferences.Editor prefsEditor ;
    Intent intentForActions;

    private TextView eurText, usdText, jpyText;
    private Button convertButton, commissionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        init();
        setListener();
        firstTimeCheck(mainDataObject);

    }

    private void init(){
        eurText = (TextView) findViewById(R.id.tv_eur);
        usdText = (TextView) findViewById(R.id.tv_usd);
        jpyText = (TextView) findViewById(R.id.tv_jpy);
        convertButton = (Button) findViewById(R.id.btn_convert);
        commissionsButton = (Button) findViewById(R.id.btn_commissions);

        SP_KEY = getString(R.string.SP_KEY);
        SP_FIRST_START_KEY = getString(R.string.SP_FIRST_START_KEY);
        SP_MAIN_DATA_OBJECT_KEY = getString(R.string.SP_MAIN_DATA_OBJECT_KEY);
        SP_COMMISSION_OBJECT_KEY = getString(R.string.SP_COMMISSION_OBJECT_KEY);

        mPrefs = getSharedPreferences(SP_KEY, MODE_PRIVATE);
        firstStart = mPrefs.getBoolean(SP_FIRST_START_KEY, true);
        prefsEditor = mPrefs.edit();
        mainDataObject = new MainDataObject(freeCommissionsAmount,commissionsTax,eur,usd,jpy);
    }


    private void firstTimeCheck(MainDataObject mainDataObject){
        if (firstStart) {
            commissionsButton.setVisibility(View.INVISIBLE);
            saveObject(mainDataObject);
            setTextViews(mainDataObject.getEUR(),
                    mainDataObject.getUSD(),
                    mainDataObject.getJPY());

            prefsEditor.putBoolean(SP_FIRST_START_KEY, false);
            prefsEditor.apply();

        } else {
            if(getCommissionsJson() != EMPTY && getCommissionsJson() != null){
                commissionsButton.setVisibility(View.VISIBLE);
            }
            mainDataObject = getMainDataObject();
            mainDataObject.setCommissionsTax(commissionsTax);
            setTextViews(mainDataObject.getEUR(),
                    mainDataObject.getUSD(),
                    mainDataObject.getJPY());
            saveObject(mainDataObject);
        }
    }

    private MainDataObject getMainDataObject(){
        Gson gson = new Gson();
        String json = mPrefs.getString(SP_MAIN_DATA_OBJECT_KEY, KEY_DEFAULT);
        MainDataObject mainDataObject = gson.fromJson(json, MainDataObject.class);
        return mainDataObject ;
    }

    private String getCommissionsJson(){
        return mPrefs.getString(SP_COMMISSION_OBJECT_KEY, KEY_DEFAULT) ;
    }

    private void saveObject(MainDataObject mainDataObject){
        Gson gson = new Gson();
        String json = gson.toJson(mainDataObject);
        prefsEditor.putString(SP_MAIN_DATA_OBJECT_KEY, json);
        prefsEditor.commit();
    }

    private void setTextViews(double eur, double usd, double jpy){
        String eurs = String.valueOf(ProjectMethods.roundTwoDecimals(eur)) + EUR ;
        String usds = String.valueOf(ProjectMethods.roundTwoDecimals(usd)) + USD;
        String jpys = String.valueOf(ProjectMethods.roundTwoDecimals(jpy)) + JPY;

        eurText.setText(eurs);
        usdText.setText(usds);
        jpyText.setText(jpys);
    }

    private void setListener(){
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScaleOutAnimation(convertButton).setDuration(ANIMATION_DURATION).setListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new ScaleInAnimation(convertButton).setDuration(ANIMATION_DURATION).animate();
                        intentForActions = new Intent(StartActivity.this,ConvertActivity.class);
                        startActivity(intentForActions);
                    }
                }).animate();

            }
        });

        commissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScaleOutAnimation(commissionsButton).setDuration(ANIMATION_DURATION).setListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new ScaleInAnimation(commissionsButton).setDuration(ANIMATION_DURATION).animate();
                        intentForActions = new Intent(StartActivity.this,CommissionsActivity.class);
                        startActivity(intentForActions);
                    }
                }).animate();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getCommissionsJson() != EMPTY && getCommissionsJson() != null){
            commissionsButton.setVisibility(View.VISIBLE);
        }
    }
}
