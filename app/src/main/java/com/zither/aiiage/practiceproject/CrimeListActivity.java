package com.zither.aiiage.practiceproject;

/**
 * @author wangyanqin
 * @date 2018/08/15
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected android.support.v4.app.Fragment createFragment() {
        return new CrimeListFragment();
    }
}
