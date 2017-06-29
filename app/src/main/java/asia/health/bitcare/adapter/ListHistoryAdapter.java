package asia.health.bitcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseRecyclerAdapter;
import asia.health.bitcare.model.StandardInfoBean;
import asia.health.bitcare.mvp.model.HistoryItem;

/**
 * Created by VoMinhQuan on 30/12/2016.
 */

public class ListHistoryAdapter extends BaseRecyclerAdapter<HistoryItem> {

    private Context context;
    private boolean iconMedicine;
    private boolean iconAfterBefore;
    private OnItemClickListener onItemClickListener;

    public ListHistoryAdapter(Context context, List<HistoryItem> items, boolean iconMedicine, boolean iconBeforAfter) {
        this.context = context;
        this.items = items;
        this.iconMedicine = iconMedicine;
        this.iconAfterBefore = iconBeforAfter;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.list_item_history_blood;
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, int position, HistoryItem item) {
        ListHistoryAdapter.HistoryListViewHolder viewHolder = (ListHistoryAdapter.HistoryListViewHolder) holder;
        viewHolder.tvDate.setText(item.getDate());
        viewHolder.tvTime.setText(item.getTime());
        if (item.getValue1() != null && !item.getValue1().equals("")) {
            viewHolder.tvValue1.setText(item.getValue1());
        }
        if (item.getValue2() != null && !item.getValue2().equals("")) {
            viewHolder.tvValue2.setText(item.getValue2());
        }
        if (item.getValue3() != null && !item.getValue3().equals("")) {
            viewHolder.tvValue3.setText(item.getValue3());
        }
        if (item.getWeight() != null && !item.getWeight().equals("")) {
            viewHolder.tvWeight.setText(item.getWeight());
        }
        //weight fragment
        if (item.getValue2() == null) {
            viewHolder.lnValue2.setVisibility(View.GONE);
            viewHolder.tvValue3.setText(item.getValue1());
            viewHolder.lnValue1.setVisibility(View.GONE);
        }
        //sugar
        if (item.getValue3() == null && item.getValue2() != null) {
            viewHolder.tvValue3.setVisibility(View.GONE);
            viewHolder.lnValue2.setVisibility(View.GONE);
            viewHolder.imgBeforeAfter.setVisibility(View.VISIBLE);
            viewHolder.tvValue1.setText(item.getValue1());
        }
        //pressure
        if (item.getValue2() != null && item.getValue3() != null) {
            viewHolder.imgValue1.setVisibility(View.VISIBLE);
            viewHolder.imgValue2.setVisibility(View.VISIBLE);
            if (Integer.parseInt(item.getValue1().toString()) > 120) {
                viewHolder.imgValue1.setImageResource(R.drawable.arrow_hi);
            } else if (Integer.parseInt(item.getValue1().toString()) < 80) {
                viewHolder.imgValue1.setImageResource(R.drawable.arrow_low);
            } else if (Integer.parseInt(item.getValue1().toString()) <= 120 &&
                    Integer.parseInt(item.getValue1().toString()) >= 80) {
                viewHolder.imgValue1.setImageResource(R.drawable.arrow_hi);
                viewHolder.imgValue1.setVisibility(View.INVISIBLE);
            }
            if (Integer.parseInt(item.getValue2().toString()) > 80) {
                viewHolder.imgValue2.setImageResource(R.drawable.arrow_hi);
            } else if (Integer.parseInt(item.getValue2().toString()) < 60) {
                viewHolder.imgValue2.setImageResource(R.drawable.arrow_low);
            } else if (Integer.parseInt(item.getValue2().toString()) <= 80 &&
                    Integer.parseInt(item.getValue2().toString()) >= 60) {
                viewHolder.imgValue2.setImageResource(R.drawable.arrow_hi);
                viewHolder.imgValue2.setVisibility(View.INVISIBLE);
            }
        }
        if (!iconMedicine) {
            viewHolder.imgMedicine.setVisibility(View.GONE);
        } else {
            if (item.isUseMedicine()) {
                viewHolder.imgMedicine.setImageResource(R.drawable.sub_pharmon);
            } else {
                viewHolder.imgMedicine.setImageResource(R.drawable.sub_pharmoff);
            }
        }
        if (!iconAfterBefore) {
            viewHolder.imgBeforeAfter.setVisibility(View.GONE);
        } else {
            if (item.getValue2().equals(context.getString(R.string.cap_before))) {
                viewHolder.imgBeforeAfter.setImageResource(R.drawable.sub_eat01);
                viewHolder.imgValue1.setVisibility(View.VISIBLE);
                if (Integer.parseInt(item.getValue1().toString()) > 120) {
                    viewHolder.imgValue1.setImageResource(R.drawable.arrow_hi);
                } else if (Integer.parseInt(item.getValue1().toString()) < 100) {
                    viewHolder.imgValue1.setImageResource(R.drawable.arrow_low);
                } else if (Integer.parseInt(item.getValue1().toString()) <= 120 &&
                        Integer.parseInt(item.getValue1().toString()) >= 100) {
                    viewHolder.imgValue1.setImageResource(R.drawable.arrow_hi);
                    viewHolder.imgValue1.setVisibility(View.INVISIBLE);
                }
            } else if (item.getValue2().equals(context.getString(R.string.cap_after))) {
                viewHolder.imgBeforeAfter.setImageResource(R.drawable.sub_eat02);
                if (Integer.parseInt(item.getValue1().toString()) > 160) {
                    viewHolder.imgValue1.setImageResource(R.drawable.arrow_hi);
                } else if (Integer.parseInt(item.getValue1().toString()) < 140) {
                    viewHolder.imgValue1.setImageResource(R.drawable.arrow_low);
                } else if (Integer.parseInt(item.getValue1().toString()) <= 160 &&
                        Integer.parseInt(item.getValue1().toString()) >= 140) {
                    viewHolder.imgValue1.setImageResource(R.drawable.arrow_hi);
                    viewHolder.imgValue1.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(getItemLayout(), parent, false);
        return new ListHistoryAdapter.HistoryListViewHolder(rootView);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private class HistoryListViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDate;
        private TextView tvTime;
        private TextView tvValue1;
        private TextView tvValue2;
        private TextView tvValue3;
        private TextView tvWeight;
        private ImageView imgMedicine;
        private ImageView imgValue1;
        private ImageView imgValue2;
        private ImageView imgBeforeAfter;
        private LinearLayout lnValue1;
        private LinearLayout lnValue2;

        HistoryListViewHolder(View itemView) {
            super(itemView);
            lnValue1 = (LinearLayout) itemView.findViewById(R.id.lnValue1);
            lnValue2 = (LinearLayout) itemView.findViewById(R.id.lnValue2);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvValue1 = (TextView) itemView.findViewById(R.id.tvValue1);
            tvValue2 = (TextView) itemView.findViewById(R.id.tvValue2);
            tvValue3 = (TextView) itemView.findViewById(R.id.tvValue3);
            tvWeight = (TextView) itemView.findViewById(R.id.tvWeight);
            imgMedicine = (ImageView) itemView.findViewById(R.id.imgMEdicine);
            imgValue1 = (ImageView) itemView.findViewById(R.id.imgValue1);
            imgValue2 = (ImageView) itemView.findViewById(R.id.imgValue2);
            imgBeforeAfter = (ImageView) itemView.findViewById(R.id.imgAfterBefore);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
