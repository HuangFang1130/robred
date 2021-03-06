package com.jiahehongye.robred.utils;

import com.jiahehongye.robred.BaseFragment;
import com.jiahehongye.robred.fragment.AddressFragment;
import com.jiahehongye.robred.fragment.HomeSingleFragment;
import com.jiahehongye.robred.fragment.MessageFragment;
import com.jiahehongye.robred.fragment.PersonalFragment;
import com.jiahehongye.robred.fragment.VedioFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/30.
 */
public class FragmentFactory {

    public static final int FRAGEMENT_HOME = 0;
    public static final int FRAGEMENT_HOT = 1;
    public static final int FRAGEMENT_MESSAGE= 2;
    public static final int FRAGEMENT_ADDRESS = 3;
    public static final int FRAGEMENT_PERSONAL = 4;
    public static FragmentFactory instances;
    public static FragmentFactory getInstances(){
        if(instances == null){
            instances = new FragmentFactory();
        }
        return instances;
    }

    private static Map<Integer, BaseFragment> mFragmentCache = new HashMap<Integer, BaseFragment>();

    public BaseFragment createFragment(int position){
        BaseFragment fragment = mFragmentCache.get(position);
        if (fragment == null) {
            switch (position) {
                case FRAGEMENT_HOME:
//                    fragment = new HomeFragment();
                    fragment = new HomeSingleFragment();
                    break;
                case FRAGEMENT_HOT:
//                    fragment = new HotFragment();
                    fragment = new VedioFragment();
                    break;
                case FRAGEMENT_MESSAGE:
                    fragment = new MessageFragment();
                    break;
                case FRAGEMENT_ADDRESS:
                    fragment = new AddressFragment();
                    break;
                case FRAGEMENT_PERSONAL:
                    fragment = new PersonalFragment();
                    break;
                default:
                    break;
            }
            mFragmentCache.put(position,fragment);
        }
        return fragment;
    }

    public static void clear(){
        mFragmentCache.clear();
    }
}
