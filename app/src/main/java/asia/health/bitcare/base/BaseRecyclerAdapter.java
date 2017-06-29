package asia.health.bitcare.base;

import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 10/26/2016.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<T> items;
    protected AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    protected abstract int getItemLayout();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        T item = getItem(position);
        onBindViewHolder(holder, position, item);
    }

    protected abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position, T item);

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        if (position >= 0)
            return items.get(position);
        else
            return null;
    }

    public void insertItem(T newItem) {
        items.add(newItem);
        notifyDataSetChanged();
    }

    public void insertItems(List<T> newItems) {
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void removeItem(T itemToRemove) {
        items.remove(itemToRemove);
        notifyDataSetChanged();
    }

    public void removeAllItems() {
        items = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
