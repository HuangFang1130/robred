package com.jiahehongye.robred.db;

import android.content.Context;

import com.hyphenate.easeui.domain.EaseUser;

import java.util.Map;

/**
 * Created by huangjunhui on 2016/12/22.10:07
 */
public class Model {


    private Context context;

    public Model(Context context) {
        this.context = context;
    }

    public Map<String, EaseUser> getContactList() {
        UserDao dao = new UserDao(context);
        return dao.getContactList();
    }
    public void saveContact(EaseUser user){
        UserDao dao = new UserDao(context);
        dao.saveContact(user);
    }

}
