package customviews;


import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;


 class FontTypeFace {

    private static HashMap<String,Typeface> fontcache=new HashMap<>();



     static Typeface getTypeFace(String fontname,Context context)
    {
        // fontaname is path
        Typeface typeface=fontcache.get(fontname);

        if(typeface==null)
        {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            }
            catch (Exception e) {
                return null;
            }
            fontcache.put(fontname,typeface);
        }
        return typeface;
    }
}
