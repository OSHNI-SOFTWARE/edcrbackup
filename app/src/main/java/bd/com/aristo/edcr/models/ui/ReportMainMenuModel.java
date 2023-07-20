package bd.com.aristo.edcr.models.ui;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.SharedPrefsUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.TempData;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import io.realm.RealmModel;

/**
 * Created by monir.sobuj on 5/17/17.
 */

public class ReportMainMenuModel extends AbstractItem<ReportMainMenuModel, ReportMainMenuModel.ViewHolder> implements RealmModel {

    private String menuText;
    private int menuIcon, columns = 3;
    private int[] colorSerials = new int[]{0, 1, 2, 1, 2, 0, 2, 0, 1};


    @Inject
    App context;

    public ReportMainMenuModel(){
        App.getComponent().inject(this);
    }

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }

    @Override
    public int getType() {
        return R.id.mainMenu;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_main_menu_grid;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        Context ctx = holder.itemView.getContext();
        holder.menuText.setText(menuText);
        holder.menuI.setImageDrawable(context.getResources().getDrawable(menuIcon));
        holder.recyclerItemMainMenu.setMinimumHeight(SharedPrefsUtils.getIntegerPreference(ctx, StringConstants.PREF_MENU_H, 150));


        int itemPosition = holder.getAdapterPosition();

        int color = TempData.MAIN_MENU_BG_COLORS[colorSerials[itemPosition]];
        SystemUtils.log("color: " + color);

        //holder.recyclerItemMainMenu.setBackgroundColor(color);
        //holder.menuText.setTextColor(context.getResources().getColor(R.color.white));
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);

        holder.menuText.setText(null);
        holder.menuI.setImageDrawable(null);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ATextView menuText;
        ImageView menuI;
        RelativeLayout recyclerItemMainMenu;

        public ViewHolder(View itemView){
            super(itemView);

            menuText                                    = (ATextView) itemView.findViewById(R.id.menuText);
            menuI                                       = (ImageView) itemView.findViewById(R.id.menuIcon);
            recyclerItemMainMenu                        = (RelativeLayout) itemView.findViewById(R.id.recyclerItemMainMenu);
        }
    }
}
