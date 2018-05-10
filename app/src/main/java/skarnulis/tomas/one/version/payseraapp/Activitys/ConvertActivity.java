package skarnulis.tomas.one.version.payseraapp.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import skarnulis.tomas.one.version.payseraapp.Models.CommissionsObject;
import skarnulis.tomas.one.version.payseraapp.Models.ConvertResponseObject;
import skarnulis.tomas.one.version.payseraapp.Models.MainDataObject;
import skarnulis.tomas.one.version.payseraapp.ProjectMethods;
import skarnulis.tomas.one.version.payseraapp.R;

public class ConvertActivity extends AppCompatActivity {

    private MainDataObject mainDataObject;
    private CommissionsObject commissionsObject;
    private List<CommissionsObject> commissionsObjectList;
    private Button convertButton;
    private EditText amountInput;
    private Spinner convertFrom, convertTo;
    private RelativeLayout convertActivityLay;

    private double moneyAmount, commissionsPrice;

    private String SP_KEY, SP_MAIN_DATA_OBJECT_KEY, SP_COMMISSION_OBJECT_KEY;
    private String EUR = "EUR", USD = "USD", JPY = "JPY", KEY_DEFAULT = "", EMPTY = "", SPACE = " ";

    private int zeroCommissionsLeft = 0;

    Intent intentForActions;
    SharedPreferences mPrefs  ;
    SharedPreferences.Editor prefsEditor ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);
        init();
        mainDataObject = getObject();

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountInput.getText().toString().equals(EMPTY) ||
                        Double.valueOf(amountInput.getText().toString()) <= 0){
                    Snackbar.make(convertActivityLay,getString(R.string.wrong_value),Snackbar.LENGTH_SHORT).show();
                }else{
                    moneyAmount = Double.valueOf(amountInput.getText().toString());
                    if (returnConvertFrom() != returnConvertTo()) {
                        if (checkIfEnoughMoney(returnConvertFrom(), moneyAmount)) {
                            showCommissionDialog(moneyAmount);
                        } else {
                            showNotEnoughBalanceDialog();
                        }
                    } else {
                        Snackbar.make(convertActivityLay,getString(R.string.text_select_different_curr),Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void init(){
        convertButton = (Button) findViewById(R.id.btn_convert);
        convertFrom = (Spinner) findViewById(R.id.sp_convert_from);
        convertTo = (Spinner) findViewById(R.id.sp_convert_to);
        amountInput = (EditText) findViewById(R.id.et_amount_input);
        convertActivityLay = (RelativeLayout) findViewById(R.id.convert_activity_lay);

        SP_KEY = getString(R.string.SP_KEY);
        SP_MAIN_DATA_OBJECT_KEY = getString(R.string.SP_MAIN_DATA_OBJECT_KEY);
        SP_COMMISSION_OBJECT_KEY = getString(R.string.SP_COMMISSION_OBJECT_KEY);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        convertFrom.setAdapter(adapter);
        convertTo.setAdapter(adapter);

        mPrefs = getSharedPreferences(SP_KEY, MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
    }

    private void makeMoneyConversation(final String fromCurrency, String toCurrency, final double amountMoney){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.CONVERT_LINK) + String.valueOf(amountMoney)+ "-" + fromCurrency + "/" + toCurrency + "/latest";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                ConvertResponseObject obj = mGson.fromJson(response, ConvertResponseObject.class);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateAndTime = sdf.format(new Date());

                subtractConvertibleAmount(amountMoney,fromCurrency);
                commissionsObject = new CommissionsObject(currentDateAndTime,returnConvertFrom(),amountMoney,
                        returnConvertTo(),obj.getAmount(),commissionsPrice);
                setConvertedCurrency(obj.getAmount(),obj.getCurrency());
                saveNewMainDataObject();
                saveCommissionsInfo(commissionsObject);
                showOperationSuccessDialog(amountMoney,obj.getAmount(),fromCurrency,obj.getCurrency());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new AlertDialog.Builder(ConvertActivity.this)
                        .setTitle(getString(R.string.text_mistake))

                        .setMessage(getString(R.string.text_mistake_explain))

                        .setNeutralButton(getString(R.string.text_dismiss), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });
        queue.add(stringRequest);
    }

    private void showOperationSuccessDialog(double moneyAmountBefore, double moneyAmountAfter, String currBefore, String currAfter){
        new AlertDialog.Builder(ConvertActivity.this)
                .setTitle(getString(R.string.text_operation_success))

                .setMessage(ProjectMethods.roundTwoDecimals(moneyAmountBefore)
                        + SPACE
                        + currBefore
                        + SPACE
                        + getString(R.string.text_succ_converted_to)
                        + SPACE
                        + ProjectMethods.roundTwoDecimals(moneyAmountAfter)
                        + SPACE
                        + currAfter)

                .setNegativeButton(getString(R.string.text_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        amountInput.setText(EMPTY);
                        dialog.dismiss();
                    }
                })

                .setPositiveButton(getString(R.string.text_balance), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intentForActions = new Intent(ConvertActivity.this,StartActivity.class);
                        startActivity(intentForActions);
                        finish();
                    }
                }).create().show();
    }

    private void showNotEnoughBalanceDialog(){
        new AlertDialog.Builder(ConvertActivity.this)
                .setTitle(getString(R.string.text_not_enough))

                .setMessage(getString(R.string.text_in_your_balance)
                        + SPACE
                        + returnHowMuchMoneyInBalance(returnConvertFrom())
                        + SPACE
                        + returnConvertFrom())

                .setNeutralButton(getString(R.string.text_try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void showCommissionDialog(final double amountMoney){
        if (mainDataObject.getFreeCommissionsAmount() > zeroCommissionsLeft) {
            new AlertDialog.Builder(ConvertActivity.this)
                    .setTitle(getString(R.string.text_convert)
                            + SPACE
                            + String.valueOf(ProjectMethods.roundTwoDecimals(amountMoney))
                            + SPACE
                            + returnConvertFrom()
                            + SPACE
                            + getString(R.string.text_to)
                            + SPACE
                            + returnConvertTo())

                    .setMessage(getString(R.string.text_commissions_free_part_one)
                            + SPACE
                            + mainDataObject.getFreeCommissionsAmount()
                            + SPACE
                            + getString(R.string.text_commissions_free_part_two) )

                    .setPositiveButton(getString(R.string.text_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int freeCommissions = mainDataObject.getFreeCommissionsAmount();
                            freeCommissions--;
                            mainDataObject.setFreeCommissionsAmount(freeCommissions);
                            makeMoneyConversation(returnConvertFrom(),returnConvertTo(),amountMoney);
                            commissionsPrice = 0.00f;
                        }
                    })

                    .setNeutralButton(getString(R.string.text_delcine), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            new AlertDialog.Builder(ConvertActivity.this)
                    .setTitle(getString(R.string.text_convert)
                            + SPACE
                            + String.valueOf(ProjectMethods.roundTwoDecimals(amountMoney))
                            + SPACE
                            + returnConvertFrom()
                            + SPACE
                            + getString(R.string.text_to)
                            + SPACE
                            + returnConvertTo())

                    .setMessage(getString(R.string.text_commissions_coast)
                            + SPACE
                            + getCommissionsPrice(ProjectMethods.roundTwoDecimals(amountMoney))
                            + SPACE
                            + returnConvertFrom())

                    .setPositiveButton(getString(R.string.text_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            commissionsPrice = Float.valueOf(String.valueOf(getCommissionsPrice(amountMoney)));
                            subtractConvertibleAmount(commissionsPrice,returnConvertFrom());
                            makeMoneyConversation(returnConvertFrom(),returnConvertTo(),amountMoney);

                        }
                    })

                    .setNeutralButton(getString(R.string.text_delcine), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
    }

    private void saveCommissionsInfo(CommissionsObject commissionsObject){
        commissionsObjectList = getCommissionsObjectList();
        commissionsObjectList.add(commissionsObject);
        Gson gson = new Gson();
        String json = gson.toJson(commissionsObjectList);
        prefsEditor.putString(SP_COMMISSION_OBJECT_KEY,json);
        prefsEditor.commit();
    }

    private void saveNewMainDataObject(){
        Gson gson = new Gson();
        String json = gson.toJson(mainDataObject);
        prefsEditor.putString(SP_MAIN_DATA_OBJECT_KEY, json);
        prefsEditor.commit();
    }

    private double getCommissionsPrice(double amountMoney){
        return ProjectMethods.roundTwoDecimals(amountMoney * mainDataObject.getCommissionsTax());
    }

    public MainDataObject getObject(){
        Gson gson = new Gson();
        String json = mPrefs.getString(SP_MAIN_DATA_OBJECT_KEY, KEY_DEFAULT);
        MainDataObject obj = gson.fromJson(json, MainDataObject.class);
        return obj ;
    }

    public List<CommissionsObject> getCommissionsObjectList(){
        Gson gson = new Gson();
        String json = mPrefs.getString(SP_COMMISSION_OBJECT_KEY, KEY_DEFAULT);

        if(json == EMPTY)
        {
            List<CommissionsObject> posts2 = new ArrayList<CommissionsObject> ();
            return posts2;
        } else {
            List<CommissionsObject> posts2 = new ArrayList<CommissionsObject> (Arrays.asList(gson.fromJson(json, CommissionsObject[].class)));
            return posts2;
        }
    }

    private String returnConvertFrom(){
        String convertFromString;
        convertFromString = convertFrom.getSelectedItem().toString();
        return convertFromString;
    }

    private String returnConvertTo(){
        String convertToString;
        convertToString = convertTo.getSelectedItem().toString();
        return convertToString;
    }

    private String returnHowMuchMoneyInBalance(String currency){
        if(currency.equals(EUR)){
            return String.valueOf(ProjectMethods.roundTwoDecimals(mainDataObject.getEUR()));
        } else if (currency.equals(USD)){
            return String.valueOf(ProjectMethods.roundTwoDecimals(mainDataObject.getUSD()));
        } else if (currency.equals(JPY)){
            return String.valueOf(ProjectMethods.roundTwoDecimals(mainDataObject.getJPY()));
        } else {
            return getString(R.string.text_convert_not_available);
        }
    }

    private boolean checkIfEnoughMoney(String currency, double amountToConvert ){
        double balanceAmount;
        if(currency.equals(EUR)){
            balanceAmount = mainDataObject.getEUR();
        } else if (currency.equals(USD)){
            balanceAmount = mainDataObject.getUSD();
        } else if (currency.equals(JPY)){
            balanceAmount = mainDataObject.getJPY();
        } else {
            return false;
        }
        if (amountToConvert <= balanceAmount && amountToConvert != 0){
            return true;
        } else {
            return false;
        }
    }

    private void subtractConvertibleAmount(double convertibleAmount, String convertibleCurrency){
        double newAmount;
        if(convertibleCurrency.equals(EUR)){
            newAmount = mainDataObject.getEUR() - convertibleAmount;
            mainDataObject.setEUR(newAmount);
        } else if (convertibleCurrency.equals(USD)){
            newAmount = mainDataObject.getUSD() - convertibleAmount;
            mainDataObject.setUSD(newAmount);
        } else if (convertibleCurrency.equals(JPY)){
            newAmount = mainDataObject.getJPY() - convertibleAmount;
            mainDataObject.setJPY(newAmount);
        }
    }

    private void setConvertedCurrency(double amount, String currency){
        double newAmount;
        if(currency.equals(EUR)){
            newAmount = mainDataObject.getEUR() + amount;
            mainDataObject.setEUR(newAmount);
        } else if (currency.equals(USD)){
            newAmount = mainDataObject.getUSD() + amount;
            mainDataObject.setUSD(newAmount);
        } else if (currency.equals(JPY)){
            newAmount = mainDataObject.getJPY() + amount;
            mainDataObject.setJPY(newAmount);
        }
    }
}
