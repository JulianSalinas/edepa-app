package imagisoft.modelview;

import android.content.res.ColorStateList;
import android.support.v4.widget.ImageViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import imagisoft.misc.DateCalculator;
import imagisoft.model.Message;
import imagisoft.model.ViewedList;
import imagisoft.modelview.activity.ActivityFragment;

/**
 * Sirve para enlazar las funciones a una actividad en específico
 */
public class NewsAdapter extends MessagesAdapterOnline {

    private int NEWS_ITEM = 1;
    private int NEWS_ITEM_WITH_SEP = 0;

    /**
     * Constructor del adaptador usado para recibir mensajes online
     */
    public NewsAdapter(ActivityFragment view) {
        super(view);
    }

    @Override
    protected void setupReference() {
        this.reference = view.getActivityCustom().getNewsReference();
    }

    @Override
    protected String setTimeDescription(Message msg){
        DateCalculator dateCalculator = new DateCalculator(view.getActivityCustom());
        return dateCalculator.getTimeAgo(msg.getTime());
    }

    /**
     * Regresa un item donde sin fecha porque el item de arriba
     * que es del mismo día ya tiene uno
     * @param msg: Message
     */
    @Override
    protected int getItemWithoutSeparator(Message msg){
        return NEWS_ITEM;
    }

    /**
     * El item necesita indicar el dia usando un separador
     * @param msg: Message
     */
    protected int getItemWithSeparator(Message msg){
        return NEWS_ITEM;
    }

    /**
     * Crear la vista de la noticia o el separador según corresponda
     */
    @Override
    public MessageVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new NewVH(view);
    }

    @Override
    public void onBindViewHolder(MessageVH holder, int position) {

        super.onBindViewHolder(holder, position);
        ViewedList viewedList = ViewedList.getInstance();
        Message msg = msgs.get(holder.getAdapterPosition());

//        String textReadAmount = String.valueOf(msg.getSeenAmount()) + " " + view.getString(R.string.text_viewed);
//        ((NewVH) holder).msgReadAmount.setText(textReadAmount);

        ((NewVH) holder).msgReadCheck.setOnClickListener(v -> {

            if(!viewedList.isRead(msg)){
                viewedList.markAsRead(msg);

                int color = view.getResources().getColor(R.color.material_light_blue);
                ImageViewCompat.setImageTintList(((NewVH) holder).msgReadCheck, ColorStateList.valueOf(color));
                ((NewVH) holder).msgReadCheckText.setText(view.getResources().getString(R.string.text_single_viewed));

                view.getActivityCustom().
                        getNewsReference()
                        .child(msg.getKey())
                        .runTransaction(new Transaction.Handler() {

                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                Message msg1 = mutableData.getValue(Message.class);

                                if (msg1 == null)
                                    return Transaction.success(mutableData);
                                else {
//                                    msg1.setSeenAmount(msg1.getSeenAmount() + 1);
                                }

                                mutableData.setValue(msg1);
//                                Log.i("NewsAdapter::", "seenAmountUpdated::" + String.valueOf(msg1.getSeenAmount()));
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                Log.d("NewsAdapter", "postTransaction:onComplete:" + databaseError);
                            }
                        });

            }
            else {
                viewedList.unmarkAsRead(msg);

                int color = view.getResources().getColor(R.color.material_grey);
                ImageViewCompat.setImageTintList(((NewVH) holder).msgReadCheck, ColorStateList.valueOf(color));
                ((NewVH) holder).msgReadCheckText.setText(view.getResources().getString(R.string.text_not_viewed));

                view.getActivityCustom().
                        getNewsReference()
                        .child(msg.getKey())
                        .runTransaction(new Transaction.Handler() {

                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                Message msg1 = mutableData.getValue(Message.class);

                                if (msg1 == null)
                                    return Transaction.success(mutableData);
                                else {
//                                    msg1.setSeenAmount(msg1.getSeenAmount() - 1);
                                }

                                mutableData.setValue(msg1);
//                                Log.i("NewsAdapter::", "seenAmountUpdated::" + String.valueOf(msg1.getSeenAmount()));
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                Log.d("NewsAdapter", "postTransaction:onComplete:" + databaseError);
                            }
                        });
            }
        });

        if(!viewedList.isRead(msg)){
            int color = view.getResources().getColor(R.color.material_grey);
            ImageViewCompat.setImageTintList(((NewVH) holder).msgReadCheck, ColorStateList.valueOf(color));
            ((NewVH) holder).msgReadCheckText.setText(view.getResources().getString(R.string.text_not_viewed));
        }
        else {
            int color = view.getResources().getColor(R.color.material_light_blue);
            ImageViewCompat.setImageTintList(((NewVH) holder).msgReadCheck, ColorStateList.valueOf(color));
            ((NewVH) holder).msgReadCheckText.setText(view.getResources().getString(R.string.text_single_viewed));
        }

    }

    protected class NewVH extends MessagesAdapter.MessageVH {

        @BindView(R.id.msg_read_amount)
        TextView msgReadAmount;

        @BindView(R.id.msg_read_check)
        ImageView msgReadCheck;

        @BindView(R.id.msg_read_check_text)
        TextView msgReadCheckText;

        NewVH(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}