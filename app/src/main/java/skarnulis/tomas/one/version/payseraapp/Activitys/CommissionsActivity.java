package skarnulis.tomas.one.version.payseraapp.Activitys;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import skarnulis.tomas.one.version.payseraapp.Adapters.CommissionsItemAdapter;
import skarnulis.tomas.one.version.payseraapp.Models.CommissionsObject;
import skarnulis.tomas.one.version.payseraapp.ProjectMethods;
import skarnulis.tomas.one.version.payseraapp.R;

public class CommissionsActivity extends AppCompatActivity {
    private RecyclerView commissionsRV;
    private TextView totalText;
    private List<CommissionsObject> commissionsObjectList;

    SharedPreferences mPrefs  ;
    SharedPreferences.Editor prefsEditor ;

    private CommissionsItemAdapter commissionsItemAdapter;
    private LinearLayoutManager layoutManager;

    private double totalEUR = 0, totalUSD = 0, totalJPY = 0;
    private String SP_KEY, SP_COMMISSION_OBJECT_KEY;
    private String EUR = "EUR", USD = "USD", JPY = "JPY";
    private String allCommissionsCoast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commissions);

        init();
        Collections.reverse(commissionsObjectList);
        calculateCommissionsTotal(commissionsObjectList);

        commissionsItemAdapter = new CommissionsItemAdapter(CommissionsActivity.this, commissionsObjectList);
        commissionsRV.setAdapter(commissionsItemAdapter);

        allCommissionsCoast = "Viso sumokėta: " +
                ProjectMethods.roundTwoDecimals(totalEUR) + " EUR, " +
                ProjectMethods.roundTwoDecimals(totalUSD) + " USD, " +
                ProjectMethods.roundTwoDecimals(totalJPY) + " JPY" ;

        totalText.setText(allCommissionsCoast);
    }

    private void init(){
        commissionsRV = (RecyclerView) findViewById(R.id.recyclerViewCommsisions);
        totalText = (TextView) findViewById(R.id.commissions_total);
        layoutManager = new LinearLayoutManager(CommissionsActivity.this);
        commissionsRV.setLayoutManager(layoutManager);
        SP_KEY = getString(R.string.SP_KEY);
        SP_COMMISSION_OBJECT_KEY = getString(R.string.SP_COMMISSION_OBJECT_KEY);
        mPrefs = getSharedPreferences(SP_KEY, MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        commissionsObjectList = getCommissionsObjectList();

    }

    private List<CommissionsObject> getCommissionsObjectList(){
        Gson gson = new Gson();
        String json = mPrefs.getString(SP_COMMISSION_OBJECT_KEY, "");
        if(json == null || json == "")
        {
            List<CommissionsObject> commissionsObjects = new ArrayList<CommissionsObject>();
            return commissionsObjects;

        } else {
            List<CommissionsObject> commissionsObjects = new ArrayList<CommissionsObject> (Arrays.asList(gson.fromJson(json, CommissionsObject[].class)));
            return commissionsObjects;
        }
    }

    private void calculateCommissionsTotal(List<CommissionsObject> list){
        for (CommissionsObject item: list) {
            String currency;
            currency = item.getConversionFromCurr();

            if(currency.equals(EUR)){
                totalEUR = totalEUR + item.getCommissionsPrice();
            } else if(currency.equals(USD)){
                totalUSD = totalUSD + item.getCommissionsPrice();
            } else if(currency.equals(JPY)){
                totalJPY = totalJPY + item.getCommissionsPrice();
            }
        }
    }
}
