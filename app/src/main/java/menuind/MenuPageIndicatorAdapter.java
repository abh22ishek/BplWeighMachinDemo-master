package menuind;

import android.support.v4.app.*;
import android.view.*;

public class MenuPageIndicatorAdapter extends FragmentStatePagerAdapter {


    int noOFDots;

    public MenuPageIndicatorAdapter(FragmentManager fm,int noOFDots) {
        super(fm);
       this. noOFDots=noOFDots;


    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                MenuIndicator1Fragment menuIndicator1Fragment=new MenuIndicator1Fragment();
                return menuIndicator1Fragment;


                case 1:
                MenuIndicator2Fragment menuIndicator2Fragment=new MenuIndicator2Fragment();
                return menuIndicator2Fragment;


            case 2:
                MenuIndicator3Fragment menuIndicator3Fragment=new MenuIndicator3Fragment();
                return menuIndicator3Fragment;


            case 3:
                MenuIndicator4Fragment menuIndicator4Fragment=new MenuIndicator4Fragment();
                return menuIndicator4Fragment;




            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOFDots;
    }
}
