package skarnulis.tomas.one.version.payseraapp.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.easyandroidanimations.library.FadeInAnimation;

import skarnulis.tomas.one.version.payseraapp.R;

public class CommissionsItemHolder extends RecyclerView.ViewHolder {

    public TextView operationDate, convertedFrom, convertedTo, commissionsPrice;
    long animationDuration = 350;

    public CommissionsItemHolder(View itemView) {
        super(itemView);
        init(itemView);
        setAnimations(itemView);
    }

    private void init(View view){
        operationDate = view.findViewById(R.id.tv_date);
        convertedFrom = view.findViewById(R.id.tv_converted_from);
        convertedTo = view.findViewById(R.id.tv_converted_to);
        commissionsPrice = view.findViewById(R.id.tv_commissions_price);
    }

    private void setAnimations(View view){
        new FadeInAnimation(view).setDuration(animationDuration).animate();
    }

}
