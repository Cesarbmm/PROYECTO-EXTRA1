package UserInterface.Form.ArbolAVL;

public class NodoAVL {
    int valor;
    int altura;
    NodoAVL izquierdo, derecho;

    public NodoAVL(int valor) {
        this.valor = valor;
        this.altura = 1; // La altura de un nuevo nodo es 1
        this.izquierdo = null;
        this.derecho = null;
    }

    // Métodos auxiliares
    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public NodoAVL getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoAVL izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoAVL getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoAVL derecho) {
        this.derecho = derecho;
    }

    // Método para actualizar la altura del nodo
    public void actualizarAltura() {
        this.altura = 1 + Math.max(altura(izquierdo), altura(derecho));
    }

    // Método para calcular el factor de equilibrio del nodo
    public int getFactorEquilibrio() {
        return altura(izquierdo) - altura(derecho);
    }

    // Método estático para calcular la altura de un nodo (maneja nodos nulos)
    public static int altura(NodoAVL nodo) {
        return (nodo == null) ? 0 : nodo.getAltura();
    }

    // Método para rotación simple a la derecha
    public static NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.getIzquierdo();
        NodoAVL T2 = x.getDerecho();

        // Realizar la rotación
        x.setDerecho(y);
        y.setIzquierdo(T2);

        // Actualizar alturas
        y.actualizarAltura();
        x.actualizarAltura();

        return x;
    }

    // Método para rotación simple a la izquierda
    public static NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.getDerecho();
        NodoAVL T2 = y.getIzquierdo();

        // Realizar la rotación
        y.setIzquierdo(x);
        x.setDerecho(T2);

        // Actualizar alturas
        x.actualizarAltura();
        y.actualizarAltura();

        return y;
    }

    // Método para rotación doble a la derecha (izquierda-derecha)
    public static NodoAVL rotacionDerechaIzquierda(NodoAVL z) {
        z.setIzquierdo(rotacionIzquierda(z.getIzquierdo()));
        return rotacionDerecha(z);
    }

    // Método para rotación doble a la izquierda (derecha-izquierda)
    public static NodoAVL rotacionIzquierdaDerecha(NodoAVL z) {
        z.setDerecho(rotacionDerecha(z.getDerecho()));
        return rotacionIzquierda(z);
    }
}