package com.example.trabalho2.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.trabalho2.Database;
import com.example.trabalho2.Entity.Consulta;

public class ConsultaDAO {
    private SQLiteDatabase database;
    private Database dbHelper;

    public ConsultaDAO(Context context) {
        dbHelper = new Database(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long inserirConsulta(Consulta consulta) {
        ContentValues values = new ContentValues();
        values.put(Database.COL_ID_VETERINARIO_CONSULTA, consulta.getIdVeterinario());
        values.put(Database.COL_ID_CLIENTE_CONSULTA, consulta.getIdCliente());

        return database.insert(Database.TABLE_CONSULTAS, null, values);
    }

    public int atualizarConsulta(Consulta consulta) {
        ContentValues values = new ContentValues();
        values.put(Database.COL_ID_VETERINARIO_CONSULTA, consulta.getIdVeterinario());
        values.put(Database.COL_ID_CLIENTE_CONSULTA, consulta.getIdCliente());

        return database.update(Database.TABLE_CONSULTAS, values,
                Database.COL_ID_CONSULTA + " = ?",
                new String[]{String.valueOf(consulta.getIdConsulta())});
    }

    public void excluirConsulta(long consultaId) {
        database.delete(Database.TABLE_CONSULTAS,
                Database.COL_ID_CONSULTA + " = " + consultaId, null);
    }

    public Consulta obterConsulta(long consultaId) {
        Cursor cursor = database.query(Database.TABLE_CONSULTAS, null,
                Database.COL_ID_CONSULTA + " = " + consultaId, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Consulta consulta = cursorToConsulta(cursor);
            cursor.close();
            return consulta;
        } else {
            return null;
        }
    }

    @SuppressLint("Range")
    private Consulta cursorToConsulta(Cursor cursor) {
        Consulta consulta = new Consulta();
        consulta.setIdConsulta(cursor.getInt(cursor.getColumnIndex(Database.COL_ID_CONSULTA)));
        consulta.setIdVeterinario(cursor.getInt(cursor.getColumnIndex(Database.COL_ID_VETERINARIO_CONSULTA)));
        consulta.setIdCliente(cursor.getInt(cursor.getColumnIndex(Database.COL_ID_CLIENTE_CONSULTA)));
        return consulta;
    }
}
