package com.example.trabalho2.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.trabalho2.Database;
import com.example.trabalho2.Entity.Cliente;

import java.util.ArrayList;

public class ClienteDAO {
    private SQLiteDatabase database;
    private Database dbHelper;

    public ClienteDAO(Context context) {
        dbHelper = new Database(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long inserirCliente(Cliente cliente) {
        ContentValues values = new ContentValues();
        values.put(Database.COL_NOME_CLIENTE, cliente.getNome());
        values.put(Database.COL_CPF, cliente.getCpf());
        values.put(Database.COL_ANIMAL, cliente.getAnimal());
        values.put(Database.COL_RACA_ANIMAL, cliente.getRacaAnimal());

        return database.insert(Database.TABLE_CLIENTES, null, values);
    }

    public int atualizarCliente(Cliente cliente) {
        ContentValues values = new ContentValues();
        values.put(Database.COL_NOME_CLIENTE, cliente.getNome());
        values.put(Database.COL_ANIMAL, cliente.getAnimal());
        values.put(Database.COL_RACA_ANIMAL, cliente.getRacaAnimal());

        return database.update(Database.TABLE_CLIENTES, values,
                Database.COL_ID_CLIENTE + " = ?",
                new String[]{String.valueOf(cliente.getIdCliente())});
    }

    public void deletarClientePorId(long clienteId) {
        database.delete(Database.TABLE_CLIENTES,
                Database.COL_ID_CLIENTE + " = ?",
                new String[]{String.valueOf(clienteId)});
    }

    public Cliente obterCliente(long clienteId) {
        Cursor cursor = database.query(Database.TABLE_CLIENTES, null,
                Database.COL_ID_CLIENTE + " = " + clienteId, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Cliente cliente = cursorToCliente(cursor);
            cursor.close();
            return cliente;
        } else {
            return null;
        }
    }

    public Cliente obterClientePorCpf(String cpf) {
        Cursor cursor = database.query(Database.TABLE_CLIENTES, null,
                Database.COL_CPF + " = ?",
                new String[]{cpf}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Cliente cliente = cursorToCliente(cursor);
                cursor.close();
                return cliente;
            }
            cursor.close();
        }
        return null;
    }

    public ArrayList<Cliente> obterTodosClientes() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        Cursor cursor = database.query(Database.TABLE_CLIENTES, null,
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Cliente cliente = cursorToCliente(cursor);
                clientes.add(cliente);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return clientes;
    }

    public String obterNomeClientePorId(int clienteId) {
        Cursor cursor = database.query(
                Database.TABLE_CLIENTES,
                new String[]{Database.COL_NOME_CLIENTE},
                Database.COL_ID_CLIENTE + " = ?",
                new String[]{String.valueOf(clienteId)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String nomeCliente = cursor.getString(cursor.getColumnIndex(Database.COL_NOME_CLIENTE));
            cursor.close();
            return nomeCliente;
        }

        return null; // Caso o cliente não seja encontrado
    }


    @SuppressLint("Range")
    private Cliente cursorToCliente(Cursor cursor) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(cursor.getInt(cursor.getColumnIndex(Database.COL_ID_CLIENTE)));
        cliente.setNome(cursor.getString(cursor.getColumnIndex(Database.COL_NOME_CLIENTE)));
        cliente.setCpf(cursor.getString(cursor.getColumnIndex(Database.COL_CPF)));
        cliente.setAnimal(cursor.getString(cursor.getColumnIndex(Database.COL_ANIMAL)));
        cliente.setRacaAnimal(cursor.getString(cursor.getColumnIndex(Database.COL_RACA_ANIMAL)));
        return cliente;
    }
}
