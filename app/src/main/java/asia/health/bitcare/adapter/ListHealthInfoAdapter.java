package asia.health.bitcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseRecyclerAdapter;

/**
 * Created by thong on 1/5/2017.
 * PROJECT BITCARE_ANDROID
 */

public class ListHealthInfoAdapter extends BaseRecyclerAdapter<String> {

    private Context context;

    public ListHealthInfoAdapter(Context context, List<String> consultationList) {
        this.context = context;
        items = consultationList;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.list_item_health_info;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(getItemLayout(), parent, false);
        return new ConsultationViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, int position, String item) {
        item = items.get(position);

        ConsultationViewHolder viewHolder = (ConsultationViewHolder) holder;
        viewHolder.tvConsultation.setText(item);
    }

    public void update(String[] listData) {
        this.items = Arrays.asList(listData);
        notifyDataSetChanged();
    }

    public String getTitle(int position) {
        String title = items.get(position);
        return title;
    }

    private class ConsultationViewHolder extends RecyclerView.ViewHolder {

        private TextView tvConsultation;

        ConsultationViewHolder(final View itemView) {
            super(itemView);

            tvConsultation = (TextView) itemView.findViewById(R.id.tvConsultation);

            tvConsultation.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, itemView, getAdapterPosition(), 0);
                }
            });
        }
    }
}
