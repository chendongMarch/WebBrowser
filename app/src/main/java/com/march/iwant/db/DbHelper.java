package com.march.iwant.db;

import android.content.Context;

import com.march.iwant.db.model.DaoMaster;
import com.march.iwant.db.model.DaoSession;

/**
 * CreateAt : 2017/1/23
 * Describe :
 *
 * @author chendong
 */
public class DbHelper {

    private static final String DB_NAME = "iwant.db";
    private static DbHelper   sDbInst;
    private        DaoSession mDaoSession;

    public static void init(Context context) {
        sDbInst = new DbHelper(context);
    }

    private DbHelper(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        mDaoSession = daoMaster.newSession();
    }

    public static DbHelper get() {
        return sDbInst;
    }

    public static DaoSession getDao() {
        return sDbInst.mDaoSession;
    }

}

