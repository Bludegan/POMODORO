
package ObjectosNegocio;

import java.util.Objects;

/**
 *
 * @author blude
 */

public class Actividades {
    public String Nombre_Tarea;
    public String estado;

    public Actividades() {
    }
    public Actividades(String Nombre_Tarea) {
        this.Nombre_Tarea = Nombre_Tarea;
    }
    
    public Actividades(String Nombre_Tarea, String estado) {
        this.Nombre_Tarea = Nombre_Tarea;
        this.estado = estado;
    }

    public String getNombre_Tarea() {
        return Nombre_Tarea;
    }

    public void setNombre_Tarea(String Nombre_Tarea) {
        this.Nombre_Tarea = Nombre_Tarea;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.Nombre_Tarea);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Actividades other = (Actividades) obj;
        return Objects.equals(this.Nombre_Tarea, other.Nombre_Tarea);
    }
    

    @Override
    public String toString() {
        return Nombre_Tarea;
    }
}


