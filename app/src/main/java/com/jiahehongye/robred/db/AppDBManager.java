package com.jiahehongye.robred.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hyphenate.easeui.domain.EaseUser;
import com.jiahehongye.robred.utils.UIUtils;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by huangjunhui on 2016/12/22.10:13
 */
public class AppDBManager  {

    private DbOpenHelper dbHelper;

    private AppDBManager(){
        dbHelper = DbOpenHelper.getInstance(UIUtils.getContext());
    }
    static AppDBManager dbMgr;
    public static synchronized AppDBManager getInstance(){
        if(dbMgr == null){
            dbMgr = new AppDBManager();
        }
        return dbMgr;
    }

    /**
     * get contact list
     *
     * @return
     */
    synchronized public Map<String, EaseUser> getContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, EaseUser> users = new Hashtable<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + UserDao.TABLE_NAME, null);
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_ID));
                String nick = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_NICK));
                String avatar = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_AVATAR));
                EaseUser user = new EaseUser(username);
                user.setNick(nick);
                user.setAvatar(avatar);
                users.put(username, user);
            }
            cursor.close();
        }
        return users;
    }

    /**
     * 保存联系人的时候使用setnick
     * @param user
     */
    public void saveContact(EaseUser user) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(UserDao.COLUMN_NAME_ID, user.getUsername());
            if (user.getNick() != null)
                values.put(UserDao.COLUMN_NAME_NICK, user.getNick());
            if (user.getAvatar() != null)
                values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
            if (db.isOpen()) {
                db.replace(UserDao.TABLE_NAME, null, values);
            }

        }catch (Exception e){

        }
    }
}
