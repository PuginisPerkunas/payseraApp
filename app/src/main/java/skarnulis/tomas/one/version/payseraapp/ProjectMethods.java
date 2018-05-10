package skarnulis.tomas.one.version.payseraapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import skarnulis.tomas.one.version.payseraapp.Models.CommissionsObject;

public class ProjectMethods {

    public static double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
}
