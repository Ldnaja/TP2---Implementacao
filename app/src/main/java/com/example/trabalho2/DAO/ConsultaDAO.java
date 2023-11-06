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

    /*public long inserirConsulta(Consulta consulta) {
        ContentValues values = new ContentValues();
        values.put(Database.COL_ID_VETERINARIO_CONSULTA, consulta.getIdVeterinario());
        values.put(Database.COL_ID_CLIENTE_CONSULTA, consulta.getIdCliente());

        return database.insert(Database.TABLE_CONSULTAS, null, values);
    }*/

    public long inserirConsulta(int idCliente, int idVeterinario) {
        ContentValues values = new ContentValues();
        values.put(Database.COL_ID_CLIENTE_CONSULTA, idCliente);
        values.put(Database.COL_ID_VETERINARIO_CONSULTA, idVeterinario);

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

    public boolean consultaExiste(int idCliente, int idVeterinario) {
        Cursor cursor = database.query(
                Database.TABLE_CONSULTAS,
                null,
                Database.COL_ID_CLIENTE_CONSULTA + " = ? AND " + Database.COL_ID_VETERINARIO_CONSULTA + " = ?",
                new String[]{String.valueOf(idCliente), String.valueOf(idVeterinario)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true; // A consulta já existe
        } else {
            return false; // A consulta não existe
        }
    }


    @SuppressLint("Range")
    private Consulta cursorToConsulta(Cursor cursor) {
        int idConsulta = cursor.getInt(cursor.getColumnIndex(Database.COL_ID_CONSULTA));
        int idVeterinario = cursor.getInt(cursor.getColumnIndex(Database.COL_ID_VETERINARIO_CONSULTA));
        int idCliente = cursor.getInt(cursor.getColumnIndex(Database.COL_ID_CLIENTE_CONSULTA));

        return new Consulta(idConsulta, idCliente, idVeterinario);
    }

}
