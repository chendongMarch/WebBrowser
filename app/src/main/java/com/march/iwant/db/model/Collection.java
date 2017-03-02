package com.march.iwant.db.model;

import com.march.iwant.db.DbHelper;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * CreateAt : 2017/1/23
 * Describe :
 *
 * @author chendong
 */
@Entity
public class Collection {
    @Id
    private Long   id;
    @Property(nameInDb = "linkUrl")
    private String linkUrl;
    @Property(nameInDb = "type")
    private int    type;


    @Generated(hash = 878389527)
    public Collection(Long id, String linkUrl, int type) {
        this.id = id;
        this.linkUrl = linkUrl;
        this.type = type;
    }

    @Generated(hash = 1149123052)
    public Collection() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public void insert() {
        DbHelper.getDao().getCollectionDao().save(this);
    }

    public List<Collection> selectAll() {
        return DbHelper.getDao().getCollectionDao().queryBuilder().build().list();
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
