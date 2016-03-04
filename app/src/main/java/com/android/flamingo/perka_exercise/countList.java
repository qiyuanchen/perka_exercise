package com.android.flamingo.perka_exercise;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Created by Qiyuan on 3/3/2016.
 */
public class countList {
    @ForeignCollectionField
    private ForeignCollection<count> counts;
    @DatabaseField(id = true)
    private int id=1;
}
