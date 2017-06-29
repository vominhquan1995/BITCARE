package asia.health.bitcare.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import asia.health.bitcare.R;
import asia.health.bitcare.base.BaseRecyclerAdapter;
import asia.health.bitcare.widget.NavRows;

/**
 * Created by HP on 09-Jan-17.
 */

public class NavAdapter extends BaseRecyclerAdapter<NavAdapter.Item> {
    private OnNavItemSelected onNavItemSelected;
    private Context context;
    private String selectedId;

    public NavAdapter(Context context) {
        this.context = context;
        initListItem();
    }

    private Drawable getIcon(int id) {
        return context.getResources().getDrawable(id);
    }

    private String getTitle(int id) {
        return context.getString(id);
    }

    private void initListItem() {
        List<Item> listItem = new ArrayList<>();

        //Home
        listItem.add(new Item(context.getString(R.string.cap_home), null));

        //Blood Pressure
        listItem.add(new Item(getTitle(R.string.cap_bloodpressure), getIcon(R.drawable.menu_01)));

        //Blood Sugar
        listItem.add(new Item(getTitle(R.string.cap_bloodsugar), getIcon(R.drawable.menu_02)));

        //Weight
        listItem.add(new Item(getTitle(R.string.cap_weight), getIcon(R.drawable.menu_03)));

        //Target Weight
        listItem.add(new Item(getTitle(R.string.cap_targetweight), getIcon(R.drawable.menu_04)));

        //Setting
        listItem.add(new Item(getTitle(R.string.cap_settings), getIcon(R.drawable.menu_05)));

        //Device Setting
        listItem.add(new Item(getTitle(R.string.cap_devicesettings), getIcon(R.drawable.menu_06)));

        //Set default selected is Home
        selectedId = listItem.get(0).getTitle();
        this.items = listItem;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.nav_rows_2;
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, int position, Item item) {
        NavHolder navHolder = (NavHolder) holder;
        if (position == 0) {
            navHolder.navRows.setIsHome(true);
            navHolder.navRows.setTitle(item.getTitle());
        } else {
            navHolder.navRows.setIsHome(false);
            navHolder.navRows.setImgMenuItem(item.getIcon());
            navHolder.navRows.setTitle(item.getTitle());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(getItemLayout(), parent, false);
        return new NavHolder(rootView);
    }

    public void setOnNavItemSelected(OnNavItemSelected onNavItemSelected) {
        this.onNavItemSelected = onNavItemSelected;
    }

    public void setSelectedItem(String itemId) {
        selectedId = itemId;
        notifyDataSetChanged();
    }

    public interface OnNavItemSelected {
        void onItemSelected(String itemId);
    }

    /**
     * RecyclerView.ViewHolder
     */
    private class NavHolder extends RecyclerView.ViewHolder {
        private NavRows navRows;

        public NavHolder(View itemView) {
            super(itemView);
            navRows = new NavRows(itemView);
            navRows.getMenuItem().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavItemSelected.onItemSelected(navRows.getTitle());
                    selectedId = navRows.getTitle();
                }
            });
        }
    }

    /**
     * Model
     */
    public class Item {
        //Title
        private String title;

        //Icon
        private Drawable icon;

        public Item() {
        }

        public Item(String title, Drawable icon) {
            this.title = title;
            this.icon = icon;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
