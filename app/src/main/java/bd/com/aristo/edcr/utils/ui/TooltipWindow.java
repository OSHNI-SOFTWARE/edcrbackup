package bd.com.aristo.edcr.utils.ui;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.modules.dcr.dcr.fragment.DCRViewPager;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;

public class TooltipWindow implements View.OnClickListener{

    private static final int MSG_DISMISS_TOOLTIP = 100;
    private Context ctx;
    private PopupWindow tipWindow;
    private View contentView;
    private LayoutInflater inflater;
    ATextView[] txtOption;

    public TooltipWindow(Context ctx, int layoutId) {
        this.ctx = ctx;
        tipWindow = new PopupWindow(ctx);
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void showToolTip(View anchor) {
        contentView = inflater.inflate(R.layout.pop_up_absent, null);
        tipWindow.setHeight(LayoutParams.WRAP_CONTENT);
        tipWindow.setWidth(LayoutParams.MATCH_PARENT);
        tipWindow.setOutsideTouchable(true);
        tipWindow.setTouchable(true);
        tipWindow.setFocusable(false);
        tipWindow.setBackgroundDrawable(new BitmapDrawable());
        tipWindow.setFocusable(true);
        tipWindow.update();
        tipWindow.setContentView(contentView);
        setOptions(contentView);
        int screen_pos[] = new int[2];
        anchor.getLocationOnScreen(screen_pos);
        Rect anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0]
                + anchor.getWidth(), screen_pos[1] + anchor.getHeight());
        int contentViewHeight = contentView.getMeasuredHeight();
        int contentViewWidth = contentView.getMeasuredWidth();
        int position_x = anchor_rect.centerX();
        int position_y = anchor_rect.top - 320;

        Log.e("positions", "x: "+position_x+"  y: "+position_y+"  height: "+contentViewHeight);

        tipWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, position_x, position_y);
    }

    public boolean isTooltipShown() {
        if (tipWindow != null && tipWindow.isShowing())
            return true;
        return false;
    }

    public void dismissTooltip() {
        if (tipWindow != null && tipWindow.isShowing())
            tipWindow.dismiss();
    }

    void setOptions(View contentView){
        txtOption = new ATextView[3];
        txtOption[0] = (ATextView) contentView.findViewById(R.id.option_0);
        txtOption[1] = (ATextView) contentView.findViewById(R.id.option_1);
        txtOption[2] = (ATextView) contentView.findViewById(R.id.option_2);
        txtOption[0].setOnClickListener(this);
        txtOption[1].setOnClickListener(this);
        txtOption[2].setOnClickListener(this);
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_TOOLTIP:
                    if (tipWindow != null && tipWindow.isShowing())
                        tipWindow.dismiss();
                    break;
            }
        };
    };

    @Override
    public void onClick(View view) {
     int id = view.getId();
        switch (id){
            case R.id.option_0:
                showToast(txtOption[0].getText().toString());
                DCRViewPager.activity.onBackPressed();
                break;
            case R.id.option_1:
                showToast(txtOption[1].getText().toString());
                DCRViewPager.activity.onBackPressed();
                break;
            case R.id.option_2:
                showToast(txtOption[2].getText().toString());
                DCRViewPager.activity.onBackPressed();
                break;

            default:
                if (tipWindow != null && tipWindow.isShowing())
                    tipWindow.dismiss();
                break;
        }
       if (tipWindow != null && tipWindow.isShowing())
            tipWindow.dismiss();
    }


    public void showToast(String text){
        Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
    }
}