package com.example.trabalho2.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.trabalho2.Database;
import com.example.trabalho2.Entity.Veterinario;

import java.util.ArrayList;

public class VeterinarioDAO {
    private SQLiteDatabase database;
    private Database dbHelper;

    public VeterinarioDAO(Context context) {
        dbHelper = new Database(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long inserirVeterinario(Veterinario veterinario) {
        ContentValues values = new ContentValues();
        values.put(Database.COL_NOME_VETERINARIO, veterinario.getNome());
        values.put(Database.COL_ESPECIALIZACAO, veterinario.getEspecializacao());
        values.put(Database.COL_CRMV, veterinario.getCrmv());

        return database.insert(Database.TABLE_VETERINARIOS, null, values);
    }

    public int atualizarVeterinario(Veterinario veterinario) {
        ContentValues values = new ContentValues();
        values.put(Database.COL_NOME_VETERINARIO, veterinario.getNome());
        values.put(Database.COL_ESPECIALIZACAO, veterinario.getEspecializacao());
        values.put(Database.COL_CRMV, veterinario.getCrmv());

        return database.update(Database.TABLE_VETERINARIOS, values,
                Database.COL_ID_VETERINARIO + " = ?",
                new String[]{String.valueOf(veterinario.getIdVeterinario())});
    }

    public void excluirVeterinario(long veterinarioId) {
        database.delete(Database.TABLE_VETERINARIOS,
                Database.COL_ID_VETERINARIO + " = " + veterinarioId, null);
    }

    public Veterinario obterVeterinario(long veterinarioId) {
        Cursor cursor = database.query(Database.TABLE_VETERINARIOS, null,
                Database.COL_ID_VETERINARIO + " = " + veterinarioId, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Veterinario veterinario = cursorToVeterinario(cursor);
            cursor.close();
            return veterinario;
        } else {
            return null;
        }
    }

    public Veterinario obterVeterinarioPorCRMV(String CRMV) {
        Cursor cursor = database.query(Database.TABLE_VETERINARIOS, null,
                Database.COL_CRMV + " =?",
                new String[]{CRMV}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Veterinario veterinario = cursorToVeterinario(cursor);
            cursor.close();
            return veterinario;
        } else {
            return null;
        }
    }

    public void deletarVeterinarioPorId(long veterinarioId) {
        database.delete(Database.TABLE_VETERINARIOS,
                Database.COL_ID_VETERINARIO + " = ?",
                new String[]{String.valueOf(veterinarioId)});
    }

    public ArrayList<Veterinario> obterTodosVeterinarios(){
        ArrayList<Veterinario> veterinarios = new ArrayList<>();
        Cursor cursor = database.query(Database.TABLE_VETERINARIOS, null,
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Veterinario veterinario = cursorToVeterinario(cursor);
                veterinarios.add(veterinario);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return veterinarios;
    }

    @SuppressLint("Range")
    private Veterinario cursorToVeterinario(Cursor cursor) {
        Veterinario veterinario = new Veterinario();
        veterinario.setIdVeterinario(cursor.getInt(cursor.getColumnIndex(Database.COL_ID_VETERINARIO)));
        veterinario.setNome(cursor.getString(cursor.getColumnIndex(Database.COL_NOME_VETERINARIO)));
        veterinario.setEspecializacao(cursor.getString(cursor.getColumnIndex(Database.COL_ESPECIALIZACAO)));
        veterinario.setCrmv(cursor.getString(cursor.getColumnIndex(Database.COL_CRMV)));
        return veterinario;
    }
}
