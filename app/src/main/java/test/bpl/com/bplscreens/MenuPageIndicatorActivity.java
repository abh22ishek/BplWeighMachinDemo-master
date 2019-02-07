package test.bpl.com.bplscreens;

import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.text.method.*;
import android.view.*;
import android.widget.*;

import menuind.*;

public class MenuPageIndicatorActivity extends FragmentActivity {


    ViewPager pager;
    TextView skip;

    MenuPageIndicatorAdapter menuPageIndicatorAdapter;

    private int dotsCount=4;    //No of tabs or images
    private ImageView[] dots;
    LinearLayout linearLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu_indicator);
        pager=findViewById(R.id.pager);
        skip=findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });



        menuPageIndicatorAdapter=new MenuPageIndicatorAdapter(getSupportFragmentManager(),dotsCount);
        pager.setAdapter(menuPageIndicatorAdapter);
        drawPageSelectionIndicators(0);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                drawPageSelectionIndicators(position);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void drawPageSelectionIndicators(int mPosition) {
        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }
        linearLayout = findViewById(R.id.viewPagerCountDots);
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(MenuPageIndicatorActivity.this);
            if (i == mPosition)
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.selected_circle));
            else
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.unselected_circle));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);
            linearLayout.addView(dots[i], params);
        }

    }
}
