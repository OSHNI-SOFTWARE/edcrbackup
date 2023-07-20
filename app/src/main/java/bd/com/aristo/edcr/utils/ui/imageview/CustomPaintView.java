package bd.com.aristo.edcr.utils.ui.imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import bd.com.aristo.edcr.R;

/**
 * Created by monir.sobuj on 9/12/2018.
 */

public class CustomPaintView extends View{

    private Bitmap _cloud;
    private Bitmap _cloud1;
    private Paint _paint;
    public CustomPaintView(Context context){
        super(context);
        _cloud = BitmapFactory.decodeResource(getResources(), R.drawable.four_oval);
        _cloud1 = BitmapFactory.decodeResource(getResources(), R.drawable.oval_solid);
        _paint = new Paint();
    }
    protected void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(_cloud, 50, 0, _paint);
        canvas.drawBitmap(_cloud1, 50, 50, _paint);
        canvas.drawBitmap(_cloud, 50, 100, _paint);
        canvas.drawBitmap(_cloud1, 50, 150, _paint);

    }
}
