package com.yassine_roma_ariane.ray.modeles.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReservationHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reservations.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "reservations";

    public static final String COL_ID = "id";
    public static final String COL_VOYAGE_ID = "voyageId";
    public static final String COL_CLIENT_ID = "clientId";
    public static final String COL_DATE = "date";
    public static final String COL_NB_PERSONNES = "nbPersonnes";
    public static final String COL_STATUT = "statut";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_VOYAGE_ID + " INTEGER, " +
                    COL_CLIENT_ID + " INTEGER, " +
                    COL_DATE + " TEXT, " +
                    COL_NB_PERSONNES + " INTEGER, " +
                    COL_STATUT + " TEXT)";

    public ReservationHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Pour l’instant, on supprime et recrée (à améliorer plus tard)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
