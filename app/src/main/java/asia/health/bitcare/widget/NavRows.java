package asia.health.bitcare.widget;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.health.bitcare.R;

/**
 * Created by HP on 09-Jan-17.
 */

public class NavRows {
    private LinearLayout menuItem1;
    private LinearLayout menuItem2;
    private ImageView imgMenuItem;
    private TextView txtMenuItem;
    private FrameLayout navRoot;

    public NavRows(View view) {
        navRoot = (FrameLayout) view.findViewById(R.id.nav_item_root);
        menuItem1 = (LinearLayout) view.findViewById(R.id.menuItem1);
        menuItem2 = (LinearLayout) view.findViewById(R.id.menuItem2);
        imgMenuItem = (ImageView) view.findViewById(R.id.imgMenuItem);
        txtMenuItem = (TextView) view.findViewById(R.id.txtMenuItem);
    }

    public void setImgMenuItem(Drawable drawable) {
        imgMenuItem.setBackground(drawable);
    }

    public String getTitle() {
        return txtMenuItem.getText().toString().trim();
    }

    public void setTitle(String title) {
        txtMenuItem.setText(title);
    }

    public void setIsHome(boolean isHome) {
        if (isHome) {
            menuItem1.setVisibility(View.GONE);
            menuItem2.setVisibility(View.VISIBLE);
        } else {
            menuItem1.setVisibility(View.VISIBLE);
            menuItem2.setVisibility(View.GONE);
        }
    }

    public FrameLayout getMenuItem() {
        return navRoot;
    }
}
