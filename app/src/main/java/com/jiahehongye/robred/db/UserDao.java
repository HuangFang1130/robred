package com.jiahehongye.robred.db;

import android.content.Context;

import com.hyphenate.easeui.domain.EaseUser;

import java.util.Map;

/**
 * Created by huangjunhui on 2016/12/22.10:11
 */
public class UserDao {

    public static final String TABLE_NAME = "uers";
    public static final String COLUMN_NAME_ID = "username";
    public static final String COLUMN_NAME_NICK = "nick";
    public static final String COLUMN_NAME_AVATAR = "avatar";

    private Context context;

    public UserDao(Context context) {
        this.context = context;

    }

    public Map<String, EaseUser> getContactList() {
        return AppDBManager.getInstance().getContactList();
    }


    public void saveContact(EaseUser user){
        AppDBManager.getInstance().saveContact(user);
    }
}
