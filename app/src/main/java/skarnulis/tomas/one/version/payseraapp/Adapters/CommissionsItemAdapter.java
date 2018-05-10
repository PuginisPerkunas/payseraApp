package skarnulis.tomas.one.version.payseraapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import skarnulis.tomas.one.version.payseraapp.Holders.CommissionsItemHolder;
import skarnulis.tomas.one.version.payseraapp.Models.CommissionsObject;
import skarnulis.tomas.one.version.payseraapp.ProjectMethods;
import skarnulis.tomas.one.version.payseraapp.R;

public class CommissionsItemAdapter extends RecyclerView.Adapter<CommissionsItemHolder> {

    private List<CommissionsObject> itemsList;
    private Context context;
    private View layoutView;

    String dateText;
    String fromText;
    String toText;
    String commissionsText;
    String SPACE = " ";

    public CommissionsItemAdapter( Context context ,List<CommissionsObject> itemsList) {
        this.itemsList = itemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommissionsItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutView = LayoutInflater.from(context).inflate(R.layout.commissions_item,null);
        CommissionsItemHolder commissionsItemHolder = new CommissionsItemHolder(layoutView);
        return commissionsItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommissionsItemHolder holder, int position) {

        dateText = itemsList.get(position).getConversionDate();

        fromText = context.getString(R.string.text_converted)
                + SPACE
                + String.valueOf(itemsList.get(position).getConversionFromAmount())
                + SPACE
                + itemsList.get(position).getConversionFromCurr();

        toText = context.getString(R.string.text_get)
                + SPACE
                + String.valueOf(itemsList.get(position).getConversionToAmount())
                + SPACE
                + itemsList.get(position).getConversionToCurr();

        commissionsText = context.getString(R.string.text_commissions_price)
                + SPACE
                + String.valueOf(ProjectMethods.roundTwoDecimals(itemsList.get(position).getCommissionsPrice()))
                + SPACE
                + itemsList.get(position).getConversionFromCurr()
                + SPACE;

        holder.operationDate.setText(dateText);
        holder.convertedFrom.setText(fromText);
        holder.convertedTo.setText(toText);
        holder.commissionsPrice.setText(commissionsText);

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
