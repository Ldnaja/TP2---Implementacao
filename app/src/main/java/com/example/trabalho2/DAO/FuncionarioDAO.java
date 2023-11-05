package com.example.trabalho2.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.trabalho2.Database;
import com.example.trabalho2.Entity.Funcionario;

import java.util.ArrayList;

public class FuncionarioDAO {
    private SQLiteDatabase database;
    private Database dbHelper;

    public FuncionarioDAO(Context context) {
        dbHelper = new Database(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long inserirFuncionario(Funcionario funcionario) {
        ContentValues values = new ContentValues();
        values.put(Database.COL_LOGIN, funcionario.getLogin());
        values.put(Database.COL_SENHA, funcionario.getSenha());

        return database.insert(Database.TABLE_FUNCIONARIOS, null, values);
    }

    public int atualizarFuncionario(Funcionario funcionario) {
        ContentValues values = new ContentValues();
        values.put(Database.COL_LOGIN, funcionario.getLogin());
        values.put(Database.COL_SENHA, funcionario.getSenha());

        return database.update(Database.TABLE_FUNCIONARIOS, values,
                Database.COL_ID_FUNCIONARIO + " = ?",
                new String[]{String.valueOf(funcionario.getIdFuncionario())});
    }

    public void excluirFuncionario(long funcionarioId) {
        database.delete(Database.TABLE_FUNCIONARIOS,
                Database.COL_ID_FUNCIONARIO + " = " + funcionarioId, null);
    }

    public Funcionario obterFuncionario(long funcionarioId) {
        Cursor cursor = database.query(Database.TABLE_FUNCIONARIOS, null,
                Database.COL_ID_FUNCIONARIO + " = " + funcionarioId, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Funcionario funcionario = cursorToFuncionario(cursor);
            cursor.close();
            return funcionario;
        } else {
            return null;
        }
    }

    public Funcionario obterFuncionarioPorLoginESenha(String login, String senha) {
        Cursor cursor = database.query(Database.TABLE_FUNCIONARIOS, null,
                Database.COL_LOGIN + " = ? AND " + Database.COL_SENHA + " = ?",
                new String[]{login, senha}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Funcionario funcionario = cursorToFuncionario(cursor);
                cursor.close();
                return funcionario;
            }
            cursor.close();
        }

        return null;
    }

    public Funcionario obterFuncionarioPorLogin(String login) {
        Cursor cursor = database.query(Database.TABLE_FUNCIONARIOS, null,
                Database.COL_LOGIN + " = ?",
                new String[]{login}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Funcionario funcionario = cursorToFuncionario(cursor);
                cursor.close();
                return funcionario;
            }
            cursor.close();
        }

        return null;
    }

    public void deletarFuncionarioPorId(long funcionarioId) {
        database.delete(Database.TABLE_FUNCIONARIOS,
                Database.COL_ID_FUNCIONARIO + " = ?",
                new String[]{String.valueOf(funcionarioId)});
    }

    public ArrayList<Funcionario> obterTodosFuncionarios(){
        ArrayList<Funcionario> funcionarios = new ArrayList<>();
        Cursor cursor = database.query(Database.TABLE_FUNCIONARIOS, null,
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Funcionario funcionario = cursorToFuncionario(cursor);
                funcionarios.add(funcionario);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return funcionarios;
    }

    @SuppressLint("Range")
    private Funcionario cursorToFuncionario(Cursor cursor) {
        Funcionario funcionario = new Funcionario();
        funcionario.setIdFuncionario(cursor.getInt(cursor.getColumnIndex(Database.COL_ID_FUNCIONARIO)));
        funcionario.setLogin(cursor.getString(cursor.getColumnIndex(Database.COL_LOGIN)));
        funcionario.setSenha(cursor.getString(cursor.getColumnIndex(Database.COL_SENHA)));
        return funcionario;
    }
}
