package com.example.trabalho2.Entity;

public class Consulta {
    private int idConsulta;
    private int idVeterinario;
    private int idCliente;

    public Consulta(int idConsulta, int idCliente, int idVeterinario) {
        this.idConsulta = idConsulta;
        this.idCliente = idCliente;
        this.idVeterinario = idVeterinario;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public int getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
