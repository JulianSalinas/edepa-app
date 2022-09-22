package edepa.info;

import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import butterknife.BindView;
import edepa.cloud.Cloud;
import edepa.cloud.CloudPlaces;
import edepa.modelview.R;


public class InfoBanksFragment extends InfoLodgingFragment implements ValueEventListener{

    @BindView(R.id.exchange_buy)
    TextView itemExchangeBuy;

    @BindView(R.id.exchange_sell)
    TextView itemExchangeSell;

    @BindView(R.id.exchange_description)
    TextView itemExchangeDescription;

    @Override
    public int getResource() {
        return R.layout.info_places_others;
    }

    @Override
    public void connectData() {
        CloudPlaces cloud = new CloudPlaces();
        cloud.setCallbacks(this);
        cloud.connectBanks();
        Cloud.getInstance()
                .getReference("exchange")
                .addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        HashMap<String, String> data = (HashMap<String, String>) dataSnapshot.getValue();
        if (data != null) {
            if (data.containsKey("buy")){
                itemExchangeBuy.setText(data.get("buy"));
            }
            if (data.containsKey("sell")){
                itemExchangeSell.setText(data.get("sell"));
            }
            if (data.containsKey("description")){
                itemExchangeDescription.setVisibility(View.VISIBLE);
                itemExchangeDescription.setText(data.get("description"));
            }
            else {
                itemExchangeDescription.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.i(getClass().getSimpleName(), "Error retrieving exchange rate");
    }
}
