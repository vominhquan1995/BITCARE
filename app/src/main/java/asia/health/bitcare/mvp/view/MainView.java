package asia.health.bitcare.mvp.view;

/**
 * Created by HP on 27-Dec-16.
 */

public interface MainView {

    void updateToolBar(int resTitle, boolean showPopupMenuLayout, boolean showInputSearchLayout, boolean showInput, boolean showSortAnswer);

    void onCloseDrawer();

    void exitApp();

    void setChecked(String id);

    void setFragmentToolBarVisible(int visible);
}
