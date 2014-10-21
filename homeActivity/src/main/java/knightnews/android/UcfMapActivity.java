package knightnews.android;

import android.support.v4.app.Fragment;

/**
 * Created by jjvg on 5/27/14.
 */
public class UcfMapActivity extends SingleFragmentActivity {


	@Override
    protected Fragment createFragment() {
        return new UcfMapFragment();
    }
}
