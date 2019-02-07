package customviews;

import android.annotation.*;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import logger.Level;
import logger.Logger;
import test.bpl.com.bplscreens.R;



@SuppressLint("AppCompatCustomView")
public class MyCustomTextView extends TextView{


    private final static int CENTRALESANS_XBOLD = 1;

    private final static int CENTRALSANS_LIGHT = 2;
    private final static int CENTRALSANS_BOOK = 3;

    private final static int AVANT_GARDE=4;
    private final static int AVANT_GAMI=5;


    String TYPEFACE_CENTRALSANS_XBOLD = "fonts/CentraleSans-XBold.otf";
    String TYPEFACE_CENTRALSANS_Light = "fonts/CentraleSans-Light.otf";
    String TYPEFACE_CENTRALSANS_BOOK = "fonts/CentraleSans-Book.otf";
    String TYPEFACE_AVANT_GARDE="fonts/ufonts.com_avantgarde.ttf";
    String TYPEFACE_AVANT_GAMI= "fonts/avangami.ttf";

    String TAG=MyCustomTextView.class.getSimpleName();

    public MyCustomTextView(Context context) {
        super(context);
    }

    public MyCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context,attrs);
    }

    public MyCustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(context,attrs);
    }


    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.MyCustomTextView);

        //The value 0 is a default, but shouldn't ever be used since the attr is an enum
        int typeface = values.getInt(R.styleable.MyCustomTextView_fontname,1);
        Logger.log(Level.INFO,TAG,"styleable typeface value="+typeface);
        switch (typeface) {

            case CENTRALESANS_XBOLD:
                //Typeface  centralSansXBold = Typeface.createFromAsset(context.getAssets(), "fonts/CentraleSans-XBold.otf");
            Typeface centralSansXBold= customviews.FontTypeFace.getTypeFace(TYPEFACE_CENTRALSANS_XBOLD, context);
                setTypeface(centralSansXBold);
                break;



            case CENTRALSANS_LIGHT:
                Typeface centralSansLight= customviews.FontTypeFace.getTypeFace(TYPEFACE_CENTRALSANS_Light, context);
                setTypeface(centralSansLight);
                break;

            case CENTRALSANS_BOOK:
                Typeface centralSansBook = customviews.FontTypeFace.getTypeFace(TYPEFACE_CENTRALSANS_BOOK, context);
                setTypeface(centralSansBook);
                break;

            case AVANT_GARDE:
               Typeface avantgarde=customviews.FontTypeFace.getTypeFace(TYPEFACE_AVANT_GARDE, context);
                setTypeface(avantgarde);
                break;


            case AVANT_GAMI:
                Typeface avangami= customviews.FontTypeFace.getTypeFace(TYPEFACE_AVANT_GAMI, context);
                setTypeface(avangami);
                break;

        }

        values.recycle();
    }


}
