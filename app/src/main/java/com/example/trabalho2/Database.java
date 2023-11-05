package com.example.trabalho2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ClinicaVeterinaria.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela Veterinarios
    public static final String TABLE_VETERINARIOS = "Veterinarios";
    public static final String COL_ID_VETERINARIO = "idveterinario";
    public static final String COL_NOME_VETERINARIO = "nome";
    public static final String COL_ESPECIALIZACAO = "especializacao";
    public static final String COL_CRMV = "CRMV";

    // Tabela Clientes
    public static final String TABLE_CLIENTES = "Clientes";
    public static final String COL_ID_CLIENTE = "idcliente";
    public static final String COL_NOME_CLIENTE = "nome";
    public static final String COL_CPF = "cpf";
    public static final String COL_ANIMAL = "animal";
    public static final String COL_RACA_ANIMAL = "racaAnimal";

    // Tabela Funcionarios
    public static final String TABLE_FUNCIONARIOS = "Funcionarios";
    public static final String COL_ID_FUNCIONARIO = "idfuncionario";
    public static final String COL_LOGIN = "login";
    public static final String COL_SENHA = "senha";

    // Tabela Consulta
    public static final String TABLE_CONSULTAS = "Consultas";
    public static final String COL_ID_CONSULTA = "idconsulta";
    public static final String COL_ID_VETERINARIO_CONSULTA = "idveterinario";
    public static final String COL_ID_CLIENTE_CONSULTA = "idcliente";

    private static final String CREATE_TABLE_VETERINARIOS = "CREATE TABLE " + TABLE_VETERINARIOS + " (" +
            COL_ID_VETERINARIO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NOME_VETERINARIO + " TEXT, " +
            COL_ESPECIALIZACAO + " TEXT, " +
            COL_CRMV + " TEXT UNIQUE" +
            ");";

    private static final String CREATE_TABLE_CLIENTES = "CREATE TABLE " + TABLE_CLIENTES + " (" +
            COL_ID_CLIENTE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NOME_CLIENTE + " TEXT, " +
            COL_CPF + " TEXT UNIQUE, " +
            COL_ANIMAL + " TEXT, " +
            COL_RACA_ANIMAL + " TEXT" +
            ");";

    private static final String CREATE_TABLE_FUNCIONARIOS = "CREATE TABLE " + TABLE_FUNCIONARIOS + " (" +
            COL_ID_FUNCIONARIO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_LOGIN + " TEXT UNIQUE, " +
            COL_SENHA + " TEXT" +
            ");";

    private static final String CREATE_TABLE_CONSULTAS = "CREATE TABLE " + TABLE_CONSULTAS + " (" +
            COL_ID_CONSULTA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ID_VETERINARIO_CONSULTA + " INTEGER, " +
            COL_ID_CLIENTE_CONSULTA + " INTEGER, " +
            "FOREIGN KEY (" + COL_ID_VETERINARIO_CONSULTA + ") REFERENCES " + TABLE_VETERINARIOS + " (" + COL_ID_VETERINARIO + "), " +
            "FOREIGN KEY (" + COL_ID_CLIENTE_CONSULTA + ") REFERENCES " + TABLE_CLIENTES + " (" + COL_ID_CLIENTE + ")" +
            ");";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VETERINARIOS);
        db.execSQL(CREATE_TABLE_CLIENTES);
        db.execSQL(CREATE_TABLE_FUNCIONARIOS);
        db.execSQL(CREATE_TABLE_CONSULTAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSULTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VETERINARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUNCIONARIOS);
        onCreate(db);
    }
}
